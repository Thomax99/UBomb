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

    /**
     * Used by the decor itself to make an interaction with the player when the player is on it (for instance, take a bonus)
     * @param player the player which is going on the decor
     */
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
    public boolean canExplode(){
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
    /**
     *
     * @return if the current decor is an explosion or not. used for not using the instruction instanceof
     */
    public boolean isExplosion(){
        return false ;
    }
    /**
     *
     * @return if the current decor is an explosive object or not. used for not using the instruction instanceof
     */
    public boolean isExplosive(){
        return false ;
    }
    /**
     *
     * @return if the current decor is a scarecrow or not. used for not using the instruction instanceof
     */
    public boolean isScarecrow(){
        return false ;
    }
}
