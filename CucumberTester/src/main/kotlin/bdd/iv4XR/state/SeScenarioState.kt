package bdd.iv4XR.state

import environments.SeAgentState
import environments.SeEnvironment
import eu.iv4xr.framework.mainConcepts.TestAgent
import spaceEngineers.controller.SpaceEngineers

class SeScenarioState {
    var  agentId: String = SpaceEngineers.DEFAULT_AGENT_ID
    var  seEnvironment: SeEnvironment? = null
    var  seAgentState: SeAgentState? = null
    var  testAgent: TestAgent? = null
}