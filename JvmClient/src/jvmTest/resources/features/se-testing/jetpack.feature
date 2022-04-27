Feature: Jetpack mode

  Scenario: C284489 Jetpack is persistent A
    Given Scenario used is "simple-place-grind-torch".
    And jetpack is off.
    When Player saves the game as "jetpack-persistent-A-saved" and reloads.
    Then jetpack is off.

  Scenario: C284489 Jetpack is persistent B
    Given Scenario used is "moon-base-flying".
    And jetpack is on.
    When Player saves the game as "jetpack-persistent-B-saved" and reloads.
    Then jetpack is on.

  Scenario: C284492 Character with jetpack can reach the maximum speed of 110 m/s
    Given Scenario used is "moon-base-flying".
    And jetpack is on.
    When Character moves forward for 3600 ticks.
    Then Character speed is 110 m/s after 7000 milliseconds.

  Scenario: C284486 Jetpack consumes fuel in Survival mode
    Given Scenario used is "floating-planet-survival-5".
    # fix appversion here
    And jetpack is on.
    And character has about 98 hydrogen.
    Then character has less than 90 hydrogen after 10000 milliseconds.

  Scenario: C284485 In Survival the jetpack can only be enabled and active when there is enough fuel
    Given Scenario used is "alient-planet-floating-in-position-no-hydrogen".
    And jetpack is off.
    When Character turns on jetpack.
    Then jetpack is off.


  Scenario: C284487 In Survival when fuel is depleted the jetpack is turned off automatically and immediately
    Given Scenario used is "floating-planet-survival-10-hydrogen".
    And jetpack is on.
    Then Character has empty hydrogen tank after 10000 milliseconds.
    And jetpack is off.

