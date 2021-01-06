package fr.ubx.poo.model.go.character.automovablepolicies;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.game.Game;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
public abstract class Automovable {
    private Monster monsterToMove ;

    Automovable(Monster monsterToMove){
        this.monsterToMove = monsterToMove ;
    }
    protected boolean canMove(Direction direction){
        return monsterToMove.canMove(direction) ;
    }
    protected Monster getMonsterToMove(){
        return monsterToMove ;
    }
    /**
     * @param level the level in which the player is
     * @return a random speed (in seconds) of monster
     */
    public static int getSpeed(int level){
        double value = Math.random(), factor = Math.pow( (double) (1.5), (double) level) ;
        if (value < factor * 0.125 ) return 1 ;
        if (value < factor * 0.375) return 2 ;
        if (value < factor*0.625) return 3 ;
        return 4 ;
    }
    /**
     * This function is used to get a random moving policy for the monsters
     * @param monsterToMove the monster that we want to move
     * @param game the game in which the monster is playing
     * @return a random policy among the existing implemented policies
     */
    public static Automovable getRandomPolicy(Monster monsterToMove, Game game){
        double value = Math.random() ;
        if (value < 0.1) return new RandomPolicy(monsterToMove) ;
        else if (value < 0.6) return new BehindPlayerPolicy(monsterToMove, game) ;
        return new OnPlayerPolicy(monsterToMove, game) ;
    }
    /**
     * This function is used on the function computeMove to know in which order a List of Direction will be considered
     * to make a move (the first position possible in this sort will be do)
     * @param directions the List of directions that we have to sort.
     */
    public abstract List<Direction> sortDirections(List<Direction> directions) ;

    /**
     * Compute a move according to the policies for a monster. The only thing needed to make a new policy is to
     * overload the function sortDirections.
     * @return the direction in which a monster has to go
     */
    public Direction computeMove(){
        List<Direction> directions = new ArrayList<>(Arrays.asList(Direction.values())) ;
        directions = sortDirections(directions) ;
        for(Direction d : directions){
            if(monsterToMove.canMove(d)){
                return d ;
            }
        }
        return null ;
    }
}

