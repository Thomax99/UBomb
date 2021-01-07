/*
 * Copyright (c) 2020. Thomas Morin && Enzo Sekkai
 */

package fr.ubx.poo.model.decor.bonus;
import fr.ubx.poo.model.go.character.Player;


public class BombRangeDec extends Bonus {
    @Override
    public String toString(){
        return "BombRangeDec" ;
    }

    /**
     * Consume the bonus and decrease the bomb range of the player
     * @param player the player who consume the bonus
     */
    @Override
    public void consumePlayer(Player player) {
        player.lessPortee() ;
    }
}
