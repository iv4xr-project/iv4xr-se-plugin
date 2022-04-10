Feature: C197572 Character speed tests.

  Background:
    Given Scenario used is "simple-place-grind-torch-with-tools-for-walking".

  Scenario Outline: C197572 Character can run (Default) (Max 6 m/s)
    When Character moves "<direction>" for 600 ticks using "<movement>".
    Then Character speed is <speed> m/s after 3000 milliseconds.
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


