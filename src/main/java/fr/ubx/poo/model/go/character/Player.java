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

public class Player extends Character {

    private final boolean alive = true;
    private boolean isInvincible = false;
    private long timeInvincible, currentTime ;
    Direction direction;
    private boolean moveRequested = false, bombRequested = false ;
    private int lives = 1;
    private int bombs = 1;
    private int key = 0;
    private int range = 1;
    private int currentBombPut = 0 ;
    private boolean winner;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
        this.bombs = game.getInitPlayerBombs();
        this.key = game.getInitPlayerKey();
        this.range = game.getInitPlayerPortee();
    }

    public void addLive(){
        lives++ ;
    }
    public Boolean isInvincible(){
        return isInvincible;
    }
    public int getLives() {
        return lives;
    }
    public void addBomb(){
        bombs++ ;
    }
    public void lessBomb(){
        bombs = (bombs <= 1 ? 1 : bombs-1) ;
    }
    public int getBombs() {
        return bombs;
    }
    private void useKey(){
        key -- ;
    }
    public void addKey(){
        key++ ;
    }
    public int getKey() {
        return key;
    }
    public void addPortee(){
        range++;
    }
    public void lessPortee(){
        range = (range <= 1 ? 1 : range-1);
    }
    public int getRange() {
        return range;
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
    public boolean canGoOnMonsterOrBoxes(){
        return true ;
    }

    public void doMove(Direction direction) {
        super.doMove(direction);
        if(game.getWorld().get(getPosition()) != null){ //there is a good decor at this position
            game.getWorld().get(getPosition()).computeDecor(this);
        }
        for(Monster monster : game.getMonsters()){
            if (monster.getPosition().equals(getPosition())){
                damage(getCurrentTime()) ;
            }
        }
        for(Box box : game.getBoxes()){
            if (box.getPosition().equals(getPosition())){
                box.doMove(direction);
            }
        }
    }

    public void requestOpenDoor(){
        Position nextPos = direction.nextPosition(getPosition());
        if (game.getWorld().get(nextPos) instanceof Door){ // find a way to avoid this
            Door door = (Door) game.getWorld().get(nextPos) ;
            if(door.isClosed() && getKey() > 0){
                door.open();
                useKey() ;
            }
        }
    }
    public void changeWorld(int lv){
        game.changeWorld(lv);
    }
    public void update(long now) {
        setCurrentTime(now);
        if ( ((getCurrentTime()-timeInvincible)/ 1000000000L) >= 1 ){ //we have to suppress magic numbers
            isInvincible = false ;
        }
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
        if (bombRequested){
            if (canBomb(getPosition())){
                game.addBomb(new Bomb(game, getPosition(), getRange(), now)) ;
                currentBombPut++ ;
            }
        }
        bombRequested = false ;
    }

    public void requestBomb(){
        bombRequested = true ;
    }
    public boolean canBomb(Position position){
        return bombs > currentBombPut  && game.getWorld().get(position) == null;
    }
    public void explose(long now){
        if(!isInvincible){
            lives-- ;
            isInvincible = true ;
            timeInvincible = now ;
        }
    }
    public void bombHasExplosed(){
        currentBombPut--;
    }
    public void damage(long now){
        if(!isInvincible){
            lives-- ;
            isInvincible = true ;
            timeInvincible = now ;
        }
    }


    public boolean isAlive() {
        if(lives==0){
            return false;
        }
        return alive;
    }
    public void princessFound(){
        winner = true ;
    }
    public boolean isWinner() {
        return winner;
    }
    

    public void remove(){
        // nothing used : hasTobeRemoved() reply false everytime for a player
    }
    private void setCurrentTime(long time){
        currentTime = time ;
    }
    private long getCurrentTime(){
        return currentTime ;
    }

}
