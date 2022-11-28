Feature: C284486 Jetpack consumes fuel in Survival mode


  Scenario: C284486 Jetpack consumes fuel in Survival mode
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284486-executor   | Observer Tier 1 |
    And jetpack is off.
    And character has about 100 hydrogen.
    When Character turns on jetpack.
    Then jetpack is on.
    Then character has less than 98 hydrogen after 3000 milliseconds.
