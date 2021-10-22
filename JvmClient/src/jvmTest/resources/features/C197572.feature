Feature: C197572 Character can run (Default) (Max 6 m/s)

  Background:
    Given I am connected to real game using json-rpc.

  Scenario: Character can run (Default) (Max 6 m/s)
    Given I load scenario "simple-place-grind-torch-with-tools".
    When Character runs forward for 120 ticks.
    And Character waits 1 seconds.
    Then Character speed is 6 m/s.



