package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class DoorPrevOpened extends Decor {
    @Override
    public String toString() {
        return "DoorPrevOpen";
    }
    public void computeDecor(Player player){
        player.changeWorld(-1) ;
    }
}
