Feature: T265385 Character can jump

  Scenario: T265381 Character can jump in gravity
    Given Scenario used is "standing-on-surface-gravity".
    Given Character is standing on a surface.
    When Character jumps.
    Then Character moves up.

  Scenario: T265381 Character can jump in non-gravity environment
    Given Scenario used is "standing-on-surface-no-gravity".
    Given Character is standing on a surface.
    When Character jumps.
    Then Character moves up.
