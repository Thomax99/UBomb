/*
 * Copyright (c) 2020. Thomas Morin
 */

package fr.ubx.poo.model.decor.explosives;

import fr.ubx.poo.model.decor.Decor;


/***
 * An Explosive object is a decor which can explode itself.
 * It has a range and a level.
 */
public abstract class Explosive extends Decor {
    private int range ;
    public Explosive(int range, int level) {
        this.range = range ;
        setLevel(level) ;
    }
    /**
     * 
     * @return the range of the explosive (ie the number of case around it in which the explosive explode)
     */
    public int getRange(){
        return range ;
    }
    /**
     * @return if the object is a bomb or not. Currently, there is just two types of Explosive : landmines or bombs
     */
    public boolean isBomb(){
        return false ;
    }
    @Override
    public boolean isExplosive(){
        return true ;
    }

}
