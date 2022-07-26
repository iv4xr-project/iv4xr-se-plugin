package testrail

import io.ktor.client.plugins.auth.providers.*
import io.ktor.http.*
import spaceEngineers.util.extractTo
import testrail.model.Case
import testrail.model.Section
import java.io.ByteArrayInputStream
import java.io.File
import java.util.zip.ZipInputStream


suspend fun downloadEverything(
    username: String,
    password: String,
    sectionId: Long = 50008,
    projectId: Long = TestRailClient.SE_PROJECT_ID,
    suiteId: Long = TestRailClient.SE_SUITE_ID,
    testCaseDirectory: File = File("./src/jvmTest/resources/features/testrail-2/"),
    mapDirectory: File = File("./src/jvmTest/resources/game-saves"),
) {
    val testRailClient = TestRailClient(
        baseUrl = Url("https://keenqa.testrail.io/"),
        authentication = BasicAuthCredentials(username, password),
    )
    val sectionHelper = testRailClient.sectionHelper()

    testRailClient.downloadSection(
        suiteId = suiteId,
        projectId = projectId,
        sectionId = sectionId,
        sectionHelper = sectionHelper,
        testCaseDirectory = testCaseDirectory
    )
    testRailClient.downloadMaps(suiteId = suiteId, destination = mapDirectory)
}

suspend fun TestRailClient.sectionHelper(
    projectId: Long = TestRailClient.SE_PROJECT_ID,
    suiteId: Long = TestRailClient.SE_SUITE_ID
): SectionHelper {
    return SectionHelper(getSections(projectId = projectId, suiteId = suiteId))
}

suspend fun TestRailClient.downloadSection(
    sectionId: Long,
    suiteId: Long = TestRailClient.SE_SUITE_ID,
    projectId: Long = TestRailClient.SE_PROJECT_ID,
    sectionHelper: SectionHelper,
    excludingTags: Set<String> = setOf("ignore", "duplicate", "creative", "difficult", "todo"),
    testCaseDirectory: File = File("./src/jvmTest/resources/features/testrail-2/"),
) {

    val sectionChildren = (sectionHelper.allChildren(sectionId).map {
        it
    } + listOf(sectionHelper.sectionsById.getValue(sectionId))).filter { section ->
        section.tags().none { tag -> tag.lowercase() in excludingTags }
    }


    val sectionChildrenIds = sectionChildren.map { it.id }

    getCases(suiteId = suiteId, projectId = projectId).cases
        .filter {
            it.tags().none { tag -> tag in excludingTags } &&
                    it.section_id in sectionChildrenIds
        }
        .forEach { case ->
            case.save(
                sectionHelper = sectionHelper,
                outputDirectory = testCaseDirectory,
            )
        }
}

suspend fun TestRailClient.downloadCase(
    caseId: Long,
    sectionHelper: SectionHelper,
    outputDirectory: File,
    overwrite: Boolean = false,
    ignoredSections: Set<Long> = setOf(49384L, 49385L, 49386L),
    namingConvention: (Section) -> String = {
        "${it.name}-${it.id}"
    }
) {
    getCase(caseId).save(
        sectionHelper = sectionHelper,
        outputDirectory = outputDirectory,
        overwrite = overwrite,
        ignoredSections = ignoredSections,
        namingConvention = namingConvention,
    )
}

fun Case.save(
    sectionHelper: SectionHelper,
    outputDirectory: File,
    overwrite: Boolean = false,
    ignoredSections: Set<Long> = setOf(49384L, 49385L, 49386L),
    namingConvention: (Section) -> String = {
        "${it.name}-${it.id}"
    }
) {
    val sectionsDirectory =
        sectionHelper.sectionsOfCase(this).filter { it.id !in ignoredSections }
            .joinToString(separator = File.separator, transform = namingConvention)
    val dir = File(outputDirectory, sectionsDirectory).apply { mkdirs() }
    val outputFile = File(dir, "C${id}.feature")
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