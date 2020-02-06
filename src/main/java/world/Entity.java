/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package world;

import helperclasses.datastructures.Vec3;

/**
 * A representation of an object in the game world. Only a few fields are exposed.
 */
public class Entity {

    public EntityType type;
    public String tag;
    public String id;
    public Vec3 position;
    public int lastUpdated = -1;
    public String property = "";

    public Entity(EntityType type){
        this.type = type;
    }

    public Entity(){
        this.type = EntityType.Entity;
    }

    // update references rather than overwrite them, because there are a lot of lists that just contain references
    public boolean update(Entity e){
        // do not update older versions
        if(!this.id.equals(e.id) || lastUpdated >= e.lastUpdated)
            return false;

        this.position = e.position;
        this.lastUpdated = e.lastUpdated;

        return true;
    }

    /**
     * @return A nice way to display an entity
     */
    @Override
    public String toString() {
        if(property.equals(""))
            return String.format("[%s %s(%s), pos:%s]", type, tag, id, position.toString());
        return String.format("[%s %s(%s), pos:%s, property:%s]", type, tag, id, position.toString(), property);
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof Entity))
            return false;
        Entity o = (Entity) other;
        return     this.tag.equals(o.tag)
                && this.property.equals(o.property)
                && this.type.equals(o.type)
                && this.id.equals(o.id)
                && this.position.equals(o.position);
    }
}
