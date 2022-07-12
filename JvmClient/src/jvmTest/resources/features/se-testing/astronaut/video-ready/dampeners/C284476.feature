Feature: C284476 Dampeners dampen against external forces (gravity).

  Scenario: C284476 Dampeners dampen against external forces (gravity).
    # TODO: scenario (C284476 not found)
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284486-executor   | Observer Tier 1 |
    And Test waits for 1 seconds.
    When Character turns on jetpack.
    Then Character thrusters work against the gravity.
