package environments

import eu.iv4xr.framework.environments.W3DAgentState
import eu.iv4xr.framework.mainConcepts.WorldModel

class SeAgentState(val agentId: String) : W3DAgentState() {

    val seEnv: SeEnvironment
        get() = env() as SeEnvironment

    private fun setOrUpdate(worldModel: WorldModel) {
        if (worldmodel == null) {
            worldmodel = worldModel
        } else {
            worldmodel.mergeNewObservation(worldModel)
        }
    }

    override fun updateState(agentId: String) {
        this.setOrUpdate(seEnv.observe())
        super.updateState(agentId)
    }
}
