package com.company;

import java.util.List;

public class Production {
    private final String start;
    private final List<List<String>> rules;

    Production(String start, List<List<String>> rules) {
        this.start = start;
        this.rules = rules;
    }

    List<List<String>> getRules() {
        return this.rules;
    }

    String getStart() {
        return this.start;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.start + " -> ");

        for (List<String> strings : this.rules) {

            for (String o : strings) {
                sb.append(o).append(" ");
            }

            sb.append("| ");
        }

        sb.replace(sb.length() - 3, sb.length() - 1, "");
        return sb.toString();
    }
}
