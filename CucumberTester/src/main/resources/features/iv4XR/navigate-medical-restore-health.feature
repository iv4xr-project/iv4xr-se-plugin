Feature: Navigate Medical Room and Restore Health

  Background:
    Given the agent loads the world "LoneSurvivor_SpaceShip".

  Scenario Outline: The agent navigates to the medical room and restores is health
    Given the agent observes the block type "<blockType>".
    When the agent navigates to the 2x2 block type "<blockType>" with a maximum <distance>.
    When the agent aims the block type "<blockType>".
    When the agent removes the helmet <wait> milliseconds.
    Then the agent health is below <minimum> percentage.

    When the agent activates the helmet.
    When the agent continuously uses the terminal <wait> milliseconds.
    Then the agent health is above <maximum> percentage.

    Examples:
      | blockType        | distance | wait | minimum | maximum |
      | LargeMedicalRoom | 7        | 5000 | 90.00   | 99.99   |
