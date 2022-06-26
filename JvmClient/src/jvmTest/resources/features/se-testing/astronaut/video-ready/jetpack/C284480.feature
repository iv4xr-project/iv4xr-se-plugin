Feature: C284480 Jetpack can be turned on/off


  Scenario: C284480 Jetpack can be turned on
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284480-executor   | Observer Tier 1 |
    And jetpack is off.
    When Character turns on jetpack.
    Then jetpack is on.


  Scenario: C284480 Jetpack can be turned off
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284480-executor   | Observer Tier 1 |
    And jetpack is off.
    When Character turns on jetpack.
    Then jetpack is on.
    When Character turns off jetpack.
    Then jetpack is off.
