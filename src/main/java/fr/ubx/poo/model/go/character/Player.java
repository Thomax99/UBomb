/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.Constants ;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.* ;
import fr.ubx.poo.model.go.character.infections.Infection;
import fr.ubx.poo.game.Game;

public class Player extends Character{

    //booleans for managing the request from user
    // scarecrowRequested and landmineRequested are used just if Constants.INPUTMODIFIED is true
    private boolean moveRequested = false, bombRequested = false, scarecrowRequested = false, landmineRequested = false, infectionRequested = false ;
    private long timeInvincible, currentTime ;
    private boolean hasScarecrow, winner = false, isInvincible = false ;
    private Infection currentInfection ; //the current infection of the player
    // managing the differents values of the elements that a player has
    private int lives, bombs, landmines, key, range ;
    // storing differents values to compute a score
    private int nbDecorComputed = 0, nbTimesDamaged = 0, nbBombsPut = 0, nbLandminesPut = 0, nbScarecrowPut = 0 ;
    public Player(Game game, Position position) {
        super(game, position);
        //recuperation of the initials values of loaded configurations
        this.lives = game.getInitPlayerLives();
        this.bombs = game.getInitPlayerBombs();
        this.key = game.getInitPlayerKey();
        this.range = game.getInitPlayerRange();
        this.hasScarecrow = game.getInitPlayerScarecrow() ;
        this.landmines = game.getInitPlayerLandmines() ;
    }
    //the nexts methods are getters. Most of time they are used by the game Engine to know the state of the differents values on the player
    @Override
    public int getLevel(){
        return game.getLevel() ; // the player is on the same level than the game
    }
    public int getBombs() {
        return bombs;
    }
    public int getLives() {
        return lives;
    }
    public int getKey() {
        return key;
    }
    public int getLandmines() {
        return landmines ;
    }
    public boolean getScarecrow(){
        return hasScarecrow ;
    }
    public int getRange() {
        return range;
    }
    public boolean isWinner() {
        return winner;
    }
    public boolean isAlive() {
        return lives != 0;
    }
    /**
     * Used to know if the player is on an invincible state or not
     * @return if the current is invincible
     */
    public Boolean isInvincible(){
        return isInvincible;
    }

