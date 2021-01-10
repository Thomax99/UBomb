/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;


import fr.ubx.poo.Constants;
import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.automovablepolicies.Automovable;
import fr.ubx.poo.game.Game;

public class Monster extends Character {
    private long lastMoveTime = 0, explosionTime = 0 ;
    private int speedMoving;
    private boolean isExplosing = false ;
    private Automovable automovingPolicy ;
    Direction direction;
    public Monster(Game game, Position position) {
        super(game, position);
        setLevel(game.getLevel()) ;
        speedMoving = Automovable.getSpeed(getLevel()-1) ;
        this.automovingPolicy = Automovable.getRandomPolicy(this, game) ;
    }

    /**
     * Update the movement display of the monster
     * @param now the given time
     */
    @Override
    public void update(long now) {
        if (isExplosing && (now-explosionTime)/Constants.secondInnanoSec >= 1){
            //the state isExplosing is finished
            isExplosing = false ;
            //we remove the monster
            remove();
        }
        if((now-lastMoveTime) /Constants.secondInnanoSec >= speedMoving && !hasToBeRemoved() && !isExplosing){
            //we move just if it is time and if the monster is not explosing or removed
            lastMoveTime = now ;
            //time to move
            Direction d = automovingPolicy.computeMove() ;
            if (d != null){ //sometimes it's impossible to do a move
                setDirection(d);
                doMove(d);
                if (game.getPlayer().getPosition().equals(getPosition())){
                    //the player and the monster are at the same position : the player is going to be damage
                    game.getPlayer().damage(now);
                }
            }
        }
    }

    /**
     * Tell if the entity can move in a certain direction.
     * @param direction the direction in which the entity is going
     * @return Yes if the entity can move in this direction, else No
     */
    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        return game.positionAllowedToMonsters(nextPos, game.getLevel()) ;
    }
    /**
     * This function is used by the view to know the type of the monster
     * This type is in fact given by the automovable policy implemented
     * @return the type of the monster
     */
    public int getMonsterType(){
        return automovingPolicy.getType().ordinal() ;
    }
    @Override
    public void explosion(long now){
        isExplosing = true ;
        explosionTime = now ; //during one second, a monster is in an explosing effect, so it can't move, but if a player come in it, the player is damaged
    }
    public boolean isExplosing(){
        return isExplosing ;
    }
}
