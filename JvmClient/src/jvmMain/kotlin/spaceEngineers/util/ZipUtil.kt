package spaceEngineers.util

import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

fun ZipInputStream.extractTo(target: File) = use { zip ->
    var entry: ZipEntry
    while (zip.nextEntry.also { entry = it ?: return } != null) {
        val file = File(target, entry.name)
        if (entry.isDirectory) {
            file.mkdirs()
        } else {
            file.parentFile.mkdirs()
            zip.copyTo(file.outputStream())
        }
    }
}
