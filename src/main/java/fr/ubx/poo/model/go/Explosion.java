/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go;

import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Stone;
import fr.ubx.poo.model.decor.Tree;
import fr.ubx.poo.model.decor.bonus.Bonus;
import fr.ubx.poo.model.go.character.*;

/***
 * A decor is an element that does not know its own position in the grid.
 */
public class Explosion extends GameObject {
    private long start ;
    private boolean exist ;
    public Explosion(Game game, Position position, long start){
        super(game, position) ;
        this.start = start ;
        exist = true ;
    }
    public void explose(){

    }
    public void update(long now) {
        exist = (now-start) / 1000000000L == 0 ;
    }
    public boolean isExisting(){
        return exist ;
    }

}
