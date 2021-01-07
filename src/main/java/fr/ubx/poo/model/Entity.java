package fr.ubx.poo.model;


/**
 * An entity is the basic brick of all the things on the game.
 */
public abstract class Entity {
    private int level ; // the level of the entity
    private boolean hasToBeRemoved = false ;
    /**
     * used to know if an object is currently living or not
     * @return if an object has to be removed or not
     */
    public boolean hasToBeRemoved(){
        return hasToBeRemoved ; //most of time the couple hasToBeRemoved - remove is implemented on this way, so we use it
    }
    /**
     * Used to signal an object that he has to be removed
     */
    public void remove(){
        hasToBeRemoved = true ;
    }
    /**
     * 
     * @param level the level in which the entity is
     */
    public void setLevel(int level){
        this.level = level ;
    }
    /**
     * 
     * @return the actual level is which is the entity is
     */
    public int getLevel(){
        return level ;
    }
    /**
     * function used to notify an entity that an explosion on it
     * @param now the moment that an explosion occur on the entity
     */
    public void explosion(long now){
        remove(); //by default, the gameObject in which an explosion occur are removed
    }
}
