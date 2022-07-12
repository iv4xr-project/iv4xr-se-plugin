Feature: C284474 Jumping off of a grid or voxel when in green state results in floating away in fall animation with white state

  Scenario Outline: C284474 Jumping off of a grid or voxel when in green state results in floating away in fall animation with white state
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | <medbay>           | Observer Tier 2 |
    And Character boots are white.
    When Character turns on jetpack.
    And Character moves "down" for 6 ticks.
    And Test waits for 0.1 seconds.
    Then Character boots are white.
    When Character turns off jetpack.
    And Test waits for 6 seconds.
    Then Character boots are green.
    And Character speed is 0 m/s.
    # Actual test:
    When Character jumps.
    And Test waits for 2 seconds.
    Then Character boots are white.
    Examples:
      | medbay             |
      | C284474-A-executor |
      | C284474-B-executor |
