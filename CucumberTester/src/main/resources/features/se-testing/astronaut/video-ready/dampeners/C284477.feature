Feature: C284477 Dampeners will eventually try to slow the character down to 0 m/s

  Scenario: C284477 Dampeners will eventually try to slow the character down to 0 m/s
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284492-executor   | Observer Tier 1 |
    When Character turns on jetpack.
    And Character moves "forward" for 335 ticks.
    Then Character speed is 110 m/s after 5500 milliseconds.
    And Character speed is 0 m/s after 4000 milliseconds.
