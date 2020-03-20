package eu.iv4xr.framework.mainConcepts;

import java.util.*;

import helperclasses.datastructures.Vec3;

public class WorldEntity extends WorldModel {
	
	/**
	 * A unique id identifying this entity.
	 */
	public String id ;
	public String name ;
	public Vec3 position ;
	public Vec3 extent ; // bounding box
	public Vec3 velocity ;
	public boolean interactable ;
	public boolean dynamic ;
	public Map<String,Number> properties = new HashMap<>();
	
	/**
	 * To keep one single copy of the entity's previous state.
	 */
	private WorldEntity previousState = null ;
	
	public boolean hasSameState(WorldEntity e) {
		if (! (position.equals(e.position)
			   && velocity.equals(e.velocity)
			   && extent.equals(e.extent)
			   && properties.size() == e.properties.size()))
			return false ;
		for (var P : properties.entrySet()) {
			var q = e.properties.get(P.getKey()) ;
			if (! P.getValue().equals(q)) return false ;
		}
		// so the entities have the same properties.. let's now check the children 
		if (this.elements.size() != e.elements.size()) return false ;
		for (var elem_ : elements.entrySet()) {
			var elem2 = e.elements.get(elem_.getKey()) ;
			if (elem2 == null) return false ;
			var elem1 = elem_.getValue() ;
			if (!elem1.hasSameState(elem2)) return false ;
		}
		return true ;	
	}
	
	public Number getProperty(String propertyName) {
		return properties.get(propertyName) ;
	}
	
	public boolean getBooleanProperty(String propertyName) {
		var N = getProperty(propertyName) ;
		if (N==null) return false ;
		return N.intValue() >= 1 ;
	}
	
	/**
	 * This will link e as the previous state of this Entity. The previous
	 * state of e is cleared to null (we only
	 * want to track the history of past state up to length 1).
	 * 
	 * This method assume that e represents the same Entity as this Entity
	 * (e.g. they have the same id).
	 */
	public void linkPreviousState(WorldEntity e) {
		this.previousState = e ;
		if (e!=null) e.previousState = null ;
	}
	
	/**
	 * True if this entity has no previous state, or if its state differs
	 * from its previous.
	 * Note that we only track 1x previous state (so there is no longer
	 * chain of previous states).
	 */
	public boolean hasChangedState() {
		if (previousState==null) return true ;
		return !hasSameState(previousState) ;
	}
	
	/**
	 * Set the time-stamp of this Entity and its elements to the given time.
	 */
	public void assignTimeStamp(long ts) {
		timestamp = ts ;
		for (var e : elements.values()) e.assignTimeStamp(ts);
	}

}
