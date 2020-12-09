/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.Box;

import fr.ubx.poo.model.decor.* ;
import fr.ubx.poo.model.decor.bonus.* ;
import fr.ubx.poo.game.Game;

public abstract class Character extends GameObject implements Movable {
    public Character(Game game, Position position){
        super(game, position) ;
    }
    public boolean canGoOnMonsterOrBoxes(){
        return false ;
    }
    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        for (Bomb bo : game.getBombs()){
            if (bo.getPosition().equals(nextPos)) return false ;
        }
        for(GameObject go : game.getMonstersAndBoxes()){
            if (go.getPosition().equals(nextPos) && (!canGoOnMonsterOrBoxes() || !go.canMoveIn(direction) )) return false ;
        }
        return game.getWorld().isInside(nextPos) && (game.getWorld().get(nextPos) == null || game.getWorld().get(nextPos).canMoveIn(direction));
    }
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }
}
