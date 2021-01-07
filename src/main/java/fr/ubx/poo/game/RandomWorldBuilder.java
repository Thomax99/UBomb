package fr.ubx.poo.game;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import java.util.Hashtable;
import java.util.Map;
import fr.ubx.poo.Constants;

/**
 * This class is useful to generate a random world
 */
public class RandomWorldBuilder {

    /**
     * @param path the path of the file config.properties in which the probabilities are registered
     * @param level the number of the current level
     * @param nb_level_max the number maximum of the level
     * @return a randomly generate world
     */
    public static WorldEntity[][] randomBuild(String path, int level, int nb_level_max) {

        // declaration of variables and loading of the differents constants that we need

        //first we load the differents default values from the Constant class : useful to have default value
        double probas[] = {Constants.defaultEmptyProba, Constants.defaultBoxProba, Constants.defaultMonsterProba, Constants.defaultStoneProba,
                        Constants.defaultTreeProba, Constants.defaultNumberIncProba, Constants.defaultNumberDecProba, Constants.defaultRangeIncProba,
                        Constants.defaultRangeDecProba, Constants.defaultHeartProba, Constants.defaultLandminerProba, Constants.defaultScarecrowProba} ;
        String probaNames[] = {Constants.emptyProbaName, Constants.boxProbaName, Constants.monsterProbaName, Constants.stoneProbaName,
                            Constants.treeProbaName, Constants.numberIncProbaName, Constants.numberDecProbaName, Constants.rangeIncProbaName,
                            Constants.rangeDecProbaName, Constants.heartProbaName, Constants.landminerProbaname, Constants.scarecrowProbaName} ;
        WorldEntity entitiesMatches[] = { WorldEntity.Empty, WorldEntity.Box, WorldEntity.Monster, WorldEntity.Stone, WorldEntity.Tree,
                                        WorldEntity.BombNumberInc, WorldEntity.BombNumberDec, WorldEntity.BombRangeInc, WorldEntity.BombRangeDec,
                                        WorldEntity.Heart, WorldEntity.Landminer, WorldEntity.BonusScarecrow} ;
        int minWidth = Constants.defaultMinWidthValue, maxWidth = Constants.defaultMaxWidthValue, minHeight = Constants.defaultMinHeightValue, maxHeight = Constants.defaultMaxHeightValue ;

        // now we load the needed values for the config.properties file
        try (InputStream input = new FileInputStream(new File(path, Constants.propertiesFileName))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            //loading the size value
            int tmpSize = Integer.parseInt(prop.getProperty(Constants.fieldMinWidthValue, minWidth+"")) ;
            minWidth = tmpSize ;
            tmpSize = Integer.parseInt(prop.getProperty(Constants.fieldMaxWidthValue, maxWidth+"")) ;
            maxWidth = tmpSize ;
            tmpSize = Integer.parseInt(prop.getProperty(Constants.fieldMinHeightValue, minHeight+"")) ;
            minHeight = tmpSize ;
            tmpSize = Integer.parseInt(prop.getProperty(Constants.fieldMaxHeightValue, maxHeight+"")) ;
            maxHeight = tmpSize ;

            //loading the probabilities
            for(int i = 0; i < probaNames.length; i++){
                double tmpProba = Double.parseDouble(prop.getProperty(probaNames[i], probas[i]+""));
                probas[i] = tmpProba ;
            }
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }


        int width = (int) (Math.random()*(maxWidth- minWidth) +minWidth), height = (int) (Math.random()*(maxHeight-minHeight) +minHeight) ; // the Dimension of a game is at least a square of 12*10 and at more a rectangle of 30*25
        WorldEntity[][] raw = new WorldEntity[height][width] ; //we generate the array of entities
        boolean isFirstLevel = (level == 1) ; //useful to know if we have to generate a position of the player and a previousDoor
        boolean isLastLevel = (level == nb_level_max) ; //useful to know if we have to generate a princess, a nextDoor and a key

        int x, y ;

        if (isFirstLevel){
            raw = computePlayerPosition(raw, width, height) ;
        }
        else {
            //we need to compute a previousDoor
            Position position = computeEmptyPosition(raw, width, height) ;
            raw[position.y][position.x] = WorldEntity.DoorPrevOpened ;
        }
        if (isLastLevel){
            //we need to generate a Princess
            Position position = computeEmptyPosition(raw, width, height) ;
            raw[position.y][position.x] = WorldEntity.Princess ;
        }
        else {
            //we need to generate a nextDoor and a key
            Position position = computeEmptyPosition(raw, width, height) ;
            raw[position.y][position.x] = WorldEntity.DoorNextClosed ;
            position = computeEmptyPosition(raw, width, height) ;
            raw[position.y][position.x] = WorldEntity.Key ;
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
    /**
     * Useful to compute on a given WorldEntity tab of given size a player position
     * Also put at the empty value the adjacents cases to not be blocked directly by differents objects
     * @param raw the given WorldEntity tab
     * @param width the given width
     * @param height the given height
     * @return the worldEntity tab with good values
     */
    private static WorldEntity[][] computePlayerPosition(WorldEntity[][] raw, int width, int height){
        //we need to compute a player position
        Position pos = computeEmptyPosition(raw, width, height) ;
        raw[pos.y][pos.x] = WorldEntity.Player ;
        //here we're going to make empty the adjacents cases of the player position
        if (pos.x - 1 >= 0) raw[pos.y][pos.x-1] = WorldEntity.Empty ;
        if (pos.x + 1 < width) raw[pos.y][pos.x+1] = WorldEntity.Empty ;
        if (pos.y - 1 >= 0) raw[pos.y-1][pos.x] = WorldEntity.Empty ;
        if (pos.y +1 < height) raw[pos.y+1][pos.x] = WorldEntity.Empty ;
        return raw ;
    }
    /**
     * Useful to find on a given WorldEntity tab of given size an empty position
     * @param raw the given WorldEntity tab
     * @param width the given width
     * @param height the given height
     * @return the empty position
     */
    private static Position computeEmptyPosition(WorldEntity[][] raw, int width, int height){
        //we need to compute an empty position
        int x, y ;
        do {
            x = (int) (Math.random()*width) ; y = (int) (Math.random()*height) ;
        } while (raw[y][x] != null) ;
        return new Position(x, y) ;
    }
}
