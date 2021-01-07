/*
 * Copyright (c) 2020. Thomas Morin && Enzo Sekkai
 */

package fr.ubx.poo.model.decor.bonus;
import fr.ubx.poo.model.go.character.Player;


public class Heart extends Bonus {
    @Override
    public String toString(){
        return "Heart" ;
    }
    @Override
    public void consumePlayer(Player player) {
        player.addLive() ;
    }
}
