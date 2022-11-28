Feature: C284469 Boots can become white when in yellow state by moving away from the surface

  Scenario: C284469 Boots can become white when in yellow state by moving away from the surface
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284469-executor   | Observer Tier 2 |
    And Character boots are white.
    When Character turns on jetpack.
    And Character moves "down" for 60 ticks.
    And Test waits for 1 seconds.
    # Actual test:
    Given Character boots are yellow.
    When Character moves "up" for 60 ticks.
    And Test waits for 1 seconds.
    And Character boots are white.

