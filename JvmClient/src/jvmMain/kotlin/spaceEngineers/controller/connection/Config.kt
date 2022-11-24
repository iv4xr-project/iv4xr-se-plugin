package spaceEngineers.controller.connection

import spaceEngineers.controller.connection.ConnectionSetup.Companion.CONNECTION_SETUP_DIR
import spaceEngineers.controller.extensions.toNullIfBlank
import spaceEngineers.controller.toFile
import java.io.File

enum class ExitMode {
    NEVER, AFTER_EACH_SCENARIO, AFTER_LAST_SCENARIO,
}

data class Config(
    val connectionSetupName: String,
    val outputDirectory: File,
    val screenshotMode: ScreenshotMode,
    val exitMode: ExitMode,
    val bddConfigPath: String,
    val scenarioPath: String,
) {

    val name: String
        get() = connectionSetupName.substringBefore(".").lowercase()

    companion object {
        val DEFAULT = Config(
            connectionSetupName = "SINGLE_COMPUTER_DEDICATED_DEV_KAREL.json",
            outputDirectory = File("./reports/"),
            screenshotMode = ScreenshotMode.ALWAYS,
            exitMode = ExitMode.AFTER_LAST_SCENARIO,
            bddConfigPath = CONNECTION_SETUP_DIR,
            scenarioPath = "./testrail/maps"
        )

        fun fromEnv(): Config {
            return fromGetter(System::getenv)
        }

        fun fromProps(): Config {
            return fromGetter(System::getProperty)
        }

        fun fromPropsOrEnv(default: Config = DEFAULT): Config {
            return fromGetter(::get, default)
        }

        fun get(key: String): String? {
            return System.getProperty(key) ?: System.getenv(key)
        }

        fun fromGetter(getter: (String) -> String?, default: Config = DEFAULT): Config {
            return Config(
                connectionSetupName = getter("connectionSetupName").toNullIfBlank() ?: default.connectionSetupName,
                outputDirectory = getter("outputDirectory").toNullIfBlank()?.toFile() ?: default.outputDirectory,
                screenshotMode = getter("screenshotMode").toNullIfBlank()?.let {
                    ScreenshotMode.valueOf(it)
                } ?: default.screenshotMode,
                exitMode = getter("exitMode").toNullIfBlank()?.let {
                    ExitMode.valueOf(it)
                } ?: default.exitMode,
                bddConfigPath = getter("bddConfigPath").toNullIfBlank() ?: default.bddConfigPath,
                scenarioPath = getter("scenarioPath") ?: default.scenarioPath,
            )
        }

        fun fromMap(map: Map<String, String?>, default: Config): Config {
            return fromGetter({ map[it] }, default = default)
        }


    }
}
