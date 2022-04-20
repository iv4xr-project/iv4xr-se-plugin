Feature: T254774 Assembler can be instructed to queue a large grid Cockpit into production

  Background:
    Given Scenario used is "automation-se-in-front-of-assembler-2".

  Scenario: T254774 Assembler can be instructed to queue a large grid Cockpit into production
    Given Character opens terminal in production tab.
    And Production queue is empty.
    When Character adds "Cockpit" to production queue.
    Then Production queue is not empty.
    And Production queue contains exactly:
      | name               | count |
      | Steel Plate        | 30    |
      | Construction Comp. | 20    |
      | Motor              | 1     |
      | Display            | 8     |
      | Computer           | 100   |
      | Bulletproof Glass  | 60    |



