package world;

import eu.iv4xr.framework.world.WorldEntity;
import helperclasses.datastructures.Vec3;

public class LabEntity extends WorldEntity {

	public LabEntity(String id, String type, boolean interactable, boolean dynamic) {
		super(id,type,interactable,dynamic) ;
	}
	
	@Override
	public boolean isBlocking() {
		switch(type) {
		   case "Door" : return ! getBooleanProperty("isOpen") ;
		   case "ColorScreen" : return true ;
		   case "Goal" : return true ;
		   default : return false ;
		}
	}
	/** 
	 * Return the center position of the entity, with the y-position shifted to the floor level.
	 */
	public Vec3 getFloorPosition() {
		return new Vec3(position.x,position.y -  extent.y, position.z) ;
	}
	
	/**
	 * Check if the agent position p is within the interaction distance of this entity; p is assumed
	 * to be the on-floor center position of the agent.
	 */
	@Override
	public boolean isWithinInteractionDistance(Vec3 p) {
		var onfloorPosition = this.getFloorPosition() ;
		var extent_ = new Vec3(extent) ;
		var min = Vec3.subtract(onfloorPosition, extent);
        var max = Vec3.sum(onfloorPosition, extent);
        if (extent.y <= 0.2) {
        	min.y -= 0.2 ;
        	max.y += 0.2 ;
        }
        return (p.x > min.x && p.x < max.x &&
                p.y > min.y  && p.y < max.y  &&
                p.z > min.z && p.z < max.z);
	}
	
	/**
	 * Let e be non-null and represent the same entity as this entity (they have the same ID), but
	 * its state is possibly different than this entity. This method checks if both entity have
	 * the same state.
	 * 
	 * We override the method here to provide a faster check.
	 */
	@Override
	public boolean hasSameState(WorldEntity old) {
		if (!this.dynamic) return true ;
		switch(this.type) {
		  case "Door"   : return getBooleanProperty("isOpen") == old.getBooleanProperty("isOpen") ;
		  case "Switch" : return getBooleanProperty("isOn") == old.getBooleanProperty("isOn") ;
		  case "ColorScreen" : return getProperty("color").equals(old.getProperty("color")) ;
		}
		return false ;
	}
	
}
