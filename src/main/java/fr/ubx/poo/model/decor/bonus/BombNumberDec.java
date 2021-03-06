/*
 * Copyright (c) 2020. Thomas Morin && Enzo Sekkai
 */

package fr.ubx.poo.model.decor.bonus;
import fr.ubx.poo.model.go.character.Player;

public class BombNumberDec extends Bonus {
    @Override
    public String toString(){
        return "BombNumberDec" ;
    }

    /**
     * Consume the bonus and remove a bomb in the inventory of the player
     * @param player the player who consume the bonus
     */
    @Override
    public void consumePlayer(Player player) {
        player.lessBomb() ;
    }
}
