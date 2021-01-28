package com.fa;

public class Transition {
    String from;
    String to;
    char with;

    Transition(String from, char with, String to) {
        this.from = from;
        this.with = with;
        this.to = to;
    }

    @Override
    public String toString() {
        return String.format("(%s,%s)->%s", this.from, this.with, this.to);
    }
}
