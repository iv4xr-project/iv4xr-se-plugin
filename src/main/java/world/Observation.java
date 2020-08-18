package world;

import java.util.function.Function;

import helperclasses.datastructures.Vec3;

public class Observation {
    public class Agent {
        public String id;
        public Vec3 position;
        public Vec3 velocity;
        public Boolean didNothing;
    }

    public class Meta {
        public Integer tick;
    }

    public class Objects {

        public class Transform {
            public Vec3 position;
            public Vec3 rotation;
            public Vec3 velocity;
        }
        public class Collider {
            public Vec3 center;
            public Vec3 size;
        }

        // Optional Observable properties:

        // Buttons or maybe doors can be interacted with
        public class Interactable {
            public Boolean interactable;
            public Float interactionCooldown;
            public Float timeSinceInteraction;
        }

        // For colored buttons
        public class Colorized {
            public Vec3 color;
        }

        // Colored screen
        public class ColorScreen {
            public Vec3 color;
        }

        // States for doors and toggle buttons
        public class Toggleable {
            public Boolean isActive;
        }
        public String name;
        public String tag;
        public String id;
        public Collider[] colliders;
        public Transform transform;

        public Interactable Interactable;
        public Colorized Colorized;
        public ColorScreen ColorScreen;
        public Toggleable Toggleable;
    }

    public Agent agent;
    public Meta meta;
    public Objects[] objects;
    public int[] navMeshIndices;

    public static LabWorldModel toWorldModel(Observation obs) {
        if (obs == null) return null;
        var wom = new LabWorldModel();
        wom.agentId   = obs.agent.id;
        wom.position  = obs.agent.position;
    	wom.extent    = new Vec3(0.2,0.75,0.2) ;
        wom.velocity  = obs.agent.velocity;
        wom.timestamp = obs.meta.tick;

        wom.didNothingPreviousGameTurn = obs.agent.didNothing;
        wom.visibleNavigationNodes = obs.navMeshIndices;

        // Agent position correction
        wom.position.y += 0.75;

        for (int i = 0; i < obs.objects.length; i++) {
            wom.updateEntity(toWorldEntity(obs.objects[i]));
        }

        return wom;
    }

    public static LabEntity toWorldEntity(Objects obj) {
        if (obj == null) return null;
        String we_type = "";

        Function<LabEntity, LabEntity> builder = we -> {
            we.position = obj.transform.position;
            we.extent = Vec3.zero();
            return we;
        };

        // TODO: combine all colliders or change how WorldEntity stores the extent.
        if (obj.colliders != null && obj.colliders.length > 0) {
            
            builder = builder.andThen(we -> {
                we.position = obj.colliders[0].center;
                we.extent = Vec3.multiply(0.5, obj.colliders[0].size);
                return we;
            });
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
        } else if (obj.tag.equals("ColorScreen") && obj.ColorScreen != null) {
            we_type = LabEntity.COLORSCREEN;
            builder = builder.andThen(we -> {
                we.properties.put("color", obj.ColorScreen.color);
                return we;
            });
        }


        LabEntity we_final = new LabEntity(
            obj.name, // obj.id would be more appropriate, but some tests still use name.
            we_type,
            true
        );
        return builder.apply(we_final);
    }
}