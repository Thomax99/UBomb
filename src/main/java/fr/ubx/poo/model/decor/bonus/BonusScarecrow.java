/*
 * Copyright (c) 2020. Thomas Morin && Enzo Sekkai
 */

package fr.ubx.poo.model.decor.bonus;
import fr.ubx.poo.model.go.character.Player;

/**
 * The scarecrow bonus allows the player to put a scarecrow on a level.
 * The scarecrow is an entity which is going to attract the monsters if their moving policies are depending on a predifined position
 * Just one scarecrow could be put on each world
 */
public class BonusScarecrow extends Bonus {
    public String toString(){
        return "Scracrow Bonus" ;
    }

    @Override
    public void consumePlayer(Player player) {
        player.hasScarecrow() ;
    }
}
