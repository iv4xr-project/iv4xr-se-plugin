import socket
from typing import List

from spaceengineers import api
from spaceengineers import communication


class ProxyAttribute:
    prefix: List[str] = list()
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
    def __init__(self, sock) -> None:
        super().__init__(prefix=list(), sock=sock)

    @staticmethod
    def localhost():
        host = "localhost"
        port = 3333
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((host, port))
        return SpaceEngineersProxy(sock=sock)

    @staticmethod
    def connect(host, port):
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((host, port))
        return SpaceEngineersProxy(sock=sock)
