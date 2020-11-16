/*
 * Copyright (c) 2020. Thomas Morin
 */

package fr.ubx.poo.model.bonus;

import fr.ubx.poo.model.Entity;


public class Bonus extends Entity {
    private final boolean alreadyTaken = false;
    public boolean isAlreadyTaken(){
        return alreadyTaken ;
    }
}
