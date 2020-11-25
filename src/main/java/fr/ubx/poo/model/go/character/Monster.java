/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.Removable;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.Bomb;

import fr.ubx.poo.model.decor.* ;
import fr.ubx.poo.model.decor.bonus.* ;
import fr.ubx.poo.game.Game;

public class Monster extends GameObject implements Movable, Removable {
    private boolean explosed;
    private long lastMoveTime = 0 ;
    Direction direction;
    public Monster(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.explosed = false;
    }
    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());

        if(!game.getWorld().isInside(nextPos) || game.getWorld().get(nextPos) instanceof Decor) return false ;
        for (Bomb bo : game.getBombs()){
            if (bo.getPosition().equals(nextPos)) return false ;

        }
        for(GameObject go : game.getMonstersAndBoxes()){
            if (go.getPosition().equals(nextPos)) return false ;
        }
        return true ;
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

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
        if (game.getPlayer().getPosition().equals(nextPos)) game.getPlayer().damage(lastMoveTime);

    }
    public void update(long now) {
        if((now-lastMoveTime) /1000000000L >= 1){
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
