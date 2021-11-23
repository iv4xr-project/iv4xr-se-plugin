Feature: C197591 Character in jet-pack can reach the maximum value of 110 m/s

  Scenario: C197591 Character in jet-pack can reach the maximum value of 110 m/s
    Given I load scenario "simple-place-grind-torch-with-tools".
    And Character turns on jetpack.
    And Character waits 1 seconds.
    When Character moves up for 200 ticks.
    And Character waits 10 seconds.
    When Character moves forward for 9999999 ticks.
    And Character waits 20 seconds.
    Then Character speed is 110 m/s.



