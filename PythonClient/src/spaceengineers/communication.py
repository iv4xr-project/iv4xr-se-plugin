import dataclasses
import json
from dataclasses import dataclass
from typing import List


def cleanup_error_data(inner_data: dict):
    if not inner_data:
        return None
    if type(inner_data) != dict:
        return inner_data
    fields_to_remove = ("Source", "WatsonBuckets")
    for field_to_remove in fields_to_remove:
        if field_to_remove in inner_data:
            inner_data.pop(field_to_remove)
    for k, v in list(inner_data.items()):
        if v is None:
            del inner_data[k]
    return inner_data


class JsonRpcError(Exception):
    data: dict

    def __init__(self, data: dict):
        self.data = data
        super().__init__(
            f"{data.get('message', 'Unknown error')} ({data.get('code', None)}). {self.inner_message()}")

    def inner_message(self):
        inner_data = self.data.get("data", None)
        if type(inner_data) == str:
            return inner_data
        inner_data = cleanup_error_data(inner_data)
        if "Message" in inner_data:
            return inner_data.get("Message", None)
        return None

    @property
    def detailed_message(self):
        result = ""
        data = self.data.copy()
        result += (data.get('message', "")) + "\n"
        inner_data = cleanup_error_data(data.get("data", None))
        if inner_data:
            if "Message" in inner_data:
                result += (inner_data.pop("Message")) + "\n"
            if "StackTraceString" in inner_data:
                result += (inner_data.pop("StackTraceString")) + "\n"
            result += str(inner_data) + "\n"
        return result


@dataclass
class JsonRpcRequest:
    method: str
    params: object
    id: int
    jsonrpc: str = "2.0"


def method_name(prefixes):
    name = ""
    for n in prefixes:
        name = name + "." + n[0].upper() + n[1:]
    return name[1:]


def send_request(request, sock):
    request_str = json.dumps(dataclasses.asdict(request), separators=(',', ':')).strip() + "\r\n"

    sock.sendall(bytearray(request_str, "utf-8"))
    data = receive_all(sock, 32768)
    json_data = json.loads(data)

    if "error" in json_data:
        raise JsonRpcError(data=json_data["error"])
    result = json_data["result"]
    from .proxy import DictUppercaseWrapper
    return DictUppercaseWrapper.wrap_if_dict(result)


def receive_all(sock, n):
    data = bytearray()
    while True:
        packet = sock.recv(4096)
        if not packet:
            return data
        data.extend(packet)
        if len(data) > 0 and data[-1] == 10:
            return data
    return data


def call_rpc(prefix, sock, *args, **kwargs):
    if len(args) > 0 and len(kwargs) > 0:
        raise ValueError("Cannot use both positional and named arguments at the same time.")

    arguments = args or kwargs
    request = JsonRpcRequest(
        method=method_name(prefix), params=arguments, id=0
    )
    result = send_request(request=request, sock=sock)
    return result


class ProxyAttribute(object):
    prefix: List[str] = list()
    sock: object

    def __init__(self, prefix, sock) -> None:
        super().__init__()
        self.prefix = prefix
        self.sock = sock

    def __call__(self, *args, **kwargs):
        return call_rpc(self.prefix, self.sock, *args, **kwargs)

    def __getattribute__(self, item):
        if item in ("prefix", "sock", "call_rpc"):
            return super(ProxyAttribute, self).__getattribute__(item)
        return ProxyAttribute(prefix=list(self.prefix) + [item], sock=self.sock)
