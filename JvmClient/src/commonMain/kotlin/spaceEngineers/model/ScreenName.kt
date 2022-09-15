package spaceEngineers.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.reflect.KClass


object ScreenNameSerializer: KSerializer<ScreenName> {
    override val descriptor: SerialDescriptor = String.serializer().descriptor
    override fun deserialize(decoder: Decoder): ScreenName {
        return ScreenName(decoder.decodeInline(descriptor).decodeString())
    }

    override fun serialize(encoder: Encoder, value: ScreenName) {
        encoder.encodeInline(descriptor).encodeString(value.name)
    }

}

@Serializable(ScreenNameSerializer::class)
@JvmInline
value class ScreenName(val name: String) {

    companion object {
        val Progress = "Progress".toScreenName()

        val Loading = "Loading".toScreenName()
        val CubeBuilder = "CubeBuilder".toScreenName()

        val MainMenu = spaceEngineers.controller.MainMenu::class.toScreenName()
        val GamePlay = spaceEngineers.controller.GamePlay::class.toScreenName()
        val Terminal = spaceEngineers.controller.Terminal::class.toScreenName()
        val Medicals = spaceEngineers.controller.Medicals::class.toScreenName()
        val MessageBox = spaceEngineers.controller.MessageBox::class.toScreenName()
        val JoinGame = spaceEngineers.controller.JoinGame::class.toScreenName()
        val ServerConnect = spaceEngineers.controller.ServerConnect::class.toScreenName()
    }
}

fun KClass<*>.toScreenName(): ScreenName {
    return simpleName?.toScreenName() ?: error("Class has no simpleName $this")
}

fun String.toScreenName(): ScreenName {
    return ScreenName(this)
}
