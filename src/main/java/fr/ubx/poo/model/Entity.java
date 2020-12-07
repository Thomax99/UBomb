package fr.ubx.poo.model;

import fr.ubx.poo.game.Direction;

public abstract class Entity {

    public boolean canMoveIn(Direction dir){ //need to be modified in function of the inherits
        return true ;
    }
    public abstract void update (long now) ;
    public boolean hasToBeRemoved(){
        return false ;
    }
    public abstract void remove() ;
}
