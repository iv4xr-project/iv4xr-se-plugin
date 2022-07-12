Feature: C284465 Boots have white when there is gravity or when not in other states

  Scenario: C284465 Boots have white when there is gravity or when not in other states A
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284465-A-executor | Observer Tier 2 |
    Then Character boots are white.

  Scenario: C284465 Boots have white when there is gravity or when not in other states B
    Given Scenario config:
      | scenario           | main_client_medbay | observer_medbay |
      | character-movement | C284465-B-executor | Observer Tier 2 |
    Then Character boots are white.
