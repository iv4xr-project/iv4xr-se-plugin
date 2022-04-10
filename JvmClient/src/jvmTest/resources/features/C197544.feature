Feature: C197544 Mass of the object is added to the overall grid mass (an update of the grid has to happen to see this)

  Background:
    Given Scenario used is "simple-place-grind-torch-with-tools-1".

  Scenario Outline: C197544 Mass of the object is added to the overall grid mass (an update of the grid has to happen to see this)
    Given Block type "<blockType>" has mass <mass>.
    And Observed grid mass is <gridMassBefore>.
    When Character selects block "<blockType>" and places it.
    Then Observed grid mass is <gridMassAfter>.

    Examples:
      | blockType             | mass | gridMassBefore | gridMassAfter |
      | LargeBlockCockpitSeat | 1768 | 50000          | 51768         |
