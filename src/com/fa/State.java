package com.fa;

import java.util.ArrayList;
import java.util.HashMap;

public class State {
    String name;
    boolean isFinal;
    boolean isStart;
    HashMap<String, ArrayList<Transition>> transitions;

    State(String name, boolean isFinal, boolean isStart) {
        this.name = name;
        this.isFinal = isFinal;
        this.isStart = isStart;
        this.transitions = new HashMap<>();
    }

    public void addTransition(String from, char with, String to) {
        if (!this.transitions.containsKey(from)) {
            if ((this.transitions.get(from)) == null ||
                    (this.transitions.get(from)).size() == 0) {
                ArrayList<Transition> transitions = new ArrayList<>();

                transitions.add(new Transition(from, with, to));

                this.transitions.put(from, transitions);
            }
        } else {
            ArrayList<Transition> transitions = this.transitions.get(from);

            transitions.add(new Transition(from, with, to));

            this.transitions.put(from, transitions);
        }
    }

    @Override
    public String toString() {
        boolean isNull = (this.transitions.get(this.name)) == null ||
                (this.transitions.get(this.name)).size() == 0;

        String result = "";

        if (isNull)
            return result;

        ArrayList<Transition> transitions = this.transitions.get(this.name);

        for (Transition transition : transitions) {
            result += String.format("%s|", transition);
        }

        return result;
    }
}
