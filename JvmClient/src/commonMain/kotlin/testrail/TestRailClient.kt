package testrail

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import testrail.model.*


class TestRailClient(
    val baseUrl: Url,
    val authentication: BasicAuthCredentials,
    val client: HttpClient = HttpClient() {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                },
            )
        }
        install(Auth) {
            basic {
                credentials { authentication }
            }
        }
    },
) {

    suspend inline fun <reified T> get(path: String): T {
        return client.get(baseUrl.toString() + API_PREFIX + path) {
        }.body<T>()
    }

    val API_PREFIX = "index.php?/api/v2/"

    suspend fun getCase(id: Long): Case {
        return get("get_case/$id")
    }

    suspend fun getCaseString(id: Long): String {
        return get("get_case/$id")
    }

    suspend fun getAttachment(id: String): ByteArray {
        return get("get_attachment/$id")
    }

    suspend fun getAttachmentsForCase(caseId: Long): AttachmentsForCase {
        return get("get_attachments_for_case/$caseId")
    }

    suspend fun getCases(projectId: Long, suiteId: Long): Cases {
        return get("get_cases/$projectId&suite_id=$suiteId")
    }

    suspend fun getSections(projectId: Long, suiteId: Long): Sections {
        return get("get_sections/$projectId&suite_id=$suiteId")
    }

    suspend fun getSectionsStr(projectId: Long, suiteId: Long): String {
        return get("get_sections/$projectId&suite_id=$suiteId")
    }

    suspend fun getAttachmentsForSuite(suiteId: Long): String {
        return get("get_attachments_for_suite/$suiteId")
    }

    suspend fun getSuite(suiteId: Long): Suite {
        return get("get_suite/$suiteId")
    }

    companion object {
        const val SE_PROJECT_ID = 3L
        const val SE_SUITE_ID = 225L
    }
}
