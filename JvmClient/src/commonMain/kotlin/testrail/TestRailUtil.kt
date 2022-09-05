package testrail

import testrail.model.Case
import testrail.model.Section
import testrail.model.Sections

fun Case.featureContent(): String {
    return """
${formattedTags()}Feature: C$id $title

${custom_preconds?.convertTables()}
""".trimIndent()
}

fun Case.relativeUrl(): String {
    return "index.php?/cases/view/$id"
}

val TAGS_REGEX = "\\[([^]]*)]".toRegex()

fun Case.formattedTags(): String {
    val tags = tags()
    if (tags.isEmpty()) {
        return ""
    }
    return tags.joinToString(" @", prefix = "@", postfix = System.lineSeparator())
}

fun Case.tags(): List<String> {
    if (title == null) {
        return emptyList()
    }
    return parseTags(title)
}

fun Section.tags(): List<String> {
    if (name.isBlank()) {
        return emptyList()
    }
    return parseTags(name)
}

fun parseTags(title: String): List<String> {
    return TAGS_REGEX.findAll(title).map {
        it.groupValues[1]
    }.toList()
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

class SectionHelper(
    val sections: Sections,
) {

    val sectionsById = sections.sections.associateBy { it.id }

    fun directChildren(section: Section): List<Section> {
        return sections.sections.filter { it.parent_id == section.id }.sortedBy { it.display_order }
    }

    fun allChildren(sectionId: Long): List<Section> {
        return allChildren(sectionsById.getValue(sectionId))
    }

    fun allChildren(section: Section): List<Section> {
        return directChildren(section).flatMap {
            listOf(it) + directChildren(it)
        }
    }

    fun sectionsOfCase(case: Case): List<Section> {
        val sections = mutableListOf<Section>()
        var section: Section? = sectionsById[case.section_id]
        do {
            section?.let {
                sections.add(it)
            }
            section = sectionsById[section?.parent_id]
        } while (section != null)
        return sections.asReversed()
    }
}
