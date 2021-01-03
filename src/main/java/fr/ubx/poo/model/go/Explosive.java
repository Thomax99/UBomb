/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.Game;

/***
 * An Explosive object is a gameobject which can explode
 */
public abstract class Explosive extends GameObject {
    private int range ;
    private boolean hasExploded = false ;

    public Explosive(Game game, Position position, int range) {
        super(game,position) ;
        this.range = range ;
        setLevel(game.getLevel()) ;
    }
    public void remove() {
        hasExploded = true ;
    }
    public boolean hasToBeRemoved(){
        return hasExploded ;
    }

    public int getRange(){
        return range ;
    }
    /**
     * 
     * @return if the object is a bomb or not. Actually, there is just two types of Explosive : landmines or bombs
     */
    public boolean isBomb(){
        return false ;
    }

    @Override
    public boolean explosion(Position p, long now){
        if(!hasToBeRemoved() && getPosition().equals(p)){
            remove() ;
            game.exploser(this, now);
            return true ;
        }
        return false ;
    }
}
