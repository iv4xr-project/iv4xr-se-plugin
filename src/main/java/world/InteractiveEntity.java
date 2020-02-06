/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package world;

import helperclasses.datastructures.Vec3;

import java.util.List;

public class InteractiveEntity extends Entity {

    public boolean isActive;
    public List<String> connectedObjects;
    public Vec3 center; // Bounding box position
    public Vec3 extents; // Half size

    public InteractiveEntity(){
        super(EntityType.Interactive);
    }

    public boolean canInteract(Vec3 point) {
        var min = Vec3.subtract(center, extents);
        var max = Vec3.sum(center, extents);
        return (point.x > min.x && point.x < max.x &&
                point.y > min.y && point.y < max.y &&
                point.z > min.z && point.z < max.z);
    }

    @Override
    public boolean update(Entity e){
        if(e instanceof InteractiveEntity && super.update(e)){
            InteractiveEntity ie = (InteractiveEntity) e;
            this.isActive = ie.isActive;
            this.center = ie.center;
            this.extents = ie.extents;
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof InteractiveEntity))
            return false;
        InteractiveEntity o = (InteractiveEntity) other;
        return     this.isActive == o.isActive
                && this.connectedObjects.equals(o.connectedObjects)
                && this.center.equals(o.center)
                && this.extents.equals(o.extents)
                && super.equals(other);
    }
}
