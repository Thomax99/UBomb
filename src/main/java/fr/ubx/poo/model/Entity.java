package fr.ubx.poo.model;

import fr.ubx.poo.game.Direction;

/**
 * An entity is the basic brick of all the things on the game.
 */
public abstract class Entity {
    private int level ; // the level of the entity
    /**
     * 
     * @param dir the direction in which something is going
     * @return if an entity which is moving in direction dir can go on this object or if not
     */
    public boolean canMoveIn(Direction dir){ //need to be modified in function of the inherits
        return true ;
    }
    public boolean hasToBeRemoved(){
        return false ;
    }
    public abstract void remove() ;
    /**
     * 
     * @param level the level in which the entity is
     */
    public void setLevel(int level){
        this.level = level ;
    }
    /**
     * 
     * @return the actual level is which is the entity is
     */
    public int getLevel(){
        return level ;
    }
    /**
     * function used to notify a gameObject that an explosion on it
     * @param now the moment that an explosion occur on the gameObject
     */
    public void explosion(long now){
        remove(); //by default, the gameObject in which an explosion occur are removed
    }
}
