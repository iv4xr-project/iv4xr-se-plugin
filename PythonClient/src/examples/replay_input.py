import json

from spaceengineers.proxy import SpaceEngineersProxy

"""
Replays input recorded previously by record_input.py
"""
if __name__ == '__main__':
    se = SpaceEngineersProxy.localhost()
    with open("recorded_input.json", "r") as text_file:
        snapshots = json.loads(text_file.read())
        se.Input.StartPlaying(snapshots)
