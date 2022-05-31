Feature: C284507 Character can jump

  Scenario: C284507 Character can jump
    Given Scenario used is "walking-platform".
    When Character jumps.
    Then Character is jumping.
