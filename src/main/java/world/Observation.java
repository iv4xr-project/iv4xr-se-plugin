package world;

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
        public String tag;
        public String id;
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

        return wom;
    }
}