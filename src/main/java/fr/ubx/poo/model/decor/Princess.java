package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Princess extends Decor{
    @Override
    public String toString() {
        return "Princess";
    }
    public void computeDecor(Player player){
        player.princessFound();
    }
}
