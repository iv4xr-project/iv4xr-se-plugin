Feature: C284510 Relative dampeners dampen in relation to the moving object the character is locked to

  Scenario: C284510 Relative dampeners dampen in relation to the moving object the character is locked to
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284510-executor   | Observer Tier 2 |
    When Character turns on jetpack.
    And Character turns on relative dampeners.
    Then Character speed and direction matches the dampening entity.
