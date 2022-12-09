# pylint: disable=E0102,E0611,C0116
"""
Step mappings related to scenario setup or utility.
"""
import time
from pathlib import Path

from behave import use_step_matcher, when, given
from behave.runner import Context

from spaceengineers.api import SpaceEngineers

use_step_matcher("re")


def get_scenario_path() -> str:
    return f"{Path(__file__).resolve().parent}/../../scenarios/".replace("/mnt/c", "C:")


@given("Scenario config:?")
def step_impl(context: Context):
    row = context.table[0]
    scenario = row["scenario"]
    main_client_medbay = row["main_client_medbay"]
    se: SpaceEngineers = context.se
    abspath = f"{get_scenario_path()}{scenario}".replace("/mnt/c", "C:")
    se.Session.LoadScenario(scenarioPath=abspath)
    se.Screens.WaitUntilTheGameLoaded()
    time.sleep(10)
    se.Screens.Medicals.SelectFaction(factionIndex=1)
    time.sleep(10)
    data = se.Screens.Medicals.Data()

    def find_medbay(item):
        return item["Name"] == main_client_medbay

    index = list(map(find_medbay, data.MedicalRooms)).index(True)
    se.Screens.Medicals.SelectRespawn(roomIndex=index)
    time.sleep(2)
    se.Screens.Medicals.Respawn()


@when("Test waits for ([0-9].*) seconds.")
def step_impl(_: Context, seconds):
    time.sleep(float(seconds))
