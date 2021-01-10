
package fr.ubx.poo.model.go.character.automovablepolicies;

import fr.ubx.poo.Constants;
import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Monster;
import java.util.List;

/**
 * This policy is trying to make a type of Monte Carlo tree search, not exact but the idea is that :
 * for each Direction, you make a given number of simulation (ie 1 random move of the player, one random move of the monster).
 * You repeat it a given number of repetition and finally, you make an average of the final distance. You sort the distance according to the values
 * of the averages
 */
public class MCPolicy extends Automovable {
    private Game game ;
    public MCPolicy(Monster monsterToMove, Game game){
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
        directions.sort((Direction d1, Direction d2) -> simulationMovement(d1.nextPosition(getMonsterToMove().getPosition()), game.getPlayerPosition()) -
                                                        simulationMovement(d2.nextPosition(getMonsterToMove().getPosition()), game.getPlayerPosition())) ;
        return directions ;
    }
    @Override
    public AutomovableType getType(){
        return AutomovableType.MCPolicy ;
    }
    /**
     * This function make all the simulation, ie Constants.nbRepetitions times an alternation of random move on p1 and p2 Constants.nbTrials times
     * @param p1 the first position
     * @param p2 the second position
     * @return the average of the finals distances after simulation
     */
    private int simulationMovement(Position p1, Position p2){
        int finalDistances = 0 ;
        for(int i = 0; i < Constants.nbRepetitions; i++){
            //we make the simulation nbRepetitions times
            Position firstPos = new Position(p1), secondPos = new Position(p2) ; // we make deep copy of the position to not alterate the others simulations
            for(int j = 0; j < Constants.nbTrials; j++){
                Direction dir ;
                do{
                    dir = Direction.random() ;
                } while( !game.getWorld().isInside(dir.nextPosition(firstPos))) ; // we want a position on the world
                firstPos = dir.nextPosition(firstPos) ;
                do{
                    dir = Direction.random() ;
                } while( !game.getWorld().isInside(dir.nextPosition(secondPos))) ; // we want a position on the world
                secondPos = dir.nextPosition(secondPos) ;
            }
            finalDistances+= firstPos.distance(secondPos) ;
        }
        return finalDistances/Constants.nbRepetitions ;
    }
}

