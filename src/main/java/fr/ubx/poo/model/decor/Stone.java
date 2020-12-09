/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;
import fr.ubx.poo.game.Direction;

public class Stone extends Decor {
    @Override
    public String toString() {
        return "Stone";
    }
    public boolean canMoveIn(Direction dir){ //need to be modified in function of the inherits
        return false ;
    }
    public boolean canExplose(){
        return false ;
    }
}
