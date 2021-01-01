
package fr.ubx.poo.model.go.character.automovablepolicies;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * This policy lead the monster to move randomly
 */
public class RandomPolicy extends Automovable {
    public RandomPolicy(Monster monsterToMove){
        super(monsterToMove) ;
    }
    public Direction computeMove(){
        List<Direction> directions = new ArrayList() ;
        Arrays.stream(Direction.values()).forEach(d -> directions.add(d));
        java.util.Collections.shuffle(directions);
        for(Direction d : directions){
            if(canMoveIn(d)){
                return d ;
            }
        }
        return null ;
    }
}

