/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.Removable;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.decor.* ;
import fr.ubx.poo.model.decor.bonus.* ;
import fr.ubx.poo.game.Game;

public class Monster extends GameObject implements Movable, Removable {
    private boolean explosed;
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
        return true ;
    }

    public void doMove(Direction direction) {
    }

    public boolean hasToBeRemoved() {
        return this.explosed;
    }

    public void remove(){
        this.explosed = true;
    }

}
