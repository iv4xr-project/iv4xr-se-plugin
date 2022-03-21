Feature: T254781 Ores can be thrown out of inventory and picked up again.

  Background:
    Given Scenario used is "automation-se-inventory-stone-2".

  Scenario Outline: T254781 Ores can be thrown out of inventory.
    Given Character inventory contains ore "<ore>".
    When Character selects inventory ore "<ore>" and drops.
    Then Character inventory does not contain ore "<ore>" anymore.
    And There is floating ore "<ore>" in space around.

    Examples:
      | ore   |
      | Stone |
