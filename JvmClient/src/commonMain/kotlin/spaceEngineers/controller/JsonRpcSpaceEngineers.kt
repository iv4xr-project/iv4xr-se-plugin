package spaceEngineers.controller

import spaceEngineers.model.*
import spaceEngineers.transport.StringLineReaderWriter


open class JsonRpcSpaceEngineers(
    val agentId: String,
    stringLineReaderWriter: StringLineReaderWriter,
    val characterPrefix: String = "Character.",
    val itemsPrefix: String = "Items.",
    val observerPrefix: String = "Observer.",
    val sessionPrefix: String = "Session.",
    val definitionsPrefix: String = "Definitions.",
    val blocksPrefix: String = "Blocks.",
    val adminPrefix: String = "Admin.",
) :
    SpaceEngineers, RpcSerializer(stringLineReaderWriter) {

    override val session: Session = object : Session {
        override fun loadScenario(scenarioPath: String) {
            processSingleParameterMethod(
                method = ::loadScenario,
                parameter = scenarioPath,
                parameterName = "scenarioPath",
                methodName = "${sessionPrefix}LoadScenario",
                parameterType = String::class,
            )
        }
    }

    override val character: Character = object : Character {
        override fun use() {
            return processNoParameterMethod<Unit>(
                method = ::use,
                methodName = "${characterPrefix}Use"
            )
        }

        override fun moveAndRotate(movement: Vec3, rotation3: Vec2, roll: Float): CharacterObservation {
            return processParameters<CharacterObservation>(
                parameters = listOf(
                    TypedParameter("movement", movement, Vec3::class),
                    TypedParameter("rotation3", rotation3, Vec2::class),
                    TypedParameter("roll", roll, Float::class),
                ),
                method = ::moveAndRotate,
                methodName = "${characterPrefix}MoveAndRotate",
            )
        }

        override fun turnOnJetpack(): CharacterObservation {
            return processNoParameterMethod<CharacterObservation>(
                method = ::turnOnJetpack,
                methodName = "${characterPrefix}TurnOnJetpack"
            )
        }

        override fun turnOffJetpack(): CharacterObservation {
            return processNoParameterMethod<CharacterObservation>(
                method = ::turnOffJetpack,
                methodName = "${characterPrefix}TurnOffJetpack"
            )
        }

        override fun beginUsingTool() {
            processNoParameterMethod<Unit>(::beginUsingTool, "${characterPrefix}BeginUsingTool")
        }

        override fun endUsingTool() {
            processNoParameterMethod<Unit>(::endUsingTool, "${characterPrefix}EndUsingTool")
        }

        override fun switchHelmet(): CharacterObservation {
            return processNoParameterMethod<CharacterObservation>(
                method = ::switchHelmet,
                methodName = "${characterPrefix}SwitchHelmet"
            )
        }
    }

    override val items: Items = object : Items {
        override fun equip(toolbarLocation: ToolbarLocation) {
            processSingleParameterMethod<ToolbarLocation, Unit>(
                method = ::equip,
                parameter = toolbarLocation,
                parameterName = "toolbarLocation",
                parameterType = ToolbarLocation::class,
                methodName = "${itemsPrefix}Equip"
            )
        }

        override fun setToolbarItem(name: String, toolbarLocation: ToolbarLocation) {
            processParameters<Unit>(
                parameters = listOf(
                    TypedParameter("name", name, String::class),
                    TypedParameter("toolbarLocation", toolbarLocation, ToolbarLocation::class),
                ),
                method = ::setToolbarItem,
                methodName = "${itemsPrefix}SetToolbarItem"
            )
        }

        override fun getToolbar(): Toolbar {
            return processNoParameterMethod<Toolbar>(
                method = ::getToolbar,
                methodName = "${itemsPrefix}GetToolbar"
            )
        }
    }

    override val blocks: Blocks = object : Blocks {
        override fun place() {
            processNoParameterMethod<Unit>(::place, "${blocksPrefix}Place")
        }
    }
    override val admin: SpaceEngineersAdmin = object : SpaceEngineersAdmin {
        override val blocks: BlocksAdmin = object : BlocksAdmin {
            override fun placeAt(blockDefinitionId: DefinitionId, position: Vec3, orientationForward: Vec3, orientationUp: Vec3): String {
                return processParameters<String>(
                    parameters = listOf(
                        TypedParameter("blockDefinitionId", blockDefinitionId, DefinitionId::class),
                        TypedParameter("position", position, Vec3::class),
                        TypedParameter("orientationForward", orientationForward, Vec3::class),
                        TypedParameter("orientationUp", orientationUp, Vec3::class),
                    ),
                    method = ::placeAt,
                    methodName = "${adminPrefix}${blocksPrefix}PlaceAt"
                )
            }

            override fun remove(blockId: String) {
                processSingleParameterMethod<String, Unit>(
                    method = ::remove,
                    methodName = "${adminPrefix}${blocksPrefix}Remove",
                    parameterName = "blockId",
                    parameter = blockId,
                    parameterType = String::class,
                )
            }

            override fun setIntegrity(blockId: String, integrity: Float) {
                processParameters<Unit>(
                    parameters = listOf(
                        TypedParameter("blockId", blockId, String::class),
                        TypedParameter("integrity", integrity, Float::class),
                    ),
                    method = ::setIntegrity,
                    methodName = "${adminPrefix}${blocksPrefix}SetIntegrity"
                )
            }
        }

        override val character: CharacterAdmin = object : CharacterAdmin {
            override fun teleport(
                position: Vec3,
                orientationForward: Vec3?,
                orientationUp: Vec3?
            ): CharacterObservation {
                return processParameters<CharacterObservation>(
                    parameters = listOf(
                        TypedParameter("position", position, Vec3::class),
                        TypedParameter("orientationForward", orientationForward, Vec3::class),
                        TypedParameter("orientationUp", orientationUp, Vec3::class),
                    ),
                    method = ::teleport,
                    methodName = "${adminPrefix}${characterPrefix}Teleport"
                )
            }

            override fun use(blockId: String, functionIndex: Int, action: Int) {
                return processParameters<Unit>(
                    parameters = listOf(
                        TypedParameter("blockId", blockId, String::class),
                        TypedParameter("functionIndex", functionIndex, Int::class),
                        TypedParameter("action", action, Int::class),
                    ),
                    method = ::use,
                    methodName = "${adminPrefix}${characterPrefix}Use"
                )
            }
        }

        override fun setFrameLimitEnabled(enabled: Boolean) {
            return processSingleParameterMethod(
                method = ::setFrameLimitEnabled,
                methodName = "${adminPrefix}SetFrameLimitEnabled",
                parameter = enabled,
                parameterName = "enabled",
                parameterType = Boolean::class,
            )
        }
    }

    override val observer: Observer = object : Observer {
        override fun observe(): CharacterObservation {
            return processNoParameterMethod<CharacterObservation>(::observe, "${observerPrefix}Observe")
        }

        override fun observeBlocks(): Observation {
            return processNoParameterMethod<Observation>(::observeBlocks, "${observerPrefix}ObserveBlocks")
        }

        override fun observeNewBlocks(): Observation {
            return processNoParameterMethod<Observation>(
                method = ::observeNewBlocks,
                methodName = "${observerPrefix}ObserveNewBlocks"
            )
        }

        override fun takeScreenshot(absolutePath: String) {
            return processSingleParameterMethod<String, Unit>(
                parameter = absolutePath,
                parameterName = "absolutePath",
                method = ::takeScreenshot,
                methodName = "${observerPrefix}TakeScreenshot",
                parameterType = String::class,
            )
        }
    }
    override val definitions: Definitions = object : Definitions {
        override fun blockDefinitions(): List<BlockDefinition> {
            return processNoParameterMethod<List<BlockDefinition>>(
                method = ::blockDefinitions,
                methodName = "${definitionsPrefix}BlockDefinitions",
            )
        }

        override fun allDefinitions(): List<DefinitionBase> {
            return processNoParameterMethod<List<DefinitionBase>>(
                method = ::allDefinitions,
                methodName = "${definitionsPrefix}AllDefinitions",
            )
        }

        override fun blockHierarchy(): Map<String, String> {
            return processNoParameterMethod(
                method = ::blockHierarchy,
                methodName = "${definitionsPrefix}BlockHierarchy",
            )
        }
    }
}
