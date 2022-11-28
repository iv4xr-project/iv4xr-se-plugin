Feature: C284511 Boot state is persistent

  Scenario Outline: C284511 Boot state is persistent - GREEN
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | <medbay>           | Observer Tier 2 |
    And Character turns on jetpack.
    And Character moves "down" for 120 ticks.
    And Test waits for 2 seconds.
    And Character turns off jetpack.
    # Actual test:
    Then Character boots are green.
    When Player saves the game as "magboots-persistent-GREEN-saved" and reloads.
    And Character boots are green.
    Examples:
      | medbay             |
      | C284511-A-executor |
      | C284511-B-executor |

  Scenario Outline: C284511 Boot state is persistent - YELLOW
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | <medbay>           | Observer Tier 2 |
    And Character turns on jetpack.
    And Character moves "down" for 180 ticks.
    And Test waits for 3 seconds.
    # Actual test:
    And Character boots are yellow.
    When Player saves the game as "magboots-persistent-YELLOW-saved" and reloads.
    And Character boots are yellow.
    Examples:
      | medbay             |
      | C284511-A-executor |
      | C284511-B-executor |

  Scenario Outline: C284511 Boot state is persistent - WHITE
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | <medbay>           | Observer Tier 2 |
    And Character boots are white.
    When Player saves the game as "magboots-persistent-WHITE-saved" and reloads.
    And Character boots are white.
    Examples:
      | medbay             |
      | C284511-A-executor |
      | C284511-B-executor |
