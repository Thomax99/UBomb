package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.decor.bonus.*;
import fr.ubx.poo.model.go.Box;


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
     * @param level the number of the current level
     * @param nb_level_max the number maximum of the level
     * @return a randomly generate world
     */
    public static WorldEntity[][] randomBuild(int level, int nb_level_max) {
        int width = (int) (Math.random()*20 +10), height = (int) (Math.random()*20 +10) ; // the Dimension of a game is at least a square of 10*10 and at more a square of 30*30
        WorldEntity[][] raw = new WorldEntity[height][width] ; //we generate the array of entities
        boolean isFirstLevel = (level == 1) ; //useful to know if we have to generate a position of the player and a previousDoor
        boolean isLastLevel = (level == nb_level_max) ; //useful to know if we have to generate a princess, a nextDoor and a key

        int x, y ;

        if (isFirstLevel){
            //we need to compute a player position
            x = (int) (Math.random()*width) ; y = (int) (Math.random()*height) ;
            raw[y][x] = WorldEntity.Player ;
        }
        else {
            //we need to compute a previousDoor
            x = (int) (Math.random()*width) ; y = (int) (Math.random()*height) ;
            raw[y][x] = WorldEntity.DoorPrevOpened ;
        }
        if (isLastLevel){
            //we need to generate a Princess
            while (raw[y][x] != null){ // the computation has to be on an empty position
                x = (int) (Math.random()*width) ; y = (int) (Math.random()*height) ;
            }
            raw[y][x] = WorldEntity.Princess ;
        }
        else {
            //we need to generate a nextDoor and a key
            while (raw[y][x] != null){ // the computation has to be on an empty position
                x = (int) (Math.random()*width) ; y = (int) (Math.random()*height) ;
            }
            raw[y][x] = WorldEntity.DoorNextClosed ;

            while (raw[y][x] != null){ // the computation has to be on an empty position
                x = (int) (Math.random()*width) ; y = (int) (Math.random()*height) ;
            }
            raw[y][x] = WorldEntity.Key ;
        }

        //PROBABILITIES
        //they are managed, ie an has 0.2 probability to be a decor which has 0.3 probability to be a bonus, ...
        double emptyProba = 0.7, boxProba = 0.1, decorProba = 0.15, monsterProba = 0.05 ;
        double stoneProba = 0.4, treeProba = 0.3, bonusProba = 0.1 ;
        double numberIncProba, numberDecProba, rangeIncProba, rangeDecProba = 0.2, heartProba = 0.05, landminerProba = 0.2, scarecrowProba = 0.05 ;
        numberIncProba = numberDecProba = rangeIncProba = heartProba = 0.15 ;
        for(int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                if (raw[i][j] == null) {
                    // empty case
                    double firstLayer = Math.random() ;
                    if (firstLayer < emptyProba){
                        raw[i][j] = WorldEntity.Empty ;
                    }
                    else if(firstLayer < emptyProba + boxProba){
                        raw[i][j] = WorldEntity.Box ;
                    }
                    else if (firstLayer < emptyProba + boxProba+monsterProba){
                        raw[i][j] = WorldEntity.Monster ;
                    }
                    else {
                        //the case is a decor. Which one ?
                        double secondLayer = Math.random() ;
                        if (secondLayer < stoneProba){
                            raw[i][j] = WorldEntity.Stone ;
                        }
                        else if (secondLayer < stoneProba + treeProba){
                            raw[i][j] = WorldEntity.Tree ;
                        }
                        else {
                            //the case is a bonus. Which one ?
                            double thirdLayer = Math.random() ;
                            if (thirdLayer < numberIncProba){
                                raw[i][j] = WorldEntity.BombNumberInc ;
                            }
                            else if (thirdLayer < numberIncProba + numberDecProba){
                                raw[i][j] = WorldEntity.BombNumberDec ;
                            }
                            else if (thirdLayer < numberIncProba + numberDecProba + rangeIncProba){
                                raw[i][j] = WorldEntity.BombRangeInc ;
                            }
                            else if (thirdLayer < numberIncProba + numberDecProba + rangeIncProba + rangeDecProba){
                                raw[i][j] = WorldEntity.BombRangeDec ;
                            }
                            else if (thirdLayer < numberIncProba + numberDecProba + rangeIncProba + rangeDecProba + heartProba){
                                raw[i][j] = WorldEntity.Heart ;
                            }
                            else if (thirdLayer < numberIncProba + numberDecProba + rangeIncProba + rangeDecProba + heartProba + landminerProba){
                                raw[i][j] = WorldEntity.Landminer ;
                            }
                            else {
                                raw[i][j] = WorldEntity.BonusScarecrow ;
                            }
                        }
                    }
                }
            }
        }

        return raw  ; // build(raw, new Dimension(height, width)) ;
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
