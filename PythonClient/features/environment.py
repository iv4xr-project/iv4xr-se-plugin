"""
Sets up the whole scenario, connects to a local SE plugin instance.
"""
from unittest import TestCase

from behave.runner import Context

from spaceengineers.proxy import SpaceEngineersProxy


def before_all(context: Context) -> None:
    """
    Runs before all tests, context can be used to save anything.
    """
    context.test = TestCase()
    context.se = SpaceEngineersProxy.localhost()
