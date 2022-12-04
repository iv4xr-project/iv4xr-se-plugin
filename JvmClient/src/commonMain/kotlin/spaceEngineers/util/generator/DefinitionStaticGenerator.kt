package spaceEngineers.util.generator

import spaceEngineers.model.DefinitionId

private fun String.toId(): String {
    val modified = this.replace("[.\\- ]".toRegex(), "_")
    if (modified.matches("[a-zA-Z][a-z0-9A-Z_]*".toRegex())) {
        return modified
    }
    if (this.isBlank()) {
        return "EMPTY"
    }
    println(modified)
    return """`$this`"""
}

// MyObjectBuilder_LCDTextureDefinition/MyObjectBuilder_AmmoMagazine/SemiAutoPistolMagazine WTF
fun generate(definitions: List<DefinitionId>): String {
    val typesById: Map<String, List<String>> =
        definitions.filter { it.id != "MyObjectBuilder_LCDTextureDefinition" }.groupBy({ it.id }) { it.type }
    return typesById.map { (id, types) ->
        types.joinToString(
            separator = "\n",
            prefix = """
object ${id.removeBuilderPrefix()}: SafeDefinitionIdId() {
""".trimStart(),
            postfix = "}"
        ) { type ->
            """ val ${type.toId()} = create(this, DefinitionIdTypes.${type.toId()})"""
        }
    }.joinToString("\n\n", postfix = "\n")
}

fun generateTypes(definitions: List<DefinitionId>): String {
    return definitions.filter { it.id != "MyObjectBuilder_LCDTextureDefinition" }.map { it.type }.toSet().joinToString("\n") { type ->
        """
val ${type.toId()} = DefinitionIdType("$type")
        """.trimIndent()
    }
}
