/*
 * Copyright (c) 2020. Thomas Morin
 */

package fr.ubx.poo.model.decor.bonus;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.character.Player;


public abstract class Bonus extends Decor{
    /**
     * Used by the computeDecor function to make an interaction with the player when a player is going on it
     * (for instance, increase the number of bombs)
     * @param player
     */
    protected abstract void consumePlayer(Player player) ;
    @Override
    public void computeDecor(Player player){
        consumePlayer(player);
        remove();
    }
}
