Feature: Magboots

  Background:
    Given Scenario used is "simple-place-grind-torch-with-tools-for-walking".

  Scenario: C197556 Boots can become yellow when in green state by initiating jet-pack
    Given Character boots are green after 100ms.
    And Character turns on jetpack.
    Then Character boots are yellow after 200ms.


  Scenario: C197556 Boots can become green
    Given Character boots are green after 100ms.
    And Character turns on jetpack.
    Then Character boots are yellow after 200ms.
    When Character turns off jetpack.
    Then Character boots are green after 100ms.
