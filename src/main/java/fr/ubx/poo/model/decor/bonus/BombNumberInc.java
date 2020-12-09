/*
 * Copyright (c) 2020. Thomas Morin && Enzo Sekkai
 */

package fr.ubx.poo.model.decor.bonus;
import fr.ubx.poo.model.go.character.Player;

public class BombNumberInc extends Bonus {
    public String toString(){
        return "BombNumberInc" ;
    }
    @Override
    public void computeDecor(Player player){
        player.addBomb();
        remove();
    }
}
