from behave.runner import Context


def before_all(context: Context):
    from unittest import TestCase
    context.test = TestCase()
    from spaceengineers.proxy import SpaceEngineersProxy
    context.se = SpaceEngineersProxy.localhost()
