Feature: T254777 Iron ore for the ingots can be located in the nearby asteroid (Equip drill it should show you).

  Background:
    Given Scenario used is "automation-se-in-front-of-survival-kit-2".

  Scenario: T254777 Iron ore for the ingots can be located in the nearby asteroid (Equip drill it should show you).
    When Character equips "HandDrillItem".
    Then Gameplay screen shows beacons for:
      | ore    |
      | Iron    |
      | Silicon |
      | Nickel  |
      | Ice     |


