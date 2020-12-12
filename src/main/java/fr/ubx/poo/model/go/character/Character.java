/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.Updatable;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.MovableGameObject;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.Box;

import fr.ubx.poo.model.decor.* ;
import fr.ubx.poo.model.decor.bonus.* ;
import fr.ubx.poo.game.Game;

public abstract class Character extends MovableGameObject implements Updatable {
    public Character(Game game, Position position){
        super(game, position) ;
    }
    public boolean canGoOnMonsterOrBoxes(){
        return false ;
    }
    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        for(Box box : game.getBoxes()){
            if (box.getPosition().equals(nextPos) && (!canGoOnMonsterOrBoxes() || !box.canMoveIn(direction) )) return false ;
        }
        for(Monster monster : game.getMonsters()){
            if (monster.getPosition().equals(nextPos) && (!canGoOnMonsterOrBoxes() || !monster.canMoveIn(direction) )) return false ;
        }
        return super.canMove(direction) && (game.getWorld().get(nextPos) == null || game.getWorld().get(nextPos).canMoveIn(direction));
    }
}
