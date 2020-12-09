/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.go.character.Player;

/***
 * A decor is an element that does not know its own position in the grid.
 */
public class Decor extends Entity {
    private boolean hasToBeRemoved = false ;

    public void computeDecor(Player player){
    }
    public boolean hasToBeRemoved(){
        return hasToBeRemoved ;
    }
    public void remove(){
        hasToBeRemoved = true ;
    }
    public void update(long now){
        //nothing to update by default
    }
    public boolean canExplose(){
        return true ;
    }


}
