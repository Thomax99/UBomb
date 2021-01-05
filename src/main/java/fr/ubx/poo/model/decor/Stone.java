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
    @Override
    public boolean canMoveIn(){ 
        return false ;
    }
    @Override
    public boolean canExplode(){
        return false ;
    }
}
