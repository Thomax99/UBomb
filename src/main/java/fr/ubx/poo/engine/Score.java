/*
 * Copyright (c) 2020. Thomas Morin
*/
package fr.ubx.poo.engine;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.List;

import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.Constants;
import fr.ubx.poo.game.Game;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class is representing a score
 */
public final class Score implements Comparable<Score> {
    private String name ;
    private int score ;
    public Score(String name, int score){
        this.name = name ;
        this.score = score ;
    }
    @Override
    public String toString() {
        return name+" : "+ score;
    }
    @Override
    public int compareTo(Score score) {
        return score.score - this.score;
    }
    public int getScore(){
        return score ;
    }
    public String getName(){
        return name ;
    }
    /**
     * Generate the list of scores which are stored at a given path on a given file
     * @param scorePath the given path
     * @param scoreFileName the given file
     * @return the good list
     */
    public static List<Score> getScoreFromFile(String scorePath, String scoreFileName){
        List<Score> back = new LinkedList<>() ;
        try (InputStream input = new FileInputStream(new File(scorePath, scoreFileName))){
            int c ; //the integer for reading the file
            String name ="" ;
            int score = 0 ;
            boolean isName = true ;
            while((c = input.read()) != -1){
                if ((char) c == '\n'){
                    // new line of the file score : adding a new score and reinitialize de values
                    //System.out.println("switch 1") ;
                    //System.out.println(name +" "+ score) ;
                    back.add(new Score(name, score)) ;
                    name = "" ; score = 0 ; isName = true ;
                }
                else if ((char) c == ':'){
                    //we have to change : the new chars are now the number
                    isName = false ;
                }
                else if(isName){
                    //the char is a part of the name
                    name+=((char)c) ;
                }
                else {
                    //the char is a part of the number
                    score = score*10 + Integer.parseInt( ((char) c)+"") ;
                }
            }
            if (!name.equals("")) //drain the buffer
                back.add(new Score(name, score)) ;

        } catch (IOException ex) {
            System.err.println("Error loading scores");
            throw new RuntimeException("Le fichier ne peut être lu") ;
        }
        return back ;
    }
    /**
     * @param scores a list of scores to ordonnate
     * @return an ordonnate String corresponding to the score in the list
     */
    public static String getStringScoreFromListScore(List<Score> scores){
        Collections.sort(scores) ;
        String back = "" ;
        for(Score score : scores){
            back+=(score+"\n") ;
        }
        return back ;
    }
    /**
     * (Re)initialize a file of score from the 10's best scores of a given list of scores
     * @param scores the given scores list
     * @param scorePath the path of the file
     * @param scoreFileName the file
     */
    public static void setFileScoreFromListScore(List<Score> scores, String scorePath, String scoreFileName){
        Collections.sort(scores) ; // sorting the list to put just the 10's bests
        try(OutputStream output = new FileOutputStream(new File(scorePath, scoreFileName))){
            int nbScoresToWrite = scores.size() > 10 ? 10 : scores.size(), nbWrite = 0 ; //we would like to write at the most the 10 best scores
            Iterator<Score> it = scores.iterator() ;
            while (it.hasNext() && nbWrite < nbScoresToWrite){
                Score score = it.next() ;
                //we would like to write the score the format is name:score
                output.write(score.getName().getBytes()) ;
                output.write(":".getBytes()) ;
                output.write((score.getScore()+"\n").getBytes()) ;
                nbWrite++ ;
            }
        } catch (IOException e){
            System.err.println("Error writing scores");
            throw new RuntimeException("Le fichier ne peut être écrit") ;
        }
    }
    /**
     * This function is used to compute a score when the game is over.
     * @param playerName the name of the player
     * @param player the player which was playing
     * @param game the game in which the player was playing
     * @return the score computed
     */
    public static Score computeScore(String playerName, final Player player, final Game game){
        //here the values of each thing : to be modified
        //here the recuperation of the scores
        int score = Constants.valueDecorComputed*player.getNbDecorComputed() + Constants.valueDamaged*player.getNbTimesDamaged() +
         Constants.valueBombPut*player.getNbBombsPut() + Constants.valueLandminePut*player.getNbLandminesPut() + Constants.valueScarecrowPut*player.getNbScarecrowPut() +
         Constants.valueBoxDestructed*game.getNbBoxDestructed() + Constants.valueMonsterKilled*game.getNbMonstersKilled() +
         Constants.valueLivesStaying*player.getLives() + Constants.valueLandminesStaying*player.getNbLandmines() + Constants.valueDecorDestructed*game.getNbDecorDestructed() +
         (player.isAlive() ? 1 : 0)*Constants.valueGameWon ;
        return new Score(playerName, score) ;
    }
}
