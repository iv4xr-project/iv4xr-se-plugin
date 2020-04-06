/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package world;

import helperclasses.datastructures.Vec3;

import java.util.List;

/**
 * Summary of what an agent's character can see.
 * This is the legacy representation as originally provided by the LabRecruits 
 * developers. This will be translated to iv4xr's WorldModel.
 */ 
public class LegacyObservation {

    public int tick; // The game tick when this observation was taken.
    public String agentID;
    public Vec3 agentPosition;
    public Vec3 velocity;
    public List<LegacyEntity> entities;
    public int[] navMeshIndices;
    public boolean didNothing;
     
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
    		var lenty =  new LabEntity(myid,LabEntity.FIREHAZARD,false) ;
    		lenty.position = new Vec3(e.position.x, e.position.y+1, e.position.z) ;
    		lenty.extent   = new Vec3(0.5, 1, 0.5) ;
    		lenty.timestamp = e.lastUpdated ;
        	return lenty ;
    	}
    	
    	switch (e.tag) {
    	case "Door" : {
    		// door
    		// door can be blocking
    		var lenty = new LabEntity(myid,LabEntity.DOOR,true) ;
    		lenty.position = new Vec3(e.position.x, e.position.y+1, e.position.z) ;
    		lenty.extent   = new Vec3(0.5, 1, 0.5) ;
    		lenty.properties.put("isOpen", iteractive_e.isActive) ;
    		lenty.timestamp = e.lastUpdated ;
        	return lenty ;
    	    }
    	case "Switch" : {
    		// button/switch, including color switch
    		// switch is non-blocking
    		var lenty = new LabEntity(myid,LabEntity.SWITCH,true) ;
    		lenty.position = new Vec3(e.position.x, e.position.y, e.position.z) ;
    		lenty.extent   = new Vec3(0.5, 0, 0.5) ;
    		lenty.properties.put("isOn", iteractive_e.isActive) ;
    		lenty.timestamp = e.lastUpdated ;
        	return lenty ;
    	    }
    	case "ColorScreen" : {
    		// Unfortunately LabRecruits does NOT pass information on the bounding box of
    		// this entity, nor its orientation.
    		// We will INSIST this orientation to be encoded as the first letter of  its ID.
    		// Note that color-screen is blocking.
    		var lenty = new LabEntity(myid,LabEntity.COLORSCREEN,true) ;
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
    		var lenty = new LabEntity(myid,LabEntity.GOAL,false) ;
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
     * To convert legacy-observation representing what an agent sees to iv4xr WorldModel (WOM).
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
    	for(LegacyEntity e : obs.entities) {
        	// convert e to a WorldEntity and assign the time stamps to it:
    		var wenty = toWorldEntity(e) ;
    		wenty.assignTimeStamp(wom.timestamp);
    		wom.elements.put(e.id,wenty) ;
    	}
    	wom.visibleNavigationNodes = obs.navMeshIndices ;
    	return wom ;
    }
}
