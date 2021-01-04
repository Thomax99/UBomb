/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model;

import fr.ubx.poo.game.Direction;

public interface Movable {
    /**
     * Used to know if an object could or go on a position
     * @param direction the direction in which the entity is going
     * @return if the entity could move in this direction
     */
    boolean canMove(Direction direction);
    /**
     * Make a move without verifying if the move is authorized
     * @param direction the direction in which the entity is going
     */
    void doMove(Direction direction);
}
