Feature: Navigate Medical Room and Restore Health

  Background:
    Given the agent-id is 'se0'
    And the agent loads the world 'LoneSurvivor_SpaceShip'

  Scenario: The agent navigates to the medical room and restores is health
    Given the agent observes the block 'LargeMedicalRoom'

    When  the agent navigates to the block 'LargeMedicalRoom'
    And   the agent aims the block 'LargeMedicalRoom'
    Then  the maximum distance to the block 'LargeMedicalRoom' is 5

    When  the agent removes the helmet 5000 milliseconds
    And   the agent activates the helmet
    Then  the agent health is below 90.00

    When  the agent uses the terminal 5000 milliseconds
    Then  the agent health is above 99.99
