/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.Constants;
import fr.ubx.poo.model.Updatable;
import fr.ubx.poo.model.decor.Decor;

public class Explosion extends Decor implements Updatable{
    private long start ;
    public Explosion(long start){
        super() ;
        this.start = start ;
    }
    @Override
    public void update(long now) {
        if ((now-start) / Constants.secondInnanoSec != 0){
            remove() ;
        }
    }
    @Override
    public boolean isExplosion(){
        return true ;
    }
}
