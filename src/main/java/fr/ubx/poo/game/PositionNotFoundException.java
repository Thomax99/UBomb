package fr.ubx.poo.game;

/**
 * Exception thrown when a position is untraceable
 */
public class PositionNotFoundException extends Exception {
    public PositionNotFoundException(String message) {
        super(message);
    }
}
