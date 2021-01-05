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
    private boolean hasToBeRemoved = false ;

    public void computeDecor(Player player){
    }
    /**
     * 
     * @return if it is possible to go on a decor or not
     */
    public boolean canMoveIn(){
        return true ;
    }
    public boolean hasToBeRemoved(){
        return hasToBeRemoved ;
    }
    public void remove(){
        hasToBeRemoved = true ;
    }
    public boolean canExplose(){
        return true ;
    }
    public boolean isPrincess(){
        return false ;
    }


}
