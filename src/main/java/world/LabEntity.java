package world;

import java.util.Collection;
import java.util.HashSet;

import environments.LabRecruitsEnvironment;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.mainConcepts.WorldModel;
import eu.iv4xr.framework.spatial.Box;
import eu.iv4xr.framework.spatial.Line;
import eu.iv4xr.framework.spatial.LineIntersectable;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.mainConcepts.Environment;

public class LabEntity extends WorldEntity implements LineIntersectable{
	
	// entity types
	public static final String DOOR = "Door" ;
	public static final String GOAL = "Goal" ;
	public static final String COLORSCREEN = "ColorScreen" ;
	public static final String FIREHAZARD = "FireHazard" ;
	public static final String SWITCH = "Switch" ;
	
	public LabEntity(String id, String type, boolean dynamic) {
		super(id,type,dynamic) ;
	}
	
	/** 
	 * Return the center position of the entity, with the y-position shifted to the floor level.
	 */
	public Vec3 getFloorPosition() {
		return new Vec3(position.x,position.y -  extent.y, position.z) ;
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
		  case DOOR   : return getBooleanProperty("isOpen") == old.getBooleanProperty("isOpen") ;
		  case SWITCH : return getBooleanProperty("isOn") == old.getBooleanProperty("isOn") ;
		  case COLORSCREEN : return getProperty("color").equals(old.getProperty("color")) ;
		}
		return false ;
	}

	@Override
	public Collection<Vec3> intersect(Line l) {
		// only these types can block movements:
		if (type.equals(DOOR) || type.equals(COLORSCREEN)) {
			// use a box to calculate the intersection with this door :D .. stretch the extent a bit larger
			Box box = new Box(this.getFloorPosition(),Vec3.add(Vec3.mul(this.extent,2f), new Vec3(0.1f,0,0.1f))) ;
			var intersections = box.intersect(l) ;
			//System.out.println(">>> " + intersections) ;
			return intersections ;
		}
		else 
			return new HashSet<>() ;
	}
	
}
