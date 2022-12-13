package com.company;

import java.util.ArrayList;

public class RecursiveDescendent {
    private Grammar grammar;
    private ArrayList<String> workingStack;
    private ArrayList<String> inputStack = new ArrayList<>();
    private String state;
    private int index;

    public RecursiveDescendent(Grammar grammar, ArrayList<String> workingStack) {
        this.grammar = grammar;
        this.workingStack = workingStack;
        this.inputStack.add(this.grammar.getStartingSymbol());
        this.state = "q";
        this.index = 1;
    }

    public Grammar getGrammar() {
        return grammar;
    }

    public void setGrammar(Grammar grammar) {
        this.grammar = grammar;
    }

    public ArrayList<String> getWorkingStack() {
        return workingStack;
    }

    public void setWorkingStack(ArrayList<String> workingStack) {
        this.workingStack = workingStack;
    }

    public ArrayList<String> getInputStack() {
        return inputStack;
    }

    public void setInputStack(ArrayList<String> inputStack) {
        this.inputStack = inputStack;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void expand(){
//        1. pop A from the input stack beta
//        2. add A1 to the working stack alpha
//        3. Get the first production of A
//        4. Add the corresponding production to the input stack
        String nonTerminal = this.inputStack.get(this.inputStack.size() - 1);
        this.workingStack.add(nonTerminal);
        Production production = this.grammar.getProductionsForNonterminal(nonTerminal).get(0);
        this.inputStack.add(production.getStart());
    }
}