Feature: C197544 Mass of the object is added to the overall grid mass (an update of the grid has to happen to see this)

  Scenario Outline: Mass of the object is added to the overall grid mass (an update of the grid has to happen to see this)
    Given I load scenario "simple-place-grind-torch-with-tools".
    And Block type "<blockType>" has mass <mass>.
    And Observed grid mass is <gridMassBefore>.
    When Character selects block "<blockType>" and places it.
    Then Observed grid mass is <gridMassAfter>.

    Examples:
      | blockType             | mass | gridMassBefore | gridMassAfter |
      | LargeBlockCockpitSeat | 1768 | 50000          | 51768         |
