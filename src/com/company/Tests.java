package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Tests {
    private Grammar grammar;

    public Tests(Grammar grammar) {
        this.grammar = grammar;
    }
    public void testMomentaryInsuccess(){
        RecursiveDescendent parser = new RecursiveDescendent(grammar, new Stack<>());
        parser.momentaryInsuccess();
        assert parser.getState().equals("b");
    }

    public void testAdvance(){
        RecursiveDescendent parser = new RecursiveDescendent(grammar, new Stack<>());
        parser.setInputStack(new ArrayList<>(List.of("a", "A")));
        parser.advance();
        assert parser.getIndex() == 2;
        assert parser.getInputStack().equals(List.of("A"));
        assert parser.getWorkingStack().size() == 1;
        assert parser.getWorkingStack().peek().equals("a");
    }

    public void testExpand(){
        RecursiveDescendent parser = new RecursiveDescendent(grammar, new Stack<>());
        parser.setInputStack(new ArrayList<>(List.of("S")));
        parser.setWorkingStack(new Stack<>());
        parser.setIndex(1);
        parser.advance();
        assert parser.getIndex()==2;
        assert parser.getInputStack().equals("A");
        assert parser.getWorkingStack().equals("a");

    }

    public void testSuccess(){
        RecursiveDescendent parser = new RecursiveDescendent(grammar, new Stack<>());
        parser.success();
        assert parser.getState().equals("f");
    }
    public void testBack(){
        RecursiveDescendent parser = new RecursiveDescendent(grammar, new Stack<>());
        parser.setState("b");
        parser.setIndex(2);
        Stack<String> workingStack = new Stack<>();
        workingStack.push("S 1");
        workingStack.push("a");
        parser.setWorkingStack(workingStack);
        assert parser.getWorkingStack().size()==1;
    }
    public void testAnotherTry(){
        RecursiveDescendent parser = new RecursiveDescendent(grammar, new Stack<>());
        parser.setState("b");
        parser.setIndex(2);
        Stack<String> workingStack = new Stack<>();
        workingStack.push("S 1");
        workingStack.push("a");
        workingStack.push("A 1");
        parser.setWorkingStack(workingStack);
        parser.setInputStack(new ArrayList<>(List.of("a", "A")));
        parser.anotherTry();
        assert parser.getWorkingStack().size()==3;
        assert parser.getWorkingStack().peek().equals("A 2");
        assert parser.getInputStack().get(0).equals("b");

        System.out.println(workingStack);
        parser.anotherTry();
        parser.anotherTry();
        parser.anotherTry();

        assert parser.getWorkingStack().size()==2;
        assert parser.getInputStack().size()==1;
    }



    public void run(){
        this.testMomentaryInsuccess();
        this.testAdvance();
        this.testExpand();
        this.testSuccess();
        this.testBack();
        this.testAnotherTry();
    }
}
