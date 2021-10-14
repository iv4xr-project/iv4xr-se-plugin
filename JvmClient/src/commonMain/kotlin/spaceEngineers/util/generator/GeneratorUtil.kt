package spaceEngineers.util.generator

const val generatedText = "// Generated automatically by BlockMappingGeneratorRunner.kt, do not change.\n"

val filePrefix = """
package spaceEngineers.model

$generatedText
""".trimStart()

const val blockDataClassesImports = """
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

"""

val typeMapping = mapOf(
    "Float" to "float",
    "Boolean" to "bool",
    "Double" to "double",
)

fun mapToCsType(type: String): String {
    return typeMapping[type] ?: error("No cs mapping found for $type")
}

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

fun String.removeDefinitionPrefix(): String {
    return removePrefix("My")
}

fun String.padTabs(tabCount: Int): String {
    val tabs = "\t".repeat(tabCount)
    return split("\n").joinToString("\n$tabs", prefix = "$tabs")
}
