/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Explosion;
import fr.ubx.poo.model.decor.Scarecrow;
import fr.ubx.poo.model.decor.explosives.Explosive;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable ;
import java.util.function.BiConsumer;

public class World {
    private final Map<Position, Decor> grid;
    //the explosions are managed on an other map because there is going to have a lot of changes
    private final Map<Position, Explosion> explosions ;
    private Position scarecrowPosition ; // the position of the scarecrow. if there is not a scarecrow on the world, the value is null
    private final WorldEntity[][] raw;
    public final Dimension dimension;

    public World(WorldEntity[][] raw) {
        this.raw = raw;
        dimension = new Dimension(raw.length, raw[0].length);
        grid = WorldBuilder.build(raw, dimension);
        scarecrowPosition = null ;
        explosions = new Hashtable<Position, Explosion>() ;
    }

    private Position findOneEntity(WorldEntity entity) throws PositionNotFoundException{
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == entity) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Entity");
    }

    public Position findPlayer() throws PositionNotFoundException {
        return findOneEntity(WorldEntity.Player) ;
    }
    public Position findPreviousDoorOpened() throws PositionNotFoundException {
        return findOneEntity(WorldEntity.DoorPrevOpened);
    }
    public Position findNextDoor() throws PositionNotFoundException {
        return findOneEntity(WorldEntity.DoorNextClosed) ;
    }
    /**
     * Used to find the positions of a given entity
     * @param entity the entity that we would like to have all the positions
     * @return the list of the positions of all the entities
     */
    private List<Position> findEntities(WorldEntity entity){
        ArrayList<Position> entitiesPosition = new ArrayList<Position>() ;
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == entity) {
                    entitiesPosition.add(new Position(x, y));
                }
            }
        }
        return entitiesPosition ;
    }
    public List<Position> findMonsters(){
        return findEntities(WorldEntity.Monster) ;
    }

    public List<Position> findBoxes(){
        return findEntities(WorldEntity.Box) ;
    }

    public Map<Position, Explosion> getExplosions(){
        return explosions ;
    }
    public Decor get(Position position) {
        return grid.get(position);
    }
    /**
     * this function is used to set a decor at a given position
     * This function is private for protect the world of unallowed set
     * If you would like to set something in the world, the best way is to make the correct function
     * which is going to call set (eg addScarecrow)
     * @param position the position in which we would like to set a decor
     * @param decor the given decor
     */
    private void set(Position position, Decor decor) {
        grid.put(position, decor);
    }
    /**
     * This function used to put a new Explosion at a given position
     * @param position The position given
     * @param now the actual time
     * @return the new object explosion
     */
    public Explosion addExplosion(Position position, long now){
        Explosion exp = new Explosion(now) ;
        explosions.put(position, exp) ;
        return exp ;
    }
    public void clear(Position position) {
        grid.remove(position);
    }
    public void forEach(BiConsumer<Position, Decor> fn) {
        grid.forEach(fn);
    }
    public Collection<Decor> values() {
        return grid.values();
    }
    public boolean isInside(Position position) {
        return position.inside(dimension) ;
    }
    /**
     * This function is used to update all the elements which are managed by the world
     * @param now the actual time
     */
    public void update(long now){
        explosions.forEach( (Position, exp) -> exp.update(now) ) ; // first we update the explosions (ie they are going to remove themselves if they lifetime is > 1s)
        Iterator<Position> it = grid.keySet().iterator() ;
        while (it.hasNext()){
            Position pos = it.next() ;
            if (grid.get(pos).hasToBeRemoved() ){
                it.remove(); //we remove all the no needed elements of the world
            }
        }
        it = explosions.keySet().iterator() ;
        while (it.hasNext()){
            Position pos = it.next() ;
            if (explosions.get(pos).hasToBeRemoved() ) it.remove(); // and all the no needed explosions of the world
        }
    }
    /**
     * This function is used to know that there is a princess at a given position
     * Useful for example for the monsters which can't go on a princess position
     * @param position the given position
     * @return if there is a princess or not
     */
    public boolean positionIsPrincess(Position position){
        Decor decor = get(position) ;
        return decor != null && decor.isPrincess() ;
    }
    /**
     * This function is used to know that there is a door at a given position
     * Useful for example for the monsters which can't go on a door position or for the player 
     * to know if he is in front of a door to open it
     * @param position the given position
     * @return if there is a door or not
     */
    public boolean positionIsDoor(Position position){
        Decor decor = get(position) ;
        return decor != null && decor.isDoor() ;
    }

    public boolean isEmpty(Position position) {
        return get(position) == null;
    }
    /**
     * 
     * @param p the position that we would like to explose
     * @return if this position could explose (if there is an object which can't explose or if the position is outside, return false. Otherwise, return true)
     */
    public boolean canExplode(Position p){
        Decor decor = get(p) ;
        return isInside(p) && (decor == null || decor.canExplode()) ;
    }
    /**
     * 
     * @param p the position that we need to explose.
     * @return if something has exploded at this position
     */
    public boolean explode(Position p){
        Decor decor = get(p) ;
        if (decor != null && decor.canExplode()){
                clear(p);
                decor.remove();
                if (p.equals(scarecrowPosition)) scarecrowPosition = null ;
                return true ;
            }
        return false ;
    }
    public boolean canBomb(Position p){
        return get(p) == null && isInside(p) ;
    }
    /**
     * Function used to know if it is possible to put a scarecrow in the world
     * (The only constraint of the scarecrow besides bombs is that it can have two scarecrows on the world)
     * @param p the position in which we would like to put a scarecrow
     * @return if it is possible to put here a scarecrow
     */
    public boolean canScarecrow(Position p){
        return !hasScarecrow() && canBomb(p) ;
    }
    /**
     * 
     * @return if there is already a scarecrow on the world
     */
    public boolean hasScarecrow(){
        return scarecrowPosition != null ;
    }
    /**
     * 
     * @return the actual position of the scarecrow, or null if there is not any scarecrow
     */
    public Position getScarecrowPosition(){
        return scarecrowPosition ;
    }
    /**
     * 
     * @return the scarecrow of the world, or null if there is not any scarecrow
     */
    public Scarecrow getScarecrow(){
        if (scarecrowPosition == null) return null ;
        return (Scarecrow) get(scarecrowPosition) ;
    }
    public void addScarecrow(Position position){
        scarecrowPosition = position ;
        set(position, new Scarecrow()) ;
    }
    /**
     * This function is used to know if it is possible to go on a given position
     * @param position the given position
     * @return if it is possible to go here or not
     */
    public boolean canGoIn(Position position){
        return isInside(position) && (isEmpty(position) || get(position).canMoveIn()) ;
    }
}
