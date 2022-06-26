Feature: C284487 In Survival when fuel is depleted the jetpack is turned off automatically and immediately

  Scenario: C284487 In Survival when fuel is depleted the jetpack is turned off automatically and immediately
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284487-executor   | Observer Tier 1 |
    And Test waits for 2 seconds.
    When Character turns on jetpack.
    Then Character has empty hydrogen tank after 120000 milliseconds.
    And jetpack is off.
