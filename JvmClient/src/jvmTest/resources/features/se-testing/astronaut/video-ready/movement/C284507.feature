Feature: C284507 Character can jump

  Scenario: C284507 Character can jump
    Given Scenario config:
  | scenario           | main_client_medbay | observer_medbay |
  | character-movement | C284507-executor   | Observer Tier 1 |
    When Character jumps.
    Then Character is jumping.
