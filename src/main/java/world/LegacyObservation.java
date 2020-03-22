/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package world;

import helperclasses.datastructures.Vec3;

import java.util.List;

/**
 * Summary of what an agent's character can see.
 * This is the legacy representation as originally provided by the LabRecruits 
 * developers. This will be translated to iv4xr's WorldModel.
 */ 
public class LegacyObservation {

    public int tick; // The game tick when this observation was taken.
    public String agentID;
    public Vec3 agentPosition;
    public Vec3 velocity;
    public List<LegacyEntity> entities;
    public int[] navMeshIndices;
    public boolean didNothing;
}
