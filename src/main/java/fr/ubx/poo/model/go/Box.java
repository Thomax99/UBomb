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

public class Box extends GameObject implements Movable {
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

        if (game.getWorld().get(nextPos) != null) return false ;
        for(GameObject go : game.getMonstersAndBoxes()){
            if(go.getPosition().equals(nextPos)) return false ;
        }
        return game.getWorld().isInside(nextPos) ;
    }

    public boolean hasToBeRemoved(){
        return explosed;
    }

    public void remove(){
        this.explosed = true;
    }

    @Override
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }
    @Override
    public boolean canMoveIn(Direction dir){ //need to be modified in function of the inherits
        return canMove(dir);
    }
    public void update(long now){
        //Box is never changing for the moment
    }

}
