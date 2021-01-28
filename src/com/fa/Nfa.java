package com.fa;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Nfa {
    public static HashMap<String, State> states = new HashMap<>();
    private static boolean isAccepted = false;

    //region INITIAL STATES
    public static void initialStates(String start, List<Grammar> grammars) {
        for (Grammar grammar : grammars) {
            boolean isStart = start.equals(grammar.from);

            boolean isAccepted = isSingleState(grammar);
            if (states.containsKey(grammar.from)) {
                addUniqueFinalState(grammar, isStart, isAccepted);
                states.get(grammar.from).addTransition(grammar.from, grammar.with, grammar.to);
            } else {
                State state = new State(grammar.from, isAccepted, isStart);

                addUniqueFinalState(grammar, isStart, isAccepted);

                state.addTransition(grammar.from, grammar.with, grammar.to);
                states.put(grammar.from, state);
            }

            if (isAccepted) {
                State finalState = states.get(grammar.from);

                finalState.isFinal = true;

                Constant.FINAL_STATES.add(grammar.from);
            }
        }
    }

    private static void addUniqueFinalState(Grammar grammar, boolean isStart, boolean isAccepted) {
        if (isAccepted) {
            State uniqueState = new State(Constant.FINAL_STATE_NAME, true, isStart);

            states.put(Constant.FINAL_STATE_NAME, uniqueState);

            grammar.to = Constant.FINAL_STATE_NAME;

            Constant.FINAL_STATES.add(Constant.FINAL_STATE_NAME);
        }
    }
    //endregion

    //region IS SINGLE STATE
    private static boolean isSingleState(Grammar grammar) {
        return (grammar.rule.length() == 1 && Character.isLowerCase(grammar.rule.charAt(0))) || grammar.rule.equals(Constant.LAMBDA_RULE);
    }
    //endregion

    //region PRINT NFA TRANSACTIONS
    public static void printNfaTransactions(String start) {
        System.out.println("> Transactions: ");
        for (State state : Nfa.states.values()) {
            if (!state.name.equals(Constant.FINAL_STATE_NAME)) {
                String[] productions = state.toString().split("\\|");
                for (String production : productions) {
                    System.out.println("\t" + production);
                }
            }
        }

        System.out.println("> Final: ");
        System.out.println(String.format("\t%s %s final.", Arrays.toString(Constant.FINAL_STATES.toArray()), Constant.FINAL_STATES.size() == 1 ? "is" : "are")
                .replace("[", "{")
                .replace("]", "}"));

        System.out.println("> Initial: ");
        System.out.println(String.format("\t{%s} is initial.", Constant.STATE_NAMES.getOrDefault(start, start)));
    }
    //endregion

    //region ACCEPTOR
    public static void acceptor(String string, String start) {
        if (string == null || string.equals("")) {
            System.out.println("rejected");
            return;
        }

        isAccepted = false;
        State init = states.get(Constant.STATE_NAMES.getOrDefault(start, start));
        acceptor(string, init);

        System.out.println(isAccepted ? "accepted" : "rejected");
    }

    public static void acceptor(String string, State current) {
        if (string.equals("")) {
            if (current.isFinal) {
                isAccepted = true;
            }
            return;
        } else {
            State state = current;

            char character = string.charAt(0);

            if (state.transitions.containsKey(state.name)) {
                List<Transition> transitions = state.transitions.get(state.name);

                for (Transition transaction : transitions) {
                    if (character == Constant.LAMBDA || transaction.with == character) {
                        State next = states.get(transaction.to);
                        acceptor(string.substring(1), next);
                    }
                }
            }
        }
    }
    //endregion
}
