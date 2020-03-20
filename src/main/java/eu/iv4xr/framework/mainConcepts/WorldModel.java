package eu.iv4xr.framework.mainConcepts;

import java.util.* ;

public class WorldModel {
	
	public long timestamp = 0 ;
	public Map<String,WorldEntity> elements = new HashMap<>() ;
	
	public WorldModel() { }
	
	public void updateTimeStamp() { timestamp++ ; }
	
	public WorldEntity getElement(String id) {
		return elements.get(id) ;
	}
	
	public Number getElementProperty(String elementId, String propertyName) {
		var e = getElement(elementId) ;
		if (e==null) return null ;
		return e.getProperty(propertyName) ;
	}
	
	public Boolean getElementBooleanProperty(String elementId, String propertyName) {
		var e = getElement(elementId) ;
		if (e==null) return null ;
		return e.getBooleanProperty(propertyName) ;
	}
	
	public void addEntity(WorldEntity e) {
		if (e==null) return ;
		var old = elements.put(e.id,e) ;
		e.linkPreviousState(old);
	}


}
