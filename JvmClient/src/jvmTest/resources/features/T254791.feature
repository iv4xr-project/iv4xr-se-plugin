Feature: T254791 Components can be added to a stockpile of currently built block (using welder)

  Background:
    Given Scenario used is "automation-se-destroyed-survival-kit-2".

  Scenario: T254791 Components can be added to a stockpile of currently built block (using welder)
    Given Character inventory contains components:
      | component    | amount |
      | SteelPlate   | 30     |
      | Computer     | 5      |
      | Display      | 1      |
      | Motor        | 4      |
      | Medical      | 3      |
      | Construction | 2      |
    When Character builds block "SurvivalKit/SurvivalKitLarge".
    And Character torches up the block in front completely.
    Then Character inventory contains no components.
