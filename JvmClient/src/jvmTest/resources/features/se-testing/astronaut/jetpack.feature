Feature: Jetpack mode

  Scenario: C284489 Jetpack is persistent A
    Given Scenario used is "walking-platform".
    And jetpack is off.
    When Player saves the game as "jetpack-persistent-A-saved" and reloads.
    Then jetpack is off.

  Scenario: C284489 Jetpack is persistent B
    Given Scenario used is "moon-base-flying".
    And jetpack is on.
    When Player saves the game as "jetpack-persistent-B-saved" and reloads.
    Then jetpack is on.

  Scenario: C284492 Character with jetpack can reach the maximum speed of 110 m/s
    Given Scenario used is "walking-platform-jetpack".
    And jetpack is on.
    When Character moves forward for 3600 ticks.
    Then Character speed is 110 m/s after 7000 milliseconds.

  Scenario: C284486 Jetpack consumes fuel in Survival mode
    Given Scenario used is "walking-platform-survival".
    And jetpack is off.
    And character has about 100 hydrogen.
    When Character turns on jetpack.
    And Character moves forward for 6000 ticks.
    Then jetpack is on.
    Then character has less than 98 hydrogen after 60000 milliseconds.

  Scenario: C284485 In Survival the jetpack can only be enabled and active when there is enough fuel
    Given Scenario used is "alien-planet-no-fuel".
    And jetpack is off.
    When Character turns on jetpack.
    Then jetpack is off.


  Scenario: C284487 In Survival when fuel is depleted the jetpack is turned off automatically and immediately
    Given Scenario used is "alien-planet-low-fuel".
    When Character turns on jetpack.
    Then Character has empty hydrogen tank after 15000 milliseconds.
    And jetpack is off.

