Feature: Observe Gravity Block

  Background:
    Given the scenario "LoneSurvivor_SpaceShip" is loaded from the directory "../JvmClient/src/jvmTest/resources/game-saves".

  Scenario Outline: The agent observes the properties of a gravity block
    Given the agent observes the block type "<blockType>".
    Then the integrity of the observed block type "<blockType>" should be <integrity>.

    Examples:
      | blockType        | integrity |
      | GravityGenerator | 20320.0   |
