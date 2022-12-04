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
                1
            } else {
                2
            }.let { startIndex ->
                previousLinePiped = true
                "  " + it.substring(startIndex) + "|"
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
    val ignoredSections: Set<Long> = DEFAULT_IGNORED_SECTIONS,
    val sectionDirectoryNaming: (Section) -> String = {
        "${it.name}-${it.id}"
    },
    val caseFileNaming: (Case) -> String = { case ->
        "C${case.id}.feature"
    },

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
        return sectionsOfSection(case.section_id)
    }

    fun sectionsOfSection(sectionId: Long): List<Section> {
        val sections = mutableListOf<Section>()
        var section: Section? = sectionsById[sectionId]
        do {
            section?.let {
                sections.add(it)
            }
            section = sectionsById[section?.parent_id]
        } while (section != null)
        return sections.asReversed()
    }

    fun fileRelativePath(case: Case): String {
        return sectionDirectory(case.section_id) + "/" + caseFileNaming(case)
    }

    fun sectionDirectory(sectionId: Long): String {
        return sectionsOfSection(sectionId).filter { it.id !in ignoredSections }
            .joinToString(separator = "/", transform = sectionDirectoryNaming)
    }

    companion object {
        val DEFAULT_IGNORED_SECTIONS = setOf(49384L, 49385L, 49386L)
    }
}
