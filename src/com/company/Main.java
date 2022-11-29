package com.company;

import java.util.Scanner;

public class Main {

    public static void menuGrammar() {
        System.out.println();
        System.out.println("1 - Non-terminals");
        System.out.println("2 - Terminals");
        System.out.println("3 - Productions");
        System.out.println("4 - Productions of a non-terminal");
        System.out.println("5 - Exit");
    }

    public static String promptForNonTerminal() {
        Scanner inScanner = new Scanner(System.in);
        return inScanner.next().trim();
    }

    public static void main(String[] args) {
        menuGrammar();
        Grammar grammar = new Grammar();
        boolean done = true;
        while (done){
            System.out.println("Your option: ");
            Scanner inScanner = new Scanner(System.in);
            int option = Integer.parseInt(inScanner.next().trim());

            switch (option) {
                case 1:
                    System.out.println(grammar.getNonTerminals());
                    System.out.println();
                    break;
                case 2:
                    System.out.println(grammar.getTerminals());
                    System.out.println();
                    break;
                case 3:
                    System.out.println("P: {");
                    for (Production production: grammar.getProductions())
                        System.out.println("    " + production);
                    System.out.println("}");
                    System.out.println();
                    break;
                case 4:
                    System.out.println(grammar.getProductionsForNonterminal(promptForNonTerminal()));
                    System.out.println();
                    break;
                case 5:
                    System.out.println("Ciao!!!");
                    System.out.println();
                    done = false;
                    break;
            }
        }

    }
}
