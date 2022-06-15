Feature: Jetpack mode


  Scenario: C284492 Character with jetpack can reach the maximum speed of 110 m/s
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284492-executor   | Observer Tier 1 |
    When Character turns on jetpack.
    And Character moves forward for 600 ticks.
    Then Character speed is 110 m/s after 5500 milliseconds.

  Scenario: C284486 Jetpack consumes fuel in Survival mode
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284486-executor   | Observer Tier 1 |
    And jetpack is off.
    And character has about 100 hydrogen.
    When Character turns on jetpack.
    Then jetpack is on.
    Then character has less than 98 hydrogen after 3000 milliseconds.

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
