/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package world;

import helperclasses.datastructures.Vec3;

/**
 * A representation of a "dynamic" object in the world. 
 * This is the legacy representation as originally provided by the LabRecruits 
 * developers. This will be translated to iv4xr's WorldEntity.
 *
 */
public class LegacyDynamicEntity extends LegacyEntity {

    public Vec3 velocity;

    public LegacyDynamicEntity(){
        super(LegacyEntityType.Dynamic);
    }

    @Override
    public boolean update(LegacyEntity e){
        if(e instanceof LegacyDynamicEntity && super.update(e)){
            this.velocity = ((LegacyDynamicEntity) e).velocity;
            return true;
        }
        return false;
    }


    @Override public String toString(){
        return (super.toString());
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof LegacyDynamicEntity))
            return false;
        LegacyDynamicEntity o = (LegacyDynamicEntity) other;
        return this.velocity.equals(o.velocity) && super.equals(other);
    }
}
