package world;

import eu.iv4xr.framework.world.WorldEntity;
import eu.iv4xr.framework.world.WorldModel;
import helperclasses.datastructures.Vec3;

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
	
	public LabWorldModel() { super() ; }
	
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
	
   /**
     * To convert various types of LabRecruits legacy-entities to WorldEntities.
     */
    public static LabEntity toWorldEntity(LegacyEntity e) {
    	var myid = e.id ;
    	var type = "?" ;
    	LegacyInteractiveEntity iteractive_e = null ;
    	if (e instanceof LegacyInteractiveEntity) iteractive_e = (LegacyInteractiveEntity) e ;
    	
    	// DYNAMIC FIRE HAZARD
    	//    "Dynamic" Fire Hazard is represented a legacy-entity :|. It is also
    	//    not dynamic in the sense of moving (so, the fire hazard is actually stationary).
    	//    The ID need to be fixed, since all fire-hazard turns out to have the same id!
    	if (e.id.startsWith("FireHazard")) {
    		// identify it by its position, since it doesn't move anyway:
    		myid = "FH" + e.position ;
    		type = "FireHazard" ;
    		var lenty =  new LabEntity(myid,type,false,false) ;
    		lenty.position = new Vec3(e.position.x, e.position.y+1, e.position.z) ;
    		lenty.extent   = new Vec3(0.5, 1, 0.5) ;
    		lenty.timestamp = e.lastUpdated ;
        	return lenty ;
    	}
    	
    	switch (e.tag) {
    	case "Door" : {
    		// door
    		// door can be blocking
    		type = e.tag ;
    		var lenty = new LabEntity(myid,type,false,true) ;
    		lenty.position = new Vec3(e.position.x, e.position.y+1, e.position.z) ;
    		lenty.extent   = new Vec3(0.5, 1, 0.5) ;
    		lenty.properties.put("isOpen", iteractive_e.isActive) ;
    		lenty.timestamp = e.lastUpdated ;
        	return lenty ;
    	    }
    	case "Switch" : {
    		// button/switch, including color switch
    		// switch is non-blocking
    		type = e.tag ;
    		var lenty = new LabEntity(myid,type,true,true) ;
    		lenty.position = new Vec3(e.position.x, e.position.y, e.position.z) ;
    		lenty.extent   = new Vec3(0.5, 0.2, 0.5) ;
    		lenty.properties.put("isOn", iteractive_e.isActive) ;
    		lenty.timestamp = e.lastUpdated ;
        	return lenty ;
    	    }
    	case "ColorScreen" : {
    		// Unfortunately LabRecruits does NOT pass information on the bounding box of
    		// this entity, nor its orientation.
    		// We will INSIST this orientation to be encoded as the first letter of  its ID.
    		// Note that color-screen is blocking.
    		type = e.tag ;
    		var lenty = new LabEntity(myid,type,false,true) ;
    		lenty.position = new Vec3(e.position.x, e.position.y+1, e.position.z) ;

    		// determining its bounding box ... need to know the screen orientation
            var orientation = myid.substring(0,1).toUpperCase() ;
            if (orientation.equals("W") || orientation.equals("E")) {
            	lenty.extent = new Vec3(1, 1, 0.5) ;
            }
            else if (orientation.equals("S") || orientation.equals("N")) {
            	lenty.extent = new Vec3(0.5, 1, 1) ;
            }
            else throw new IllegalArgumentException("Color switch " + myid + " needs to encode its orientation as the first letter of its id.") ;
    		
            lenty.properties.put("color", iteractive_e.property) ;
            lenty.timestamp = e.lastUpdated ;
        	return lenty ;
    	    }
    	
    	case "Goal" : {
    		// Unfortunately LabRecruits does NOT pass information on the bounding box of
    		// this entity.
    		type = e.tag ;
    		var lenty = new LabEntity(myid,type,false,false) ;
    		lenty.position = new Vec3(e.position.x, e.position.y+0.9, e.position.z) ;
    		lenty.extent = new Vec3(0.25, 0.9, 0.25) ;
            lenty.timestamp = e.lastUpdated ;
        	return lenty ;
    	    }
    	}
    	
    	// Other agents, NPCs are not observed! TODO: fix this.
    	
    	// unknown entity type!
    	throw new IllegalArgumentException("Unknown LabRecruits enitity type.") ;
    }
    
    /**
     * To convert legacy-observation represebting what an agent sees to iv4xr WorldModel (WOM).
     */
    public static LabWorldModel toWorldModel(LegacyObservation obs) {
    	if (obs==null) return null ;
    	var wom = new LabWorldModel() ;
    	wom.agentId = obs.agentID ;
    	wom.position = new Vec3(obs.agentPosition) ;
    	wom.position.y += 0.75 ;
    	wom.extent = new Vec3(0.2,0.75,0.2) ;
    	wom.timestamp = obs.tick ;
    	wom.velocity = obs.velocity ;
    	wom.didNothingPreviousGameTurn = obs.didNothing ;
    	for(LegacyEntity e : obs.entities) wom.addEntity(toWorldEntity(e)) ;
    	wom.visibleNavigationNodes = obs.navMeshIndices ;
    	return wom ;
    }
}
