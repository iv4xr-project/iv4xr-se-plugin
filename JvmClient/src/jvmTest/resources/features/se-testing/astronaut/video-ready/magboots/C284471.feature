Feature: C284471 Boots can become yellow when in green state by initiating jetpack

  Scenario: C284471 Boots can become yellow when in green state by initiating jetpack
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284471-executor   | Observer Tier 2 |
    And Character boots are white.
    When Character turns on jetpack.
    And Character moves "down" for 60 ticks.
    And Test waits for 1 seconds.
    Then Character boots are yellow.
    And Character turns off jetpack.
    # Actual test:
    Given Character boots are green.
    When Character turns on jetpack.
    Then Character boots are yellow.
