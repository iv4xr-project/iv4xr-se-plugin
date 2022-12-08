package spaceEngineers.labrecruits

import kotlinx.coroutines.runBlocking

class LabRecruitsCoroutinesFreeController(
    val labRecruitsController: LabRecruitsController,
) {


    fun observeActiveObjects() = labRecruitsController.observeActiveObjects()

    fun buttonBlockById(buttonId: String) = labRecruitsController.buttonBlockById(buttonId)

    fun doorBlockById(doorId: String) = labRecruitsController.doorBlockById(doorId)


    fun goToButton(buttonId: String) = runBlocking {
        labRecruitsController.goToButton(buttonId)
    }

     fun goToDoor(doorId: String) = runBlocking {
        labRecruitsController.goToDoor(doorId)
    }

    fun pressButton(buttonId: String) = labRecruitsController.pressButton(buttonId)

}
