Feature: C284485 In Survival the jetpack can only be enabled and active when there is enough fuel

  Scenario: C284485 In Survival the jetpack can only be enabled and active when there is enough fuel
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284485-executor   | Observer Tier 1 |
    And Test waits for 2 seconds.
    When Character turns on jetpack.
    Then jetpack is on.
    When Test waits for 115 seconds.
    Then jetpack is off.
    When Character turns on jetpack.
    Then jetpack is off.
