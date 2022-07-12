package testrail

import spaceEngineers.util.extractTo
import testrail.model.Case
import testrail.model.Section
import java.io.ByteArrayInputStream
import java.io.File
import java.util.zip.ZipInputStream

suspend fun TestRailClient.downloadCase(
    caseId: Long,
    projectId: Long = 3,
    outputDirectory: File = File("./src/jvmTest/resources/features/testrail/"),
    overwrite: Boolean = false,
) {
    val case = getCase(caseId)
    //val attachmentsForCase = getAttachmentsForCase(caseId)

    val ignoredSections = setOf(49384L, 49385L, 49386L)

    val sections = getSections(case, projectId).filter { it.id !in ignoredSections }.joinToString(separator = "/") {
        "${it.name}-${it.id}"
    }
    val dir = File(outputDirectory, sections).apply { mkdirs() }
    val outputFile = File(dir, "C$caseId.feature")
    if (overwrite || !outputFile.exists()) {
        outputFile.writeText(case.featureContent())
    }
}

suspend fun TestRailClient.getSections(case: Case, projectId: Long): List<Section> {
    val sectionsById = getSections(projectId = projectId, suiteId = case.suite_id).sections.associateBy {
        it.id
    }
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

suspend fun TestRailClient.downloadMaps(
    suiteId: Long,
    destination: File = File("./src/jvmTest/resources/game-saves")
): List<String>? =
    getSuite(suiteId).description?.trim()?.split(",")?.map { attachmentId ->
        attachmentId.also {
            ZipInputStream(ByteArrayInputStream(getAttachment(attachmentId))).extractTo(destination)
        }
    }
