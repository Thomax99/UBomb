/*
 * Copyright (c) 2020. Thomas Morin
 */

package fr.ubx.poo;

/**
 * Generalist class to store all the constants which are needed on the code
 */
public final class Constants {
    public static long secondInnanoSec = 1000000000L ; // storage of the number on nanoseconds in one second

    // values of fields in the config properties file

    //general properties
    public static String propertiesFileName = "config.properties", fieldLivesName = "lives",
                                 fieldBombsName = "bombs", fieldKeyName = "key", fieldRangeName = "range",
                                 fieldLandminesName = "landmines", fieldScarecrowName ="scarecrow", fieldPrefixName = "prefix",
                                 fieldSuffixName ="suffix" ;

    //random properties
    public static String emptyProbaName = "emptyProba", boxProbaName = "boxProba", monsterProbaName = "monsterProba", stoneProbaName ="stoneProba",
                                        treeProbaName = "treeProba", numberIncProbaName = "numberIncProba", numberDecProbaName = "numberDecProba",
                                        rangeIncProbaName = "rangeIncProba", rangeDecProbaName = "rangeDecProba", heartProbaName = "heartProba",
                                        landminerProbaname = "landminerProba", scarecrowProbaName = "scarecrowProba", fieldMinWidthValue = "minWidthValue",
                                        fieldMaxWidthValue = "maxWidthValue", fieldMinHeightValue = "minHeightValue", fieldMaxHeightValue = "maxHeightValue";

    // default values for the random generation
    public static int defaultMinWidthValue = 12, defaultMaxWidthValue = 30, defaultMinHeightValue = 10, defaultMaxHeightValue = 25 ;
    public static double defaultEmptyProba = 0.73, defaultBoxProba = 0.07, defaultMonsterProba = 0.02, defaultStoneProba = 0.07,
                        defaultTreeProba = 0.02, defaultNumberIncProba = 0.012, defaultNumberDecProba = 0.012, defaultRangeIncProba = 0.015,
                        defaultRangeDecProba = 0.02, defaultHeartProba = 0.01, defaultLandminerProba = 0.017, defaultScarecrowProba = 0.009 ;

    //default values for loading game from files
    public static int defaultInitPlayerLives = 3, defaultInitPlayerBombs = 3, defaultInitPlayerKey = 0, defaultInitPlayerRange = 1, defaultInitPlayerLandmines = 0 ;
    public static String defaultPrefixLoading = "level", defaultSuffixLoading = ".txt" ;
    public static boolean defaultInitScarecrow = false ;

}
