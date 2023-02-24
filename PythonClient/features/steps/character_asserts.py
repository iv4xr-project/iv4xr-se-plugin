# pylint: disable=E0102,E0611,C0116
"""
Asserts to check the character state through SE plugin.
"""
from behave import use_step_matcher, step, then
from behave.runner import Context

from spaceengineers.api import SpaceEngineers

use_step_matcher("re")


@step("jetpack is off.")
def step_impl(context: Context):
    se: SpaceEngineers = context.se
    context.test.assertFalse(se.Observer.Observe().JetpackRunning)


@then("jetpack is on.")
def step_impl(context: Context):
    se: SpaceEngineers = context.se
    context.test.assertTrue(se.Observer.Observe().JetpackRunning)
