
package fr.ubx.poo.model.go.character.automovablepolicies;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Monster;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * This policy lead the monster to go at the position of the player
 */
public class OnPlayerPolicy extends Automovable {
    private Game game ;
    public OnPlayerPolicy(Monster monsterToMove, Game game){
        super(monsterToMove) ;
        this.game = game ;
    }
    @Override
    public List<Direction> sortDirections(List<Direction> directions){
        Position playerPos = game.getPlayerPosition() ;
        directions.sort((Direction d1, Direction d2) -> d1.nextPosition(getMonsterToMove().getPosition()).distance(playerPos) - d2.nextPosition(getMonsterToMove().getPosition()).distance(playerPos)) ;
        return directions ;
    }
}

