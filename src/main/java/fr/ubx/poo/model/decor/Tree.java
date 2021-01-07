/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

public class Tree extends Decor {
    @Override
    public String toString() {
        return "Tree";
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
