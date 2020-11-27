package world;

import java.util.*;
import environments.LabRecruitsEnvironment;
import eu.iv4xr.framework.mainConcepts.W3DEnvironment;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.mainConcepts.WorldModel;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.mainConcepts.Environment;

public class LabWorldModel extends WorldModel {

	public int health ;
	public int score ;
	public String mood ;

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
	public List<WorldEntity> mergeNewObservation(WorldModel observation) {
		LabWorldModel observation_ = (LabWorldModel) observation ;
		this.health = observation_.health ;
		this.score  = observation_.score ;
		this.mood = observation_.mood ;
		this.visibleNavigationNodes = observation_.visibleNavigationNodes ;
		return super.mergeNewObservation(observation) ;
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
		var extent = new Vec3(Math.max(target.extent.x,0.3f),
				              Math.max(target.extent.y,0.3f),
				              Math.max(target.extent.z,0.3f)) ;

		var min = Vec3.sub(target_onfloorPosition,extent);
        var max = Vec3.add(target_onfloorPosition,extent);
        var agent_floorp = getFloorPosition() ;

        //System.out.println(">>> agent @" + agent_floorp + ", e.id @" + target_onfloorPosition) ;

        return (agent_floorp.x > min.x && agent_floorp.x < max.x &&
        		agent_floorp.y > min.y && agent_floorp.y < max.y &&
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
	public LabWorldModel interact(W3DEnvironment env, String interactionType, WorldEntity e) {
		// For now, Lab Recruits only have one interaction type, so we are not going
		// to check interactionType.
		var target = (LabEntity) e ;
		// using != should be ok in this case, as they should point to the same string as well:
		if (target.type != LabEntity.SWITCH) return null ;
		return (LabWorldModel) super.interact(env, interactionType, e) ;
	}

	@Override
	public LabWorldModel observe(W3DEnvironment env) {
		return (LabWorldModel) super.observe(env) ;
	}

	@Override
	public LabWorldModel moveToward(W3DEnvironment env, Vec3 targetLocation) {
		return (LabWorldModel) super.moveToward(env, targetLocation) ;
	}


}
