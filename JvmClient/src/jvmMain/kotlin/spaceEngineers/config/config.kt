package spaceEngineers.config

import spaceEngineers.controller.connection.Config
import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.connection.ConnectionSetup

// Lazily loaded configuration for bdd tests. The main reason is, because bdd glue needs to reference this, but the
// glue classes are created by the testing framework so this is kind of dirty way to provide connection context to them.
// For now has to be in main and not test sources. Everything is loaded lazily so shouldn't interfere with the normal code.
val bddRunConfig by lazy { Config.fromPropsOrEnv() }
val cfg by lazy { ConnectionSetup.loadConfigFromFile(bddRunConfig.connectionSetupName, bddRunConfig.bddConfigPath) }

val globalCm: ConnectionManager by lazy { ConnectionManager(config = bddRunConfig, connectionSetup = cfg) }

