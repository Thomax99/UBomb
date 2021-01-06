/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.Updatable;
import fr.ubx.poo.model.decor.Decor;

public class Explosion extends Decor implements Updatable{
    private long start ;
    public Explosion(long start){
        super() ;
        this.start = start ;
    }
    public void update(long now) {
        if ((now-start) / 1000000000L != 0){
            remove() ;
        }
    }
    @Override
    public boolean isExplosion(){
        return true ;
    }
}
