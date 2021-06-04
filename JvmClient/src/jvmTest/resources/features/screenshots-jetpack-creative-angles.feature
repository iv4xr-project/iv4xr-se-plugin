Feature: Example how to describe block observations using cucumber.
  To demonstrate how can we define features using scenarios and automatically test them.

  Background:
    Given I am connected to real game using json-rpc.
    # Given I am connected to real game.
    And Output directory is "~/screenshots".

  Scenario Outline: Checking scenario character is at correct starting location and can move.
    #Given I load scenario "empty-world-creative".
    When Character sets toolbar slot 4, page 0 to "<blockType>".
    When Character selects block "<blockType>" and places it.
    When Character sets toolbar slot 0, page 0 to "Welder2Item".
    When Character sets toolbar slot 1, page 0 to "AngleGrinder2Item".
    When Character moves forward for 5 units.
    When Character teleports to first mount point of "<blockType>".
    #Then Current target block is same ast last built block.
    Then Character takes screenshots from "left,right,top,bottom,front,back" "<distance>" units away.
    #Then Character grinds down to 1% below each threshold and takes screenshots from "left,right,top,bottom,front,back" "<distance>" units away.

    #Then Character saves metadata about each threshold and file names.

    Examples:
      | blockType              | distance |
      | LargeRefinery          | 20       |
      | LargeBlockLaserAntenna | 10       |
      | LargeHydrogenEngine    | 10       |
      | LargeStairs            | 10       |
      | LargeRamp              | 10       |
      | LargeLCDPanelWide      | 10       |


