/*
 * Copyright (c) 2020. Thomas Morin
 */

package fr.ubx.poo.model.decor.bonus;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.character.Player;


public abstract class Bonus extends Decor{
    protected abstract void consumePlayer(Player player) ;
    public void computeDecor(Player player){
        consumePlayer(player);
        remove();
    }
}
