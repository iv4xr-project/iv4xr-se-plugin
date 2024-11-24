Feature: Basic Assembler progress

  Background:
    Given the agent-id is 'se0'
    And the agent loads the world 'LoneSurvivor_SpaceShip'

  Scenario: The agent builds a BasicAssembler block to progress the technology research
    When  the agent navigates to the block 'HeavyBlock'
    Then  the maximum distance to the block 'HeavyBlock' is 3.5

    When  the agent equips the tool 'AngleGrinderItem'
    And   the agent aims the block 'HeavyBlock'
    And   the agent uses the tool 5000 milliseconds
    Then  the agent inventory contains more than 40 units of the item 'SteelPlate'

    When  the agent drops the item 'MetalGrid' from the inventory
    Then  the agent inventory does not contain the item 'MetalGrid'

    When  the agent equips the tool 'AngleGrinderItem'
    And   the agent aims the block 'HeavyBlock'
    And   the agent uses the tool 5000 milliseconds
    Then  the agent inventory contains more than 60 units of the item 'SteelPlate'

    When  the agent rotates 2000 milliseconds
    And   the agent builds the assembler block 'BasicAssembler'
    Then  the agent observes the block 'BasicAssembler'

    When  the agent equips the tool 'WelderItem'
    And   the agent navigates to the block 'BasicAssembler'
    And   the agent aims the block 'BasicAssembler'
    And   the agent uses the tool 15000 milliseconds
    Then  the integrity of block 'BasicAssembler' is above 60.00 percentage

    When  the agent drops all the items from the inventory
    Then  the agent inventory does not contain the item 'SteelPlate'

    When  the agent equips the tool 'AngleGrinderItem'
    And   the agent navigates to the assembler block 'Assembler/LargeAssembler'
    And   the agent aims the block 'Assembler/LargeAssembler'
    And   the agent uses the tool 8000 milliseconds
    Then  the agent inventory contains more than 40 units of the item 'Construction'

    When  the agent equips the tool 'WelderItem'
    And   the agent navigates to the block 'BasicAssembler'
    And   the agent aims the block 'BasicAssembler'
    And   the agent uses the tool 15000 milliseconds
    Then  the integrity of block 'BasicAssembler' is above 99.99 percentage