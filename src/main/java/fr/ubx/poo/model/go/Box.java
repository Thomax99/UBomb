package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Stone;
import fr.ubx.poo.model.decor.Tree;
import fr.ubx.poo.model.decor.bonus.Bonus;

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
        if (game.getWorld().get(nextPos) instanceof Decor || game.getWorld().get(nextPos) instanceof Bonus) return false ;
        else if(game.getWorld().findBoxes().contains(nextPos)){
            for(int i = 0; i<game.getBoxes().size(); i++){
                Box box = game.getBoxes().get(i);
                if(box.getPosition().x == nextPos.x && box.getPosition().y == nextPos.y){
                    return false;
                }
            }
        }
        else if(game.getWorld().isInside(nextPos)){
            return true;
        }
        return false;

    }

    @Override
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

}
