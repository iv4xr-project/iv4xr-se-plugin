"""
Records input of the game for 5 seconds and then saves it into a json file.
"""
import json
from time import sleep

from spaceengineers.proxy import SpaceEngineersProxy

if __name__ == "__main__":
    se = SpaceEngineersProxy.localhost()
    se.Input.StartRecording()
    sleep(5)
    result = se.Input.StopRecording()
    json_result = json.dumps(result)
    with open("recorded_input.json", "w", encoding="utf-8") as text_file:
        text_file.write(json_result)
