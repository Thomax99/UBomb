/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo;

import fr.ubx.poo.engine.GameEngine;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Iterator;
import java.util.List;

import org.w3c.dom.NameList;

public class Main extends Application {
    private boolean isRandom ; // to know if the game has to generate randomly
    private int nbLevels ; // if isRandom, this number gives the number of levels to generate
    private String name ; // the name of the player
    @Override
    public void start(Stage stage) {
        Parameters params = getParameters();
        List<String> list = params.getRaw();
        loadParameters(getParameters().getRaw());
        ImageFactory.getInstance().load();
        String path = getClass().getResource("/sample").getFile();
        Game game = new Game(path, isRandom, nbLevels);
        GameEngine engine = new GameEngine("UBomb", path, game, stage, name);
        engine.start();
    }
    /**
     * This function load all the parameters need to load a game from a list
     * If the needed parameters are not on the list, it put default args
     * @param parameters the given list
     */
    private void loadParameters(List<String> parameters){
        isRandom = Constants.defaultRandomValue ;
        nbLevels = Constants.defaultNbLevels; // if isRandom, this number gives the number of levels to generate
        name = Constants.defaultName ; // the name of the player

        // the parameters format is "type=random nbLevels=5 name=Michel"
        Iterator<String> it = parameters.iterator() ;
        while (it.hasNext()){
            String param = it.next() ;
            if (param.length() > Constants.parameterNameField.length() &&
                param.substring(0, Constants.parameterNameField.length()).equals(Constants.parameterNameField)){
                // the parameter is the name
                name = param.substring(Constants.parameterNameField.length()) ; //we recup the name
            }
            else if (param.length() > Constants.parameterTypeField.length() &&
                    param.substring(0, Constants.parameterTypeField.length()).equals(Constants.parameterTypeField)){
                //the parameter is the type of the game
                String type = param.substring(Constants.parameterTypeField.length()) ;
                if (type.equals("random")) isRandom = true ;
            }
            else if (param.length() > Constants.parameterNbLevelsField.length() &&
                    param.substring(0, Constants.parameterNbLevelsField.length()).equals(Constants.parameterNbLevelsField)){
                //the parameter is the number of levels
                String number = param.substring(Constants.parameterNbLevelsField.length()) ;
                try {
                    nbLevels = Integer.parseInt(number) ;
                } catch(NumberFormatException e){
                    System.err.println("The parameter " + number+" is not a number");
                    nbLevels = Constants.defaultNbLevels ;
                }
            }
        }
        if (nbLevels > 9) nbLevels = 9 ; //the max nb_level value is 9
    }

    public static void main(String[] args) {
        launch(args);
    }

}
