/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.Direction;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.Updatable;
import fr.ubx.poo.model.Movable;

public abstract class MovableGameObject extends GameObject implements Movable {
    protected MovableGameObject(Game game, Position position){
        super(game, position) ;
    }
    public boolean canMove(Direction direction){
        Position nextPos = direction.nextPosition(getPosition());
        return game.getWorld().isInside(nextPos) && !game.positionIsBomb(nextPos, getLevel()) ;
    }
    public void doMove(Direction direction){
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

}
