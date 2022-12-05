# pylint: disable=C0116
"""
This module handles lower level TCP and json-rpc communication.
"""
import dataclasses
import json
from dataclasses import dataclass


def cleanup_error_data(inner_data: dict):
    if not inner_data:
        return None
    if type(inner_data) != dict:
        return inner_data
    fields_to_remove = ("Source", "WatsonBuckets")
    for field_to_remove in fields_to_remove:
        if field_to_remove in inner_data:
            inner_data.pop(field_to_remove)
    for key, value in list(inner_data.items()):
        if value is None:
            del inner_data[key]
    return inner_data


class JsonRpcError(Exception):
    """
    Exception representing json-rpc error. Also trying to tidy up messy C# exception if possible.
    """

    data: dict

    def __init__(self, data: dict):
        self.data = data
        super().__init__(
            f"{data.get('message', 'Unknown error')} ({data.get('code', None)}). {self.inner_msg()}"
        )

    def inner_msg(self):
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
        result += (data.get("message", "")) + "\n"
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
    """
    Data class as per json-rpc request specification.
    """

    method: str
    params: object
    id: int
    jsonrpc: str = "2.0"


class DictUppercaseWrapper(dict):
    """
    Wrapper around dict to allow dot notation and also by lowercase starting names.
    """

    def __getattr__(self, item):
        edited_item = item[0].upper() + item[1:]
        if edited_item in self:
            return DictUppercaseWrapper.wrap_if_dict(super().__getitem__(edited_item))
        return DictUppercaseWrapper.wrap_if_dict(super().__getitem__(item))

    @staticmethod
    def wrap_if_dict(dict_to_wrap):
        if isinstance(dict_to_wrap, dict):
            return DictUppercaseWrapper(dict_to_wrap)
        return dict_to_wrap


def method_name(prefixes):
    name = ""
    for prefix in prefixes:
        name = name + "." + prefix[0].upper() + prefix[1:]
    return name[1:]


def send_request(request, sock):
    request_str = (
        json.dumps(dataclasses.asdict(request), separators=(",", ":")).strip() + "\r\n"
    )

    sock.sendall(bytearray(request_str, "utf-8"))
    data = receive_all(sock)
    json_data = json.loads(data)

    if "error" in json_data:
        raise JsonRpcError(data=json_data["error"])
    result = json_data["result"]
    return DictUppercaseWrapper.wrap_if_dict(result)


def receive_all(sock):
    data = bytearray()
    while True:
        packet = sock.recv(4096)
        if not packet:
            return data
        data.extend(packet)
        if len(data) > 0 and data[-1] == 10:
            return data


def call_rpc(prefix, sock, *args, **kwargs):
    if len(args) > 0 and len(kwargs) > 0:
        raise ValueError(
            "Cannot use both positional and named arguments at the same time."
        )
    arguments = args or kwargs
    request = JsonRpcRequest(method=method_name(prefix), params=arguments, id=0)
    result = send_request(request=request, sock=sock)
    return result
