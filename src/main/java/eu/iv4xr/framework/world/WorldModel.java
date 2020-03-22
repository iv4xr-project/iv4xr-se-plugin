package eu.iv4xr.framework.world;

import java.io.Serializable;
import java.util.* ;

import helperclasses.datastructures.Vec3;

/**
 * This describes a fragment of a virtual world in terms of how it is structurally
 * populated by in-world entities. This fragment can represent what an agent currently
 * sees. We can also use the same representation to represent the agent's belief on
 * how the world is structured; this may incorporate its past knowledge which may no
 * longer be uptodate.
 */
public class WorldModel {
	
	public String agentId ;
	public Vec3 position ; // agent's position
	public Vec3 velocity ; // agent's velocity
	public Vec3 extent ; // agent's dimension (x,y,z size/2)
	public long timestamp = -1 ;
	public Map<String,WorldEntity> elements = new HashMap<>() ;
	
	public WorldModel() { }
	
	/**
	 * Increase the time stamp by one unit.
	 */
	public void increaseTimeStamp() { timestamp++ ; }
	
	/**
	 * Search a top-level entity with the given id. Note that this method does NOT search 
	 * recursively in the set of sub-entities.
	 */
	public WorldEntity getElement(String id) {
		return elements.get(id) ;
	}
		
	public void addEntity(WorldEntity e) {
		if (e==null) return ;
		var old = elements.put(e.id,e) ;
		e.linkPreviousState(old);
	}


}
