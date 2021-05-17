package environments

import eu.iv4xr.framework.mainConcepts.W3DAgentState
import eu.iv4xr.framework.mainConcepts.WorldModel

class SeAgentState(val agentId: String) : W3DAgentState() {

    val seEnv: SeEnvironment
        get() = env() as SeEnvironment

    private fun setOrUpdate(worldModel: WorldModel) {
        if (wom == null) {
            wom = worldModel
        } else {
            wom.mergeNewObservation(worldModel)
        }
    }


    override fun updateState() {
        this.setOrUpdate(seEnv.observe())
        super.updateState()
    }
}