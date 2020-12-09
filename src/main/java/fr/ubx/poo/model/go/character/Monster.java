/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.decor.* ;
import fr.ubx.poo.model.decor.bonus.* ;
import fr.ubx.poo.game.Game;

public class Monster extends Character {
    private boolean explosed;
    private long lastMoveTime = 0 ;
    private int timeAttack;
    Direction direction;
    public Monster(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        //gestion aleatoire de la vitesse en fonction du niveau
        double value = Math.random(), factor = Math.pow( (double) (1.5), (double) (game.getLevel()-1)) ;
        if (value < factor * 0.125 ) timeAttack = 1 ;
        else if (value < factor * 0.375) timeAttack = 2 ;
        else if (value < factor*0.625) timeAttack = 3 ;
        else timeAttack = 4 ;
        this.explosed = false;
    }
    public Direction getDirection() {
        return direction;
    }

    public void computeMove(){
        Direction directions[] = {Direction.S, Direction.N, Direction.W, Direction.E} ;
        Position playerPos = game.getPlayer().getPosition() ;
        int distances[] = new int[4] ;
        for(int i = 0; i < 4; i++){
            Position p = directions[i].nextPosition(getPosition()) ;
            distances[i] =(int) Math.sqrt( (p.x - playerPos.x)*(p.x - playerPos.x) + (p.y - playerPos.y)*(p.y - playerPos.y) ) ;
        }
        for(int i = 0; i < 4; i++){
            for(int j = i; j < 4; j++){
                if(distances[j] < distances[i]){
                    int tmp = distances[j] ;
                    distances[j] = distances[i] ;
                    distances[i] = tmp ;
                    Direction dtmp = directions[j] ;
                    directions[j] = directions[i] ;
                    directions[i] = dtmp ;
                }
            }
        }
        for(int i = 0; i < 4; i++){
            if(canMove(directions[i])){
                doMove(directions[i]) ;
                break ;
            }
        }
    }

    public void update(long now) {
        if((now-lastMoveTime) /1000000000L >= timeAttack){
            lastMoveTime = now ;
            computeMove() ;
        }
    }
    public boolean hasToBeRemoved() {
        return this.explosed;
    }

    public void remove(){
        this.explosed = true;
    }
}
