package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.decor.bonus.*;
import fr.ubx.poo.model.go.Box;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import java.util.Hashtable;
import java.util.Map;

public class WorldBuilder {
    private final Map<Position, Decor> grid = new Hashtable<>();

    private WorldBuilder() {
    }

    public static Map<Position, Decor> build(WorldEntity[][] raw, Dimension dimension) {
        WorldBuilder builder = new WorldBuilder();
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                Position pos = new Position(x, y);
                Decor decor = processEntity(raw[y][x]);
                if (decor != null)
                    builder.grid.put(pos, decor);
            }
        }
        return builder.grid;
    }
    /**
     * THIS IS NOT A PORTABLE IMPLEMENTATION
     * @param path the path of the file config.properties in which the probabilities are registered
     * @param level the number of the current level
     * @param nb_level_max the number maximum of the level
     * @return a randomly generate world
     */
    public static WorldEntity[][] randomBuild(String path, int level, int nb_level_max) {
        int width = (int) (Math.random()*18 +12), height = (int) (Math.random()*15 +10) ; // the Dimension of a game is at least a square of 12*10 and at more a rectangle of 30*25
        WorldEntity[][] raw = new WorldEntity[height][width] ; //we generate the array of entities
        boolean isFirstLevel = (level == 1) ; //useful to know if we have to generate a position of the player and a previousDoor
        boolean isLastLevel = (level == nb_level_max) ; //useful to know if we have to generate a princess, a nextDoor and a key

        int x, y ;

        if (isFirstLevel){
            //we need to compute a player position
            x = (int) (Math.random()*width) ; y = (int) (Math.random()*height) ;
            raw[y][x] = WorldEntity.Player ;
            //here we're going to make empty the adjacents cases of the player position
            if (x - 1 >= 0) raw[y][x-1] = WorldEntity.Empty ;
            if (x + 1 < width) raw[y][x+1] = WorldEntity.Empty ;
            if (y - 1 >= 0) raw[y-1][x] = WorldEntity.Empty ;
            if (y +1 < height) raw[y+1][x] = WorldEntity.Empty ;

        }
        else {
            //we need to compute a previousDoor
            x = (int) (Math.random()*width) ; y = (int) (Math.random()*height) ;
            raw[y][x] = WorldEntity.DoorPrevOpened ;
        }
        if (isLastLevel){
            //we need to generate a Princess
            while (raw[y][x] != null){ // the computation has to be on an null position
                x = (int) (Math.random()*width) ; y = (int) (Math.random()*height) ;
            }
            raw[y][x] = WorldEntity.Princess ;
        }
        else {
            //we need to generate a nextDoor and a key
            while (raw[y][x] != null){ // the computation has to be on an null position
                x = (int) (Math.random()*width) ; y = (int) (Math.random()*height) ;
            }
            raw[y][x] = WorldEntity.DoorNextClosed ;

            while (raw[y][x] != null){ // the computation has to be on an empty position
                x = (int) (Math.random()*width) ; y = (int) (Math.random()*height) ;
            }
            raw[y][x] = WorldEntity.Key ;
        }

        double probas[] = {0.7, 0.07, 0.05, 0.07, 0.02, 0.012, 0.012, 0.015, 0.02, 0.01, 0.02, 0.006} ;
        String probaNames[] = {"emptyProba", "boxProba", "monsterProba", "stoneProba", "treeProba", "numberIncProba",
                                "numberDecProba", "rangeIncProba", "rangeDecProba", "heartProba", "landminerProba", "scarecrowProba"} ;
        WorldEntity entitiesMatches[] = {WorldEntity.Empty, WorldEntity.Box, WorldEntity.Monster, WorldEntity.Stone, WorldEntity.Tree,
                                        WorldEntity.BombNumberInc, WorldEntity.BombNumberDec, WorldEntity.BombRangeInc, WorldEntity.BombRangeDec,
                                        WorldEntity.Heart, WorldEntity.Landminer, WorldEntity.BonusScarecrow} ;
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            //searching for the probabilities on the configuration file
            for(int i = 0; i < probaNames.length; i++){
                double tmp = Double.parseDouble(prop.getProperty(probaNames[i], probas[i]+""));
                probas[i] = tmp ;
            }
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }

        //this part manage the randomisation of the world
        for(y = 0; y < height; y++){
            for (x = 0; x < width; x++){
                if (raw[y][x] == null) {
                    // empty case
                    double proba = Math.random() ;
                    for(int k = 0; k < probaNames.length; k++){
                        double sum = 0.0 ; //sum is an accumulator which contains all the probas already seen
                        for(int i = 0 ; i < k ; i++){
                            sum+=probas[i] ;
                        }
                        if (proba < sum + probas[k]){
                            //proba is between the sum of probas and the sum of probas plus the new proba
                            raw[y][x] = entitiesMatches[k] ;
                            break ;
                        }
                    }
                }
            }
        }
        return raw  ;
    }

    private static Decor processEntity(WorldEntity entity) {
        switch (entity) {
            case Stone:
                return new Stone();
            case Tree:
                return new Tree();
            case DoorNextClosed:
                return new Door(true, true);
            case DoorPrevOpened:
                return new Door(false, false);
            case DoorNextOpened :
                return new Door(false, true) ;
            case Princess:
                return new Princess();
            case Key:
                return new Key();
            case Heart:
                return new Heart() ;
            case BombNumberInc:
                return new BombNumberInc() ;
            case BombNumberDec:
                return new BombNumberDec() ;
            case BombRangeDec:
                return new BombRangeDec() ;
            case BombRangeInc:
                return new BombRangeInc() ;
            case Landminer:
                return new Landminer() ;
            case BonusScarecrow:
                return new BonusScarecrow() ;
            default:
                return null;
        }
    }
}
