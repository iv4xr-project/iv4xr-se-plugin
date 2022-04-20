Feature: C197572 Character can run (Default) (Max 6 m/s)

  Background:
    Given Scenario used is "simple-place-grind-torch-with-tools-for-walking".

  Scenario: C197572 Character can run (Default) (Max 6 m/s)
    When Character runs forward for 120 ticks.
    Then Character speed is 6 m/s after 1000 milliseconds.



