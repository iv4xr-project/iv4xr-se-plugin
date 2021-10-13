package spaceEngineers.util.generator

import spaceEngineers.controller.blockDefinitionMappings
import spaceEngineers.controller.blockMappings
import kotlin.reflect.KClass


const val commonBlockDefinitionFields = """
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,
    @SerialName("BuildProgressModels")
    override val buildProgressModels: List<BuildProgressModel>,
    @SerialName("Type")
    override val type: String,
    @SerialName("Size")
    override val size: Vec3F,
    @SerialName("CubeSize")
    override val cubeSize: CubeSize,
    @SerialName("MountPoints")
    override val mountPoints: List<MountPoint>,
    @SerialName("Public")
    override val public: Boolean,
    @SerialName("AvailableInSurvival")
    override val availableInSurvival: Boolean,
    @SerialName("Enabled")
    override val enabled: Boolean,
"""

class BlockDefinitionMappingGenerator(
    val cls: String,
    val fields: Map<String, KClass<*>>,
    val overriddenFields: Map<String, KClass<*>> = emptyMap(),
    val parents: List<String>,
    val defaultParent: String = "BlockDefinition",
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

public class ${cls} : ${parentCall()} {
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
        return parents.filter { it in blockDefinitionMappings }
    }

}
