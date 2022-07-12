Feature: T254786 Refined ingots can be thrown out of inventory and picked up again.

  Background:
    Given Scenario used is "automation-se-inventory-ingots-2".

  Scenario Outline: T254786 Refined ingots can be thrown out of inventory.
    Given Character inventory contains ingot "<ingot>".
    When Character selects inventory ingot "<ingot>" and drops.
    Then Character inventory does not contain ingot "<ingot>" anymore.
    And There is floating ingot "<ingot>" in space around.
    Examples:
      | ingot  |
      | Iron   |
      | Nickel |
      | Stone  |


