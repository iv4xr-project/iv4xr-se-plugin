package spaceEngineers.js

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.events.KeyboardEvent
import org.w3c.xhr.XMLHttpRequest
import spaceEngineers.controller.*
import spaceEngineers.model.Vec3
import spaceEngineers.transport.StringLineReaderWriter

class XMLRequestReaderWriter(
    val url: String,
    val async: Boolean = false,
) : StringLineReaderWriter {

    var result: String? = null

    override fun sendAndReceiveLine(line: String): String {
        val request = XMLHttpRequest();
        request.open("POST", url, async)
        request.send("$line\n")
        return request.responseText
    }
}

class JsSpaceEngineers(url: String, async: Boolean = true) : JsonRpcSpaceEngineers(
    agentId = "browser",
    stringLineReaderWriter = XMLRequestReaderWriter(url = url, async = async)
) {

}

fun setInterval(timeMillis: Long, handler: () -> Unit) = GlobalScope.launch {
    while (true) {
        delay(timeMillis)
        handler()
    }
}

var moveVector = Vec3.ZERO

fun main() {
    val url = "http://192.168.0.164:9888/"
    println("Hello SE")
    val spaceEngineers: SpaceEngineers = JsSpaceEngineers(url = url, async = false)
    val asyncSE: SpaceEngineers = JsSpaceEngineers(url = url, async = true)
    println(spaceEngineers.observer.observe())

    var index = 0
    setInterval(250L) {
        val image = document.getElementById("img") as HTMLImageElement
        index += 1
        image.src = "ss.png?index=$index"
        try {
            asyncSE.observer.takeScreenshot("C:/Users/karel.hovorka/git/iv4xr-se-plugin/JvmClient/src/jsMain/resources/ss.png")
        } catch (e: Exception) {
        }

    }
    window.addEventListener("keydown", {
        keydown(it as KeyboardEvent)
    })
    window.addEventListener("keyup", {
        keyup(it as KeyboardEvent)
    })

    setInterval(30L) {
        if (moveVector != Vec3.ZERO) {
            try {
                asyncSE.character.moveAndRotate(moveVector)
            } catch (e: Exception) {
            }
        }
        moveVector = Vec3.ZERO

    }

}

fun mapVector(which: Int): Vec3? {
    return when (which) {
        87 -> {
            Vec3.FORWARD
        }
        83 -> {
            Vec3.BACKWARD
        }
        65 -> {
            Vec3.LEFT
        }
        68 -> {
            Vec3.RIGHT
        }
        else -> null
    }

}

fun keydown(event: KeyboardEvent) {
    mapVector(event.which)?.let {
        moveVector = it
    }
}

fun keyup(event: KeyboardEvent) {
    moveVector = Vec3.ZERO
}



