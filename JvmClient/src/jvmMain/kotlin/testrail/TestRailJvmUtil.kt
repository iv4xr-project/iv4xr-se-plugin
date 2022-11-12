package testrail

import io.ktor.client.plugins.auth.providers.*
import io.ktor.http.*
import org.apache.commons.lang3.StringUtils
import spaceEngineers.util.extractTo
import spaceEngineers.util.nonWhitespaceEquals
import testrail.model.Case
import testrail.model.Section
import java.io.ByteArrayInputStream
import java.io.File
import java.util.zip.ZipInputStream


suspend fun downloadEverything(
    username: String,
    password: String,
    sectionIds: List<Long> = listOf(49388, 50008),
    projectId: Long = TestRailClient.SE_PROJECT_ID,
    suiteId: Long = TestRailClient.SE_SUITE_ID,
    overwrite: Boolean = false,
    testCaseDirectory: File = File("./src/jvmTest/resources/testrail/features/"),
    mapDirectory: File = File("./src/jvmTest/resources/testrail/maps"),
) {
    val testRailClient = TestRailClient(
        baseUrl = Url("https://keenqa.testrail.io/"),
        authentication = BasicAuthCredentials(username, password),
    )
    val sectionHelper = testRailClient.sectionHelper()

    sectionIds.forEach { sectionId ->
        testRailClient.downloadSection(
            suiteId = suiteId,
            projectId = projectId,
            sectionId = sectionId,
            sectionHelper = sectionHelper,
            testCaseDirectory = testCaseDirectory,
            overwrite = overwrite,
        )
    }
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
    overwrite: Boolean = false,
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
