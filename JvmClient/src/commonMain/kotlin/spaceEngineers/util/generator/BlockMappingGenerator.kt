package spaceEngineers.util.generator

import spaceEngineers.controller.blockMappings
import kotlin.reflect.KClass

const val commonFields = """
    @SerialName("Id")
    override val id: String,
    @SerialName("Position")
    override val position: Vec3,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3,
    @SerialName("BlockType")
    override val blockType: String,
    @SerialName("MaxIntegrity")
    override val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    override val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    override val integrity: Float = 0f,
    @SerialName("MinPosition")
    override val minPosition: Vec3,
    @SerialName("MaxPosition")
    override val maxPosition: Vec3,
    @SerialName("Size")
    override val size: Vec3,
    @SerialName("UseObjects")
    override val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    override val functional: Boolean = false,
    @SerialName("Working")
    override val working: Boolean = false,
"""

val filePrefix = """
package spaceEngineers.model

// Generated file using BlockMappingGenerator.kt.
""".trimStart()

const val blockDataClassesImports = """
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

"""

val regex = "override val ([a-zA-Z]+):".toRegex()

val commonFieldNames = regex.findAll(commonFields).map {
    it.groupValues[1]
}.toList()

fun String.interfaceName(): String {
    return this
}

fun String.dataclassName(): String {
    return "Data$this"
}

fun String.camelCase(): String {
    return this[0].lowercase() + substring(1)
}

fun String.removeBuilderPrefix(): String {
    return removePrefix("MyObjectBuilder_")
}

val filteredParents = setOf("MyObjectBuilder_CubeBlock", "MyObjectBuilder_Base", "Object")

class BlockMappingGenerator(
    val cls: String,
    val fields: Map<String, KClass<*>>,
    val overriddenFields: Map<String, KClass<*>> = emptyMap(),
    val parents: List<String>,
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

    private fun parentCall(): String {
        val ip = importantParents()
        return """
${ip.firstOrNull() ?: "Block"} 
        """.trimIndent()
    }

    private fun overriddenFields(): String {
        return overriddenFields.entries.joinToString("\n") { (field, type) ->
            """    @SerialName("$field")
    override val ${field.camelCase()}: ${type.simpleName},"""
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

fun getOverriddenFields(parents: List<String>): Map<String, KClass<*>> {
    return parents.flatMap {
        blockMappings[it]?.entries ?: emptySet()
    }.associate {
        it.key to it.value
    }
}

fun findImportantParent(
    blockId: String,
    idsWithSerializers: Set<String> = blockMappings.keys,
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
    idToTypes: Map<String, List<String>>
): List<String> {
    val parent = findImportantParent(blockId, parentMappings = parentMappings) ?: return emptyList()
    val blockTypes = idToTypes[blockId] ?: return emptyList()
    return blockTypes.map { """    "$it" to ${parent.dataclassName()}.serializer()""" }
}

fun generateMappings(parentMappings: Map<String, String>, idToTypes: Map<String, List<String>>): String {
    return parentMappings.keys.flatMap {
        generateMappingsForSingleClass(it, parentMappings = parentMappings, idToTypes = idToTypes)
    }.joinToString(
        separator = ",\n", prefix = """
val generatedSerializerMappings = mutableMapOf(
""".trimStart(), postfix = """
)
""".trimIndent()
    )
}
