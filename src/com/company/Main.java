package com.company;

import java.util.Scanner;
import java.util.Stack;

public class Main {

    public static void main(String[] args) {
        System.out.println("ALLES GUTE");
        Grammar grammar = new Grammar();
        RecursiveDescendent parser = new RecursiveDescendent(grammar, new Stack<>());
        parser.parsingStrategy("S A");
        //Tests tests= new Tests(grammar);
        //tests.run();
        if (parser.getState().equals("f")){
            ParserOutput out = new ParserOutput(parser.getTree());
            out.printTree();
        }


    }
}
