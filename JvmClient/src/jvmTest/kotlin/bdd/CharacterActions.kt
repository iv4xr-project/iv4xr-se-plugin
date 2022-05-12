package bdd

import io.cucumber.java.PendingException
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.delay
import spaceEngineers.controller.extensions.blockingMoveForwardByDistance
import spaceEngineers.controller.extensions.grindDownToPercentage
import spaceEngineers.controller.extensions.torchBackToMax
import spaceEngineers.model.*
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.model.extensions.normalizeAsMovement
import spaceEngineers.model.extensions.normalizeAsRun
import spaceEngineers.movement.CompositeDirection3d
import spaceEngineers.movement.ReplayMovement
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

    @When("Character moves forward for {int} ticks.")
    fun character_moves_forward_for_ticks(ticks: Int) = mainClient {
        val replayMovement = ReplayMovement(this)
        replayMovement.move(CompositeDirection3d.FORWARD, ticks = ticks)
    }

    @When("Character moves backward for {int} ticks.")
    fun character_moves_backward_for_ticks(ticks: Int) = mainClient {
        val replayMovement = ReplayMovement(this)
        replayMovement.move(CompositeDirection3d.BACKWARD, ticks = ticks)
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
        val vector = Vec3F.directionFromString(direction)

        character.moveAndRotate(vector.normalizeAsMovement(movementType), ticks = ticks)
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
        val toolbarLocation = items.toolbar().findLocation(tool) ?: error("Item $tool not found in the toolbar!")
        items.equip(toolbarLocation)
        pause()
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
            loadScenario(scenarioId)

        }
    }

    @When("Character jumps.")
    fun character_jumps() = mainClient {
        character.jump()
    }

    @When("Character unparks.")
    fun character_unparks() = mainClient {
        if (character.switchParkedStatus()) {
            character.switchParkedStatus()
        }
    }
}
