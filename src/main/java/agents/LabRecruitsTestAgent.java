/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package agents;

import communication.agent.AgentCommand;
import communication.system.Request;
import environments.LabRecruitsEnvironment;
import eu.iv4xr.framework.mainConcepts.TestAgent;
import eu.iv4xr.framework.mainConcepts.TestDataCollector;
import nl.uu.cs.aplib.mainConcepts.GoalStructure;
import nl.uu.cs.aplib.multiAgentSupport.ComNode;
import world.BeliefState;

/**
 * A subclass of {@link eu.iv4xr.framework.mainConcepts.TestAgent} to test
 * the Lab Recruits game. It provides some overriding of inherited methods,
 * but facilitating some convenience type casting.
 */
public class LabRecruitsTestAgent extends TestAgent {

	
    /**
     * The constructor for the test agent.
     */
	public LabRecruitsTestAgent(String id) {
		super(id,null) ;
    }
	
    /**
     * The constructor for the test agent with an id or role attached to itself (this is required for agent communication).
     */
    public LabRecruitsTestAgent(String id, String role) {
        super(id, role);
    }
    
    public LabRecruitsTestAgent attachState(BeliefState state) {
    	state.id = this.id ;
    	state.worldmodel.agentId = this.id ;
    	super.attachState(state);
    	return this ;
    }
    
    public LabRecruitsTestAgent attachEnvironment(LabRecruitsEnvironment env) {
    	super.attachEnvironment(env) ;
    	return this ;
    }
    
    @Override
    public LabRecruitsTestAgent setGoal(GoalStructure g) {
    	super.setGoal(g) ;
    	return this ;
    }
    
    @Override
    public LabRecruitsTestAgent registerTo(ComNode comNode) {
    	super.registerTo(comNode) ;
    	return this ;
    }
    
    @Override
    public LabRecruitsTestAgent setTestDataCollector(TestDataCollector dc) {
    	super.setTestDataCollector(dc) ;
    	return this ;
    }

    public boolean success(){
        if(currentGoal != null){
            return currentGoal.getStatus().success();
        }
        if(lastHandledGoal != null){
            return lastHandledGoal.getStatus().success();
        }
        return false;
    }

    public void printStatus(){
        if(currentGoal != null){
            currentGoal.printGoalStructureStatus();
            return;
        }
        if(lastHandledGoal != null){
            lastHandledGoal.printGoalStructureStatus();
            return;
        }
        System.out.println("NO GOAL COMPLETED");
    }

    //public void refresh() {
        //getState().updateBelief(env().observe(getState().id));
    //}

    public BeliefState getState(){
        return (BeliefState) this.state;
    }

    public LabRecruitsEnvironment env(){
        return getState().env();
    }
}
