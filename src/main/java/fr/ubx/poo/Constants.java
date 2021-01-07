/*
 * Copyright (c) 2020. Thomas Morin
 */

package fr.ubx.poo;

/**
 * Generalist class to store all the constants which are needed on the code
 */
public final class Constants {
    public static long secondInnanoSec = 1000000000L ; // storage of the number on nanoseconds in one second

    // default values for the random generation
    public static int defaultMinWidthValue = 12, defaultMaxWidthValue = 30, defaultMinHeightValue = 10, defaultMaxHeightValue = 25 ;
    public static double defaultEmptyProba = 0.73, defaultBoxProba = 0.07, defaultMonsterProba = 0.02, defaultStoneProba = 0.07,
                        defaultTreeProba = 0.02, defaultNumberIncProba = 0.012, defaultNumberDecProba = 0.012, defaultRangeIncProba = 0.015,
                        defaultRangeDecProba = 0.02, defaultHeartProba = 0.01, defaultLandminerProba = 0.017, defaultScarecrowProba = 0.009 ;
    public static String propertiesFileName = "config.properties" ;
}
