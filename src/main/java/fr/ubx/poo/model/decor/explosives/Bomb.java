package fr.ubx.poo.model.decor.explosives;

public class Bomb extends Explosive {

    private int state ;
    private long start;
    public Bomb(int range, int level, long start) {
        super(range, level);
        state = 3;
        this.start = start ;
    }
    public void update(long now) {
        state = (int)((start-now)/1000000000L) + 3 ;
    }
    public boolean isExplosing(){
        return state == -1  ;
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