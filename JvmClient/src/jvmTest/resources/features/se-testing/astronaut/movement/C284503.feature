Feature: C284503 Character can fall when in gravity

  Scenario: C284503 Character can fall when in gravity
    Given Scenario used is "moon-base-flying-2".
    And jetpack is on.
    And Character speed is 0 m/s.
    When Character turns off jetpack.
    And Test waits for 1 seconds.
    Then Character begins to fall towards the ground.
