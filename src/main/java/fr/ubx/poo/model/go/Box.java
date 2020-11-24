package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.Removable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Stone;
import fr.ubx.poo.model.decor.Tree;
import fr.ubx.poo.model.decor.bonus.Bonus;
import fr.ubx.poo.model.go.character.*;

public class Box extends GameObject implements Movable, Removable {
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
        if (game.getWorld().get(nextPos) instanceof Decor || game.getWorld().get(nextPos) instanceof Bonus) return false ;
        for(GameObject go : game.getMonstersAndBoxes()){
            if (go instanceof Box){
                Box box = (Box) go ;
                if (box.getPosition().equals(nextPos)){
                        return false;
                }
            }
            else if (go instanceof Monster){
                Monster monster = (Monster) go ;
                if (monster.getPosition().equals(nextPos)){
                    return false;
                }
            }
        }
        if(game.getWorld().isInside(nextPos)){
            return true;
        }
        return false;
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

}
