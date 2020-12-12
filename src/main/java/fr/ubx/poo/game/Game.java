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
    public List<Bomb> getBombs() {
        return bombs;
    }

    public World getWorld() {
        return getWorld(this.nb_level) ;
    }
    public List<Box> getBoxes(){
        return getBoxes(this.nb_level) ;
    }
    public List<Monster> getMonsters(){
        return getMonsters(this.nb_level) ;
    }
    public List<Box> getBoxes(int level){
        return boxes.get(level-1) ;
    }
    public List<Monster> getMonsters(int level){
        return monsters.get(level-1) ;
    }

    public World getWorld(int level) {
        return worlds.get(level-1);
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
        List<Monster> monsters = getMonsters(bomb.getLevel()) ;
        List<Box> boxes = getBoxes(bomb.getLevel()) ;
        World world = getWorld(bomb.getLevel()) ;
        world.addExplosion(bomb.getPosition(), now);
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
                    Iterator<Monster> itm = monsters.iterator() ;
                    while(itm.hasNext()){
                        Monster monster = itm.next() ;
                        if (monster.getPosition().equals(p) && ! somethingExplosed){
                            monster.remove();
                            somethingExplosed = true ;
                            itm.remove() ;
                        }
                    }
                    Iterator<Box> itb = boxes.iterator() ;
                    while(itb.hasNext()){
                        Box box = itb.next() ;
                        if (box.getPosition().equals(p) && ! somethingExplosed){
                            box.remove();
                            somethingExplosed = true ;
                            itb.remove() ;
                        }
                    }
                    for (Bomb bombAdj : getBombs()){
                        if (!bombAdj.hasToBeRemoved() && bombAdj.getPosition().equals(p) && !somethingExplosed){
                            exploser(bombAdj, now) ;
                            somethingExplosed = true ;
                        }
                    }
                }
                world.addExplosion(p, now);
            }
        }
    }
    public void update(long now){
        getBombs().removeIf(bomb -> bomb.hasToBeRemoved()) ;
        getMonsters().removeIf(go ->  go.hasToBeRemoved()) ;
        getBoxes().removeIf(go ->  go.hasToBeRemoved()) ;

        getMonsters().forEach(go -> go.update(now));
        getBombs().forEach(bomb -> bomb.update(now));
        getWorld().update(now) ;

        for(Bomb bomb : getBombs()){
            if (bomb.isExplosing())
                exploser(bomb, now) ;
        }
    }
    public Map<Position, Explosion> getExplosions(){
        return getWorld().getExplosions() ;
    }
}