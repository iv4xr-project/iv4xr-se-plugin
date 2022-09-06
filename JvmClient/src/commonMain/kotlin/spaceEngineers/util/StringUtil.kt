package spaceEngineers.util

fun String.stripWhitespace(): String {
    return replace("\\s".toRegex(), "")
}

fun String.nonWhitespaceEquals(other: String): Boolean {
    return stripWhitespace() == other.stripWhitespace()
}
