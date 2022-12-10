package bdd.setup

import bdd.*
import bdd.screenshots.ScreenshotSteps
import io.cucumber.core.backend.ObjectFactory
import io.cucumber.core.exception.CucumberException
import bdd.connection.Config
import bdd.connection.ConnectionManager
import bdd.connection.ConnectionSetup

private val bddRunConfig by lazy { Config.fromPropsOrEnv().apply { println(this) } }
private val cfg by lazy { ConnectionSetup.loadConfigFromFile(bddRunConfig.connectionSetupName, bddRunConfig.bddConfigPath) }

val globalCm: ConnectionManager by lazy { ConnectionManager(config = bddRunConfig, connectionSetup = cfg) }

class CustomObjectFactory @JvmOverloads constructor(
    val connectionManager: ConnectionManager = globalCm
) : ObjectFactory {


    private val instances: MutableMap<Class<*>, Any> = HashMap()
    override fun start() {
        // No-op
    }

    override fun stop() {
        instances.clear()
    }

    override fun addClass(clazz: Class<*>?): Boolean {
        return true
    }

    override fun <T : Any> getInstance(type: Class<T>): T {
        return when (type) {
            BlockAsserts::class.java -> instances.getOrPut(type) {
                BlockAsserts(connectionManager)
            }

            CharacterActions::class.java -> instances.getOrPut(type) {
                CharacterActions(connectionManager)
            }

            CharacterAsserts::class.java -> instances.getOrPut(type) {
                CharacterAsserts(connectionManager)
            }

            DebugAsserts::class.java -> instances.getOrPut(type) {
                DebugAsserts(connectionManager)
            }

            FloatingObjectAsserts::class.java -> instances.getOrPut(type) {
                FloatingObjectAsserts(connectionManager)
            }

            ScenarioSetupSteps::class.java -> instances.getOrPut(type) {
                ScenarioSetupSteps(connectionManager)
            }

            ScreenSteps::class.java -> instances.getOrPut(type) {
                ScreenSteps(connectionManager)
            }

            ScreenshotSteps::class.java -> instances.getOrPut(type) {
                ScreenshotSteps(connectionManager)
            }

            UtilSteps::class.java -> instances.getOrPut(type) {
                UtilSteps(connectionManager)
            }

            else -> fallback(type)
        }.let { type.cast(it) }
    }

    fun <T : Any> fallback(type: Class<T>): T {
        return instances.getOrPut(type) {
            cacheNewInstance(type)
        }.let { type.cast(it) }
    }

    private fun <T : Any> cacheNewInstance(type: Class<T>): T {
        try {
            val constructor = type.getConstructor()
            return constructor.newInstance()
        } catch (e: NoSuchMethodException) {
            throw CucumberException(
                String.format(
                    "" +
                            "%s does not have a public zero-argument constructor.\n" +
                            "\n" +
                            "To use dependency injection add an other ObjectFactory implementation such as:\n" +
                            " * cucumber-picocontainer\n" +
                            " * cucumber-spring\n" +
                            " * cucumber-jakarta-cdi\n" +
                            " * ...etc\n",
                    type
                ), e
            )
        } catch (e: Exception) {
            throw CucumberException(String.format("Failed to instantiate %s", type), e)
        }
    }
}
