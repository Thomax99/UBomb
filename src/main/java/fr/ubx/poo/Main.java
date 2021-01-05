/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo;

import fr.ubx.poo.engine.GameEngine;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.view.image.ImageFactory;
import fr.ubx.poo.view.sprite.Sprite;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Parameters params = getParameters();
        List<String> list = params.getRaw();
        boolean isRandom = (list.size() > 0 && list.get(0).equals("random")) ; // if the games has to be randmoly generate
        int nb_levels = 5; // by default, we initialize this variable at 5. If isRandom = false, this variable is not used
        if(list.size() > 1){
            String param2 = list.get(1) ;
            try{
                nb_levels = Integer.parseInt(param2) ;
            }
            catch(NumberFormatException e){
                System.err.println("The parameter " + param2+" is not a number");
            }
        }
        ImageFactory.getInstance().load();
        String path = getClass().getResource("/sample").getFile();
        Game game = new Game(path, isRandom, nb_levels);
        GameEngine engine = new GameEngine("UBomb", game, stage);
        engine.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
