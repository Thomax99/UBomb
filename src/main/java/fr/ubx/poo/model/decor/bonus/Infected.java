/*
 * Copyright (c) 2020. Thomas Morin && Enzo Sekkai
 */

package fr.ubx.poo.model.decor.bonus;
import fr.ubx.poo.model.go.character.Player;

/**
 * This bonus has an effect to infect a player
 */
public class Infected extends Bonus {
    @Override
    public String toString(){
        return "Infected" ;
    }
    @Override
    public void consumePlayer(Player player) {
        player.addInfection() ;
    }
}
