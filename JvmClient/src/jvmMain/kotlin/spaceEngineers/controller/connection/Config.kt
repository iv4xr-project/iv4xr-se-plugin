package spaceEngineers.controller.connection

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
) {

    companion object {
        val DEFAULT = Config(
            connectionSetupName = "default-name",
            outputDirectory = File("./reports/"),
            screenshotMode = ScreenshotMode.ALWAYS,
            exitMode = ExitMode.AFTER_LAST_SCENARIO,
        )

        fun fromEnv(): Config {
            return fromGetter { System.getenv(it) }
        }

        fun fromProps(): Config {
            return fromGetter { System.getProperty(it) }
        }

        fun fromPropsOrEnv(): Config {
            return fromGetter { System.getProperty(it) ?: System.getenv(it) }
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
                } ?: DEFAULT.exitMode
            )
        }


    }
}