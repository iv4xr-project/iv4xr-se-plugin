Feature: C284492 Character with jetpack can reach the maximum speed of 110 m/s


  Scenario: C284492 Character with jetpack can reach the maximum speed of 110 m/s
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284492-executor   | Observer Tier 1 |
    When Character turns on jetpack.
    And Character moves "forward" for 600 ticks.
    Then Character speed is 110 m/s after 6500 milliseconds.