    //here the next methods are used by bonus to make a good treatment when they are going on them
    public void addBomb(){
        bombs++ ;
    }
    public void lessBomb(){
        bombs = (bombs <= 1 ? 1 : bombs-1) ; //the smaller number of bombs is 1
    }
    public void addLive(){
        lives++ ;
    }
    public void addKey(){
        key++ ;
    }
    public void princessFound(){
        winner = true ;
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
    public void lessPortee(){
        range = (range <= 1 ? 1 : range-1); //the smaller range is 1
    }
    public void addInfection(){
        infectionRequested = true ; //this will be treat by the update method to notify that there is a new infection to put
    }
    //this function are used by the game engine to compute the score
    public int getNbDecorComputed(){
        return nbDecorComputed ;
    }
    public int getNbTimesDamaged(){
        return nbTimesDamaged ;
    }
    public int getNbBombsPut(){
        return nbBombsPut ;
    }
    public int getNbLandminesPut(){
        return nbLandminesPut ;
    }
    public int getNbScarecrowPut(){
        return nbScarecrowPut ;
    }
    public int getNbLandmines(){
        return landmines ;
    }
    @Override
    public void doMove(Direction direction) {
        super.doMove(direction);
        if(!game.getWorld().isEmpty(getPosition())){
            //there is a decor at the position on which the player is going
            game.getWorld().get(getPosition()).computeDecor(this); //notify the decor that there is the player on it
            nbDecorComputed++ ;
        }
        //here we manage the interactions between other gameObject (ie damage the player if he is going on a monster position or moving the box)
        game.getMonsters().stream().filter(monster -> monster.getPosition().equals(getPosition())).forEach(monster -> damage(getCurrentTime())) ;
        game.getBoxes().stream().filter(box -> box.getPosition().equals(getPosition())).forEach(box -> box.doMove(direction));
    }
    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        return game.positionAllowedToPlayer(nextPos, direction, game.getLevel()) ;
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
    /**
     * Change the world to the world which is linked with the level
     * @param lv the level (world) in which the player enter
     */
    public void changeWorld(int lv){
        game.changeWorld(lv);
    }
    @Override
    public void update(long now) {
        setCurrentTime(now); // updating the time
        if ( ((getCurrentTime()-timeInvincible)/ Constants.secondInnanoSec) >= 1 ){
            // supress the invicibility of the player after one second
            isInvincible = false ;
        }
        if (hasAnInfection() && currentInfection.isTerminated(now)){
            //we suppress the infection
            currentInfection = null ;
        }
        if (infectionRequested){
            //we make an new infection
            currentInfection = Infection.getRandomInfection(this, now) ;
            infectionRequested = false ;
        }
        if (moveRequested) {
            //doing a move
            if (hasAnInfection()){
                //we call the method makeAction of the infection : it is called when moving
                currentInfection.makeAction();
            }
            if (canMove(getDirection())) {
                doMove(getDirection());
            }
            moveRequested = false;
        }

        // the managing of the request depends of the value Constants.INPUTMODIFIED : if we can modify them, there is a different treament
        if (Constants.INPUTMODIFIED){
            // there is a different input foreach possible request (landmine, scarecrow and bomb)
            if (landmineRequested){
                //the landmine is a bomb which will be placed in front of the player
                Position nextPosition = getDirection().nextPosition(getPosition()) ; // compute the position
                if (canLandmine(nextPosition)){ //searching if it possible to put a landmine
                    game.addLandmine(nextPosition, range) ;
                    landmines-- ;
                    nbLandminesPut++ ;
                }
                landmineRequested = false ;
            }
            if (scarecrowRequested){
                if(canScarecrow(getPosition())){
                    game.addScarecrow(getPosition());
                    hasScarecrow = false ;
                    nbScarecrowPut++ ;
                }
                scarecrowRequested = false ;
            }
            if (bombRequested){
                if (canBomb(getPosition())){
                    game.addBomb(getPosition(), range, now) ;
                    bombs-- ;
                    nbBombsPut++ ;
                }
                bombRequested = false ;
            }
        }
        else {
            // the boolean bombRequested manage all the request :
            // if we can put a scarecrow, we put it. Else, if we have a landmine we have to put them
            //if we haven't landmine, we put a bomb
            if (bombRequested){
                if(canScarecrow(getPosition())){
                    game.addScarecrow(getPosition());
                    hasScarecrow = false ;
                    nbScarecrowPut++ ;
                }
                else {
                    if (canLandmine(getDirection().nextPosition(getPosition()))){ //searching if it possible to put a landmine
                        game.addLandmine(getDirection().nextPosition(getPosition()), range) ;
                        landmines-- ;
                        nbLandminesPut++ ;
                    }
                    else if (landmines == 0 && canBomb(getPosition())){ //we put a bomb just if there is just one bomb
                        game.addBomb(getPosition(), range, now) ;
                        bombs-- ;
                        nbBombsPut++ ;
                    }
                }
                bombRequested = false ;
            }
        }
    }
    /**
     * Used to notify the player object that we have requested a door opening
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
     * Used to notify the object player that we would like to move
     * @param direction the direction on which we move
     */
    public void requestMove(Direction direction) {
        if (direction != getDirection()) {
            setDirection(direction);
        }
        moveRequested = true;
    }
    /**
     * Used to notify the object player that we would like to put a scarecrow
     * Used just if the value of Constants.INPUTMODIFIED is true
     */
    public void requestScarecrow() {
        scarecrowRequested = true;
    }
    /**
     * Used to notify the object player that we would like to put a bomb
     */
    public void requestBomb(){
        bombRequested = true ;
    }
    /**
     * Used to notify the object player that we would like to put a landmine
     * Used just if the value of Constants.INPUTMODIFIED is true
     */
    public void requestLandmine(){
        landmineRequested = true ;
    }
    public void bombHasExplosed(){
        bombs++;
    }
    /**
     * Used to notify the player that something which is hostile has touched him
     * @param now the time that the touching occurs
     */
    public void damage(long now){
        if(!isInvincible){ // if the player is invincible, he cannot be damage
            lives-- ;
            isInvincible = true ; // a damage lead to one second of invincibility 
            timeInvincible = now ;
            nbTimesDamaged++ ;
        }
    }
    @Override
    public void explosion(long now){
        damage(now);
    }
    @Override
    public boolean hasToBeRemoved(){
        return false ; // a player is never removed of a game
    }
    /**
     * Useful to know if the player has a correct infection or not
     * COuld be used by the sprites to know the state of the player
     * @return if the player is currently infected
     */
    public boolean hasAnInfection(){
        return currentInfection != null ;
    }
    /**
     * Used to notify the player that the actual time has been changed.
     * Useful for managing the invincibility time
     * @param time
     */
    private void setCurrentTime(long time){
        currentTime = time ;
    }
    private long getCurrentTime(){
        return currentTime ;
    }
    private void useKey(){
        key -- ;
    }
}
