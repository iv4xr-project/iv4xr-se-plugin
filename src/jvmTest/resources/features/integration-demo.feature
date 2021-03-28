Feature: Example how to describe block observations using cucumber.
  To demonstrate how can we define features using scenarios and automatically test them.

  Background:
    #Given I am connected to real game using json-rpc.
    Given I am connected to real game.
    And Toolbar has mapping:
      | slot | page | blockType                 |
      | 1    | 0    | LargeHeavyBlockArmorBlock |
      | 2    | 0    | LargeBlockCockpitSeat     |
      | 3    | 0    | LargeBlockSmallGenerator  |
      | 6    | 0    | LargeBlockGyro            |

  Scenario Outline: Checking scenario character is at correct starting location and can move.
    Given I load scenario "simple-place-grind-torch-with-tools".
    When I observe.
    Then Character is at (532.7066, -45.193184, -24.395466).
    Then Character forward orientation is (-1, 0, 0).
    Then I see no block of type "<blockType>".
    When Character selects block "<blockType>" and places it.
    Then I can see 1 new block(s) with data:
      | blockType   | integrity   | maxIntegrity | buildIntegrity |
      | <blockType> | <integrity> | <integrity>  | <integrity>    |
    When Character sets toolbar slot 4, page 0 to "Welder2Item".
    When Character sets toolbar slot 5, page 0 to "AngleGrinder2Item".
    When Character moves forward for 16 units.
    When Character grinds to 10.0% integrity.
    When Character torches block back up to max integrity.

    Examples:
      | blockType                 | integrity |
      | LargeBlockGyro            | 63105.0   |
      | LargeBlockSmallGenerator  | 12065.0   |
      | LargeBlockCockpitSeat     | 7380.0    |
      | LargeHeavyBlockArmorBlock | 16500.0   |


