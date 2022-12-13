package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class RecursiveDescendent {
    private Grammar grammar;
    private Stack<String> workingStack; // terminal or productionrule "S 1"
    private List<String> inputStack = new ArrayList<>();
    private String state;
    private int index;

    public RecursiveDescendent(Grammar grammar, Stack<String> workingStack) {
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

    public Stack<String> getWorkingStack() {
        return workingStack;
    }

    public void setWorkingStack(Stack<String> workingStack) {
        this.workingStack = workingStack;
    }

    public List<String> getInputStack() {
        return inputStack;
    }

    public void setInputStack(List<String> inputStack) {
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
//        2. Get the first production of A
//        3. add A 1 to the working stack alpha
//        4. Add the corresponding production to the input stack
        String nonTerminal = this.inputStack.remove(0);
        Production production = this.grammar.getProductionsForNonterminal(nonTerminal).get(0);
        this.workingStack.push(nonTerminal + " 1");
        this.inputStack.addAll(0, production.getRules().get(1));
    }

    public void advance(){
        String nonterminal=this.inputStack.remove(0);
        this.workingStack.add(nonterminal);
        this.index+=1;
    }

    public void momentaryInsuccess(){
        this.state="b"; // first step
    }

    public void back(){
        String last= this.workingStack.pop(); //step1
        this.inputStack.add(0,last); //step2
        this.index-=1;
    }

    public void anotherTry(){
        String last = this.workingStack.pop(); // first step
        String[] lastParts = last.split(" ");
        int productionNumber=Integer.parseInt(lastParts[1]);
        Production terminalProduction = this.grammar.getProductionsForNonterminal(lastParts[0]).get(0);
        if( terminalProduction.hasNextRule(productionNumber)) {
            this.state="q";
            this.workingStack.push(lastParts[0]+" "+ (productionNumber + 1));
            Integer lastLength= terminalProduction.getRules().get(productionNumber).size();
            this.inputStack = this.inputStack.subList(lastLength, inputStack.size());
            this.inputStack.addAll(0, terminalProduction.getRules().get(productionNumber+1));
        }
        else if(this.index==1 && lastParts[0].equals(this.grammar.getStartingSymbol())){
            this.state="e";
        }
        else {
            Integer lastLength= terminalProduction.getRules().get(productionNumber).size();
            this.inputStack = this.inputStack.subList(lastLength, inputStack.size());
            this.inputStack.add(lastParts[0]);
        }


    }

    public void success(){
        this.state="f";// first step
    }
}