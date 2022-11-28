Feature: C284489 Jetpack is persistent


  Scenario: C284489 Jetpack is persistent A
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284489-executor   | Observer Tier 1 |
    And jetpack is off.
    When Player saves the game as "C284489-A-jetpack-off-saved" and reloads.
    Then jetpack is off.

  Scenario: C284489 Jetpack is persistent B
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284489-executor   | Observer Tier 1 |
    And jetpack is off.
    When Character turns on jetpack.
    And Player saves the game as "C284489-B-jetpack-onsaved" and reloads.
    Then jetpack is on.
