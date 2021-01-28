package com.fa;

import java.util.ArrayList;
import java.util.List;

public class GrammarToNfa {

    //region STANDARD STATES
    public static void standardization(List<Grammar> grammars) {
        for (int i = 0; i < grammars.size(); i++) {
            Grammar grammar = grammars.get(i);

            grammar.from = Constant.STATE_NAMES.getOrDefault(grammar.from, grammar.from);
            grammar.to = Constant.STATE_NAMES.getOrDefault(grammar.to, grammar.to);
        }
    }
    //endregion

    //region IMPORT VARIABLE TO VARIABLE PRODUCTIONS
    public static void importVariableProductions(List<Grammar> grammars) {
        List<Grammar> addition = new ArrayList<>();
        for (int i = 0; i < grammars.size(); i++) {
            Grammar grammar = grammars.get(i);

            String from = grammar.from;
            char with = grammar.with;

            if (Character.isUpperCase(with)) {
                for (Grammar rule : grammars) {
                    if (rule.from.equals(with + "")) {
                        String production = rule.rule;
                        addition.add(GrammarToNfa.makeGrammar(from, production));
                    }
                }
                grammars.remove(i);
            }
        }

        grammars.addAll(addition);
    }
    //endregion

    //region ADD MIDDLE STATES
    public static void addMiddleStates(List<Grammar> grammars) {
        for (int i = 0; i < grammars.size(); i++) {
            Grammar grammar = grammars.get(i);

            boolean needMiddleState = needMiddleState(grammar);
            if (needMiddleState) {
                String variable = Constant.COMPLIMENT_ALPHABETS.get(0);
                Constant.COMPLIMENT_ALPHABETS.remove(0);

                Constant.STATE_NAMES.put(variable, Constant.BASE_STATE_NAME + Constant.STATE_NAMES.size());

                String production = grammar.with + variable;

                grammars.add(GrammarToNfa.makeGrammar(variable, grammar.to));
                grammars.set(i, GrammarToNfa.makeGrammar(grammar.from, production));
            }
        }
    }

    //region NEED MIDDLE STATE
    private static boolean needMiddleState(Grammar grammar) {
        if (grammar.rule.length() == 1)
            return false;

        char first = grammar.rule.charAt(0);
        char second = grammar.rule.charAt(1);

        return Character.isLowerCase(first) && Character.isLowerCase(second);
    }
    //endregion
    //endregion

    //region RIGHT OR LEFT LINEAR GRAMMAR
    public static void changeGrammarTypeToRightLinear(String start, List<Grammar> grammars) {
        String rightOrLeft = determineGrammarType(grammars);

        if (rightOrLeft.equals("Left")) {
            makeLeftLinearToRightLinearGrammar(start, grammars);
        }
    }
    //endregion

    //region CONVERT LEFT LINEAR INTO RIGHT LINEAR GRAMMAR

    private static void makeLeftLinearToRightLinearGrammar(String start, List<Grammar> grammars) {
        for (int i = 0; i < grammars.size(); i++) {
            Grammar grammar = grammars.get(i);
            grammar = convertFromFinalStateToInitialState(start, grammar);
            grammar = convertInitialStateToInitialState(start, grammar);
            grammar = convertFromInitialStateToFinalState(start, grammar);
            grammar = convertProductionToProduction(start, grammar);

            grammars.set(i, grammar);
        }
    }

    //A->p : S->pA
    private static Grammar convertFromFinalStateToInitialState(String start, Grammar grammar) {
        if (grammar.rule.length() == 1 &&
                Character.isLowerCase(grammar.rule.charAt(0)) && !grammar.from.equals(start)) {

            String production = "";
            if (grammar.rule.equals(Constant.LAMBDA)) {
                production = grammar.from;
            } else {
                production = grammar.rule + grammar.from;
            }
            grammar = makeGrammar(start, production);
        }

        return grammar;
    }

    //S->Ap : A->p
    private static Grammar convertFromInitialStateToFinalState(String start, Grammar grammar) {
        if (grammar.rule.length() > 1 &&
                Character.isUpperCase(grammar.rule.charAt(0)) && grammar.from.equals(start)) {
            String production = grammar.to;
            grammar = makeGrammar(grammar.with + "", production);
        }

        return grammar;
    }

    //B->Ap : A->pB
    private static Grammar convertProductionToProduction(String start, Grammar grammar) {
        if (grammar.rule.length() > 1 &&
                Character.isUpperCase(grammar.rule.charAt(0)) && !grammar.from.equals(start)) {
            String production = grammar.to + grammar.from, from = grammar.with + "";
            grammar = makeGrammar(from, production);
        }

        return grammar;
    }

    //S->p : S->p | A->B : B->A
    private static Grammar convertInitialStateToInitialState(String start, Grammar grammar) {
        if (grammar.rule.length() == 1 &&
                Character.isUpperCase(grammar.rule.charAt(0)) && !grammar.from.equals(start)) {
            grammar = makeGrammar(grammar.rule, grammar.from);
        }

        return grammar;
    }

    //endregion

    //region DETERMINE THIS GRAMMAR TYPE : LEFT, RIGHT OR NON OF THEM
    private static String determineGrammarType(List<Grammar> grammars) {
        String rightOrLeft = isRightLinearOrNot(grammars);

        if (rightOrLeft.equals("Error")) {
            System.out.println("This grammar is not right or left linear.\nPlease Enter new right or left linear grammar.");
            System.exit(0);
        }

        return rightOrLeft;
    }
    //endregion

    //region PROCESS TO CHECK GRAMMAR IS RIGHT OR LEFT LINEAR OR NON OF THEM
    private static String isRightLinearOrNot(List<Grammar> grammars) {
        boolean[] checkPoints = new boolean[grammars.size()];

        for (int i = 0; i < grammars.size(); i++) {
            Grammar grammar = grammars.get(i);

            if (grammar.rule.length() == 1) {
                checkPoints[i] = i != 0 && checkPoints[i - 1];
            } else if (grammar.rule.length() > 1) {
                checkPoints[i] = Character.isUpperCase(grammar.rule.charAt(0));
            } else {
                checkPoints[i] = true;
            }
        }

        boolean allTrue = areAllTrue(checkPoints);
        boolean allFalse = areAllFalse(checkPoints);

        if (!allTrue && !allFalse)
            return "Error";
        else if (allTrue)
            return "Left";
        else
            return "Right";
    }

    private static boolean areAllTrue(boolean[] array) {
        for (boolean element : array) if (!element) return false;
        return true;
    }

    private static boolean areAllFalse(boolean[] array) {
        for (boolean element : array) if (element) return false;
        return true;
    }
    //endregion

    //region CREATE GRAMMAR
    public static Grammar makeGrammar(String from, String production) {
        char with = production.charAt(0);
        String to = production.substring(1);
        return new Grammar(from, with, to);
    }
    //endregion
}
