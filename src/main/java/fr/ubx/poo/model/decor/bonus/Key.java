package fr.ubx.poo.model.decor.bonus;
import fr.ubx.poo.model.go.character.Player;
public class Key extends Bonus{
    @Override
    public String toString() {
        return "Key";
    }
    @Override
    public void computeDecor(Player player){
        player.addKey() ;
        remove();
    }
    public boolean canExplose(){
        return false ;
    }
}
