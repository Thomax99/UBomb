/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;
import fr.ubx.poo.game.Direction;

public class Tree extends Decor {
    @Override
    public String toString() {
        return "Tree";
    }
    @Override
    public boolean canMoveIn(){
        return false ;
    }
    public boolean canExplose(){
        return false ;
    }
}
