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

    /**
     *
     * @return if the door is closed or not
     */
    public boolean isClosed(){
        return isClosed ;
    }

    /**
     * set the status of the door to open
     */
    public void open(){
        isClosed = false ;
    }

    /**
     * If the door is not closed, move the player to an other world,
     * the previous one or the next one, depending of the door
     * @param player who will be moved to an other world
     */
    @Override
    public void computeDecor(Player player){
        if (!isClosed()) player.changeWorld( (isNext ? 1 : -1) ) ;
    }

    /**
     *
     * @return the state of the Door
     */
    @Override
    public boolean canMoveIn(){
        return !isClosed() ;
    }

    /**
     * @return false
     */
    @Override
    public boolean canExplode(){
        //Door can't explode
        return false ;
    }


    @Override
    public boolean isDoor(){
        return true ;
    }
}
