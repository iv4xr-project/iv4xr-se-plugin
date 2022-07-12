Feature: T265381 Character can fall when in gravity
      # any kind of addictional info, description of the scenario (game save)
  The scenario is created simple world, character is on planet, floating in space above ground,
  has enough fuel for jetpack.

  Background:
    Given Scenario used is "character-flying-in-the-air-and-planet-gravity".

  Scenario: T265381 Character can fall when in gravity
    Given Character is floating in a gravity field with jetpack turned on.
    When Character turns off jetpack.
    Then Character starts falling towards the gravity source.
