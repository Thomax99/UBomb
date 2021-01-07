
package fr.ubx.poo.model.go.character.automovablepolicies;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Monster;
import java.util.List;

/**
 * This policy lead the monster to go in front of the player, for blocking him with others monsters
 */
public class InFrontOfPlayerPolicy extends Automovable {
    private Game game ;
    public InFrontOfPlayerPolicy(Monster monsterToMove, Game game){
        super(monsterToMove) ;
        this.game = game ; // we register the game because it can give the player position later
    }
    /**
     * Compute a move that that allow the monster to be behind the player.
     * @param directions the List of directions that we have to sort.
     * @return A List of Direction that allow the monster to be behind the player
     */
    @Override
    public List<Direction> sortDirections(List<Direction> directions){
        Position frontPlayerPos = game.getPlayerDirection().nextPosition(game.getPlayerPosition()) ; //we compute the position in front of the player
        directions.sort((Direction d1, Direction d2) -> d1.nextPosition(getMonsterToMove().getPosition()).distance(frontPlayerPos) - d2.nextPosition(getMonsterToMove().getPosition()).distance(frontPlayerPos)) ;
        return directions ;
    }
    @Override
    public AutomovableType getType(){
        return AutomovableType.InFrontOfPlayerPolicy ;
    }
}

