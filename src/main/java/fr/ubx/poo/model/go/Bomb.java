package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;

import java.util.Timer;
import java.util.TimerTask;
import java.util.List ;
import java.util.Iterator;

public class Bomb extends GameObject {

    private int state, range, level ;
    private boolean hasExplosed = false ;
    private long start;
    public Bomb(Game game, Position position, int range, long start) {
        super(game, position);
        this.range = range ;
        this.level = game.getLevel() ;
        state = 3;
        this.start = start ;
    }
    public void update(long now) {
        state = (int)((start-now)/1000000000L) + 3 ;
        System.out.println(state) ;
    }
    public boolean isExplosing(){
        return state == -1 ;
    }
    public void explose() {
        hasExplosed = true ;
    }
    public boolean hasExplosed(){
        return hasExplosed ;
    }
    public int getLevel(){
        return level ;
    }
    public int getRange(){
        return level ;
    }

    public int getState() {
        return state;
    }

    @Override
    public String toString() {
        return "Bomb";
    }
}