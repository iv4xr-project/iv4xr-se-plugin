import time

from behave import *
from behave.runner import Context

from spaceengineers.api import SpaceEngineers

use_step_matcher("re")


@given("Scenario config:?")
def step_impl(context: Context):
    row = context.table[0]
    scenario = row["scenario"]
    main_client_medbay = row["main_client_medbay"]
    se: SpaceEngineers = context.se
    import os
    abspath = os.path.abspath("./scenarios/" + scenario)
    se.Session.LoadScenario(scenarioPath=abspath)
    se.Screens.WaitUntilTheGameLoaded()
    time.sleep(10)
    se.Screens.Medicals.SelectFaction(factionIndex=1)
    time.sleep(10)
    data = se.Screens.Medicals.Data()
    test = lambda item: item["Name"] == main_client_medbay
    index = list(map(test, data.MedicalRooms)).index(True)
    se.Screens.Medicals.SelectRespawn(roomIndex=index)
    time.sleep(2)
    se.Screens.Medicals.Respawn()


@when("Test waits for ([0-9].*) seconds.")
def step_impl(context: Context, seconds):
    import time
    time.sleep(float(seconds))
