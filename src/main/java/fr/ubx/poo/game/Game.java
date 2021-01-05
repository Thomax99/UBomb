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

import javax.lang.model.util.ElementScanner6;

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
import fr.ubx.poo.model.decor.explosives.*;

public class Game {
    private final List<World> worlds;
    private final Player player;
    private final List<List<Monster>> monsters ;
    private final List<List<Box>> boxes ;
    private final Map<Position, Explosive> explosives ;
    private final String worldPath;
    private int nb_level, level_max_initialised, nb_total_levels ;
    private boolean hasChangedWorld = false, hasLevelChange = false, hasNewExplosions = false, randomlyGenerate ;
    private String initPrefString ;
    public int initPlayerLives;
    public int initPlayerBombs;
    public int initPlayerKey;
    public int initPlayerPortee;
    /**
     * 
     * @param worldPath the path to find the levels in case that the Game is not random and the configuration EVERYTIME
     * you need a valid path !!
     * @param isRandom if the game is randomly generate or not
     * @param nb_levels the number of levels that we would like to play in case of isRandom is true. Otherwise, never used.
     */
    public Game(String worldPath, boolean isRandom, int nb_levels){
        this.nb_level = level_max_initialised = 1 ;
        this.nb_total_levels = nb_levels ;
        this.worldPath = worldPath;
        this.randomlyGenerate = isRandom ;
        loadConfig(worldPath);
        worlds = new ArrayList<>() ;
        monsters = new ArrayList<>() ;
        boxes = new ArrayList<>() ;
        explosives = new Hashtable<>() ;
        initializeWorld() ;
        Position positionPlayer = null;

        try {
            positionPlayer = getWorld().findPlayer();
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

        player = new Player(this, positionPlayer);
        initializeEntities() ;
    }
    private void initializeWorld(){
        if (randomlyGenerate)
            //the initialization is not from the files
            worlds.add(new World(WorldBuilder.randomBuild(nb_level, nb_total_levels))) ;
        else
            worlds.add(loadWorld(this.nb_level)) ;
    }

    private void initializeEntities(){
        monsters.add(new LinkedList<>()) ;
        boxes.add(new LinkedList<>()) ;
        getWorld().findMonsters().forEach(p -> getMonsters().add(new Monster(this, p) )) ;
        getWorld().findBoxes().forEach(p -> getBoxes().add(new Box(this, p) )) ;
    }

    public void changeWorld(int new_level){
        this.nb_level+=new_level ;
        if (this.nb_level > level_max_initialised){
            initializeWorld();
            initializeEntities() ;
            level_max_initialised++ ;
        }
        Position positionPlayer = null ;
        try{
            if(new_level == 1)
                positionPlayer = getWorld().findPreviousDoorOpened();
            else if (new_level == -1)
                positionPlayer = getWorld().findNextDoor();
            else{
                System.err.println("unauthorized level change");
                throw new RuntimeException("Can't change world with value : "+ new_level);

            }
        } catch (PositionNotFoundException e) {
                System.err.println("Position not found : " + e.getLocalizedMessage());
                throw new RuntimeException(e);
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
    /**
     * This function is used to know if there is a landmine at a given position and level
     * @param pos the given position
     * @param level the given level
     * @return if there is a landmine at the position pos on the level level
     */
    public boolean positionIsLandmine(Position pos, int level){
        Explosive explosive = explosives.get(pos) ;
        return explosive != null && !explosive.isBomb() && explosive.getLevel() == level ;
    }
    /**
     * This function is used to know if there is a bomb at a given position and level
     * @param pos the given position
     * @param level the given level
     * @return if there is a bomb at the position pos on the level level
     */
    public boolean positionIsBomb(Position pos, int level){
        Explosive explosive = explosives.get(pos) ;
        return explosive != null && explosive.isBomb() && explosive.getLevel() == level ;
    }
    public void addBomb(Position pos, int range, long start){
        explosives.put(pos, new Bomb(range, getLevel(), start)) ;
        hasLevelChange = true ;
    }
    public void addLandmine(Position pos, int range){
        explosives.put(pos, new Landmine(range, getLevel())) ;
        hasLevelChange = true ;
    }
    public void addScarecrow(Position pos){
        getWorld().putScarecrow(pos);
        hasLevelChange = true ;
    }
    public boolean hasScarecrow(){
        return getWorld().hasScarecrow() ;
    }
    public Position getScarecrowPosition(){
        return getWorld().getScarecrowPosition() ;
    }
    public Scarecrow getScarecrow(){
        return getWorld().getScarecrow() ;
    }
    public Map<Position, Explosive> getExplosives(){
        return explosives ;
    }
    public void levelChanged(){
        hasLevelChange = false ;
    }
    public boolean hasLevelChange(){
        return hasLevelChange ;
    }
    public boolean canBomb(Position p){
        return getWorld().canBomb(p) ;
    }
    public boolean canLandmine(Position p){
        for(Box box : getBoxes()){
            if (p.equals(box.getPosition())) return false ; // a landmine can't be put on a box
        }
        return canBomb(p) ; //otherwise a Landmine could be put as a bomb
    }
    public boolean canScarecrow(Position p){
        return getWorld().canScarecrow(p) ;
    }
    public boolean hasNewExplosions(){
        return hasNewExplosions ;
    }
    /**
     * 
     * @return the Direction in which the player is theorically going. Useful for the moving policies
     * And to have an abstraction for implementing the Scarecrow decor
     */
    public Direction getPlayerDirection(){
        return getPlayer().getDirection() ;
    }
    /**
     * 
     * @return the Position in which the player is. Useful for the moving policies
     * And to have an abstraction for implementing the Scarecrow decor
     */
    public Position getPlayerPosition(){
        if (getWorld().hasScarecrow()) return getWorld().getScarecrowPosition() ;
        return getPlayer().getPosition() ;
    }
    public void newExplosionsPut(){
        hasNewExplosions = true ;
    }
    /**
     * This function is useful to manage all the explosion recursively for each
     * explosive placed in position position
     * @param position The position of the explosive item
     * @param now the moment of the explosion
     */
    public void explode(Position position, long now){
        getPlayer().bombHasExplosed(); // notify the player that he has a bomb which explode
        hasNewExplosions = true ; // useful for the gameEngine and the management of the sprites
        hasLevelChange = true ;

        //getting the explosive engine
        Explosive explosive = explosives.get(position) ;
        if (explosive == null) throw new RuntimeException("Error : the only positions which can explode has to have an explosive engine on it") ;
        explosive.explosion(now) ; // we notify the explosion

        Direction directions[] = Direction.values();
        List<Monster> monsters = getMonsters(explosive.getLevel()) ;
        List<Box> boxes = getBoxes(explosive.getLevel()) ;
        World world = getWorld(explosive.getLevel()) ;

        final Position startPosition = position ; //we make a copy of the position to reinitialize it at each loop turn



        //management of the explosion on the position of the explosion (useful for landmines)
        world.addExplosion(position, now);
        if (getPlayer().getPosition().equals(position))
            player.explosion(now) ;
            monsters.stream().filter(monster -> monster.getPosition().equals(startPosition)).forEach(monster -> monster.explosion(now));

        for(Direction d : directions){
            position = startPosition ; // we reinitialize the variable
            boolean somethingExploded = false ; //useful to stop an explosion if something explode
            for (int j = 0; j < explosive.getRange() && !somethingExploded; j++){
                //decor explosion part
                final Position p = d.nextPosition(position) ; //this variable is declared as final because we need a final variable to use Streams interfaces
                if(!world.canExplose(p)) break ;
                somethingExploded = world.explose(p) ;

                //Game object explosion part
                if (getPlayer().getPosition().equals(p)){
                    player.explosion(now) ;
                    somethingExploded = true ;
                }
                for(Monster monster : monsters){
                    if (monster.getPosition().equals(p)){
                        monster.explosion(now) ;
                        somethingExploded = true ;
                    }
                }
                for(Box box : boxes){
                    if (box.getPosition().equals(p)){
                        box.explosion(now) ;
                        somethingExploded = true ;
                    }
                }

                Explosive exploAdj = explosives.get(p) ;
                if (exploAdj != null && !exploAdj.hasToBeRemoved()){
                    explode(p, now) ;
                }
                world.addExplosion(p, now);
                position = p ; // managing the depth of the range
            }
        }

    }
    public void update(long now){
        getMonsters().removeIf(go ->  go.hasToBeRemoved()) ;
        getBoxes().removeIf(go ->  go.hasToBeRemoved()) ;

        getMonsters().forEach(go -> go.update(now));
        getWorld().update(now) ;
        explosives.forEach( (pos, explosive) -> {
            if(explosive.isBomb()){
                Bomb bomb = (Bomb) explosive ;
                bomb.update(now) ;
                if (bomb.isExplosing())
                    explode(pos, now) ;
            }  });

        //landmine management
        Explosive explosive = explosives.get(getPlayer().getPosition()) ;
        if (explosive != null && !explosive.isBomb()){
            //there is a landmine
            explode(getPlayer().getPosition(), now) ;
        }
        for (Monster monster : getMonsters()){
            explosive = explosives.get(monster.getPosition()) ;
            if (explosive != null && !explosive.isBomb()){
                //there is a landmine
                explode(monster.getPosition(), now) ;
            }
        }

        //now we remove the explosive which has exploded
        Iterator<Position> it = explosives.keySet().iterator() ;
        while (it.hasNext()){
            Position pos = it.next() ;
            if (explosives.get(pos).hasToBeRemoved() ) it.remove();
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