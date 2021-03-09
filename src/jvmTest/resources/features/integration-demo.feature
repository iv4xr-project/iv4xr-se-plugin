Feature: Example how to describe block observations using cucumber.
  To demonstrate how can we define features using scenarios and automatically test them.

  Background:
    Given I am connected to real game.

  Scenario: Checking scenario character is at correct starting location.
    Given I load scenario "Scenario1".
    When I observe.
    Then Character is at (-903.388, 2235.7278, 4266.1577).
    Then Character forward orientation is (-0.9446019, 0.27600434, 0.17762).
    When Character moves forward for 10 units.
    Then Character is 10 units away from starting location.