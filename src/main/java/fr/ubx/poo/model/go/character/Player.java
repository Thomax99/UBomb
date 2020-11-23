/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.*;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.decor.* ;
import fr.ubx.poo.model.decor.bonus.* ;
import fr.ubx.poo.game.Game;
import javafx.geometry.Pos;

public class Player extends GameObject implements Movable {

    private final boolean alive = true;
    Direction direction;
    private boolean moveRequested = false, bombRequested = false ;
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
    private void damage(){
        lives--;
    }
    public int getLives() {
        return lives;
    }
    private void addBomb(){
        bombs++ ;
    }
    private void lessBomb(){
        bombs-- ;
        bombs = (bombs < 0 ? 0 : bombs) ;
    }
    public int getBombs() {
        return bombs;
    }
    private void useKey(){
        key -- ;
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

    public boolean canBomb(){
        return getBombs() > 0 ;
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
    public void requestBomb(){
        bombRequested = true ;
    }
    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        if (game.getWorld().get(nextPos) instanceof Tree || game.getWorld().get(nextPos) instanceof Stone) return false ;
        for(GameObject go : game.getMonstersAndBoxes()){
            if (go instanceof Box){
                Box box = (Box) go ;
                if (box.getPosition().equals(nextPos)){
                    if(box.canMove(direction)){
                        box.doMove(direction);
                        return true;
                    }
                    else {
                        return false ;
                    }
                }
            }
            else if (go instanceof Monster){
                Monster monster = (Monster) go ;
                if (monster.getPosition().equals(nextPos)){
                    damage() ;
                    return true;
                }
            }
        }
        if (game.getWorld().isInside(nextPos)){
            return true;
        }
        return false;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
        if (game.getWorld().get(nextPos) instanceof Bonus){
            Bonus bonus = (Bonus) game.getWorld().get(nextPos) ;
            if(!bonus.isAlreadyTaken()){
                game.getWorld().clear(nextPos);
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
        if (game.getWorld().get(nextPos) instanceof DoorNext){
            DoorNext door = (DoorNext) game.getWorld().get(nextPos) ;
            if (! door.isClosed()) game.changeWorld(1);
        }
        if (game.getWorld().get(nextPos) instanceof DoorPrevOpened){
            game.changeWorld(-1);
        }
        if(game.getWorld().get(nextPos) instanceof Decor){
            Decor decor = (Decor) game.getWorld().get(nextPos) ;
            if(decor instanceof Princess){
                winner = true;
            }
        }

    }
    public void requestOpenDoor(){
        Position nextPos = direction.nextPosition(getPosition());
        if (game.getWorld().get(nextPos) instanceof DoorNext){
            DoorNext door = (DoorNext) game.getWorld().get(nextPos) ;
            if(door.isClosed() && getKey() > 0){
                door.open();
                useKey() ;
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
        if (bombRequested){
            if (canBomb()){
                game.addBomb(new Bomb(game, getPosition(), getPortee(), now)) ;
                lessBomb();
            }
        }
        bombRequested = false ;
    }
    public void explose(){
        lives-- ;
    }

    public boolean isWinner() {
        return winner;
    }

    public boolean isAlive() {
        if(lives==0){
            return false;
        }
        return alive;
    }

}
