Feature: C197574 Character can crouch / stand back up

  Scenario: Character can crouch / stand back up
    Given I load scenario "simple-place-grind-torch-with-tools".
    And Character is standing.
    When Character crouches.
    Then Character is crouching.

# TODO: reverse scenario
