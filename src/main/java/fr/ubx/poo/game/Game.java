/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.ToIntFunction;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
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
    private final List<List<Monster>> monsters ;
    private final List<List<Box>> boxes ;
    private final List<Bomb> bombs ;
    private final List<Map<Position, Explosion>> explosion ;
    private final String worldPath;
    private int nb_level, level_max ;
    private boolean hasChangedWorld = false, hasBombChange = false, hasNewExplosions = false ;
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
        monsters = new ArrayList<>() ;
        boxes = new ArrayList<>() ;
        bombs = new LinkedList<>() ;
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
        monsters.add(new LinkedList<>()) ;
        boxes.add(new LinkedList<>()) ;
        explosion.add(new Hashtable<>()) ;
        for(Position p : getWorld().findMonsters()){
           getMonsters().add(new Monster(this, p)) ;
        }
        for(Position p : getWorld().findBoxes()){
            getBoxes().add(new Box(this, p)) ;
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
        hasChangedWorld = true ;
    }
    public boolean hasChangedWorld(){
        return hasChangedWorld ;
    }
    public void changeMade(){
        hasChangedWorld = false ;
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

    public List<Bomb> getBombs(){
        return bombs;
    }

    public World getWorld() {
        return getWorld(this.nb_level) ;
    }
    public World getWorld(int level) {
        return worlds.get(level-1);
    }

    public List<Monster> getMonsters(){
        return getMonsters(this.nb_level) ;
    }
    public List<Monster> getMonsters(int level){
        return monsters.get(level-1) ;
    }

    public List<Box> getBoxes(){
        return getBoxes(this.nb_level) ;
    }
    public List<Box> getBoxes(int level){
        return boxes.get(level-1) ;
    }

    public Player getPlayer() {
        return this.player;
    }
    public void addBomb(Bomb bomb){
        getBombs().add(bomb) ;
        hasBombChange = true ;
    }
    public void bombChange(){
        hasBombChange = false ;
    }
    public boolean hasBombChange(){
        return hasBombChange ;
    }
    public boolean canBomb(Position p){
        return getWorld().canBomb(p) ;
    }
    public void exploser(Bomb bomb, long now){
        getPlayer().bombHasExplosed();
        hasNewExplosions = true ;
        hasBombChange = true ;
        Direction directions[] = {Direction.S, Direction.N, Direction.W, Direction.E};
        List<Monster> monsters = getMonsters(bomb.getLevel()) ;
        List<Box> boxes = getBoxes(bomb.getLevel()) ;
        World world = getWorld(bomb.getLevel()) ;
        world.addExplosion(bomb.getPosition(), now);

        for(Direction d : directions){ // a regler
            Position pos = bomb.getPosition();
            boolean somethingExplosed = false ;
            for (int j = 0; j < bomb.getRange() && !somethingExplosed; j++){
                //decor explosion part
                final Position p = d.nextPosition(pos) ; //this variable is declared as final because we need a final variable to use Streams interfaces
                pos = p ;
                if(!world.canExplose(p)) break ;
                somethingExplosed = world.explose(p) ;

                //Game object explosion part

                somethingExplosed = somethingExplosed || player.explosion(p, now) ;
                somethingExplosed = monsters.stream().map(monster -> monster.explosion(p, now)).reduce(somethingExplosed, (b1, b2) -> b1 || b2 ) ;
                somethingExplosed = boxes.stream().map(box -> box.explosion(p, now)).reduce(somethingExplosed, (b1, b2) -> b1 || b2 ) ;
                somethingExplosed = monsters.stream().map(monster -> monster.explosion(p, now)).reduce(somethingExplosed, (b1, b2) -> b1 || b2 ) ;
                somethingExplosed = getBombs().stream().map(bombAdj -> bombAdj.explosion(p, now)).reduce(somethingExplosed, (b1, b2) -> b1 || b2 ) ;
                world.addExplosion(p, now);
            }
        }
    }
    public boolean hasNewExplosions(){
        return hasNewExplosions ;
    }
    public void newExplosionsPut(){
        hasNewExplosions = true ;
    }
    public void update(long now){
        getBombs().removeIf(bomb -> bomb.hasToBeRemoved()) ;
        getMonsters().removeIf(go ->  go.hasToBeRemoved()) ;
        getBoxes().removeIf(go ->  go.hasToBeRemoved()) ;

        getMonsters().forEach(go -> go.update(now));
        getBombs().forEach(bomb -> bomb.update(now));
        getWorld().update(now) ;

        for(Bomb bomb : getBombs()){
            if (bomb.isExplosing()){
                bomb.explosion(bomb.getPosition(), now) ;
            }
        }
    }
    public Map<Position, Explosion> getExplosions(){
        return getWorld().getExplosions() ;
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
                    Optional<WorldEntity> entity = WorldEntity.fromCode((char) c) ;
                    if (entity.isPresent())
                        mapEntities[nb_read/width][nb_read%width] = entity.get() ;
                    else
                        mapEntities[nb_read/width][nb_read%width] = WorldEntity.Empty ;
                    nb_read++ ;
                }
            }
        } catch (IOException ex) {
            System.err.println("Error loading game");
        }
        return new World(mapEntities) ;
    }
}