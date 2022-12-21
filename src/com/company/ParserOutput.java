package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParserOutput {
    private List<Component> tree;

    public ParserOutput(List<Component> tree) {
        this.tree = tree;
    }

    public void printTree() {
        String[] headers = {"Father", "Sibling", "Value", "Production"};
        List rows = new ArrayList<>();

        for (Component component : this.tree) {
            rows.add(Arrays.toString(new String[]{String.valueOf(component.getFather()), String.valueOf(component.getSibling()), component.getValue(), String.valueOf(component.getProduction())}));
        }

        for (Object row : rows) {
            System.out.format("%-10s%-10s%-10s%-10s\n", row);
        }

    }
}
