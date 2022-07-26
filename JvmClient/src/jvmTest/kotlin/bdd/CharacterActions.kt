package bdd

import bdd.setup.connectClientsDirectly
import bdd.setup.connectToFirstFriendlyGame
import bdd.setup.createLobbyGame
import bdd.setup.exitToMainMenu
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.delay
import spaceEngineers.controller.extensions.blockingMoveForwardByDistance
import spaceEngineers.controller.extensions.grindDownToPercentage
import spaceEngineers.controller.extensions.toNullIfMinusOne
import spaceEngineers.controller.extensions.torchBackToMax
import spaceEngineers.model.*
import spaceEngineers.model.CharacterMovement
import spaceEngineers.model.DefinitionId.Companion.ID_PREFIX
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.model.extensions.normalizeAsRun
import spaceEngineers.movement.*
import kotlin.test.assertEquals


class CharacterActions : AbstractMultiplayerSteps() {

    @When("Character grinds to {double} integrity.")
    fun character_grinds_to_integrity(integrity: Double) = mainClient {
        val percentage = integrity / blockToGrind().maxIntegrity * 100.0
        grindDownToPercentage(percentage)
    }

    private fun blockToGrind(): Block = mainClient {
        observer.observeBlocks().allBlocks.first { it.id == context.lastNewBlockId }
    }


    @When("Character grinds to {double}% integrity.")
    fun character_grinds_until_to_integrity_percentage(percentage: Double) = mainClient {
        grindDownToPercentage(percentage)
    }


    @When("Character moves up for {int} ticks.")
    fun character_moves_up_for_ticks(ticks: Int) = mainClient {
        character.moveAndRotate(Vec3F.UP, ticks = ticks)
    }

    @When("Character moves {string} for {int} ticks.")
    fun character_moves_direction_for_ticks(direction: String, ticks: Int) = mainClient {
        val replayMovement = ReplayMovement(this)
        replayMovement.move(CompositeDirection3d.directionFromString(direction), ticks = ticks)
    }

    @When("Character runs forward for {int} ticks.")
    fun character_runs_forward_for_ticks(ticks: Int) = mainClient {
        character.moveAndRotate(Vec3F.FORWARD.normalizeAsRun(), ticks = ticks)
    }

    @When("Character crouches.")
    fun character_crouches() = mainClient {
        character.moveAndRotate(Vec3F.DOWN, ticks = 0)
    }

    @When("Character moves {string} for {int} ticks using {string}.")
    fun character_moves_for_ticks_using(direction: String, ticks: Int, movement: String) = mainClient {
        val movementType = if (movement == "crouch") {
            assertEquals(CharacterMovement.standing, observer.observe().movement.value.toInt())
            character.moveAndRotate(Vec3F.DOWN, ticks = 0)
            delay(50)
            assertEquals(CharacterMovement.crouching, observer.observe().movement.value.toInt())
            CharacterMovementType.WALK
        } else {
            CharacterMovementType.valueOf(movement.uppercase())
        }
        val movementWrapper = ReplayMovement(this)
        movementWrapper.move(
            CompositeDirection3d.directionFromString(direction),
            movementType = movementType,
            ticks = ticks
        )
    }

    @When("Character turns on jetpack.")
    fun character_turns_on_jetpack() = mainClient {
        character.turnOnJetpack()
        pauseAfterAction()
    }

    @When("Character turns off jetpack.")
    fun character_turns_off_jetpack() = mainClient {
        character.turnOffJetpack()
        pauseAfterAction()
    }

    @Then("Character uses.")
    fun character_uses() = mainClient {
        character.use()
        pauseAfterAction()
    }


    @When("Character selects block {string} and places it.")
    fun character_places_selects_block_and_places_it(blockType: String) = mainClient {
        val toolbar = items.toolbar()
        val toolbarLocation =
            toolbar.findLocation(blockType) ?: error("cannot find $blockType in toolbar")
        val definitionId =
            toolbar.items.first { it?.id?.type == blockType }?.id ?: error("Cannot find $blockType in toolbar")
        items.setToolbarItem(definitionId, toolbarLocation);
        items.equip(toolbarLocation)
        delay(150)
        blocks.place()
        items.equip(ToolbarLocation(9, 0))
    }

    @When("Character moves forward for {float} units.")
    fun character_moves_forward_for_units(units: Float) = mainClient {
        blockingMoveForwardByDistance(
            distance = units,
            startPosition = observer.observe().position,
            timeoutMs = 40000,
        )
    }

