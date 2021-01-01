
package fr.ubx.poo.model.go.character.automovablepolicies;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * This policy lead the monster to go at the position of the player
 */
public class OnPlayerPolicy extends Automovable {
    private Player playerToGoOn ;
    public OnPlayerPolicy(Monster monsterToMove, Player playerToGoOn){
        super(monsterToMove) ;
        this.playerToGoOn = playerToGoOn ;
    }

    public Direction computeMove(){
        Position playerPos = playerToGoOn.getPosition() ;
        List<Direction> directions = new ArrayList() ;
        Arrays.stream(Direction.values()).forEach(d -> directions.add(d));
        directions.sort((Direction d1, Direction d2) -> d1.nextPosition(getMonsterToMove().getPosition()).distance(playerPos) - d2.nextPosition(getMonsterToMove().getPosition()).distance(playerPos)) ;
        for(Direction d : directions){
            if(canMoveIn(d)){
                return d ;
            }
        }
        return null ;
    }
}

