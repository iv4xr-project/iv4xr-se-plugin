Feature: Magboots


  Scenario: C197556 Boots can become yellow when in green state by initiating jet-pack
    Given Scenario used is "walking-platform".
    And Character boots are green.
    And Character turns on jetpack.
    Then Character boots are yellow.


  Scenario: C197556 Boots can become green
    Given Scenario used is "walking-platform-jetpack".
    Then Character boots are yellow.
    When Character turns off jetpack.
    Then Character boots are green.


  Scenario: C284511 Boot state is persistent A
    Given Scenario used is "walking-platform".
    And Character boots are green.
    When Player saves the game as "magboots-persistent-A-saved" and reloads.
    And Character boots are green.


  Scenario: C284511 Boot state is persistent B
    Given Scenario used is "moon-base-flying".
    And Character boots are white.
    When Player saves the game as "magboots-persistent-B-saved" and reloads.
    And Character boots are white.
