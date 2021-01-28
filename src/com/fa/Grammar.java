package com.fa;

import javax.xml.crypto.dsig.TransformService;
import java.util.HashMap;
import java.util.HashSet;

public class Grammar {
    String from;
    String to;
    char with;
    String rule;

    Grammar(String from, char with, String to) {
        this.from = from;
        this.with = with;
        this.to = to;
        this.rule = (with + to).replace(" ", "");
    }

    public void addGrammar(String from, char with, String to) {
        this.from = from;
        this.with = with;
        this.to = to;
    }

    @Override
    public String toString() {
        return String.format("%s->%s%s", this.from, this.with, this.to);
    }
}
