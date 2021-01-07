/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import java.util.Random;

public enum Direction {
    N {
        @Override
        public Position nextPosition(Position pos, int delta) {
            return new Position(pos.x, pos.y - delta);
        }
        @Override
        public Direction oppositeDirection(){
            return Direction.S ;
        };

    },
    E {
        @Override
        public Position nextPosition(Position pos, int delta) {
            return new Position(pos.x + delta, pos.y);
        }
        @Override
        public Direction oppositeDirection(){
            return Direction.W ;
        };
    },
    S {
        @Override
        public Position nextPosition(Position pos, int delta) {
            return new Position(pos.x, pos.y + delta);
        }
        @Override
        public Direction oppositeDirection(){
            return Direction.N ;
        };
    },
    W {
        @Override
        public Position nextPosition(Position pos, int delta) {
            return new Position(pos.x - delta, pos.y);
        }
        @Override
        public Direction oppositeDirection(){
            return Direction.E ;
        };
    },
    ;

    private static final Random randomGenerator = new Random();

    /***
     * @return a pseudorandom direction
     */
    public static Direction random() {
        return values()[randomGenerator.nextInt(values().length)];
    }
    /**
     * @return the opposite direction of this direction
     */
    public abstract Direction oppositeDirection() ;


    public abstract Position nextPosition(Position pos, int delta);

    final public Position nextPosition(Position pos) {
        return nextPosition(pos, 1);
    }

}