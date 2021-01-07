/*
 * Copyright (c) 2020. Thomas Morin
*/
package fr.ubx.poo.engine;

import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.util.Collections;
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
}
