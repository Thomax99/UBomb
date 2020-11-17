/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.decor.* ;
import fr.ubx.poo.model.decor.bonus.* ;
import fr.ubx.poo.game.Game;

public class Player extends GameObject implements Movable {

    private final boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives = 1;
    private int bombs = 1;
    private int key = 0;
    private int portee = 1;

    private boolean winner;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
        this.bombs = game.getInitPlayerBombs();
        this.key = game.getInitPlayerKey();
        this.portee = game.getInitPlayerPortee();
    }
    private void addLive(){
        lives++ ;
    }
    public int getLives() {
        return lives;
    }
    private void addBomb(){
        bombs++ ;
    }
    private void lessBomb(){
        bombs-- ;
    }
    public int getBombs() {
        return bombs;
    }
    private void addKey(){
        key++ ;
    }
    public int getKey() {
        return key;
    }
    private void addPortee(){
        portee++;
    }
    private void lessPortee(){
        portee--;
    }
    public int getPortee() {
        return portee;
    }

    public Direction getDirection() {
        return direction;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        if (game.getWorld().get(nextPos) instanceof Tree || game.getWorld().get(nextPos) instanceof Stone) return false ;
        return game.getWorld().isInside(nextPos); // changer avec la methode isInside de la classe World
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
        if (game.getWorld().get(nextPos) instanceof Bonus){
            Bonus bonus = (Bonus) game.getWorld().get(nextPos) ;
            if(!bonus.isAlreadyTaken()){
                bonus.take() ;
                if (bonus instanceof Heart){
                    addLive();
                }
                else if (bonus instanceof Key){
                    addKey();
                }
                else if (bonus instanceof BombNumberInc){
                    addBomb();
                }
                else if (bonus instanceof BombNumberDec){
                    lessBomb();
                }
                else if(bonus instanceof BombRangeInc){
                    addPortee();
                }
                else if(bonus instanceof BombRangeDec){
                    lessPortee();
                }
            }
        }
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }

    public boolean isWinner() {
        return winner;
    }

    public boolean isAlive() {
        return alive;
    }

}
