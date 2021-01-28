package com.fa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Constant {
    public static List<String> COMPLIMENT_ALPHABETS = new ArrayList<>();
    public static HashMap<String, String> STATE_NAMES = new HashMap<>();
    public static HashSet<String> FINAL_STATES = new HashSet<>();

    public static String FINAL_STATE_NAME = "Qf";
    public static String BASE_STATE_NAME = "Q";
    public static char LAMBDA = '?';
    public static String LAMBDA_RULE = "?";

    Constant() {
        COMPLIMENT_ALPHABETS.add("L");
        COMPLIMENT_ALPHABETS.add("M");
        COMPLIMENT_ALPHABETS.add("N");
        COMPLIMENT_ALPHABETS.add("O");
        COMPLIMENT_ALPHABETS.add("P");
        COMPLIMENT_ALPHABETS.add("Q");
        COMPLIMENT_ALPHABETS.add("R");
        COMPLIMENT_ALPHABETS.add("S");
        COMPLIMENT_ALPHABETS.add("T");
        COMPLIMENT_ALPHABETS.add("U");
        COMPLIMENT_ALPHABETS.add("V");
        COMPLIMENT_ALPHABETS.add("W");
        COMPLIMENT_ALPHABETS.add("X");
        COMPLIMENT_ALPHABETS.add("Y");
        COMPLIMENT_ALPHABETS.add("Z");
    }
}
