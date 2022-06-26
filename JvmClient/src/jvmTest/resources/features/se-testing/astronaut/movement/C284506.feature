Feature: C284506 Character can move

  Scenario Outline: C284506 Character can move
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284506-executor   | Observer Tier 1 |
    When Character moves "<direction>" for 120 ticks using "<movement>".
    Then Character speed is <speed> m/s after 1200 milliseconds.
    Examples:
      | direction      | movement | speed |
      | forward        | crouch   | 2     |
      | forward        | walk     | 3     |
      | forward        | run      | 6     |
      | forward        | sprint   | 10    |
      | forward-left   | crouch   | 2     |
      | forward-left   | walk     | 3     |
      | forward-left   | run      | 6     |
      | forward-left   | sprint   | 6     |
      | left           | crouch   | 2     |
      | left           | walk     | 3     |
      | left           | run      | 6     |
      | left           | sprint   | 6     |
      | backward-left  | crouch   | 2     |
      | backward-left  | walk     | 3     |
      | backward-left  | run      | 6     |
      | backward-left  | sprint   | 6     |
      | backward       | crouch   | 2     |
      | backward       | walk     | 3     |
      | backward       | run      | 6     |
      | backward       | sprint   | 6     |
      | backward-right | crouch   | 2     |
      | backward-right | walk     | 3     |
      | backward-right | run      | 6     |
      | backward-right | sprint   | 6     |
      | right          | crouch   | 2     |
      | right          | walk     | 3     |
      | right          | run      | 6     |
      | right          | sprint   | 6     |
      | forward-right  | crouch   | 2     |
      | forward-right  | walk     | 3     |
      | forward-right  | run      | 6     |
      | forward-right  | sprint   | 6     |
