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
import java.util.List;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Iterator;

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
     * Useful for loading a random world according to the config properties file
     * @param path the path of the file config.properties in which the probabilities are registered
     * @param level the number of the current level
     * @param nb_level_max the number maximum of the level
     * @return a randomly generate world
     */
    public static WorldEntity[][] randomBuild(String path, int level, int nb_level_max) {
        return RandomWorldBuilder.randomBuild(path, level, nb_level_max)  ;
    }
    /**
     * This function is used to load a world from file
     * @param n the level of the new world
     * @param worldPath the path in which we can found the file
     * @param prefix the prefix of the file
     * @param suffix the suffix of the file
     * @return a WorldEntity tab corresponding to the world from file
     * @throws WorldNotValidException
     */
    public static WorldEntity[][] loadWorldFromFile(int n, String worldPath, String prefix, String suffix) throws WorldNotValidException{
        List<List<WorldEntity>> mapEntitiesList = new LinkedList<>() ;
        // to read the world, we first put it on a list object, and after we convert it on a classical array
        try (InputStream input = new FileInputStream(new File(worldPath, prefix+n+suffix))){
            int nb_lines = 0, c ;
            mapEntitiesList.add(new LinkedList<>()) ; //we make the first line
            while((c = input.read()) != -1){
                if ((char) c != '\n'){
                    //we have an entity character
                    Optional<WorldEntity> entity = WorldEntity.fromCode((char) c) ;
                    if (entity.isPresent())
                        mapEntitiesList.get(nb_lines).add(entity.get()) ;
                    else
                        mapEntitiesList.get(nb_lines).add(WorldEntity.Empty) ; //by default, the value is Empty
                }
                else {
                    //we have to put a new line
                    mapEntitiesList.add(new LinkedList<>()) ;
                    nb_lines++ ;
                }
            }
        } catch (IOException ex) {
            System.err.println("Error loading game");
            throw new RuntimeException("Le fichier ne peut être lu") ;
        }

        //now we have to ensure that all the lines have the same length
        int lineLength = mapEntitiesList.get(0).size() ; //we are sure that there is a first line (we create it explicitly upper)
        Iterator<List<WorldEntity>> it = mapEntitiesList.iterator() ;
        while(it.hasNext()){
            List<WorldEntity> line = it.next() ;
            if (line.size() != lineLength){
                throw new WorldNotValidException("Le fichier n'est pas conforme") ;
            }
        }
        // now we can create the classical array : we're sure that it is regular
        WorldEntity[][] mapEntities = new WorldEntity[mapEntitiesList.size()][mapEntitiesList.get(0).size()] ;
        for(int i = 0; i < mapEntitiesList.size() ; i++){
            for(int j = 0; j < mapEntitiesList.get(0).size(); j++){
                mapEntities[i][j] = mapEntitiesList.get(i).get(j) ;
            }
        }
        return mapEntities ;
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
