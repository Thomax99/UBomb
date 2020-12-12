/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import java.util.ArrayList;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.Box;

import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.decor.* ;
import fr.ubx.poo.model.decor.bonus.* ;
import fr.ubx.poo.game.Game;

public class Monster extends Character {
    private boolean explosed;
    private long lastMoveTime = 0 ;
    private int speedMoving;
    Direction direction;
    public Monster(Game game, Position position) {
        super(game, position);
        //gestion aleatoire de la vitesse en fonction du niveau
        speedMoving = getSpeed(game.getLevel()-1) ;
        this.explosed = false;
    }

    public void computeMove(){
        Position playerPos = game.getPlayer().getPosition() ;
        ArrayList<Direction> directions = new ArrayList() ;
        directions.add(Direction.N) ; directions.add(Direction.S) ; directions.add(Direction.E) ; directions.add(Direction.W) ;
        directions.sort((Direction d1, Direction d2) -> d1.nextPosition(getPosition()).distance(playerPos) - d2.nextPosition(getPosition()).distance(playerPos)) ;
        for(Direction d : directions){
            if(canMove(d)){
                setDirection(d);
                doMove(d) ;
                break ;
            }
        }
    }
    public void update(long now) {
        if((now-lastMoveTime) /1000000000L >= speedMoving){
            lastMoveTime = now ;
            computeMove() ;
        }
    }
    public boolean hasToBeRemoved() {
        return this.explosed;
    }
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        for(Box box : game.getBoxes()){
            if (box.getPosition().equals(nextPos)) return false ;
        }
        for(Monster monster : game.getMonsters()){
            if (monster.getPosition().equals(nextPos)) return false ;
        }
        return super.canMove(direction) ;
    }
    public void remove(){
        this.explosed = true;
    }
    public static int getSpeed(int level){
        double value = Math.random(), factor = Math.pow( (double) (1.5), (double) level) ;
        if (value < factor * 0.125 ) return 1 ;
        if (value < factor * 0.375) return 2 ;
        if (value < factor*0.625) return 3 ;
        return 4 ;
    }
}
