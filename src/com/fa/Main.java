package com.fa;

import java.util.*;

public class Main {

    //region STATIC
    public static Scanner scanner = new Scanner(System.in);
    public static Constant _ = new Constant();
    public static HashSet<String> alphabets = new HashSet<>();
    public static List<Grammar> grammars = new ArrayList<>();
    //endregion

    public static void main(String[] args) {
        //region START STATE
        System.out.println("Enter start state:\nNotice:\n\t* Enter this input in UPPER case");
        String start = scanner.next().toUpperCase();
        //endregion

        //region ALPHABETS
        System.out.println("Enter alphabets: \nNotice:\n\t* Separate them with ','\n\t* Enter this input in UPPER case");
        String next = scanner.next().toUpperCase();

        String[] points = next.replace(" ", "").split(",");

        alphabets = new HashSet<>(Arrays.asList(points));

        //endregion

        //region INITIAL STATE NAMES
        initialStateNames();
        //endregion

        //region GRAMMARS
        System.out.println("Enter grammar.\nRules:\n\t* To enter grammar use this rules: S->aX\nNotice: \n\t* To quit enter q or Q\n\t* This program just accepts right linear or left linear grammar.\n\t* Use '?' for lambda.");

        while (scanner.hasNextLine()) {
            String grammar = scanner.nextLine();
            if (grammar.equals(""))
                continue;
            if (grammar.toLowerCase().equals("q"))
                break;
            String[] productions = grammar.split("->");
            String from = productions[0];
            if (grammar.contains("|")) {
                productions = productions[1].split("\\|");
                for (String production : productions) {
                    grammars.add(GrammarToNfa.makeGrammar(from, production));
                }
            } else {
                grammars.add(GrammarToNfa.makeGrammar(from, productions[1]));
            }
        }
        //endregion

        //region CREATE STATES

        //region CONVERT LEFT LINEAR INTO RIGHT LINEAR GRAMMAR
        GrammarToNfa.changeGrammarTypeToRightLinear(start, grammars);
        //endregion

        //region ADD MIDDLE STATES
        GrammarToNfa.addMiddleStates(grammars);
        //endregion

        //region IMPORT VARIABLE TO VARIABLE PRODUCTIONS
        GrammarToNfa.importVariableProductions(grammars);
        //endregion

        //region STANDARD STATES
        GrammarToNfa.standardization(grammars);
        //endregion

        //region INITIAL STATES
        Nfa.initialStates(start, grammars);
        //endregion

        //endregion

        //region Transition function
        Nfa.printNfaTransactions(start);
        //endregion

        //region String Test
        boolean isContinue = true;
        while (isContinue) {
            System.out.println("> Enter the string: ");
            String string = scanner.next();
            Nfa.acceptor(string, start);

            System.out.println("Press 'c' to continue ...");
            string = scanner.next();

            isContinue = string.equals("c");
        }
        //endregion
    }

    //region PRIVATE
    private static void initialStateNames() {
        for (String alphabet : alphabets) {
            Constant.STATE_NAMES.put(alphabet, Constant.BASE_STATE_NAME + (Constant.STATE_NAMES.size()));
        }
    }
    //endregion
}