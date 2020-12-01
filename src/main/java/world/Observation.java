package world;

import java.io.Serializable;
import java.util.function.Function;

import eu.iv4xr.framework.spatial.Vec3;


/**
 * The observation JSON generated in Observation.cs in the game.
 */
public class Observation {
    public class Agent {
        public String id;
        public Vec3 position;
        public Vec3 velocity;
        public Boolean didNothing;
        public int health;
        public int score ;
        public String mood ;
    }

    static public class Meta {
        /** Integer representation of the time */
        public Integer tick;

        /** ISO 8601 timestamp of the data sent by the game */
        public String time;
    }

    /**
     * The layout of the SerializedGameObject generated in APLSynced.cs
     */
    static public class GameObject {

        /** The transform of the game object. */
    	static public class Transform {
            public Vec3 position;
            public Vec3 rotation;
            public Vec3 velocity;
        }

        /** The axis aligned bounding box of Unity's collider */
    	static public class Collider {
            /** The center of the collider in world space */
            public Vec3 center;
            /** The size of the bounding box */
            public Vec3 size;
        }

        // Optional Observable properties:
        /** Buttons or maybe doors can be interacted with */
    	static public class Interactable {
            public Boolean interactable;
            public Float   interactionCooldown;
            public Float   timeSinceInteraction;
        }

        /** For colored buttons */
    	static public class Colorized {
            public Color color;
        }

        /** Colored screen */
    	static public class ColorScreen {
            public Color color;
        }
        
    	static public class Color implements Serializable {
			private static final long serialVersionUID = 1L;
			public float r;
        	public float g;
        	public float b;
        	@Override
        	public String toString() {
        		return("" + r + "/" + g + "/" + b) ;
        	}
        }

        /** States for doors and toggle buttons */
    	static public class Toggleable {
            public Boolean isActive;
        }

        /** The fire hazard */
    	static public class FireHazard {

        }

        // Object data
        public String name;
        public String tag;
        public String id;
        public Collider[] colliders;
        public Transform  transform;

        // Optional observable component data
        public Interactable Interactable;
        public Colorized    Colorized;
        public ColorScreen  ColorScreen;
        public Toggleable   Toggleable;
        public FireHazard   FireHazard;
    }

    public Agent agent;
    public Meta meta;
    public GameObject[] objects;
    public int[] navMeshIndices;

    public static LabWorldModel toWorldModel(Observation obs) {
        if (obs == null) return null;
        var wom = new LabWorldModel();
        wom.agentId   = obs.agent.id;
        wom.health = obs.agent.health ;
        wom.score = obs.agent.score ;
        wom.mood = obs.agent.mood ;
        wom.position  = obs.agent.position;
    	wom.extent    = new Vec3(0.2f,0.75f,0.2f) ;
        wom.velocity  = obs.agent.velocity;
        wom.timestamp = obs.meta.tick;

        wom.didNothingPreviousGameTurn = obs.agent.didNothing;
        wom.visibleNavigationNodes = obs.navMeshIndices;

        // Agent position correction
        wom.position.y += 0.75;

        for (int i = 0; i < obs.objects.length; i++) {
            var we = toWorldEntity(obs.objects[i]);
            we.assignTimeStamp(wom.timestamp);
            wom.updateEntity(we);
        }

        return wom;
    }
    
    static String constructId(GameObject obj) {
    	switch(obj.tag) {
    	   case "Door" : return obj.name ;
    	   case "Switch" : return obj.name ;
    	   case "ColorScreen" : return obj.name ;
    	   case "Decoration" :
    		   // decorations are statics, so we can use their x,y,z coordinates to identify them
    		   int k = obj.name.indexOf("Prefab") ;
    		   String id_ = obj.name.substring(0,k) + "@" + obj.transform.position.toString() ; 
    		   return id_ ;
    	   default :
    		   // well other cases
    		   // (1) Fire
    		   if (obj.FireHazard != null) {
    			   // Firehazard does not move around, so we will use their positions to identify them
    			   k = obj.name.indexOf("(Clone)") ;
        		   id_ = obj.name.substring(0,k) + "@" + obj.transform.position.toString() ; 
        		   return id_ ;
    		   }
    		   // other cases ... not sure. Use name?
    		   return obj.name ;
    	}
    }

    public static LabEntity toWorldEntity(GameObject obj) {
        if (obj == null) return null;
        String we_type = "";
        // dynamic game-object can change state; for now we will just declare all game-objects sent by LR to be
        // dynamic. TODO: refine this in the future. E.g. a chair is not dynamic.
        boolean isDynamic = true ; 

        Function<LabEntity, LabEntity> builder = we -> {
            we.position = obj.transform.position;
            we.extent = Vec3.zero();
            return we;
        };

        // TODO: combine all colliders or change how WorldEntity stores the extent.
        if (obj.colliders != null && obj.colliders.length > 0) {
            
            builder = builder.andThen(we -> {
                we.position = obj.colliders[0].center;
                we.extent = Vec3.mul(obj.colliders[0].size, 0.5f);
                we.extent.x = Math.abs(we.extent.x) ;
                we.extent.y = Math.abs(we.extent.y) ;
                we.extent.z = Math.abs(we.extent.z) ;
                return we;
            });
            if (obj.colliders.length > 1) {
                System.out.println(String.format("Multiple colliders found for object "
                		 + obj.name + "/" + obj.id + "/" + obj.tag +
                		 ", this is currently not supported.", obj.name));
            }
        }

        if (obj.tag.equals("Door") && obj.Toggleable != null) {
            we_type = LabEntity.DOOR;
            builder = builder.andThen(we -> {
                we.properties.put("isOpen", obj.Toggleable.isActive);
                return we;
            });

        } else if (obj.tag.equals("Switch") && obj.Interactable != null) {
            we_type = LabEntity.SWITCH;
            if (obj.Toggleable != null) {
                builder = builder.andThen(we -> {
                    we.properties.put("isOn", obj.Toggleable.isActive);
                    return we;
                });
            } else {
                builder = builder.andThen(we -> {
                    we.properties.put("isOn", false);
                    return we;
                });
            }
            if (obj.Colorized != null) {
                builder = builder.andThen(we ->{
                    we.properties.put("color", obj.Colorized.color);
                    return we;
                });
            }
        } else if (obj.tag.equals("ColorScreen") && obj.ColorScreen != null) {
            we_type = LabEntity.COLORSCREEN;
            builder = builder.andThen(we -> {
            	//System.out.println("### org color: " + obj.ColorScreen.color) ;
                we.properties.put("color", obj.ColorScreen.color);
                return we;
            });
        } else if (obj.tag.equals("Goal")) {
        	we_type = LabEntity.GOAL ;
        }
        else if (obj.FireHazard != null) {
            we_type = LabEntity.FIREHAZARD;
        }

        LabEntity we_final = new LabEntity(
            //obj.name, // TODO: obj.id would be more appropriate, but some tests still use name.
            constructId(obj),
            we_type,
            isDynamic
        );
        return builder.apply(we_final);
    }
}