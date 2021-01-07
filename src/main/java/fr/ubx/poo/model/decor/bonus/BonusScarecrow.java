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
    @Override
    public String toString(){
        return "Scrarecrow Bonus" ;
    }

    /**
     * Consume the bonus and place a scarecrow
     * @param player the player who consume the bonus
     */
    @Override
    public void consumePlayer(Player player) {
        player.hasScarecrow() ;
    }
}
