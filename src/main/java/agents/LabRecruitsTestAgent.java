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
import world.BeliefState;

/**
 * A dedicated sub-class of {@link eu.iv4xr.framework.mainConcepts.TestAgent} to test
 * the Lab Recruits game.
 */
public class LabRecruitsTestAgent extends TestAgent {

    /**
     * The constructor for the test agent without an id or role attached to itself
     * @param state: The agent state
     */
    public LabRecruitsTestAgent(BeliefState state) {
        attachState(state);
    }

    /**
     * The constructor for the test agent with an id or role attached to itself (this is required for agent communication)
     * @param state: The agent state
     * @param id: The id of the agent
     * @param role: The role of the agent
     */
    public LabRecruitsTestAgent(BeliefState state, String id, String role) {
        super(id, role);
        state.id = id;
        attachState(state);
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

    public void refresh() {
        getState().markObservation(env().getResponse(Request.command(AgentCommand.doNothing(getState().id))));
    }

    public BeliefState getState(){
        return (BeliefState) this.state;
    }

    public LabRecruitsEnvironment env(){
        return getState().env();
    }
}
