Feature: C284475 Dampeners can be enabled/disabled

  Scenario: C284475 Dampeners can be enabled/disabled
    Given Scenario used is "moon-base-flying-2".
    And dampeners are on.
    When Character turns off dampeners.
    Then dampeners are off.
