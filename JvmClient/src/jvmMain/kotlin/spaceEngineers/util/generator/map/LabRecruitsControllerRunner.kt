package spaceEngineers.util.generator.map

import kotlinx.coroutines.runBlocking
import spaceEngineers.controller.SpaceEngineersJavaProxyBuilder
import spaceEngineers.controller.extend
import spaceEngineers.labrecruits.LabRecruitsController


fun main() = runBlocking {
    val batch = SpaceEngineersJavaProxyBuilder().localhost()
    val se = batch.extend()
    val labRecruitsController = LabRecruitsController(se)
    with(labRecruitsController) {
        println("goto b0")
        goToButton("b0")

        println("press b0")
        pressButton("b0")

        println("goto door0")
        goToDoor("door0")

        println("goto door1")
        goToDoor("door1")
    }

}
