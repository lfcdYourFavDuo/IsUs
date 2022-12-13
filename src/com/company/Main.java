package com.company;

import java.util.Scanner;
import java.util.Stack;

public class Main {

    public static void main(String[] args) {
        System.out.println("ALLES GUTE");
        Grammar grammar = new Grammar();
        Tests tests= new Tests(grammar);
        tests.run();

    }
}
