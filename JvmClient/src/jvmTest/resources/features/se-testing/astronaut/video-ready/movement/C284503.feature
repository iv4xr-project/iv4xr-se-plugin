Feature: C284503 Character can fall when in gravity

  Scenario: C284503 Character can fall when in gravity
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284503-executor   | Observer Tier 1 |
    Then Character begins to fall towards the ground.
