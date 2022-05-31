Feature: C284506 Character can move

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
      | forward      | walk     | 3     |
      | forward      | run      | 6     |
      | forward      | sprint   | 10    |
      | left         | crouch   | 2     |
      | left         | walk     | 3     |
      | left         | run      | 6     |
      | left         | sprint   | 6     |


