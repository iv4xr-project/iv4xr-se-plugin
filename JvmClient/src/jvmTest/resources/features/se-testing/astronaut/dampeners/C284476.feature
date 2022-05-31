Feature: C284476 Dampeners dampen against external forces (gravity).

  Scenario: C284476 Dampeners dampen against external forces (gravity).
    Given Scenario used is "moon-base-flying-2".
    And jetpack is on.
    And Character speed is 0 m/s.
    When Character turns off dampeners.
    And Test waits for 1 seconds.
    Then Character begins to fall towards the ground.
