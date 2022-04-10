Feature: C197556 Boots can become yellow when in green state by initiating jet-pack

  Background:
    Given Scenario used is "simple-place-grind-torch-with-tools-for-walking".

  Scenario: C197556 Boots can become yellow when in green state by initiating jet-pack
    And Character turns on jetpack.
    And Character turns off jetpack.
    And Character boots are green after 100ms.
    And Character turns on jetpack.
    Then Character boots are yellow after 200ms.



