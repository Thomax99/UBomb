package fr.ubx.poo.model;

import fr.ubx.poo.game.Direction;

public abstract class Entity {
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
}
