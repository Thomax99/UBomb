/*
 * Copyright (c) 2020. Thomas Morin && Enzo Sekkai
 */

package fr.ubx.poo.model.decor.bonus;
import fr.ubx.poo.model.go.character.Player;

public class Landminer extends Bonus {
    @Override
    public String toString(){
        return "Landminer" ;
    }

    /**
     * Consume the bonus to create a landmine in front of the player
     * @param player the player who consume the bonus
     */
    @Override
    public void consumePlayer(Player player) {
        player.hasLandmine() ;
    }
}
