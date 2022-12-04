package testrail

import io.ktor.client.plugins.auth.providers.*
import io.ktor.http.*
import spaceEngineers.controller.toFile
import spaceEngineers.util.extractTo
import testrail.model.Case
import java.io.ByteArrayInputStream
import java.io.File
import java.util.zip.ZipInputStream

suspend fun getTestrailPaths(
    sectionIds: List<Long>,
    username: String,
    password: String,
    ignoredParentSections: Set<Long>
): Map<Long, File> {
    val testRailClient = TestRailClient(
        baseUrl = Url("https://keenqa.testrail.io/"),
        authentication = BasicAuthCredentials(username, password),
    )
    val sectionHelper = testRailClient.sectionHelper(ignoredSections = ignoredParentSections)
    return sectionIds.associateWith {
        sectionHelper.sectionDirectory(it).toFile()
    }
}

suspend fun TestRailClient.downloadEverything(
    sectionIds: List<Long> = listOf(49388, 50008),
    projectId: Long = TestRailClient.SE_PROJECT_ID,
    suiteId: Long = TestRailClient.SE_SUITE_ID,
    overwrite: Boolean = false,
    ignoredParentSections: Set<Long> = SectionHelper.DEFAULT_IGNORED_SECTIONS,
    testCaseDirectory: File = File("./src/jvmTest/resources/testrail/features/"),
    mapDirectory: File = File("./src/jvmTest/resources/testrail/maps"),
): Map<Long, File> {
    val sectionHelper = sectionHelper(ignoredSections = ignoredParentSections)

    sectionIds.forEach { sectionId ->
        downloadSection(
            suiteId = suiteId,
            projectId = projectId,
            sectionId = sectionId,
            sectionHelper = sectionHelper,
            testCaseDirectory = testCaseDirectory,
            overwrite = overwrite,
        )
    }
    downloadMaps(suiteId = suiteId, destination = mapDirectory)
    return sectionIds.associateWith {
        sectionHelper.sectionDirectory(it).toFile()
    }
}

suspend fun TestRailClient.sectionHelper(
    projectId: Long = TestRailClient.SE_PROJECT_ID,
    suiteId: Long = TestRailClient.SE_SUITE_ID,
    ignoredSections: Set<Long> = setOf(49384L, 49385L, 49386L),
): SectionHelper {
    return SectionHelper(getSections(projectId = projectId, suiteId = suiteId), ignoredSections = ignoredSections)
}

suspend fun TestRailClient.downloadSection(
    sectionId: Long,
    suiteId: Long = TestRailClient.SE_SUITE_ID,
    projectId: Long = TestRailClient.SE_PROJECT_ID,
    sectionHelper: SectionHelper,
    overwrite: Boolean = false,
    excludingTags: Set<String> = setOf("ignore", "duplicate", "creative", "difficult", "todo"),
    testCaseDirectory: File = File("./src/jvmTest/resources/features/testrail-2/"),
) {

    val sectionChildren = (
        sectionHelper.allChildren(sectionId).map {
            it
        } + listOf(sectionHelper.sectionsById.getValue(sectionId))
        ).filter { section ->
        section.tags().none { tag -> tag.lowercase() in excludingTags }
    }

    val sectionChildrenIds = sectionChildren.map { it.id }

    getCases(suiteId = suiteId, projectId = projectId).cases
        .filter {
            it.tags().none { tag -> tag in excludingTags } &&
                it.section_id in sectionChildrenIds
        }
        .filter {
            it.custom_preconds != null
        }
        .forEach { case ->
            case.save(
                sectionHelper = sectionHelper,
                outputDirectory = testCaseDirectory,
                overwrite = overwrite,
            )
        }
}

suspend fun TestRailClient.downloadCase(
    caseId: Long,
    sectionHelper: SectionHelper,
    outputDirectory: File,
    overwrite: Boolean = false,
) {
    getCase(caseId).save(
        sectionHelper = sectionHelper,
        outputDirectory = outputDirectory,
        overwrite = overwrite,
    )
}

fun Case.save(
    sectionHelper: SectionHelper,
    outputDirectory: File,
    overwrite: Boolean = false,
) {
    val sectionsDirectory = sectionHelper.sectionDirectory(section_id)
    File(outputDirectory, sectionsDirectory).mkdirs()
    val outputFile = File(outputDirectory, sectionHelper.fileRelativePath(this))
    if (overwrite || !outputFile.exists()) {
        outputFile.writeText(featureContent())
    }
}

suspend fun TestRailClient.downloadMaps(
    suiteId: Long,
    destination: File,
): List<String>? =
    getSuite(suiteId).description?.trim()?.split(",")?.map { attachmentId ->
        attachmentId.also {
            ZipInputStream(ByteArrayInputStream(getAttachment(attachmentId))).extractTo(destination)
        }
    }
