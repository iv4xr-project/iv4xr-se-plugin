Feature: C284506 Character can move

  Scenario Outline: C284506 Character can move
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284506-executor   | Observer Tier 1 |
    When Character moves "<direction>" for 120 ticks using "<movement>".
    Then Character speed is <speed> m/s after 1200 milliseconds.
    Examples:
      | direction    | movement | speed |
      | forward      | sprint   | 10    |
      | forward-left | walk     | 3     |
      | left         | crouch   | 2     |
      | right        | run      | 6     |
