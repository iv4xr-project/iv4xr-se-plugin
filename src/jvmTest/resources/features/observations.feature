Feature: Example how to describe block observations using cucumber.
  To demonstrate how can we define features using scenarios and automatically test them.

  Background:
    Given I am connected to server.

  Scenario: Requesting for blocks.
    When I request for blocks.
    Then I receive observation.
    And I see 1 grid with 1 block.

  Scenario Outline: Requesting for blocks.
    When I request for blocks.
    Then I receive observation.
    And I see 1 grid with 1 block.
    And Block with id "<blockId>" has <maxIntegrity> max integrity, <integrity> integrity and <buildIntegrity> build integrity.
    Examples:
      | blockId | maxIntegrity | integrity | buildIntegrity |
      | blk     | 10.0         | 5.0       | 1.0            |


  Scenario: Moving example (not implemented).
    When I move character forwards by 10 units.
    Then I see character position forward by 10 units.


  Scenario: Moving example 2 (not implemented).
    Given Character is at position (x, y, z) facing north.
    When I move character forwards by 10 units.
    Then I see character at (x, y + 10, z).