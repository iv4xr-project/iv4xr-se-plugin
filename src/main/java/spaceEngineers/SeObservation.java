package spaceEngineers;

import helperclasses.datastructures.Vec3;
import world.LegacyEntity;
import java.util.List;

public class SeObservation {

    public String agentID;
    public Vec3 position;
    public Vec3 velocity;

    public List<SeEntity> entities;

    public List<SeBlock> blocks;
}
