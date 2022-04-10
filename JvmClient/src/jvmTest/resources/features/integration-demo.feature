Feature: Integration demo


  Background:
    Given Scenario used is "simple-place-grind-torch-with-tools-1".
    Given Toolbar has mapping:
      | slot | page | blockType                 |
      | 1    | 0    | LargeHeavyBlockArmorBlock |
      | 2    | 0    | LargeBlockCockpitSeat     |
      | 3    | 0    | LargeBlockSmallGenerator  |
      | 6    | 0    | LargeBlockGyro            |

  Scenario Outline: Build, grind and weld.
    Given Character is at (532.7066, -45.193184, -24.395466).
    And Character forward orientation is (-1, 0, 0).
    And I see no block of type "<blockType>".
    When Character selects block "<blockType>" and places it.
    Then I can see 1 new block(s) with data:
      | blockType   | integrity   | maxIntegrity | buildIntegrity |
      | <blockType> | <integrity> | <integrity>  | <integrity>    |
    When Character sets toolbar slot 4, page 0 to "PhysicalGunObject/Welder2Item".
    When Character sets toolbar slot 5, page 0 to "PhysicalGunObject/AngleGrinder2Item".
    When Character moves forward for 16 units.
    When Character grinds to 10.0% integrity.
    When Character torches block back up to max integrity.

    Examples:
      | blockType                 | integrity |
      | LargeBlockGyro            | 63105.0   |
      | LargeBlockSmallGenerator  | 12065.0   |
      | LargeBlockCockpitSeat     | 7380.0    |
      | LargeHeavyBlockArmorBlock | 16500.0   |


