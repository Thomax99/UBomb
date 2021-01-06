/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
    private final Player player;
    //the differents entities are managed by lists of Collections. Each collection match to the current set on a given level
    private final List<World> worlds;
    private final List<List<Monster>> monsters ;
    private final List<List<Box>> boxes ;
    private final List<Map<Position, Explosive>> explosives ;
    private final String worldPath;
    private int nb_level, level_max_initialized, nb_total_levels ;
    private boolean hasChangedWorld = false, hasElementsLevelChange = false, hasNewExplosions = false, randomlyGenerate ;
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
        this.nb_level = level_max_initialized = 1 ;
        this.nb_total_levels = nb_levels ;
        this.worldPath = worldPath;
        this.randomlyGenerate = isRandom ;
        loadConfig(worldPath);
        worlds = new ArrayList<>() ;
        monsters = new ArrayList<>() ;
        boxes = new ArrayList<>() ;
        explosives  = new ArrayList<>() ;
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
    /**
     * This function is used to initialize a new world in function of the private variables nb_level, randomlyGenerate and nb_total_levels if randomlyGenerate
     */
    private void initializeWorld(){
        if (randomlyGenerate)
            //the initialization is not from the files
            worlds.add(new World(WorldBuilder.randomBuild(worldPath, nb_level, nb_total_levels))) ;
        else
            worlds.add(loadWorld(this.nb_level)) ;
    }

    private void initializeEntities(){
        monsters.add(new LinkedList<>()) ;
        boxes.add(new LinkedList<>()) ;
        explosives.add(new Hashtable<>()) ;
        getWorld().findMonsters().forEach(p -> getMonsters().add(new Monster(this, p) )) ;
        getWorld().findBoxes().forEach(p -> getBoxes().add(new Box(this, p) )) ;
    }

    /**
     * This function is used to notify a game that the World in which the player is has changed (for instance he takes a door)
     * @param new_level The addition of this parameter and the game current level gives the level in which the player is
     */
    public void changeWorld(int new_level){
        this.nb_level+=new_level ;
        if (this.nb_level > level_max_initialized){
            initializeWorld();
            initializeEntities() ;
            level_max_initialized++ ;
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
        Explosive explosive = getExplosives().get(pos) ;
        return explosive != null && !explosive.isBomb() && explosive.getLevel() == level ;
    }
    /**
     * This function is used to know if there is a bomb at a given position and level
     * @param pos the given position
     * @param level the given level
     * @return if there is a bomb at the position pos on the level level
     */
    public boolean positionIsBomb(Position pos, int level){
        Explosive explosive = getExplosives().get(pos) ;
        return explosive != null && explosive.isBomb() && explosive.getLevel() == level ;
    }
    /**
     * This function is used to add a bomb on a given position, with a given range, at a given time
     * This function doesn't verify if it is allowed to put a bomb on this position, you need to use the function canBomb
     * to put legal bombs
     * @param pos the position in which the bomb is put
     * @param range the range of the bomb
     * @param start the time that you put a bomb
     */
    public void addBomb(Position pos, int range, long start){
        getExplosives().put(pos, new Bomb(range, getLevel(), start)) ;
        hasElementsLevelChange = true ;
    }
    /**
     * This function is used to add a landmine on a given position, with a given range
     * This function doesn't verify if it is allowed to put a landmine on this position, you need to use the function canLandmine
     * to put legal landmines
     * @param pos the position in which the landmine is put
     * @param range the range of the landmine
     */
    public void addLandmine(Position pos, int range){
        getExplosives().put(pos, new Landmine(range, getLevel())) ;
        hasElementsLevelChange = true ;
    }
    /**
     * This function is used to add a scarecrow on a given position
     * This function doesn't verify if it is allowed to put a scarecrow on this position, you need to use the function canScarecrow
     * to put legal scarecrows
     * @param pos the position in which the scarecrow is put
     */
    public void addScarecrow(Position pos){
        getWorld().addScarecrow(pos);
        hasElementsLevelChange = true ;
    }
    /**
     * 
     * @return if there is a scarecrow on the current world
     */
    public boolean hasScarecrow(){
        return getWorld().hasScarecrow() ;
    }
    /**
     * 
     * @return if there is a scarecrow on the current world, return this position. Otherwise, return null
     */
    public Position getScarecrowPosition(){
        return getWorld().getScarecrowPosition() ;
    }
    /**
     * 
     * @return the scarecrow. Useful for the management of the sprites
     */
    public Scarecrow getScarecrow(){
        return getWorld().getScarecrow() ;
    }
    /**
     * This function is used to give all the explosives engines which are currently on the world
     * @return a Map which contains the Explosive objects at the good locations
     */
    public Map<Position, Explosive> getExplosives(){
        return getExplosives(getLevel()) ;
    }
     /**
     * This function is used to give all the explosives engines which are currently on a given level
     * @param level the given level
     * @return a Map which contains the Explosive objects at the good locations
     */
    public Map<Position, Explosive> getExplosives(int level){
        return explosives.get(level - 1) ;
    }
    /**
     * Function used to notify the game that the gameEngine has made the change in case of the elements of the level has been changed
     */
    public void elementsLevelChanged(){
        hasElementsLevelChange = false ;
    }
    /**
     * Function used by the gameEngine to know if elements on the current level have been changed or not
     * @return if there was a level changed
     */
    public boolean hasElementsLevelChange(){
        return hasElementsLevelChange ;
    }
    /**
     * Function used by the gameEngine to know if the level has been changed
     * @return if there was a world change
     */
    public boolean hasChangedWorld(){
        return hasChangedWorld ;
    }
    /**
     * Function used to notify the game that the gameEngine has made the change in case of the world has been changed
     */
    public void worldChangeMade(){
        hasChangedWorld = false ;
    }
    /**
     * Function used to notify the game that the gameEngine has made the change in case of new explosions
     */
    public void newExplosionsPut(){
        hasNewExplosions = false ;
    }
    /**
     * Function used by the gameEngine to know if the level has new explosions
     * @return if there is new explosions
     */
    public boolean hasNewExplosions(){
        return hasNewExplosions ;
    }
    /**
     * function used to know if it is possible to put a bomb at a given position
     * @param p the position in which we would like to put a bomb
     * @return if it is possible or not to put a bomb here
     */
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
    /**
     * This function is useful to manage all the explosion recursively for each
     * explosive placed in position position
     * @param position The position of the explosive item
     * @param now the moment of the explosion
     * @param level the level in which the explosion occur
     */
    public void explode(Position position, long now, int level){
        hasNewExplosions = true ; // useful for the gameEngine and the management of the sprites
        hasElementsLevelChange = true ;

        //getting the explosive engine
        Explosive explosive = getExplosives(level).get(position) ;
        if (explosive == null) throw new RuntimeException("Error : the only positions which can explode has to have an explosive engine on it") ;
        if (explosive.isBomb()) getPlayer().bombHasExplosed(); // notify the player that he has a bomb which explode

        explosive.explosion(now) ; // we notify the explosion

        //gathering of all the needed variables
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
                if(!world.canExplode(p)) break ; //there is a decor which block the explosion
                somethingExploded = world.explode(p) ;

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

                Explosive exploAdj = getExplosives(level).get(p) ;
                if (exploAdj != null && !exploAdj.hasToBeRemoved()){
                    explode(p, now, level) ;
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
        //we need to make explosions of bombs for each level
        explosives.forEach(map -> map.forEach((pos, explosive) -> {
                                        if(explosive.isBomb()){
                                            Bomb bomb = (Bomb) explosive ;
                                            bomb.update(now) ;
                                            if (bomb.isExplosing()){
                                                explode(pos, now, bomb.getLevel()) ;
                                            }
                                        }
                                    } ) ) ;

        //landmine management : just on the current world
        Explosive explosive = getExplosives().get(getPlayer().getPosition()) ;
        if (explosive != null && !explosive.isBomb()){
            //there is a landmine
            explode(getPlayer().getPosition(), now, this.nb_level) ;
        }
        for (Monster monster : getMonsters()){
            explosive = getExplosives().get(monster.getPosition()) ;
            if (explosive != null && !explosive.isBomb()){
                //there is a landmine
                explode(monster.getPosition(), now, this.nb_level) ;
            }
        }

        //now we remove the explosive which has exploded
        Iterator<Position> it = getExplosives().keySet().iterator() ;
        while (it.hasNext()){
            Position pos = it.next() ;
            if (getExplosives().get(pos).hasToBeRemoved() ) it.remove();
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
    /**
     * This function compute if it is possible for a gameObject which can move to go at a given position
     * @param position the given position
     * @return if it is possible for a gameObject to go at this position
     */
    public boolean positionAllowedToMovableGameObjects(Position position){
        return getWorld().isInside(position) && !positionIsBomb(position, getLevel()) ; // a gameobject can't go outside of the world or on a bomb
    }
    /**
     * This function compute if it is possible for a Character to go at a given position
     * @param position the given position
     * @return if it is possible for a character to go at this position
     */
    public boolean positionAllowedToCharacters(Position position){
        return positionAllowedToMovableGameObjects(position) && (getWorld().canMoveIn(position)) ;
        //a character can't go on an unauthorized decor
    }
    /**
     * This function compute if it is possible for a monster to go at a given position
     * @param position the given position
     * @return if it is possible for a monster to go at this position
     */
    public boolean positionAllowedToMonsters(Position position){
        for(Box box : getBoxes()){
            if (box.getPosition().equals(position)) return false ; //a monster can't go on a box everytime
        }
        return !getWorld().positionIsDoor(position) && !getWorld().positionIsPrincess(position) && positionAllowedToCharacters(position); //another not allowed position for a monster is a princess position ;
    }
    /**
     * This function compute if it is possible for a player to go at a given position, if he came from a given direction
     * @param position the given position
     * @param dir the given direction
     * @return if it is possible for a player to go at this position
     */
    public boolean positionAllowedToPlayer(Position position, Direction dir){
        for(Box box : getBoxes()){
            if (box.getPosition().equals(position) && !box.canMove(dir)) return false ; //a monster can't go on a box if it can move in this direction
        }
        return positionAllowedToCharacters(position);
    }
    /**
     * This function compute if it is possible for a box to go at a given position
     * @param position the given position
     * @return if it is possible for a box to go at this position
     */
    public boolean positionAllowedToBoxes(Position position){
        for(Monster monster : getMonsters()){
            if (monster.getPosition().equals(position)) return false ; // a box can't go on a monster
        }
        for (Box box : getBoxes()){
            if (box.getPosition().equals(position)) return false ; // or on another box
        }
        return getWorld().isEmpty(position) && positionAllowedToMovableGameObjects(position) ; //a box can't go on a decor
    }
}