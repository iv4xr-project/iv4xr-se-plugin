Feature: C284476 Dampeners dampen against external forces (gravity).

  Scenario: C284476 Dampeners dampen against external forces (gravity).
    Given Scenario used is "automation-se-inventory-cockpit".
    When Character uses.
    And Character unparks.
    And Character moves forward for 120 ticks.
    And Test waits for 2 seconds.
    # Maybe stop auto movement if there's slight desync - movement after getting out of cockpit can stop dampeners:
    # And Character moves forward for 0 ticks.
    And Character uses.
    Then Dampeners are switched to relative mode.