    @When("Character sets toolbar slot {int}, page {int} to {string}.")
    fun character_sets_toolbar_slot_page_to(slot: Int, page: Int, itemName: String) = mainClient {
        val location = ToolbarLocation(slot, page)

        items.setToolbarItem(DefinitionId.parse(itemName), location)

        // Note: maybe we should use the toolbar mapping instead, and have some way to define exact names of the tools
        if (itemName.contains("Welder")) {
            context.torchLocation = location
        } else if (itemName.contains("AngleGrinder")) {
            context.grinderLocation = location
        }
    }

    @When("Character torches block back up to max integrity.")
    fun character_torches_block_back_up_to_max_integrity() = mainClient {
        torchBackToMax(context.lastNewBlock!!)
    }

    @Given("Toolbar has mapping:")
    fun toolbar_has_mapping(dataTable: List<Map<String, String>>) = mainClient {
        context.updateToolbarLocation(dataTable)
    }

    @When("Character equips {string}.")
    fun character_equips(tool: String) = mainClient {
        val toolbarLocation = items.toolbar()
            .let { toolbar ->
                toolbar.findLocation(DefinitionId.parse(tool)) ?: error(
                    "Item $tool not found in the toolbar, found: ${
                        toolbar.items.filterNotNull().map { it.id }
                    }"
                )
            }
        items.equip(toolbarLocation)
        smallPause()
    }

    @When("Character turns off dampeners.")
    fun character_turns_off_dampeners() = mainClient {
        character.turnOffDampeners()
    }

    @When("Character turns on dampeners.")
    fun character_turns_on_dampeners() = mainClient {
        character.turnOnDampeners()
    }

    @When("Player saves the game as {string} and reloads.")
    fun player_saves_the_game_as_and_reloads(scenarioId: String) {
        val cs = testSetup.connectionManager.connectionSetup
        if (cs.ds) {
            exitToMainMenu()
            connectClientsDirectly(waitForMedical = false)
        } else if (cs.lobby) {
            mainClient {
                screens.gamePlay.showMainMenu()
                smallPause()
                screens.mainMenu.saveAs()
                smallPause()
                screens.saveAs.setName(scenarioId)
                smallPause()
                screens.saveAs.pressOk()
                pause()
                exitToMainMenu {
                    throw it
                }
                //TODO: remove pause, wait until the game is in main menu
                pause()
                createLobbyGame(scenarioId, filterSaved = false)
                connectToFirstFriendlyGame()

            }
        }
    }

    @When("Character jumps.")
    fun character_jumps() = mainClient {
        //character.jump()
        this.input.startPlaying(
            listOf(
                FrameSnapshot(
                    InputSnapshot(
                        keyboard = KeyboardSnapshot(pressedKeys = listOf(32), text = listOf(' '))
                    )
                )
            )
        )
    }

    @When("Character unparks.")
    fun character_unparks() = mainClient {
        if (character.switchParkedStatus()) {
            character.switchParkedStatus()
        }
    }

    @When("Uses item {int} from toolbar.")
    fun uses_item_from_toolbar(indexStarting1: Int) = mainClient {
        //TODO: more robust, don't send keys
        //items.activate(ToolbarLocation(page = 0, slot = indexStarting1 - 1))
        delay(500)
        val chr = when (indexStarting1) {
            1 -> '+'
            2 -> 'ě'
            3 -> 'š'
            4 -> 'č'
            else -> error("no char for index $indexStarting1")
        }
        this.input.startPlaying(
            listOf(
                FrameSnapshot(
                    InputSnapshot(
                        keyboard = KeyboardSnapshot(pressedKeys = listOf(49), text = listOf(chr))
                    )
                )
            )
        )
    }

    @When("Character turns on relative dampeners.")
    fun character_turns_on_relative_dampeners() = mainClient {
        character.turnOnRelativeDampeners()
    }

    @Given("Character {string} the {string} mouse button.")
    fun character_the_mouse_button(mouseActionName: String, mouseButtonName: String) = mainClient {
        val clickCount = when (mouseActionName) {
            "clicks" -> {
                1
            }
            "double-clicks" -> {
                2
            }
            else -> error("No action handler for $mouseActionName, try 'clicks' or 'double-clicks'.")
        }

        input.startPlaying(
            FrameSnapshot.clicks(
                mouseButton = MouseButton.valueOf(mouseButtonName.uppercase()),
                clickCount = clickCount
            )
        )
    }

    @Given("Character shoots.")
    fun character_shoots() = mainClient {
        input.startPlaying(
            FrameSnapshot.shoots()
        )
    }

    @When("Character drops {string} from the inventory.")
    fun character_drops_from_the_inventory(definitionIdStr: String) = mainClient {
        val definitionId = DefinitionId.parse(definitionIdStr)
        observer.observe().inventory.items.indexOfFirst { it.id == definitionId }.toNullIfMinusOne()
            ?: error("Item $definitionId not found in inventory")
        character.showInventory()
        with(screens.terminal.inventory) {
            val firstLeftInventory = this.data().leftInventories.first()
            val itemIndex = firstLeftInventory.items.indexOfFirst { it.id == definitionId }.toNullIfMinusOne()
                ?: error("Item $definitionId not found in inventory (2)")
            left.selectItem(itemIndex)
            dropSelected()
        }
    }

    @When("Character takes {string} from target block's inventory.")
    fun character_takes_from_target_block_s_inventory(definitionIdStr: String) = mainClient {
        character.showInventory()
        val definitionId = DefinitionId.parse(definitionIdStr)
        with(screens.terminal) {
            val data = inventory.data()
            val itemId = data.rightInventories.first().items.firstOrNull { it.id == definitionId }?.itemId
                ?: error("Item $definitionId not found in the right inventory. Found: ${data.rightInventories.first().items.map { it.id }}")
            inventory.transferInventoryItemToLeft(0, 0, itemId)
            close()
        }
    }

    @When("Character takes {string} from target block's inventory unless he already has one.")
    fun character_takes_from_target_block_s_inventory_unless_he_already_has_one(definitionIdStr: String) = mainClient {
        val definitionId = DefinitionId.parse(definitionIdStr)
        if (observer.observe().inventory.items.map { it.id }.contains(definitionId)) {
            return@mainClient
        }
        character_takes_from_target_block_s_inventory(definitionIdStr)
    }


    @When("Character closes the terminal.")
    fun character_closes_the_terminal() = mainClient {
        screens.terminal.close()
    }

    @Given("Character uses control {string}.")
    fun character_uses_control(controlName: String) {
        character_uses_control_for_seconds(controlName, 1)
    }

    @Given("Character uses control {string} for {int} ticks.")
    fun character_uses_control_for_seconds(controlName: String, ticks: Int) = mainClient {
        when (controlName) {
            "PRIMARY_TOOL_ACTION" -> {
                input.startPlaying(FrameSnapshot.clicks())
            }
            "SECONDARY_TOOL_ACTION" -> {
                input.startPlaying(FrameSnapshot.clicks(MouseButton.RIGHT))
            }
            else -> error("Not implemented control $controlName")
        }
    }

    @When("Character unequips.")
    fun character_unequips() = mainClient {
        items.unEquipWeapon()
    }

    @When("Character aims down.")
    fun character_aims_down() = mainClient {
        character.moveAndRotate(rotation3 = Vec2F.ROTATE_DOWN * 10f, ticks = 240)
    }

    @When("Character begins using tool.")
    fun character_starts_using_tool() = mainClient {
        character.beginUsingTool()
    }

    @When("Character ends using tool.")
    fun character_ends_using_tool() = mainClient {
        character.endUsingTool()
    }

    @When("Character opens toolbar config.")
    fun character_opens_toolbar_config() = mainClient {
        screens.gamePlay.showToolbarConfig()
    }

    @When("Character selects {string} category.")
    fun character_selects_category(category: String) = mainClient {
        with(screens.toolbarConfig) {
            val data = data()
            val categoryIndex = data.categories.indexOf(category).toNullIfMinusOne()
                ?: error("Missing category $category, possible: ${data.categories}")
            delay(50)
            selectCategory(categoryIndex)
        }
        delay(50)

    }

    @When("Drags any Emote to the toolbar's slot {int}.")
    fun drags_any_emote_to_the_toolbar_s_slot(slotIndexStarting1: Int) = mainClient {
        with(screens.toolbarConfig) {
            val data = data()
            println(data)
            //println(data.gridItems.filterNotNull())
            val emoteDefinitions = setOf("${ID_PREFIX}EmoteDefinition", "${ID_PREFIX}AnimationDefinition")
            val emotes = data.gridItems.filterNotNull().filter { it.id in emoteDefinitions }
            val randomEmote = emotes.random()
            val randomEmoteIndex =
                data.gridItems.indexOf(randomEmote).toNullIfMinusOne() ?: error("Random emote not found $randomEmote")
            dropGridItemToToolbar(randomEmoteIndex, slotIndexStarting1 - 1)
        }
    }


    @When("Character closes the toolbar config screen.")
    fun character_closes_the_toolbar_config_screen() = mainClient {
        screens.toolbarConfig.close()
        delay(500)
    }

    //  MySession.Static.LocalCharacter.AnimationController.Controller.GetLayerByName("Body").CurrentNode.Name !!!!!!

}
