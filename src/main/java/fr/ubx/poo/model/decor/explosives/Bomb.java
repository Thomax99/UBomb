package fr.ubx.poo.model.decor.explosives;

public class Bomb extends Explosive {
    private int state ; //the state of the bomb (a countdown of second)
    private long start; //the time of the start of the bomb
    public Bomb(int range, int level, long start) {
        super(range, level);
        state = 3; //by default, bombs explode after 4 seconds
        this.start = start ;
    }
    public void update(long now) {
        state = (int)((start-now)/1000000000L) + 3 ;
    }
    /**
     * This function is used to know if a Bomb is explosing itself (ie if the timer of the bomb is passed)
     * @return if the bomb is explosing itself
     */
    public boolean isExplosing(){
        return state == -1 && !hasToBeRemoved()  ; //the state has to be -1 and the bomb cannot has already explode
    }
    public int getState() {
        return state;
    }
    @Override
    public boolean isBomb(){
        return true ;
    }
    @Override
    public String toString() {
        return "Bomb";
    }
    @Override
    public boolean canMoveIn() {
        return false ;
    }
}