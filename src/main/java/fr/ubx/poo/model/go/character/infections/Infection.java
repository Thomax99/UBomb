package fr.ubx.poo.model.go.character.infections;

import fr.ubx.poo.Constants;
import fr.ubx.poo.model.go.character.Player;

/**
 * This class is used to represent an infection for the player if he takes an infection bonus
 * An infection is active during 3 seconds
 */
public abstract class Infection {
    private Player playerInfected ;
    private long start ;
    Infection(Player playerInfected, long start){
        this.playerInfected = playerInfected ;
        this.start = start ;
    }
    protected Player getPlayerInfected(){
        return playerInfected ;
    }
    /**
     * This function is used to get a random infection for the player
     * @param player the player which is infected
     * @param now the time in which the player is infected
     * @return a random infection among the existing implemented infections
     */
    public static Infection getRandomInfection(Player player, long now){
        double random = Math.random() ;
        if (random < 0.5) return new PutBombInfection(player, now) ;
        return new RandomMoveInfection(player, now);
    }
    /**
     * This function is called by the player when a move is requested to make something according to the override of this method
     */
    public abstract void makeAction() ;
    /**
     * This function is used to know if an infection is terminated or not accotding to a time
     * @param now the current time
     * @return if the infection is terminated
     */
    public boolean isTerminated(long now){
        return (now - start)/Constants.secondInnanoSec > Constants.timeInfection ; //an infection last 3 seconds
    }
}

