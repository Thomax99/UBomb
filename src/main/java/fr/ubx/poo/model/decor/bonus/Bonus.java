/*
 * Copyright (c) 2020. Thomas Morin
 */

package fr.ubx.poo.model.decor.bonus;

import fr.ubx.poo.model.decor.Decor;


public class Bonus extends Decor {
    private boolean alreadyTaken = false;
    public boolean isAlreadyTaken(){
        return alreadyTaken ;
    }
    public void take(){
        alreadyTaken = true ;
    }
}