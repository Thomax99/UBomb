
package fr.ubx.poo.model.go.character.automovablepolicies;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;
import java.util.Collections;
import java.util.List;

/**
 * This policy lead the monster to move randomly
 */
public class RandomPolicy extends Automovable {
    public RandomPolicy(Monster monsterToMove){
        super(monsterToMove) ;
    }
    @Override
    public List<Direction> sortDirections(List<Direction> directions){
        Collections.shuffle(directions);
        return directions ;
    }
}

