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

public class Player extends GameObject implements Movable {

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
    private void addLive(){
        lives++ ;
    }
    public void damage(long now){
        if(!isInvincible){
            lives-- ;
            isInvincible = true ;
            timeInvincible = now ;
        }
    }

    public Boolean isInvincible(){
        return isInvincible;
    }
    public int getLives() {
        return lives;
    }
    private void addBomb(){
        bombs++ ;
    }
    private void lessBomb(){
        bombs = (bombs <= 1 ? 1 : bombs--) ;
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
        range++;
    }
    private void lessPortee(){
        range = (range <= 1 ? 1 : range--);
    }
    public int getRange() {
        return range;
    }

    public boolean canBomb(){
        return bombs>currentBombPut ;
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
        if (game.getWorld().get(nextPos) instanceof Tree || game.getWorld().get(nextPos) instanceof Stone) return false ; // better use canMoveIn
        if (game.getWorld().get(nextPos) instanceof DoorNext){
            DoorNext door = (DoorNext) game.getWorld().get(nextPos) ;
            return !door.isClosed();
        }
        for(GameObject go : game.getMonstersAndBoxes()){
            if (go instanceof Box){
                Box box = (Box) go ;
                if (box.getPosition().equals(nextPos)){
                    return box.canMove(direction) ;
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
        if (game.getWorld().get(nextPos) instanceof Bonus){ //we need to add a method in the class Decor which could be "Recoverable" : if the object is a bonus, send true
            Bonus bonus = (Bonus) game.getWorld().get(nextPos) ;
            if(!bonus.hasToBeRemoved()){
                game.getWorld().clear(nextPos);
                bonus.remove() ; //we need to clarify and make better this portion
                // maybe on the decor, we add a method takeDecor. This function do nothing if it's just a normal decor, otherwise it makes the good treatment 
                // in function of the parameter player given 
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
        for(GameObject go : game.getMonstersAndBoxes()){
            if (go instanceof Box){
                Box box = (Box) go ;
                if (box.getPosition().equals(nextPos)){
                    box.doMove(direction);
                }
            }
            else if (go instanceof Monster){
                Monster monster = (Monster) go ;
                if (monster.getPosition().equals(nextPos)){
                    damage(currentTime) ;
                }
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
        currentTime = now ;
        if ( ((currentTime-timeInvincible)/ 1000000000L) >= 1 ){ //we have to suppress magic numbers
            isInvincible = false ;
        }
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
        if (bombRequested){
            if (canBomb()){
                game.addBomb(new Bomb(game, getPosition(), getRange(), now)) ;
                currentBombPut++ ;
            }
        }
        bombRequested = false ;
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
