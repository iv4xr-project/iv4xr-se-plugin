Feature: Example how to describe block observations using cucumber.
  To demonstrate how can we define features using scenarios and automatically test them.

  Background:
    Given I am connected to real game using json-rpc.
    # Given I am connected to real game.
    And Output directory is "~/screenshots".

  Scenario Outline: Checking scenario character is at correct starting location and can move.
    Given I load scenario "simple-place-grind-torch-with-tools".
    When I observe.
    Then Character is at (532.7066, -45.193184, -24.395466).
    Then Character forward orientation is (-1, 0, 0).
    Then I see no block of type "<blockType>".
    When Character selects block "<blockType>" and places it.
    Then I can see 1 new block(s) with data:
      | blockType   |
      | <blockType> |
    When Character sets toolbar slot 0, page 0 to "<blockType>".
    When Character sets toolbar slot 1, page 0 to "<blockType>".
    When Character sets toolbar slot 2, page 0 to "<blockType>".
    When Character sets toolbar slot 3, page 0 to "<blockType>".
    When Character sets toolbar slot 4, page 0 to "Welder2Item".
    When Character sets toolbar slot 5, page 0 to "Welder2Item".
    When Character sets toolbar slot 6, page 0 to "AngleGrinder2Item".
    When Character sets toolbar slot 7, page 0 to "AngleGrinder2Item".
    When Character moves forward for 16 units.
    Then Character steps 2 units back and takes screenshot at initial integrity.
    Then Character grinds down to 1% below each threshold, steps 2 units back and takes screenshot.
    Then Character saves metadata about each threshold and file names.

    Examples:
      | blockType                     |
      | LargeBlockArmorCornerInv      |
      | LargeHeavyBlockArmorBlock     |
      | LargeHeavyBlockArmorCornerInv |
      | LargeHalfArmorBlock           |
