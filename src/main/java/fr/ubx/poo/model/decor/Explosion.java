/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.Removable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Stone;
import fr.ubx.poo.model.decor.Tree;
import fr.ubx.poo.model.decor.bonus.Bonus;
import fr.ubx.poo.model.go.character.*;

/***
 * A decor is an element that does not know its own position in the grid.
 */
public class Explosion extends Decor implements Removable {
    private long start ;
    private boolean exist ;
    public Explosion(long start){
        super() ;
        this.start = start ;
        exist = true ;
    }
    public void update(long now) {
        exist = (now-start) / 1000000000L == 0 ;
    }
    public boolean hasToBeRemoved(){
        return !exist ;
    }
    public void remove(){
        exist = false ;
    }
}
