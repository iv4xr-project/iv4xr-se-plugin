Feature: C197588 Jetpack persists through entering/exiting cockpit blocks

  Background:
    Given I am connected to real game using json-rpc.
    And Toolbar has mapping:
      | slot | page | blockType                 |
      | 2    | 0    | LargeBlockCockpitSeat     |

  Scenario: Jetpack persists through entering/exiting cockpit blocks
    Given I load scenario "simple-place-grind-torch-with-tools".
    When Character selects block "LargeBlockCockpitSeat" and places it.
    When Character moves forward for 16 units.
    And jetpack is off.
    And Character turns on jetpack.
    Then jetpack is on.
    And Character uses.
    And Character waits 1 seconds.
    And Character uses.
    Then jetpack is on.



