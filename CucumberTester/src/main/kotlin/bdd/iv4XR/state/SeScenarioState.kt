package bdd.iv4XR.state

import environments.SeAgentState
import environments.SeEnvironment
import eu.iv4xr.framework.mainConcepts.TestAgent

class SeScenarioState {
    var  agentId: String? = null
    var  seEnvironment: SeEnvironment? = null
    var  seAgentState: SeAgentState? = null
    var  testAgent: TestAgent? = null
}