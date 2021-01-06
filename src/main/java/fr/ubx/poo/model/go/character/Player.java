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
    private boolean moveRequested = false, bombRequested = false, hasScarecrow = false, scarecrowRequested = false, landmineRequested = false ;
    private int lives = 1;
    private int bombs = 1, landmines = 0;
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
    public int getLandmines() {
        return landmines ;
    }
    public void addPortee(){
        range++;
    }
    public void hasLandmine(){
        landmines++;
    }
    public void hasScarecrow(){
        hasScarecrow = true ;
    }
    public boolean getScarecrow(){
        return hasScarecrow ;
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
    public void requestScarecrow() {
        scarecrowRequested = true;
    }

    /**
     * Compute a move in a Direction, depending of the surrounding,
     * the player can move a box or can not be allowed to move
     * @param direction The direction where the player will move
     */
    @Override
    public void doMove(Direction direction) {
        super.doMove(direction);
        if(game.getWorld().get(getPosition()) != null) //there is a good decor at this position
            game.getWorld().get(getPosition()).computeDecor(this);
        game.getMonsters().stream().filter(monster -> monster.getPosition().equals(getPosition())).forEach(monster -> damage(getCurrentTime())) ;
        game.getBoxes().stream().filter(box -> box.getPosition().equals(getPosition())).forEach(box -> box.doMove(direction));
    }

    /**
     * Tell if the entity can move in a certain direction.
     * @param direction the direction in which the entity is going
     * @return Yes if the entity can move in this direction, else No
     */
    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        return game.positionAllowedToPlayer(nextPos, direction) ;
    }

    /**
     * Open the door if the player have a key and is close to the door
     */
    public void requestOpenDoor(){
        Position nextPos = getDirection().nextPosition(getPosition());
        if (game.getWorld().positionIsDoor(nextPos)){
            Door door = (Door) game.getWorld().get(nextPos) ;
            if(door.isClosed() && getKey() > 0){
                door.open();
                useKey() ;
            }
        }
    }

    /**
     * Change the world to the world which is linked with the level
     * @param lv the level (world) where the player enter
     */
    public void changeWorld(int lv){
        game.changeWorld(lv);
    }

    /**
     *
     * @param now
     */
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
        if (landmineRequested){
            //the landmine is a bomb which will be placed in front of the player
            Position nextPosition = getDirection().nextPosition(getPosition()) ; // compute the position
            if (canLandmine(nextPosition)){
                game.addLandmine(nextPosition, range) ;
                landmines-- ;
            }
            landmineRequested = false ;
        }
        if (scarecrowRequested){
            if(canScarecrow(getPosition())){
                game.addScarecrow(getPosition());
                hasScarecrow = false ;
            }
            scarecrowRequested = false ;
        }
        if (bombRequested){
            if (canBomb(getPosition())){
                game.addBomb(getPosition(), range, now) ;
                bombs-- ;
            }
        }
        bombRequested = false ;
    }

    public void requestBomb(){
        bombRequested = true ;
    }
    public void requestLandmine(){
        landmineRequested = true ;
    }
    public boolean canBomb(Position position){
        return bombs > 0 && game.canBomb(position) ;
    }
    public boolean canLandmine(Position position){
        return landmines > 0 && game.canLandmine(position) ;
    }
    public boolean canScarecrow(Position position){
        return hasScarecrow && game.canScarecrow(position) ;
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
    @Override
    public boolean hasToBeRemoved(){
        return false ; // a player is never removed of a game
    }

}
