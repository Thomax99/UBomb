/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.decor.* ;
import fr.ubx.poo.model.decor.bonus.* ;
import fr.ubx.poo.game.Game;

public class Monster extends GameObject implements Movable {

    Direction direction;
    public Monster(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
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

}
