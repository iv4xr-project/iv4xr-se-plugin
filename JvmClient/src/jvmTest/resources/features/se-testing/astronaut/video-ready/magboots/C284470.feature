Feature: C284470 Boots can become green when in yellow state by initiating magboots

  Scenario: C284470 Boots can become green when in yellow state by initiating magboots
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284470-executor   | Observer Tier 2 |
    And Character boots are white.
    When Character turns on jetpack.
    And Character moves "down" for 60 ticks.
    And Test waits for 1 seconds.
    # Actual test:
    Given Character boots are yellow.
    When Character turns off jetpack.
    Then Character boots are green.
