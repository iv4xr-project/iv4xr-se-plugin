from spaceengineers.api import SpaceEngineers
from spaceengineers.communication import ProxyAttribute


class DictUppercaseWrapper(dict):

    def __getattr__(self, item):
        edited_item = item[0].upper() + item[1:]
        if edited_item in self:
            return DictUppercaseWrapper.wrap_if_dict(super().__getitem__(edited_item))
        return DictUppercaseWrapper.wrap_if_dict(super().__getitem__(item))

    @staticmethod
    def wrap_if_dict(d):
        if isinstance(d, dict):
            return DictUppercaseWrapper(d)
        return d


class SpaceEngineersProxy(ProxyAttribute, SpaceEngineers):

    def __init__(self, sock) -> None:
        super().__init__(prefix=list(), sock=sock)

    @staticmethod
    def localhost():
        host = "localhost"
        port = 3333
        import socket
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((host, port))
        return SpaceEngineersProxy(sock=sock)

    @staticmethod
    def connect(host, port):
        import socket
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((host, port))
        return SpaceEngineersProxy(sock=sock)


