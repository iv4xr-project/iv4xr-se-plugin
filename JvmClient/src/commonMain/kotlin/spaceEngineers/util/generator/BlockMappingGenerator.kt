package spaceEngineers.util.generator

import spaceEngineers.controller.serializer.blockMappings
import kotlin.reflect.KClass

const val commonBlockFields = """
    @SerialName("Id")
    override val id: BlockId,
    @SerialName("Position")
    override val position: Vec3F,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3F,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3F,
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,
    @SerialName("MaxIntegrity")
    override val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    override val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    override val integrity: Float = 0f,
    @SerialName("MinPosition")
    override val minPosition: Vec3F,
    @SerialName("MaxPosition")
    override val maxPosition: Vec3F,
    @SerialName("GridPosition")
    override val gridPosition: Vec3I,
    @SerialName("Size")
    override val size: Vec3F,
    @SerialName("UseObjects")
    override val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    override val functional: Boolean = false,
    @SerialName("Working")
    override val working: Boolean = false,
    @SerialName("OwnerId")
    override val ownerId: CharacterId,
    @SerialName("BuiltBy")
    override val builtBy: CharacterId,
"""

val regex = "override val ([a-zA-Z]+):".toRegex()

val commonFieldNames = regex.findAll(commonBlockFields).map {
    it.groupValues[1]
}.toList()

val filteredParents = setOf("MyObjectBuilder_CubeBlock", "MyObjectBuilder_Base", "Object")

class BlockMappingGenerator(
    val cls: String,
    val fields: Map<String, KClass<*>>,
    val overriddenFields: Map<String, KClass<*>> = emptyMap(),
    val parents: List<String>,
    val defaultParent: String = "Block",
    val commonFields: String,
) {

    fun generateInterface(): String {
        return """
interface ${cls.interfaceName()}: ${parentCall()} {
${interfaceFields()}
}
        """.trimIndent()
    }

    private fun interfaceFields(): String {
        return fields.entries.joinToString("\n") { (field, type) ->
            """    val ${field.camelCase()}: ${type.simpleName}"""
        }
    }

    fun generate(): String {
        return """
${generateInterface()}
${generateDataClass()}
        """.trimIndent()
    }

    fun generateDataClass(): String {
        return """
@Serializable
data class ${cls.dataclassName()}($commonFields
${overriddenFields()}

${fields()}
) : ${cls.interfaceName()}
        """.trimIndent()
    }

    fun generateCsClass(): String {
        return """

public class $cls : ${parentCall()}
{
${csFields()}
}
        """.trimIndent()
    }

    private fun parentCall(): String {
        val ip = importantParents()
        return """
${ip.firstOrNull() ?: defaultParent} 
        """.trimIndent()
    }

    private fun overriddenFields(): String {
        return overriddenFields.entries.joinToString("\n") { (field, type) ->
            """    @SerialName("$field")
    override val ${field.camelCase()}: ${type.simpleName},"""
        }
    }

    private fun csFields(): String {
        return (fields).entries.joinToString("\n") { (field, type) ->
            """    public ${mapToCsType(type.simpleName!!)} $field;"""
        }
    }

    private fun fields(): String {
        return fields.entries.joinToString("\n") { (field, type) ->
            """    @SerialName("$field")
    override val ${field.camelCase()}: ${type.simpleName},"""
        }
    }

    private fun importantParents(): List<String> {
        return parents.filter { it in blockMappings }
    }
}

fun getBlockParentsById(id: String, parentMappings: Map<String, String>): List<String> {
    var id_: String = id
    val result = mutableListOf<String>()
    while (parentMappings.get(id_) != null) {
        id_ = parentMappings.get(id_)!!
        result.add(id_)
    }
    return result
}

fun getOverriddenFields(parents: List<String>, mappings: Map<String, Map<String, KClass<*>>>): Map<String, KClass<*>> {
    return parents.flatMap {
        mappings[it]?.entries ?: emptySet()
    }.associate {
        it.key to it.value
    }
}

fun findImportantParent(
    blockId: String,
    idsWithSerializers: Set<String>,
    parentMappings: Map<String, String>,
): String? {
    var id: String? = blockId
    if (id in idsWithSerializers) {
        return id!!
    }
    while (id != null) {
        id = parentMappings[id]
        if (id in idsWithSerializers) {
            return id!!
        }
    }
    return null
}

fun generateMappingsForSingleClass(
    blockId: String,
    parentMappings: Map<String, String>,
    idsWithSerializers: Set<String>,
): List<String> {
    val parents = listOf(blockId) + getBlockParentsById(blockId, parentMappings)

    return parents.mapNotNull {
        findImportantParent(it, parentMappings = parentMappings, idsWithSerializers = idsWithSerializers)?.let { importantParent ->
            """    "$it" to ${importantParent.dataclassName()}.serializer()"""
        }
    }
}

fun generateMappings(parentMappings: Map<String, String>, idsWithSerializers: Set<String>, variableName: String): String {
    return parentMappings.keys.flatMap {
        generateMappingsForSingleClass(it, parentMappings = parentMappings, idsWithSerializers = idsWithSerializers)
    }.distinct().joinToString(
        separator = ",\n",
        prefix = """
val $variableName = mutableMapOf(
""".trimStart(),
        postfix = """
)
        """.trimIndent()
    )
}

fun generateMappingsForSingleCsClass(
    blockId: String,
    parentMappings: Map<String, String>,
    idsWithSerializers: Set<String>,
): List<String> {
    val parent = findImportantParent(blockId, parentMappings = parentMappings, idsWithSerializers = idsWithSerializers) ?: return emptyList()
    if (parent == blockId) return emptyList()
    return listOf("""    { "$blockId", "$parent" }""")
}

fun generateCsMappings(parentMappings: Map<String, String>, idsWithSerializers: Set<String>, className: String): String {
    return parentMappings.keys.flatMap {
        generateMappingsForSingleCsClass(it, parentMappings = parentMappings, idsWithSerializers = idsWithSerializers)
    }.joinToString(
        separator = ",\n",
        prefix = """
public static class $className
{
    public static readonly Dictionary<string, string> Mapping = new Dictionary<string, string>
    {
""".trim().padTabs(1) + "\n",
        postfix = """,
        };
    }"""
    ) {
        it.padTabs(2)
    }
}
