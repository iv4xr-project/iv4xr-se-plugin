# pylint: disable=R1734
"""
Proxy wrapper that handles all the methods to rpc calls.
"""
import socket
from typing import List

from spaceengineers import api
from spaceengineers import communication


class ProxyAttribute:
    """
    Generic class that helps with mapping python method calls to json-rpc calls.
    """

    prefix: List[str] = []
    sock: object

    def __init__(self, prefix, sock) -> None:
        super().__init__()
        self.prefix = prefix
        self.sock = sock

    def __call__(self, *args, **kwargs):
        return communication.call_rpc(self.prefix, self.sock, *args, **kwargs)

    def __getattribute__(self, item):
        if item in ("prefix", "sock", "call_rpc"):
            return super().__getattribute__(item)
        return ProxyAttribute(prefix=list(self.prefix) + [item], sock=self.sock)


class SpaceEngineersProxy(ProxyAttribute, api.SpaceEngineers):
    """
    The main class to use when communicating with plugin.
    It implements the interface and maps everything to json-rpc calls.
    """

    def __init__(self, sock) -> None:
        super().__init__(prefix=list(), sock=sock)

    @staticmethod
    def localhost() -> "SpaceEngineersProxy":
        """
        :return: The proxy connected to the default port and localhost.
        """
        host = "localhost"
        port = 3333
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((host, port))
        return SpaceEngineersProxy(sock=sock)

    @staticmethod
    def connect(host, port) -> "SpaceEngineersProxy":
        """
        :param host: Hostname of the plugin to connect.
        :param port: Port on which the plugin runs (default is 3333).
        :return: The proxy connected to the hostname and port.
        """
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((host, port))
        return SpaceEngineersProxy(sock=sock)
