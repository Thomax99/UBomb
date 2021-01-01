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

public class Box extends MovableGameObject implements Movable {
    private boolean explosed;
    public Box(Game game, Position position) {
        super(game, position);
        this.explosed = false;
    }

    @Override
    public String toString() {
        return "Box";
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        return game.getMonsters().stream().map(monster -> ! monster.getPosition().equals(nextPos))
                .reduce(
                    game.getBoxes().stream().map(box -> !box.getPosition().equals(nextPos))
                        .reduce(super.canMove(direction) && game.getWorld().get(nextPos) == null, (b1, b2) -> b1 && b2),
                    (b1, b2) -> b1 && b2
                );
    }

    public boolean hasToBeRemoved(){
        return explosed;
    }
    public void remove(){
        this.explosed = true;
    }
    public boolean canMoveIn(Direction dir){
        return canMove(dir);
    }
    @Override
    public boolean explosion(Position p, long now){
        if(getPosition().equals(p)){
            remove();
            return true ;
        }
        return false ;
    }
}
