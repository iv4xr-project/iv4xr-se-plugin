Feature: C284491 Jetpack is automatically engaged when leaving an artificial gravity field


  Scenario: C284491 Jetpack is automatically engaged when leaving an artificial gravity field
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284491-executor   | Observer Tier 1 |
    And jetpack is off.
    When Test waits for 4 seconds.
    Then jetpack is on.

