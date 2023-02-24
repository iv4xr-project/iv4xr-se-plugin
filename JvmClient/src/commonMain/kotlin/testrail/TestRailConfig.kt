package testrail

import io.ktor.client.plugins.auth.providers.*
import io.ktor.http.*

data class TestRailConfig(
    val username: String,
    val password: String,
    val suiteId: Long,
    val projectId: Long,
    val excludingTags: Set<String>,
    val ignoredParentSections: List<Long>,
    val testCaseDirectory: String,
    val mapsDirectory: String,
) {
    companion object {
        fun fromMap(map: Map<String, String>, default: TestRailConfig = DEFAULT_TESTRAIL_CONFIG): TestRailConfig {
            return TestRailConfig(
                username = map["username"] ?: default.username,
                password = map["password"] ?: default.password,
                suiteId = map["suite_id"]?.toLongOrNull() ?: default.suiteId,
                projectId = map["project_id"]?.toLongOrNull() ?: default.projectId,
                excludingTags = map["excluding_tags"]?.split(",")?.toSet() ?: default.excludingTags,
                ignoredParentSections = map["ignored_parent_sections"]?.split(",")?.map(String::toLong)
                    ?: default.ignoredParentSections,
                testCaseDirectory = map["test_case_directory"] ?: default.testCaseDirectory,
                mapsDirectory = map["maps_directory"] ?: default.mapsDirectory,
            )
        }
    }

    fun toClient(): TestRailClient {
        return TestRailClient(
            baseUrl = Url("https://keenqa.testrail.io/"),
            authentication = BasicAuthCredentials(username, password),
        )
    }
}

val DEFAULT_TESTRAIL_CONFIG = TestRailConfig(
    username = "bot@example.com",
    password = "testrailpass",
    suiteId = TestRailClient.SE_SUITE_ID,
    projectId = TestRailClient.SE_PROJECT_ID,
    excludingTags = setOf("ignore", "duplicate", "creative", "difficult", "todo"),
    ignoredParentSections = listOf(49384, 49385, 49386),
    testCaseDirectory = "./testrail/features/",
    mapsDirectory = "./testrail/maps/",
)
