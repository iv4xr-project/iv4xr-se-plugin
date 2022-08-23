package spaceEngineers.controller.extensions

fun String?.toNullIfBlank(): String? {
    return takeUnless { isNullOrBlank() }
}
