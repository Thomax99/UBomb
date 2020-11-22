package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;

import java.util.Timer;
import java.util.TimerTask;
import java.util.List ;
import java.util.Iterator;

public class Bomb extends GameObject {

    private int state, range, level;
    private Timer timer ;
    public Bomb(Game game, Position position, int range, int level) {
        super(game, position);
        this.range = range ;
        this.level = level ;
        state = 4;
        TimerTask task = new TimerTask(){
            public void run(){
                addState() ;
            }
        } ;
        timer = new Timer() ;
        timer.scheduleAtFixedRate(task, 1000L, 1000L);
    }
    public void addState() {
        this.state--;
        if(state == 0) explose() ;
        if (state == -1){
            timer.cancel();
            timer.purge() ;
        }
    }
    public void explose() {
        /* checker les alentours par rapport a range de la bomb
        Si range == 1 on doit faire un check dans 
        Direction.S.nextPosition(getPosition)
        */
        Direction directions[] = {Direction.S, Direction.N, Direction.W, Direction.E};
        for(int i =  0; i < 4; i++){ // a regler
            Direction d = directions[i] ;
            Position p = getPosition();
            for (int j = 0; j < range; j++){
                p = d.nextPosition(p);
                List<GameObject> monstersAndBoxes = game.getMonstersAndBoxes(level);
                Iterator<GameObject> it = monstersAndBoxes.iterator();
    
                while(it.hasNext()){
                    if(it.next().getPosition() == p){
                        it.next().explose();

                    }
                }
            }
        }
        System.out.println("explosion") ;
    }
    public boolean hasExplosed(){
        return state < 0 ;
    }

    public int getState() {
        return state;
    }

    @Override
    public String toString() {
        return "Bomb";
    }
}