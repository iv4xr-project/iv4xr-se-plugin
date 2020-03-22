package world;

import eu.iv4xr.framework.world.WorldEntity;

public class LabEntity extends WorldEntity {

	public LabEntity(String id, String type, boolean interactable, boolean dynamic) {
		super(id,type,interactable,dynamic) ;
	}
	
	@Override
	public boolean isBlocking() {
		switch(type) {
		   case "Door" : return ! getBooleanProperty("isOpen") ;
		   case "ColorSwitch" : return true ;
		   default : return false ;
		}
	}
	
}
