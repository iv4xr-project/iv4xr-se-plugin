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
) {

    val name: String
        get() = connectionSetupName.substringBefore(".").lowercase()

    companion object {
        val DEFAULT = Config(
            connectionSetupName = "OFFLINE_STEAM.json",
            outputDirectory = File("./reports/"),
            screenshotMode = ScreenshotMode.ALWAYS,
            exitMode = ExitMode.AFTER_LAST_SCENARIO,
            bddConfigPath = CONNECTION_SETUP_DIR,
        )

        fun fromEnv(): Config {
            return fromGetter(System::getenv)
        }

        fun fromProps(): Config {
            return fromGetter(System::getProperty)
        }

        fun fromPropsOrEnv(): Config {
            return fromGetter(::get)
        }

        fun get(key: String): String? {
            return System.getProperty(key) ?: System.getenv(key)
        }

        private fun fromGetter(getter: (String) -> String?): Config {
            return Config(
                connectionSetupName = getter("connectionSetupName").toNullIfBlank() ?: DEFAULT.connectionSetupName,
                outputDirectory = getter("outputDirectory").toNullIfBlank()?.toFile() ?: DEFAULT.outputDirectory,
                screenshotMode = getter("screenshotMode").toNullIfBlank()?.let {
                    ScreenshotMode.valueOf(it)
                } ?: DEFAULT.screenshotMode,
                exitMode = getter("exitMode").toNullIfBlank()?.let {
                    ExitMode.valueOf(it)
                } ?: DEFAULT.exitMode,
                bddConfigPath = getter("bddConfigPath").toNullIfBlank() ?: DEFAULT.bddConfigPath,
            )
        }


    }
}
