/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package world;

import helperclasses.datastructures.Vec3;

// A representation of a dynamic object in the world.
public class DynamicEntity extends Entity {

    public Vec3 velocity;

    public DynamicEntity(){
        super(EntityType.Dynamic);
    }

    @Override
    public boolean update(Entity e){
        if(e instanceof DynamicEntity && super.update(e)){
            this.velocity = ((DynamicEntity) e).velocity;
            return true;
        }
        return false;
    }


    @Override public String toString(){
        return (super.toString());
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof DynamicEntity))
            return false;
        DynamicEntity o = (DynamicEntity) other;
        return this.velocity.equals(o.velocity) && super.equals(other);
    }
}
