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


import fr.ubx.poo.model.go.Box;
import fr.ubx.poo.model.go.character.*;

public class Game {

    private final World world;
    private final Player player;
    private final List<Monster> monsters ;
    private final List<Box> boxes ;
    private final String worldPath;
    public int initPlayerLives;
    public int initPlayerBombs;
    public int initPlayerKey;
    public int initPlayerPortee;

    public Game(String worldPath) {
        world = new WorldStatic();
        this.worldPath = worldPath;
        loadConfig(worldPath);
        Position positionPlayer = null;
        try {
            positionPlayer = world.findPlayer();
            player = new Player(this, positionPlayer);
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        monsters = new ArrayList<>() ;
        for(Position p : world.findMonsters()){
            monsters.add(new Monster(this, p)) ;
        }
        boxes = new ArrayList<>() ;
        for(Position p : world.findBoxes()){
            boxes.add(new Box(this, p)) ;
        }
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

    private void loadConfig(String path) {
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            initPlayerLives = Integer.parseInt(prop.getProperty("lives", "3"));
            initPlayerBombs = Integer.parseInt(prop.getProperty("bombs", "3"));
            initPlayerKey = Integer.parseInt(prop.getProperty("key", "3"));
            initPlayerPortee = Integer.parseInt(prop.getProperty("portee", "3"));
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    public World getWorld() {
        return world;
    }
    public List<Monster> getMonsters(){
        return monsters ;
    }
    public List<Box> getBoxes(){
        System.out.println(boxes.size()) ;
        return boxes ;
    }

    public Player getPlayer() {
        return this.player;
    }


}
