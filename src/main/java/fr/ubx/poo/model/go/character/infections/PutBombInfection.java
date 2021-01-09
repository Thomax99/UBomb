package fr.ubx.poo.model.go.character.infections;

import fr.ubx.poo.model.go.character.Player;

/**
 * This class is used to represent an infection which is going to put bomb each time that the player move
 */
public class PutBombInfection extends Infection{
    PutBombInfection(Player playerInfected, long now){
        super(playerInfected, now) ;
    }
    public void makeAction(){
        getPlayerInfected().requestBomb() ;
    }
}

