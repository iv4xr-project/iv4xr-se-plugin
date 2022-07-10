package testrail

import spaceEngineers.util.extractTo
import java.io.ByteArrayInputStream
import java.io.File
import java.util.zip.ZipInputStream

suspend fun TestRailClient.downloadCase(
    caseId: Long,
    outputDirectory: File = File("./src/jvmTest/resources/features/testrail/"),
    overwrite: Boolean = false,
) {
    val case = getCase(caseId)
    //val attachmentsForCase = getAttachmentsForCase(caseId)
    val outputFile = File(outputDirectory, "C$caseId.feature")
    if (overwrite || !outputFile.exists()) {
        outputFile.writeText(case.featureContent())
    }
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
