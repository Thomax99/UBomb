/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import java.util.ArrayList;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.automovablepolicies.Automovable;
import fr.ubx.poo.model.go.Box;
import fr.ubx.poo.model.go.character.automovablepolicies.*;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.decor.* ;
import fr.ubx.poo.model.decor.bonus.* ;
import fr.ubx.poo.game.Game;

public class Monster extends Character {
    private boolean explosed;
    private long lastMoveTime = 0 ;
    private int speedMoving;
    private Automovable automovingPolicy ;
    Direction direction;
    public Monster(Game game, Position position) {
        super(game, position);

        speedMoving = Automovable.getSpeed(game.getLevel()-1) ;
        this.automovingPolicy = Automovable.getRandomPolicy(this, game.getPlayer()) ;
        this.explosed = false;
    }
    public void update(long now) {
        if((now-lastMoveTime) /1000000000L >= speedMoving){
            lastMoveTime = now ;
            //time to move
            Direction d = automovingPolicy.computeMove() ;
            if (d == null){
                throw new RuntimeException("impossible to move: ");
            }
            setDirection(d);
            doMove(d);
        }
    }
    public boolean hasToBeRemoved() {
        return this.explosed;
    }
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        return game.getBoxes().stream()
            .map(box -> !box.getPosition().equals(nextPos))
                .reduce(super.canMove(direction), (b1, b2) -> b1 && b2) ;
    }
    public void remove(){
        this.explosed = true;
    }
    public boolean explosion(Position p, long now){
        if(getPosition().equals(p)){
            remove() ;
            return true ;
        }
        return false ;
    }

}
