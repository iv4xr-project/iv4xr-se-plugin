Feature: Screenshots

  Background:
    Given Scenario used is "simple-place-grind-torch-with-tools".
    Given Output directory is "~/screenshots".

  Scenario Outline: Taking screenshot at each block threshold.
    Given Character is at (532.7066, -45.193184, -24.395466).
    And Character forward orientation is (-1, 0, 0).
    And I see no block of type "<blockType>".
    When Character sets toolbar slot 0, page 0 to "CubeBlock/<blockType>".
    When Character selects block "<blockType>" and places it.
    Then I can see 1 new block(s) with data:
      | blockType   |
      | <blockType> |
    When Character sets toolbar slot 4, page 0 to "PhysicalGunObject/Welder2Item".
    When Character sets toolbar slot 6, page 0 to "PhysicalGunObject/AngleGrinder2Item".
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
