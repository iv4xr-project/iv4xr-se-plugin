package world;

import java.util.*;
import environments.LabRecruitsEnvironment;
import eu.iv4xr.framework.world.WorldEntity;
import eu.iv4xr.framework.world.WorldModel;
import helperclasses.datastructures.Vec3;
import nl.uu.cs.aplib.mainConcepts.Environment;

public class LabWorldModel extends WorldModel {
	
	/**
	 * Describing the part of the static world that the agent currently sees.
	 * Here, it is described as a set of nodes in the world's navigation graph
	 * that the agent sees. Each node is represented by a single integer which
	 * is an index to where the node is stored in the world's navigation graph.
	 * The graph itself is not represented in this object; though it is assumed
	 * that the agent has a way to access it.
	 */
	public int[] visibleNavigationNodes ; 
	
	public boolean didNothingPreviousGameTurn ;
	
	// Lab Recruits so far only have one interaction-type with items in the game;
	// let's just call it "interact".
	public static final String INTERACT = "interact" ;
	
	final Map<String,Set<String>> availableInteractionTypes_ = new HashMap<>() ;
	
	public LabWorldModel() { super() ; 
	   // specify which interaction type is possible on which entity types:
	   Set<String> justSwitch = new HashSet<>() ;
	   justSwitch.add(LabEntity.SWITCH) ;
	   availableInteractionTypes_.put(INTERACT,justSwitch) ;
	}
	
	@Override
	public LabEntity getElement(String id) {
		return (LabEntity) super.getElement(id) ;
	}

	/** 
	 * Return the center position of the agent, with the y-position shifted to the floor level.
	 */
	public Vec3 getFloorPosition() {
		return new Vec3(position.x,position.y -  extent.y, position.z) ;
	}
	
    @Override
    public Map<String,Set<String>> availableInteractionTypes() {
		return availableInteractionTypes_ ;
	}
    
    /**
     * The agent can interact with e is e is a switch, and the agent is at e's location.
     */
    @Override
    public boolean canInteract(String interactionType, WorldEntity e) {
    	// since Lab Reruits so far only have one interaction type, we skip
    	// checking interactionType.
    	var target = (LabEntity) e ;
    	
    	// only switches/buttons can be interacted:
    	if (target.type != LabEntity.SWITCH) return false ;
    	    	
		var target_onfloorPosition = target.getFloorPosition() ;
		var min = Vec3.subtract(target_onfloorPosition, target.extent);
        var max = Vec3.sum(target_onfloorPosition, target.extent);
        if (e.extent.y <= 0.2) {
        	min.y -= 0.2 ;
        	max.y += 0.2 ;
        }
        var agent_floorp = getFloorPosition() ;
    	
        return (agent_floorp.x > min.x && agent_floorp.x < max.x &&
        		agent_floorp.y > min.y  && agent_floorp.y < max.y  &&
        		agent_floorp.z > min.z && agent_floorp.z < max.z);
	}
    
	@Override
	public boolean isBlocking(WorldEntity e) {
		switch(e.type) {
		   case LabEntity.DOOR : return ! e.getBooleanProperty("isOpen") ;
		   case LabEntity.COLORSCREEN : return true ;
		   case LabEntity.GOAL : return true ;
		   default : return false ;
		}
	}
	
	@Override
	public LabWorldModel interact(Environment env, String interactionType, WorldEntity e) {
		// For now, Lab Recruits only have one interaction type, so we are not going
		// to check interactionType.
		var target = (LabEntity) e ;
		if (target.type != LabEntity.SWITCH) return null ;
		return ((LabRecruitsEnvironment) env).interactWith(this.agentId, target.id) ;
	}
	
	@Override
	public LabWorldModel observe(Environment env) {
		return ((LabRecruitsEnvironment) env).observe(this.agentId) ;
	}
	
	@Override
	public LabWorldModel moveToward(Environment env, Vec3 position) {
		return ((LabRecruitsEnvironment) env).moveToward(this.agentId,this.getFloorPosition(), position) ;
	}
	

}
