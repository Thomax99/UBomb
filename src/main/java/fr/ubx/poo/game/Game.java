/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Map;
import java.util.Hashtable;
import java.util.Iterator;

import fr.ubx.poo.model.decor.bonus.Key;
import fr.ubx.poo.model.go.*;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.decor.bonus.Bonus;

import fr.ubx.poo.model.go.character.*;

public class Game {

    private final List<World> worlds;
    private final Player player;
    private final List<List<GameObject>> monstersAndBoxes ; // the array of monsters AND Boxes
    private final List<Bomb> bombs ;
    private final List<Map<Position, Explosion>> explosion ;
    private final String worldPath;
    private int nb_level, level_max ;
    private boolean hasAChange = false ;
    private String initPrefString ;
    public int initPlayerLives;
    public int initPlayerBombs;
    public int initPlayerKey;
    public int initPlayerPortee;

    public Game(String worldPath, int nb_level){
        this.nb_level = level_max = nb_level ;
        this.worldPath = worldPath;
        loadConfig(worldPath);
        worlds = new ArrayList<>() ;
        monstersAndBoxes = new ArrayList<>() ;
        bombs = new ArrayList<>() ;
        explosion = new ArrayList<>() ;
        initializeGame() ;
        Position positionPlayer = null;
        try {
            positionPlayer = getWorld().findPlayer();
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        player = new Player(this, positionPlayer);
    }

    public void initializeGame(){
        worlds.add(loadWorld(this.nb_level)) ;
        monstersAndBoxes.add(new LinkedList<>()) ;
        explosion.add(new Hashtable<>()) ;
        for(Position p : getWorld().findMonsters()){
            getMonstersAndBoxes().add(new Monster(this, p)) ;
        }
        for(Position p : getWorld().findBoxes()){
            getMonstersAndBoxes().add(new Box(this, p)) ;
        }
    }

    public void changeWorld(int new_level){
        this.nb_level+=new_level ;
        if (this.nb_level > level_max){
            initializeGame();
            level_max++ ;
        }
        Position positionPlayer = null ;
        if (new_level == 1){
            try {
                positionPlayer = getWorld().findPreviousDoorOpened();
            } catch (PositionNotFoundException e) {
                System.err.println("Position not found : " + e.getLocalizedMessage());
                throw new RuntimeException(e);
            }
        }
        else if (new_level == -1){
            try {
                positionPlayer = getWorld().findNextDoor();
            } catch (PositionNotFoundException e) {
                System.err.println("Position not found : " + e.getLocalizedMessage());
                throw new RuntimeException(e);
            }
        }
        player.setPosition(positionPlayer);
        hasAChange = true ;
    }
    public boolean hasAChange(){
        return hasAChange ;
    }
    public void changeMade(){
        hasAChange = false ;
    }

    public Game(String worldPath) {
        this(worldPath,1) ;
    }

    public int getInitPlayerLives() {
        return initPlayerLives;
    }
    public int getInitPlayerBombs() {
        return initPlayerBombs;
    }
    public int getInitPlayerKey() {
        return initPlayerKey;
    }
    public int getInitPlayerPortee() {
        return initPlayerPortee;
    }
    public int getLevel() {
        return nb_level ;
    }

    private void loadConfig(String path) {
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            initPlayerLives = Integer.parseInt(prop.getProperty("lives", "3"));
            initPlayerBombs = Integer.parseInt(prop.getProperty("bombs", "3"));
            initPlayerKey = Integer.parseInt(prop.getProperty("key", "3"));
            initPlayerPortee = Integer.parseInt(prop.getProperty("portee", "3"));
            initPrefString = prop.getProperty("prefix", "level") ;
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }
    private World loadWorld(int n){
        int width = 0, height = 0 ;
        try (InputStream input = new FileInputStream(new File(worldPath, initPrefString+n+".txt"))){
            int c;
            while( (c = input.read()) != -1){
                if ((char) c == '\n') break ;
                width++ ;
            }
            if (width != 0) height ++ ;
            while((c = input.read()) != -1){
                if ((char) c == '\n') height++ ;
            }
        } catch (IOException ex) {
            System.err.println("Error loading game");
        }
        WorldEntity[][] mapEntities = new WorldEntity[height][width] ;
        try (InputStream input = new FileInputStream(new File(worldPath, initPrefString+n+".txt"))){
            int c, nb_read = 0;
            while((c = input.read()) != -1){
                if ((char) c != '\n'){
                    mapEntities[nb_read/width][nb_read%width] = WorldEntity.fromCode((char) c).get() ;
                    nb_read++ ;
                }


                            }
        } catch (IOException ex) {
            System.err.println("Error loading game");
        }
        return new World(mapEntities) ;
    }
    public List<Bomb> getBombs() {
        return bombs;
    }

    public World getWorld() {
        return getWorld(this.nb_level) ;
    }

    public List<GameObject> getMonstersAndBoxes(){
        return getMonstersAndBoxes(this.nb_level) ;
    }

    public World getWorld(int level) {
        return worlds.get(level-1);
    }
    public List<GameObject> getMonstersAndBoxes(int level){
        return monstersAndBoxes.get(level-1) ;
    }
    public Player getPlayer() {
        return this.player;
    }
    public Bomb addBomb(Bomb bomb){
        getBombs().add(bomb) ;
        return bomb;
    }
    public void exploser(Bomb bomb, long now){
        bomb.remove();
        player.bombHasExplosed();
        Direction directions[] = {Direction.S, Direction.N, Direction.W, Direction.E};
        List<GameObject> monstersBoxes = getMonstersAndBoxes(bomb.getLevel()) ;
        World world = getWorld(bomb.getLevel()) ;
        world.set(bomb.getPosition(), new Explosion(now));
        for(int i =  0; i < 4; i++){ // a regler
            Direction d = directions[i] ;
            Position p = bomb.getPosition();
            boolean somethingExplosed = false ;
            for (int j = 0; j < bomb.getRange() && !somethingExplosed; j++){

                p = d.nextPosition(p);
                if (!world.isInside(p)) break ;
                Decor decor = world.get(p) ;
                if (decor != null){
                    if (!world.get(p).canExplose()) break ;
                    world.clear(p);
                    decor.remove();
                }
                else {
                    if(player.getPosition().equals(p) && this.nb_level == bomb.getLevel()){
                        player.damage(now);
                        somethingExplosed = true ;
                    }
                    Iterator<GameObject> it = monstersBoxes.iterator() ;
                    while(it.hasNext()){
                        GameObject go = it.next() ;
                        if (go.getPosition().equals(p) && ! somethingExplosed){
                            go.remove();
                            somethingExplosed = true ;
                            it.remove() ;
                        }
                    }
                    for (Bomb bombAdj : getBombs()){
                        if (!bombAdj.hasToBeRemoved() && bombAdj.getPosition().equals(p) && !somethingExplosed){
                            exploser(bombAdj, now) ;
                            somethingExplosed = true ;
                        }
                    }
                }
                world.set(p, new Explosion(now));
            }
        }
    }
    public void update(long now){
        getBombs().removeIf(bomb -> bomb.hasToBeRemoved()) ;
        getMonstersAndBoxes().removeIf(go ->  go.hasToBeRemoved()) ;
        getMonstersAndBoxes().forEach(go -> go.update(now));

        World world = getWorld() ;

        world.update(now) ;

        for(Bomb bomb : getBombs()){
            if (bomb.isExplosing()){
                exploser(bomb, now) ;
            }
        }

    }
    public Map<Position, Explosion> getExplosions(){
        Map<Position, Explosion> expl = new Hashtable() ;
        World world = worlds.get(this.nb_level-1) ;
        for (int i = 0; i < world.dimension.height; i++){
            for (int j = 0; j < world.dimension.width; j++){
                Position p = new Position(j,i) ;
                Decor d = world.get(p) ;
                if (d instanceof Explosion){
                    expl.put(p,(Explosion) d) ;
                }
            }
        }
        return expl ;
    }
}