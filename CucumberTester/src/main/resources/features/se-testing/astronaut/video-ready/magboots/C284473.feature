Feature: C284473 Falling onto a grid or voxel when not in gravity automatically changes to green state

  Scenario Outline: C284473 Falling onto a grid or voxel when not in gravity automatically changes to green state
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | <medbay>           | Observer Tier 2 |
    And Character boots are white.
    When Character turns on jetpack.
    And Character moves "down" for 6 ticks.
    And Test waits for 0.1 seconds.
    Then Character boots are white.
    When Character turns off jetpack.
    And Test waits for 5 seconds.
    Then Character speed is 0 m/s.
    And Character boots are green.
    Examples:
      | medbay             |
      | C284473-A-executor |
      | C284473-B-executor |
