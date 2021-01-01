
package fr.ubx.poo.model.go.character.automovablepolicies;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;

public abstract class Automovable {
    private Monster monsterToMove ;

    Automovable(Monster monsterToMove){
        this.monsterToMove = monsterToMove ;
    }
    protected boolean canMoveIn(Direction direction){
        return monsterToMove.canMove(direction) ;
    }
    protected Monster getMonsterToMove(){
        return monsterToMove ;
    }
    public static int getSpeed(int level){
        double value = Math.random(), factor = Math.pow( (double) (1.5), (double) level) ;
        if (value < factor * 0.125 ) return 1 ;
        if (value < factor * 0.375) return 2 ;
        if (value < factor*0.625) return 3 ;
        return 4 ;
    }
    public static Automovable getRandomPolicy(Monster monsterToMove, Player player){
        double value = Math.random() ;
        if (value < 0.1) return new RandomPolicy(monsterToMove) ;
        else if (value < 0.6) return new BehindPlayerPolicy(monsterToMove, player) ;
        return new OnPlayerPolicy(monsterToMove, player) ;
    } 

    public abstract Direction computeMove() ;
}

