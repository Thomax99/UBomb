/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.view.sprite.Sprite;
import fr.ubx.poo.view.sprite.SpriteFactory;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.character.*;



import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;



public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle, scorePath, playerName;
    private final Game game;
    private final Player player;
    private final List<Sprite> sprites = new LinkedList<>() ;
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;

    public GameEngine(final String windowTitle, final String scorePath, Game game, final Stage stage, String playerName) {
        this.windowTitle = windowTitle;
        this.scorePath = scorePath ;
        this.game = game;
        this.playerName = playerName ;
        this.player = game.getPlayer();
        initialize(stage, game);
        buildAndSetGameLoop();
    }
    /**
     * Function used to (re)initialize the display of a given game
     * @param stage the stage in which we gave to display
     * @param game the given game
     */
    private void initialize(Stage stage, Game game) {
        this.stage = stage;
        Group root = new Group();
        layer = new Pane();

        //size management
        int height = game.getWorld().dimension.height;
        int width = game.getWorld().dimension.width;
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);

        //style management
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        //reinitialize the input and the statusBar
        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);

        // Create decor sprites
        //this first is just for managing the different elements stored on the World (stone, bonus, ...)
        game.getWorld().forEach( (pos,d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        //In case of coming back on an old level, those lines permit to display the sprites of bombs (decor which are stored on the game)
        game.getExplosives().forEach( (pos,d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d))); //bombs

        //create game object Sprite
        spritePlayer = SpriteFactory.createPlayer(layer, player);
        game.getMonsters().forEach(monster -> sprites.add(SpriteFactory.createMonster(layer, monster)));
        game.getBoxes().forEach(box -> sprites.add(SpriteFactory.createBox(layer, box)) );
    }

    protected final void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions

                processInput(now);

                // Do actions
                update(now);

                // Graphic update
                render();
                statusBar.update(game);
            }
        };
    }
    private void processInput(long now) {

        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        }
        if (input.isMoveDown()) {
            player.requestMove(Direction.S);
        }
        if (input.isMoveLeft()) {
            player.requestMove(Direction.W);
        }
        if (input.isMoveRight()) {
            player.requestMove(Direction.E);
        }
        if (input.isMoveUp()) {
            player.requestMove(Direction.N);
        }
        if (input.isKey()){
            player.requestOpenDoor() ;
        }
        if (input.isBomb()) {
            player.requestBomb() ;
        }
        if (input.isLandmine()){
            player.requestLandmine() ;
        }
        if (input.isScarecrow()){
            player.requestScarecrow() ;
        }
        input.clear();
    }


    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(20));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 500, Color.WHITE);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }

    /**
     * This function is used by an AnimationTimer to update the differents states of the game and sprites
     * @param now the actual time
     */
    private void update(long now) {
        game.update(now) ; // here we update all the model part

        //now we're going to update the view with some flags from the model
        if(game.hasElementsLevelChange()){
            //there is some decor changes out of the explosions (explosives, scarecrow, ...)
            game.getNewDecors().forEach( (pos,d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d))); //and we recreate them
            game.elementsLevelChanged();
        }
        if(game.hasChangedWorld()){
            // the level has been changed !
            // we are going to remove and remake all the elements by a new itilalization
            sprites.forEach(Sprite::remove);
            sprites.clear();
            stage.close();
            initialize(stage, game);
            //we notify the game that the worldChange has been made, and that the new elements of the levels has been put
            game.worldChangeMade();
            game.elementsLevelChanged();
        }
        player.update(now);
        if (!player.isAlive()) {
            //the game is lost
            gameLoop.stop();
            Score score = Score.computeScore(playerName, player, game) ;
            List<Score> scores = Score.getScoreFromFile(scorePath, "scores") ;
            scores.add(score) ;
            showMessage("Perdu !\n Votre score est de "+ score.getScore()+" :/\n" + Score.getStringScoreFromListScore(scores), Color.RED);
        }
        if (player.isWinner()) {
            //the game is won
            gameLoop.stop();
            Score score = Score.computeScore(playerName, player, game) ;
            List<Score> scores = Score.getScoreFromFile(scorePath, "scores") ;
            scores.add(score) ;
            showMessage("Gagné !!\n Votre score est de "+ score.getScore()+" :)\n" + Score.getStringScoreFromListScore(scores), Color.GREEN);
            Score.setFileScoreFromListScore(scores, scorePath, "scores"); // we write the file just if the game is won
        }
    }

    private void render() {
        sprites.forEach(Sprite::render);
        // last rendering to have player in the foreground
        spritePlayer.render();
    }

    public void start() {
        gameLoop.start();
    }
}
