Feature: C197591 Character in jet-pack can reach the maximum value of 110 m/s

  Background:
    Given Scenario used is "simple-place-grind-torch-with-tools-for-walking".

  Scenario: C197591 Character in jet-pack can reach the maximum value of 110 m/s
    When Character turns on jetpack.
    And Character moves forward for 9999999 ticks.
    Then Character speed is 110 m/s after 5500 milliseconds.



