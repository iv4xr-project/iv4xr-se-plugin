package spaceEngineers

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import environments.SocketReaderWriter
import environments.sendCommand_
import eu.iv4xr.framework.mainConcepts.W3DEnvironment
import java.io.Closeable
import java.lang.reflect.Modifier

class SpaceEngEnvironment(val socketReaderWriter: SocketReaderWriter) : W3DEnvironment(), Closeable {
    val gson = socketReaderWriter.gson

    public override fun sendCommand_(cmd: EnvOperation): Any? {
        return socketReaderWriter.sendCommand_(cmd)
    }

    fun <T> getSeResponse(req: SeRequest<T>): T {
        // WP note:
        // the actual id of the agent and the id of its target (if it interacts with
        // something) are put inside the req object ... :|
        val json = sendCommand("APlib", "Se", "request", gson.toJson(req)) as String
        // we do not have to cast to T, since req.responseType is of type Class<T>
        //System.out.println(json);
        return gson.fromJson(json, req.responseType)
    }

    override fun close() {
        socketReaderWriter.close()
    }

    companion object {
        const val DEFAULT_HOSTNAME = "localhost"

        const val DEFAULT_PORT = 9678

        val SPACE_ENG_GSON = GsonBuilder()
            .serializeNulls()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .create()

        fun localhost(): SpaceEngEnvironment {
            return SpaceEngEnvironment(
                SocketReaderWriter(
                    host = DEFAULT_HOSTNAME,
                    port = DEFAULT_PORT,
                    gson = SPACE_ENG_GSON
                )
            )
        }
    }

}