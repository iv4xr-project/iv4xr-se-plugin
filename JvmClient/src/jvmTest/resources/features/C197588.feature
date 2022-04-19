Feature: C197588 Jetpack persists through entering/exiting cockpit blocks

  Background:
    Given Scenario used is "simple-place-grind-torch-with-tools".

  Scenario: C197588 Jetpack persists through entering/exiting cockpit blocks
    When Character selects block "LargeBlockCockpitSeat" and places it.
    When Character moves forward for 15 units.
    And jetpack is off.
    And Character turns on jetpack.
    Then jetpack is on.
    And Character uses.
    And Test waits for 1 seconds.
    And Character uses.
    Then jetpack is on.



