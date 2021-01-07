package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Stone;
import fr.ubx.poo.model.decor.Tree;
import fr.ubx.poo.model.decor.bonus.Bonus;
import fr.ubx.poo.model.go.character.*;

/**
 * This class represents the Box object. A box is a gameObject which can move.
 */
public class Box extends MovableGameObject {
    public Box(Game game, Position position) {
        super(game, position);
        setLevel(game.getLevel()) ;
    }

    @Override
    public String toString() {
        return "Box";
    }

    /**
     * check if the bax can be moved in a certain direction
     * @param direction the direction in which the entity is going
     * @return if the box can be moved
     */
    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        return game.positionAllowedToBoxes(nextPos) ;
    }
}
