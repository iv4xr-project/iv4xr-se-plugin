Feature: Observe Gravity Block

  Background:
    Given the scenario "LoneSurvivor_SpaceShip" is loaded from the directory "../JvmClient/src/jvmTest/resources/game-saves".

  Scenario Outline: Observe the properties of a gravity block
    Given observed block type "<blockType>" exists.
    Then the integrity of the observed block type "<blockType>" should be <integrity>.

    Examples:
      | blockType        | integrity |
      | GravityGenerator | 20320.0   |
