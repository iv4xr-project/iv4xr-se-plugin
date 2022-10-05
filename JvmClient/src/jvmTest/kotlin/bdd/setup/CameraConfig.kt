package bdd.setup

enum class CameraConfig(val text: String) {
    FIRST_PERSON("1st person"), THIRD_PERSON("3rd person");

    companion object {
        fun fromText(text: String): CameraConfig {
            return values().firstOrNull { it.text == text } ?: error("Don't know camera $text")
        }
    }
}
