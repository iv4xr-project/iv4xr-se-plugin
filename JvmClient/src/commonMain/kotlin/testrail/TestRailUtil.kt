package testrail

import testrail.model.Case

fun Case.featureContent(): String {
    return """
Feature: C$id $title

${custom_preconds?.convertTables()}
""".trimIndent()
}


fun String.convertTables(): String {

    var previousLinePiped = false
    return lineSequence().map { it ->
        if (it.startsWith("||")) {
            if (previousLinePiped) {
                previousLinePiped = true
                "  " + it.substring(1) + "|"
            } else {
                previousLinePiped = true
                "  " + it.substring(2) + "|"
            }
        } else {
            previousLinePiped = false
            it
        }.let {
            if (it.trim().startsWith("Scenario")) {
                "  $it"
            } else {
                "    $it"
            }

        }
    }.joinToString("\r\n")
}
