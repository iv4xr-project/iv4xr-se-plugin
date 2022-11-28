Feature: C284468 Boots can become yellow when in white state by moving towards the surface

  Scenario: C284468 Boots can become yellow when in white state by moving towards the surface
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284468-executor   | Observer Tier 2 |
    And Character boots are white.
    When Character turns on jetpack.
    And Character moves "down" for 60 ticks.
    And Test waits for 1 seconds.
    Then Character boots are yellow.
