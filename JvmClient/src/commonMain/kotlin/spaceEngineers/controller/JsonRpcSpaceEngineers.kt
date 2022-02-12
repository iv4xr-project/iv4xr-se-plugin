package spaceEngineers.controller

import kotlinx.serialization.InternalSerializationApi
import spaceEngineers.model.*
import spaceEngineers.navigation.NavGraph
import spaceEngineers.transport.StringLineReaderWriter


@OptIn(InternalSerializationApi::class, kotlin.ExperimentalStdlibApi::class)
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
    val screensPrefix: String = "Screens.",
) :
    SpaceEngineers, RpcSerializer(stringLineReaderWriter) {

    override val session: Session = object : Session {
        override fun loadScenario(scenarioPath: String) {
            processSingleParameterMethod<String, Unit>(
                parameter = scenarioPath,
                parameterName = "scenarioPath",
                methodName = "${sessionPrefix}LoadScenario",
                parameterType = String::class,
            )
        }

        override fun connect(address: String) {
            processSingleParameterMethod<String, Unit>(
                parameter = address,
                parameterName = "address",
                methodName = "${sessionPrefix}Connect",
                parameterType = String::class,
            )
        }

        override fun disconnect() {
            processNoParameterMethod<Unit>(
                methodName = "${sessionPrefix}Disconnect",
            )
        }

        override fun exitGame() {
            processNoParameterMethod<Unit>(
                methodName = "${sessionPrefix}ExitGame",
            )
        }
    }

    override val character: Character = object : Character {
        override fun use() {
            return processNoParameterMethod<Unit>(
                methodName = "${characterPrefix}Use"
            )
        }

        override fun moveAndRotate(movement: Vec3F, rotation3: Vec2F, roll: Float, ticks: Int): CharacterObservation {
            return processParameters<CharacterObservation>(
                parameters = listOf(
                    TypedParameter("movement", movement, Vec3F::class),
                    TypedParameter("rotation3", rotation3, Vec2F::class),
                    TypedParameter("roll", roll, Float::class),
                    TypedParameter("ticks", ticks, Int::class)
                ),
                methodName = "${characterPrefix}MoveAndRotate",
            )
        }

        override fun turnOnJetpack(): CharacterObservation {
            return processNoParameterMethod<CharacterObservation>(
                methodName = "${characterPrefix}TurnOnJetpack"
            )
        }

        override fun turnOffJetpack(): CharacterObservation {
            return processNoParameterMethod<CharacterObservation>(
                methodName = "${characterPrefix}TurnOffJetpack"
            )
        }

        override fun beginUsingTool() {
            processNoParameterMethod<Unit>("${characterPrefix}BeginUsingTool")
        }

        override fun endUsingTool() {
            processNoParameterMethod<Unit>("${characterPrefix}EndUsingTool")
        }

        override fun switchHelmet(): CharacterObservation {
            return processNoParameterMethod<CharacterObservation>(
                methodName = "${characterPrefix}SwitchHelmet"
            )
        }
    }

    override val items: Items = object : Items {
        override fun equip(toolbarLocation: ToolbarLocation) {
            processSingleParameterMethod<ToolbarLocation, Unit>(
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
                methodName = "${itemsPrefix}SetToolbarItem"
            )
        }

        override fun toolbar(): Toolbar {
            return processNoParameterMethod<Toolbar>(
                methodName = "${itemsPrefix}Toolbar"
            )
        }
    }

    override val blocks: Blocks = object : Blocks {
        override fun place() {
            processNoParameterMethod<Unit>("${blocksPrefix}Place")
        }
    }
    override val admin: SpaceEngineersAdmin = object : SpaceEngineersAdmin {
        override val blocks: BlocksAdmin = object : BlocksAdmin {
            override fun placeAt(
                blockDefinitionId: DefinitionId,
                position: Vec3F,
                orientationForward: Vec3F,
                orientationUp: Vec3F
            ): String {
                return processParameters<String>(
                    parameters = listOf(
                        TypedParameter("blockDefinitionId", blockDefinitionId, DefinitionId::class),
                        TypedParameter("position", position, Vec3F::class),
                        TypedParameter("orientationForward", orientationForward, Vec3F::class),
                        TypedParameter("orientationUp", orientationUp, Vec3F::class),
                    ),
                    methodName = "${adminPrefix}${blocksPrefix}PlaceAt"
                )
            }

            override fun placeInGrid(
                blockDefinitionId: DefinitionId,
                gridId: String,
                minPosition: Vec3I,
                orientationForward: Vec3I,
                orientationUp: Vec3I
            ): String {
                return processParameters<String>(
                    parameters = listOf(
                        TypedParameter("blockDefinitionId", blockDefinitionId, DefinitionId::class),
                        TypedParameter("minPosition", minPosition, Vec3I::class),
                        TypedParameter("gridId", gridId, String::class),
                        TypedParameter("orientationForward", orientationForward, Vec3I::class),
                        TypedParameter("orientationUp", orientationUp, Vec3I::class),
                    ),
                    methodName = "${adminPrefix}${blocksPrefix}PlaceInGrid"
                )
            }

            override fun remove(blockId: String) {
                processSingleParameterMethod<String, Unit>(
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
                    methodName = "${adminPrefix}${blocksPrefix}SetIntegrity"
                )
            }
        }

        override val character: CharacterAdmin = object : CharacterAdmin {
            override fun teleport(
                position: Vec3F,
                orientationForward: Vec3F?,
                orientationUp: Vec3F?
            ): CharacterObservation {
                return processParameters<CharacterObservation>(
                    parameters = listOf(
                        TypedParameter("position", position, Vec3F::class),
                        TypedParameter("orientationForward", orientationForward, Vec3F::class),
                        TypedParameter("orientationUp", orientationUp, Vec3F::class),
                    ),
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
                    methodName = "${adminPrefix}${characterPrefix}Use"
                )
            }

            override fun create(
                name: String,
                position: Vec3F,
                orientationForward: Vec3F,
                orientationUp: Vec3F
            ): CharacterObservation {
                return processParameters<CharacterObservation>(
                    parameters = listOf(
                        TypedParameter("name", name, String::class),
                        TypedParameter("position", position, Vec3F::class),
                        TypedParameter("orientationForward", orientationForward, Vec3F::class),
                        TypedParameter("orientationUp", orientationUp, Vec3F::class),
                    ),
                    methodName = "${adminPrefix}${characterPrefix}Create"
                )
            }

            override fun switch(id: String) {
                return processSingleParameterMethod<String, Unit>(
                    methodName = "${adminPrefix}${characterPrefix}Switch",
                    parameter = id,
                    parameterName = "id",
                    parameterType = String::class,
                )
            }

            override fun remove(id: String) {
                return processSingleParameterMethod<String, Unit>(
                    methodName = "${adminPrefix}${characterPrefix}Remove",
                    parameter = id,
                    parameterName = "id",
                    parameterType = String::class,
                )
            }
        }
        override val observer: ObserverAdmin = object : ObserverAdmin {
            override fun observeCharacters(): List<CharacterObservation> {
                return processNoParameterMethod<List<CharacterObservation>>(
                    methodName = "${adminPrefix}${observerPrefix}ObserveCharacters"
                )
            }

        }

        override fun setFrameLimitEnabled(enabled: Boolean) {
            return processSingleParameterMethod(
                methodName = "${adminPrefix}SetFrameLimitEnabled",
                parameter = enabled,
                parameterName = "enabled",
                parameterType = Boolean::class,
            )
        }
    }

    override val screens: Screens = object : Screens {
        override fun focusedScreen(): String {
            return processNoParameterMethod("${screensPrefix}FocusedScreen")
        }

        override fun waitUntilTheGameLoaded() {
            return processNoParameterMethod("${screensPrefix}WaitUntilTheGameLoaded")
        }

        val medicalScreenPrefix = "${screensPrefix}Medicals."

        override val medicals: Medicals = object : Medicals {
            override fun medicalRooms(): List<MedicalRoom> {
                return processNoParameterMethod<List<MedicalRoom>>("${medicalScreenPrefix}MedicalRooms")
            }

            override fun respawn(roomIndex: Int) {
                processSingleParameterMethod<Int, Unit>(
                    parameter = roomIndex,
                    parameterName = "roomIndex",
                    parameterType = Int::class,
                    methodName = "${medicalScreenPrefix}Respawn"
                )
            }

            override fun factions(): List<Faction> {
                return processNoParameterMethod<List<Faction>>("${medicalScreenPrefix}Factions")
            }

            override fun chooseFaction(factionIndex: Int) {
                processSingleParameterMethod<Int, Unit>(
                    parameter = factionIndex,
                    parameterName = "factionIndex",
                    parameterType = Int::class,
                    methodName = "${medicalScreenPrefix}ChooseFaction"
                )
            }
        }
        override val terminal: Terminal = object : Terminal {

            val terminalScreenPrefix = "${screensPrefix}Terminal."

            override fun data(): TerminalScreenData {
                return processNoParameterMethod<TerminalScreenData>("${terminalScreenPrefix}Data")
            }

            override fun selectTab(index: Int) {
                processSingleParameterMethod<Int, Unit>(
                    methodName = "${terminalScreenPrefix}SelectTab",
                    parameter = index,
                    parameterName = "index",
                    parameterType = Int::class
                )
            }

            override fun addToProductionQueue(index: Int) {
                processSingleParameterMethod<Int, Unit>(
                    methodName = "${terminalScreenPrefix}AddToProductionQueue",
                    parameter = index,
                    parameterName = "index",
                    parameterType = Int::class
                )
            }

            override fun removeFromProductionQueue(index: Int) {
                processSingleParameterMethod<Int, Unit>(
                    methodName = "${terminalScreenPrefix}RemoveFromProductionQueue",
                    parameter = index,
                    parameterName = "index",
                    parameterType = Int::class
                )
            }

            override fun selectBlueprint(index: Int) {
                processSingleParameterMethod<Int, Unit>(
                    methodName = "${terminalScreenPrefix}SelectBlueprint",
                    parameter = index,
                    parameterName = "index",
                    parameterType = Int::class
                )
            }

            override fun selectAssembler(index: Int) {
                processSingleParameterMethod<Int, Unit>(
                    methodName = "${terminalScreenPrefix}SelectAssembler",
                    parameter = index,
                    parameterName = "index",
                    parameterType = Int::class
                )
            }

            override fun enterBlueprintSearchBox(text: String) {
                processSingleParameterMethod<String, Unit>(
                    methodName = "${terminalScreenPrefix}EnterBlueprintSearchBox",
                    parameter = text,
                    parameterName = "text",
                    parameterType = String::class
                )
            }

            override fun toggleProductionRepeatMode() {
                return processNoParameterMethod(
                    "${terminalScreenPrefix}ToggleProductionRepeatMode"
                )
            }

            override fun toggleProductionCooperativeMode() {
                return processNoParameterMethod(
                    "${terminalScreenPrefix}ToggleProductionCooperativeMode"
                )
            }
        }

    }

    override val observer: Observer = object : Observer {
        override fun observe(): CharacterObservation {
            return processNoParameterMethod<CharacterObservation>("${observerPrefix}Observe")
        }

        override fun observeBlocks(): Observation {
            return processNoParameterMethod<Observation>("${observerPrefix}ObserveBlocks")
        }

        override fun observeNewBlocks(): Observation {
            return processNoParameterMethod<Observation>(
                methodName = "${observerPrefix}ObserveNewBlocks"
            )
        }

        override fun observeCharacters(): List<CharacterObservation> {
            return processNoParameterMethod<List<CharacterObservation>>(
                methodName = "${observerPrefix}ObserveCharacters"
            )
        }

        override fun navigationGraph(): NavGraph {
            return processNoParameterMethod("${observerPrefix}NavigationGraph")
        }

        override fun takeScreenshot(absolutePath: String) {
            return processSingleParameterMethod<String, Unit>(
                parameter = absolutePath,
                parameterName = "absolutePath",
                methodName = "${observerPrefix}TakeScreenshot",
                parameterType = String::class,
            )
        }

        override fun switchCamera() {
            return processNoParameterMethod<Unit>(
                methodName = "${observerPrefix}SwitchCamera"
            )
        }
    }
    override val definitions: Definitions = object : Definitions {
        override fun blockDefinitions(): List<BlockDefinition> {
            return processNoParameterMethod<List<BlockDefinition>>(
                methodName = "${definitionsPrefix}BlockDefinitions",
            )
        }

        override fun allDefinitions(): List<DefinitionBase> {
            return processNoParameterMethod<List<DefinitionBase>>(
                methodName = "${definitionsPrefix}AllDefinitions",
            )
        }

        override fun blockHierarchy(): Map<String, String> {
            return processNoParameterMethod(
                methodName = "${definitionsPrefix}BlockHierarchy",
            )
        }

        override fun blockDefinitionHierarchy(): Map<String, String> {
            return processNoParameterMethod(
                methodName = "${definitionsPrefix}BlockDefinitionHierarchy",
            )
        }
    }
}
