/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model;

import fr.ubx.poo.game.Direction;

public interface Movable {
    /**
     * Used to know if an object could go on a given position or not.
     * Each movable object has to overload this method
     * @param direction the direction in which the entity is going
     * @return if the entity could move in this direction
     */
    boolean canMove(Direction direction);
    /**
     * Make a move without verifying if the move is authorized
     * This function manage all the needed interactions of others elements of the game
     * @param direction the direction in which the entity is going
     */
    void doMove(Direction direction);
}
