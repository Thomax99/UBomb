package fr.ubx.poo.model.go.character.infections;

import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.game.Direction;

/**
 * This class is used to represent an infection which is going to move randomly the player
 */
public class RandomMoveInfection extends Infection{
    RandomMoveInfection(Player playerInfected, long now){
        super(playerInfected, now) ;
    }
    public void makeAction(){
        Direction dir = Direction.random() ;
        getPlayerInfected().setDirection(dir) ;
    }
}

