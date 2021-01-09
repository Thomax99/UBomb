/*
 * Copyright (c) 2020. Thomas Morin && Enzo Sekkai
 */

package fr.ubx.poo.model.decor.bonus;
import fr.ubx.poo.model.go.character.Player;

/**
 * This bonus has an effect of infected a player, so for the moment, this bonus make the player crazy (for 3 seconds, each movement is random)
 */
public class Infected extends Bonus {
    @Override
    public String toString(){
        return "Infected" ;
    }

    /**
     * increase the life by 1 of the player
     * @param player the player who consume the bonus
     */
    @Override
    public void consumePlayer(Player player) {
        player.addInfection() ;
    }
}
