package fr.ubx.poo.model.decor;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.game.Direction;
public class Door extends Decor{
    private boolean isClosed ;
    private final boolean isNext ;
    public Door(boolean isClosed, boolean isNext){
        this.isClosed = isClosed ;
        this.isNext = isNext ;
    }
    @Override
    public String toString() {
        return "Door";
    }
    public boolean isClosed(){
        return isClosed ;
    }
    public void open(){
        isClosed = false ;
    }
    @Override
    public void computeDecor(Player player){
        if (!isClosed()) player.changeWorld( (isNext ? 1 : -1) ) ;
    }
    @Override
    public boolean canMoveIn(Direction dir){ //need to be modified in function of the inherits
        return !isClosed() ;
    }
    public boolean canExplose(){
        return false ;
    }
}
