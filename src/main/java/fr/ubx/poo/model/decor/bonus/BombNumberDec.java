/*
 * Copyright (c) 2020. Thomas Morin && Enzo Sekkai
 */

package fr.ubx.poo.model.decor.bonus;
import fr.ubx.poo.model.go.character.Player;

public class BombNumberDec extends Bonus {
    public String toString(){
        return "BombNumberDec" ;
    }
    @Override
    public void consumePlayer(Player player) {
        player.lessBomb() ;
    }
}
