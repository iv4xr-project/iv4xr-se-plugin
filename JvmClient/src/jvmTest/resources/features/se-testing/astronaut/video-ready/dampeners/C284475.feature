Feature: C284475 Dampeners can be enabled/disabled

  Scenario: C284475 Dampeners can be enabled/disabled
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284475-executor   | Observer Tier 1 |
    And dampeners are on.
    When Character turns off dampeners.
    Then dampeners are off.
