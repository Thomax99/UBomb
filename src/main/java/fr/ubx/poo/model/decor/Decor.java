/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.Updatable;

/***
 * A decor is an element that does not know its own position in the grid.
 */
public class Decor extends Entity {

    public void computeDecor(Player player){
    }
    /**
     * 
     * @return if it is possible to go on a decor or not
     */
    public boolean canMoveIn(){
        return true ;
    }
    /**
     * 
     * @return if a decor can be explosed by a bomb or not
     */
    public boolean canExplose(){
        return true ;
    }
    /**
     * 
     * @return if the current decor is a princess or not. used for not using the instruction instanceof
     */
    public boolean isPrincess(){
        return false ;
    }
    /**
     *
     * @return if the current decor is a door or not. used for not using the instruction instanceof
     */
    public boolean isDoor(){
        return false ;
    }

}
