/*
 * Copyright (c) 2020. Thomas Morin && Enzo Sekkai
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Direction;
/**
 * A Scarecrow is an entity which is attracting the monsters if their moving policies are guided by a position
 * The way to put it is to take a scarecrow bonus and the bomb is now a scarecrow
 * There is a constraint in the placement of the scarecrow : they can be just one by level because otherwise the monsters can't know where they have to go
 */
public class Scarecrow extends Decor {
    @Override
    public String toString() {
        return "Scarecrow";
    }
    @Override
    public boolean canMoveIn(){
        return false ; //it is impossible to move on a scarecrow
    }
    @Override
    public boolean isScarecrow(){
        return true ;
    }
}
