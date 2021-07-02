Feature: Example how to describe block observations using cucumber.
  To demonstrate how can we define features using scenarios and automatically test them.

  Background:
    Given I am connected to real game using json-rpc.

  Scenario: Checking scenario character is at correct starting location and can move.
    Given I load scenario "simple-place-grind-torch".
    When I observe.
    Then Character is at (532.7066, -45.193184, -24.395466).
    Then Character forward orientation is (-1, 0, 0).
    When Character moves forward for 10 units.
    Then Character is 10 units away from starting location.
