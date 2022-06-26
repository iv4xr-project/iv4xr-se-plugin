Feature: C284478 Dampeners preserve their state when entering/exiting static or cockpit block that is not moving

  Scenario: C284478 Dampeners preserve their state when entering/exiting static or cockpit block that is not moving A
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284478-executor   | Observer Tier 2 |
    And Character dampeners are on.
    And UI dampeners are on.
    And Character is not inside a block.

    When Character uses.
    And Test waits for 1 seconds.
    Then Character is inside the block.
    Then Character is inside block "MyCockpit".
    When Character uses.
    Then Character dampeners are on.
    And UI dampeners are on.

    When Character turns on jetpack.
    And Character moves "right" for 100 ticks.
    And Test waits for 3 seconds.


  Scenario: C284478 Dampeners preserve their state when entering/exiting static or cockpit block that is not moving B
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284478-executor   | Observer Tier 1 |
    And Character dampeners are on.
    And UI dampeners are on.
    And Character is not inside a block.

    When Character turns off dampeners.
    And Character uses.
    Then Character is inside the block.
    Then Character is inside block "MyCockpit".
    Then Character dampeners are off.
    #And UI dampeners are on. - that depends on state of the cockpit (presets of the map), not the character itself

    When Test waits for 1 seconds.
    And Character uses.
    Then dampeners are off.
    And Character dampeners are off.
    And UI dampeners are off.
