package bdd

import io.cucumber.java.en.Then
import spaceEngineers.model.DefinitionId
import kotlin.test.assertTrue

class FloatingObjectAsserts : AbstractMultiplayerSteps() {

    @Then("There is floating ore {string} in space around.")
    fun there_is_floating_ore_in_space_around(type: String) = observers {
        val definition = DefinitionId.ore(type)
        assertTrue(observer.observeFloatingObjects().any { it.itemDefinition.definitionId == definition })
    }

    @Then("There is floating ingot {string} in space around.")
    fun there_is_floating_in_space_around(type: String) = observers {
        val definition = DefinitionId.ingot(type)
        assertTrue(observer.observeFloatingObjects().any { it.itemDefinition.definitionId == definition })
    }
}
