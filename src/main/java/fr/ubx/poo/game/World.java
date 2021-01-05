/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Explosion;
import fr.ubx.poo.model.decor.Scarecrow;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable ;
import java.util.function.BiConsumer;

public class World {
    private final Map<Position, Decor> grid;
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
     * Used to find the positions of an given entity
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
    public void set(Position position, Decor decor) {
        grid.put(position, decor);
    }
    public void addExplosion(Position position, long now){
        explosions.put(position, new Explosion(now)) ;
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
    public void putScarecrow(Position position){
        scarecrowPosition = position ;
        set(position, new Scarecrow()) ;
    }

    public boolean isInside(Position position) {
        return position.inside(dimension) ;
    }
    public void update(long now){
        explosions.forEach( (Position, dec) -> dec.update(now) ) ;
        Iterator<Position> it = grid.keySet().iterator() ;
        while (it.hasNext()){
            Position pos = it.next() ;
            if (grid.get(pos).hasToBeRemoved() ){
                it.remove();
            }
        }
        it = explosions.keySet().iterator() ;
        while (it.hasNext()){
            Position pos = it.next() ;
            if (explosions.get(pos).hasToBeRemoved() ) it.remove();
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
    public boolean canExplose(Position p){
        Decor decor = get(p) ;
        return isInside(p) && (decor == null || decor.canExplose()) ;
    }
    /**
     * 
     * @param p the position that we need to explose.
     * @return if something has explosed at this position
     */
    public boolean explose(Position p){
        Decor decor = get(p) ;
        if (decor != null && decor.canExplose()){
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
     * (The only constraint of the scarecrow besides bombs is that it can have two scarecrows on the game)
     * @param p the position in which we would like to put a scarecrow
     * @return if it is possible to put here a scarecrow
     */
    public boolean canScarecrow(Position p){
        return !hasScarecrow() && canBomb(p) ;
    }
    public boolean hasScarecrow(){
        return scarecrowPosition != null ;
    }
    public Position getScarecrowPosition(){
        return scarecrowPosition ;
    }
    public Scarecrow getScarecrow(){
        return (Scarecrow) get(scarecrowPosition) ;
    }
    public boolean canMoveIn(Position position){
        return isEmpty(position) || get(position).canMoveIn() ;
    }
}
