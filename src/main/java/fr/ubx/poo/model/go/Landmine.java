package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;

import java.util.Timer;
import java.util.TimerTask;
import java.util.List ;
import java.util.Iterator;


/**
 * The Landmine is a type of bomb which explode only when there is something pushed on
 */
public class Landmine extends Explosive {
    public Landmine(Game game, Position position, int range) {
        super(game, position, range);
    }

    @Override
    public String toString() {
        return "Landmine";
    }
}