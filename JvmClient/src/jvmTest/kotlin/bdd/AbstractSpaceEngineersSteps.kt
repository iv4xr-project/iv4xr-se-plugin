package bdd

import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.SpaceEngineersTestContext

abstract class AbstractSpaceEngineersSteps(spaceEngineers: ContextControllerWrapper = CucumberStaticConnection.get()) :
    SpaceEngineers by spaceEngineers {

    val environment: ContextControllerWrapper
        get() = CucumberStaticConnection.get()

    val context: SpaceEngineersTestContext
        get() = environment.context

}
