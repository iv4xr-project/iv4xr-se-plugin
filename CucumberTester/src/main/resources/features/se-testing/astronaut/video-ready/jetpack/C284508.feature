Feature: C284508 Character can move with Jetpack

  Scenario Outline: C284508 Character can move with Jetpack
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay | delay_after_spawn |
      | character-movement | Space Suit         | Observer Tier 1 | 5                 |
    When Character turns on jetpack.
    And Character remembers it's position.
    And Character moves "<direction>" for 60 ticks using "run".
    And Test waits for 1 seconds.
    Then Character is positioned <distance>m in the "<direction>" from it's original position.
    Examples:
      | direction      | distance |
      | forward        | 11.57    |
      | forward-left   | 16.37    |
      | left           | 11.57    |
      | backward-left  | 16.37    |
      | backward       | 11.57    |
      | backward-right | 16.37    |
      | right          | 11.57    |
      | forward-right  | 16.37    |
