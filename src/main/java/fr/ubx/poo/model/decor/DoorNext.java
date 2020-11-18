package fr.ubx.poo.model.decor;

public class DoorNext extends Decor{
    private boolean isClosed = true ;
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
}
