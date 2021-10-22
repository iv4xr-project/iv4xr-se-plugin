Feature: C197574 Character can crouch / stand back up

  Background:
    Given I am connected to real game using json-rpc.


  Scenario: Character can crouch / stand back up
    Given I load scenario "simple-place-grind-torch-with-tools".
    And Character is standing.
    When Character crouches.
    Then Character is crouching.

# TODO: reverse scenario
