/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model;

/**
 * This interface represent an object which has a state which his possibly modified just with the time
 */
public interface Updatable {
    /**
     * This function permit to update an object according to a given time
     * @param now the given time
     */
    void update(long now) ;
}
