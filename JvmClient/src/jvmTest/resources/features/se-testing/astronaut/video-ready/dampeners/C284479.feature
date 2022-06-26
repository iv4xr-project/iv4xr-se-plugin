Feature: C284479 Dampeners are switched to Relative Dampeners when exiting a moving cockpit block

  Scenario: C284479 Dampeners are switched to Relative Dampeners when exiting a moving cockpit block
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay | faction            |
      | character-movement | C284479-executor   | Observer Tier 2 | Astronaut Movement |
    When Character uses.
    Then Character is inside block "MyCockpit".
    When Uses item 1 from toolbar.
    And Test waits for 1 seconds.
    And Character moves "left" for 110 ticks.
    And Test waits for 2 seconds.
    # Maybe stop auto movement if there's slight desync - movement after getting out of cockpit can stop dampeners:
    # And Character moves forward for 0 ticks.
    And Character uses.
    Then Dampeners are switched to relative mode.

