Feature: Character movement


  Scenario: C284506 Character can fall when in gravity.
    Given Scenario used is "moon-base-flying".
    And jetpack is on.
    And Character speed is 0 m/s.
    When Character turns off jetpack.
    And Test waits for 1 seconds.
    Then Character begins to fall towards the ground.


  Scenario Outline: C284506 Character can move
    Given Scenario used is "walking-platform".
    When Character moves "<direction>" for 600 ticks using "<movement>".
    Then Character speed is <speed> m/s after 2000 milliseconds.
    Examples:
      | direction    | movement | speed |
      | forward-left | crouch   | 2     |
      | forward-left | walk     | 3     |
      | forward-left | run      | 6     |
      | forward-left | sprint   | 6     |
      | forward      | crouch   | 2     |
      | forward      | walk     | 4     |
      | forward      | run      | 6     |
      | forward      | sprint   | 10    |
      | left         | crouch   | 2     |
      | left         | walk     | 3     |
      | left         | run      | 6     |
      | left         | sprint   | 6     |


