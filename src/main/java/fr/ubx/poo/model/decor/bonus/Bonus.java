/*
 * Copyright (c) 2020. Thomas Morin
 */

package fr.ubx.poo.model.decor.bonus;
import fr.ubx.poo.model.Removable;
import fr.ubx.poo.model.decor.Decor;


public class Bonus extends Decor implements Removable{
    private boolean alreadyTaken = false;
    public boolean hasToBeRemoved(){
        return alreadyTaken ;
    }
    public void remove(){
        alreadyTaken = true ;
    }
}
