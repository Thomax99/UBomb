/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.Updatable;

/***
 * A GameObject can acces the game and knows its position in the grid.
 */
public abstract class GameObject extends Entity {
    protected final Game game;
    private int level ;
    private Position position;
    public GameObject(Game game, Position position) {
        this.game = game;
        this.position = position;
    }
    protected void setLevel(int level){
        this.level = level ;
    }
    public int getLevel(){
        return level ;
    }


    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
    /**
     * 
     * @param p the position which is explosing
     * @return if this gameObject has explosed with an explosion at this position. This function has to manage all
     * the explosion of the game Object if it has to explose.
     */
    public abstract boolean explosion(Position p, long now) ;
}
