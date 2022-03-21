Feature: T254794 Components can be removed from a stockpile of currently built block (using grinder)

  Background:
    Given Scenario used is "automation-se-in-front-of-survival-kit-2".
    #And Test waits for 10000000 seconds.

  Scenario: T254794 Components can be removed from a stockpile of currently built block (using grinder)
    Given Character inventory contains no components.
    When Character grinds down the block in front completely.
    Then Character inventory contains components:
      | component    | amount |
      | SteelPlate   | 30     |
      | Computer     | 5      |
      | Display      | 1      |
      | Motor        | 4      |
      | Medical      | 3      |
      | Construction | 2      |
