/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Explosion;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import java.util.function.BiConsumer;

public class World {
    private final Map<Position, Decor> grid;
    private final WorldEntity[][] raw;
    public final Dimension dimension;

    public World(WorldEntity[][] raw) {
        this.raw = raw;
        dimension = new Dimension(raw.length, raw[0].length);
        grid = WorldBuilder.build(raw, dimension);
    }

    public Position findPlayer() throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Player) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Player");
    }
    public Position findPreviousDoorOpened() throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.DoorPrevOpened) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Previous door");
    }
    public Position findNextDoor() throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.DoorNextClosed) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Previous door");
    }
    public List<Position> findMonsters(){
        ArrayList<Position> monstersPositions = new ArrayList<Position>() ;
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Monster) {
                    monstersPositions.add(new Position(x, y));
                }
            }
        }
        return monstersPositions ;
    }

    public List<Position> findBoxes(){
        ArrayList<Position> boxesPositions = new ArrayList<Position>() ;
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Box) {
                    boxesPositions.add(new Position(x, y));
                }
            }
        }
        return boxesPositions ;
    }


    public Decor get(Position position) {
        return grid.get(position);
    }

    public void set(Position position, Decor decor) {
        grid.put(position, decor);
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
    public void update(long now){
        forEach( (Position, dec) -> dec.update(now) ) ;
        Iterator<Position> it = grid.keySet().iterator() ;
        while (it.hasNext()){
            Position pos = it.next() ;
            if (grid.get(pos).hasToBeRemoved() ) it.remove();
        }
    }

    public boolean isEmpty(Position position) {
        return grid.get(position) == null;
    }
}
