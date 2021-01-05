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
    private boolean moveRequested = false, bombRequested = false, bombIsLandmine =false, bombIsScarecrow = false ;
    private int lives = 1;
    private int bombs = 1;
    private int key = 0;
    private int range = 1;
    private boolean winner;

    public Player(Game game, Position position) {
        super(game, position);
        this.lives = game.getInitPlayerLives();
        this.bombs = game.getInitPlayerBombs();
        this.key = game.getInitPlayerKey();
        this.range = game.getInitPlayerPortee();
    }
    @Override
    public int getLevel(){
        return game.getLevel() ;
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
    public void hasLandmine(){
        bombIsLandmine = true;
    }
    public void hasScarecrow(){
        bombIsScarecrow = true ;
    }
    public void lessPortee(){
        range = (range <= 1 ? 1 : range-1);
    }
    public int getRange() {
        return range;
    }
    public void requestMove(Direction direction) {
        if (direction != getDirection()) {
            setDirection(direction);
        }
        moveRequested = true;
    }
    @Override
    public void doMove(Direction direction) {
        super.doMove(direction);
        if(game.getWorld().get(getPosition()) != null) //there is a good decor at this position
            game.getWorld().get(getPosition()).computeDecor(this);
        game.getMonsters().stream().filter(monster -> monster.getPosition().equals(getPosition())).forEach(monster -> damage(getCurrentTime())) ;
        game.getBoxes().stream().filter(box -> box.getPosition().equals(getPosition())).forEach(box -> box.doMove(direction));
    }
    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        return game.getBoxes().stream().map(box -> !box.getPosition().equals(nextPos) || box.canMoveIn(direction)).reduce(super.canMove(direction), (b1, b2) -> b1 && b2 ) ;

    }

    public void requestOpenDoor(){
        Position nextPos = getDirection().nextPosition(getPosition());
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
            if (canMove(getDirection())) {
                doMove(getDirection());
            }
        }
        moveRequested = false;
        if (bombRequested){
            if (bombIsScarecrow){
                if (game.canScarecrow(getPosition())){
                    game.addScarecrow(getPosition());
                    bombIsScarecrow = false ;
                }
            }
            else if (bombIsLandmine){
                //the landmine is a bomb which will be placed in front of the player
                Position nextPosition = getDirection().nextPosition(getPosition()) ; // compute the position
                if (canBomb(nextPosition)){
                    game.addLandmine(nextPosition, range) ;
                    bombIsLandmine = false ;
                    bombs-- ;
                }
            }
            else if (canBomb(getPosition())){
                game.addBomb(getPosition(), range, now) ;
                bombs-- ;
            }
        }
        bombRequested = false ;
    }

    public void requestBomb(){
        bombRequested = true ;
    }
    public boolean canBomb(Position position){
        return bombs > 0 && game.canBomb(position) ;
    }
    public void bombHasExplosed(){
        bombs++;
    }
    public void damage(long now){
        if(!isInvincible){
            lives-- ;
            isInvincible = true ;
            timeInvincible = now ;
        }
    }

    public boolean isAlive() {
        return lives != 0 && alive;
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
    @Override
    public void explosion(long now){
        damage(now);
    }

}
