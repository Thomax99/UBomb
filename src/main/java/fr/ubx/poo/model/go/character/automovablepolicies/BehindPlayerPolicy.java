
package fr.ubx.poo.model.go.character.automovablepolicies;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * This policy lead the monster to go behind the the player, for blocking him with others monsters
 */
public class BehindPlayerPolicy extends Automovable {
    private Game game ;
    public BehindPlayerPolicy(Monster monsterToMove, Game game){
        super(monsterToMove) ;
        this.game = game ; // we register the game because it can give the player position later
    }
    @Override
    public List<Direction> sortDirections(List<Direction> directions){
        Position behindPlayerPos = game.getPlayerDirection().oppositeDirection().nextPosition(game.getPlayerPosition()) ;
        directions.sort((Direction d1, Direction d2) -> d1.nextPosition(getMonsterToMove().getPosition()).distance(behindPlayerPos) - d2.nextPosition(getMonsterToMove().getPosition()).distance(behindPlayerPos)) ;
        return directions ;
    }
}

