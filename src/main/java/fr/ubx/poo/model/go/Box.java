package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Stone;
import fr.ubx.poo.model.decor.Tree;

public class Box extends GameObject implements Movable {
    public Box(Game game, Position position) {
        super(game, position);
    }

    @Override
    public String toString() {
        return "Box";
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        if (game.getWorld().get(nextPos) instanceof Tree || game.getWorld().get(nextPos) instanceof Stone) return false ;
        return game.getWorld().isInside(nextPos);
    }

    @Override
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

}
