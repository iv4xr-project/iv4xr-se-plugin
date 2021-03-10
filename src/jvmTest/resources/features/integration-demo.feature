Feature: Example how to describe block observations using cucumber.
  To demonstrate how can we define features using scenarios and automatically test them.

  Background:
    Given I am connected to real game.

  Scenario: Checking scenario character is at correct starting location and can move.
    Given I load scenario "simple-place-grind-torch-with-tools".
    When I observe.
    Then Character is at (532.7066, -45.193184, -24.395466).
    Then Character forward orientation is (-1, 0, 0).
    Then I see no block of type "LargeHeavyBlockArmorBlock".
    When Character places selects block and places it.
    Then I can see 1 new block(s) with data:
      | id | blockType                 | integrity | maxIntegrity | buildIntegrity |
      | -  | LargeHeavyBlockArmorBlock | 16500.0   | 16500.0      | 16500.0        |
    When Character moves forward for 15 units.
    When Character grinds until to 10000.0 integrity.
    When Character torches block back up to max integrity.

