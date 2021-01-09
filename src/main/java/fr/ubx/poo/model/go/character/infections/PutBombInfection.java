package fr.ubx.poo.model.go.character.infections;

import fr.ubx.poo.model.go.character.Player;

/**
 * This class is used to represent an infection which is going to put bomb each time that the player move
 * @author Thomas Morin et Enzo Sekkai
 */
public class PutBombInfection extends Infection{
    PutBombInfection(Player playerInfected, long now){
        super(playerInfected, now) ;
    }
    @Override
    public void makeAction(){
        getPlayerInfected().requestBomb() ;
    }
}

