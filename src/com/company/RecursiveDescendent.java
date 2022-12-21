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
    private List<Component> tree;

    public RecursiveDescendent(Grammar grammar, Stack<String> workingStack) {
        this.grammar = grammar;
        this.workingStack = workingStack;
        this.inputStack.add(this.grammar.getStartingSymbol());
        this.state = "q";
        this.index = 1;
        this.tree = new ArrayList<>();
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

    public List<Component> getTree() {
        return tree;
    }

    public void setTree(List<Component> tree) {
        this.tree = tree;
    }

    public void expand() {
//        1. pop A from the input stack beta
//        2. Get the first production of A
//        3. add A 1 to the working stack alpha
//        4. Add the corresponding production to the input stack
        String nonTerminal = this.inputStack.remove(0);
        Production production = this.grammar.getProductionsForNonterminal(nonTerminal).get(0);
        this.workingStack.push(nonTerminal + " 1");
        this.inputStack.addAll(0, production.getRules().get(1));
    }

    public void advance() {
        String nonterminal = this.inputStack.remove(0);
        this.workingStack.add(nonterminal);
        this.index += 1;
    }

    public void momentaryInsuccess() {
        this.state = "b"; // first step
    }

    public void back() {
        String last = this.workingStack.pop(); //step1
        this.inputStack.add(0, last); //step2
        this.index -= 1;
    }

    public void anotherTry() {
        String last = this.workingStack.pop(); // first step
        String[] lastParts = last.split(" ");
        int productionNumber = Integer.parseInt(lastParts[1]);
        Production terminalProduction = this.grammar.getProductionsForNonterminal(lastParts[0]).get(0);
        if (terminalProduction.hasNextRule(productionNumber)) {
            this.state = "q";
            this.workingStack.push(lastParts[0] + " " + (productionNumber + 1));
            Integer lastLength = terminalProduction.getRules().get(productionNumber).size();
            this.inputStack = this.inputStack.subList(lastLength, inputStack.size());
            this.inputStack.addAll(0, terminalProduction.getRules().get(productionNumber + 1));
        } else if (this.index == 1 && lastParts[0].equals(this.grammar.getStartingSymbol())) {
            this.state = "e";
        } else {
            Integer lastLength = terminalProduction.getRules().get(productionNumber).size();
            this.inputStack = this.inputStack.subList(lastLength, inputStack.size());
            this.inputStack.add(lastParts[0]);
        }


    }

    public void success() {
        this.state = "f";// first step
    }

    @Override
    public String toString() {
        return "RecursiveDescendent{" +
                "workingStack=" + workingStack +
                ", inputStack=" + inputStack +
                ", state='" + state + '\'' +
                ", index=" + index +
                '}';
    }

    public void parsingStrategy(String seq) {
        // Parse a sequence using descendent recursive parsing
        String[] seqSplit = seq.split(" ");
        while (!this.state.equals("f") && !this.state.equals("e")) {
            System.out.println(this);
            if (this.state.equals("q")) {
                if (this.index == seq.length() && this.inputStack.isEmpty()) {
                    this.success();
                }
                // empty input stack and end of the sequence not reached => momentary insuccess
                else if (this.inputStack.isEmpty()) {
                    this.momentaryInsuccess();
                }
                // else if head(input stack) is a non-terminal => expand
                else if (this.grammar.getNonTerminals().contains(this.inputStack.get(0))) {
                    this.expand();
                }
                // else if head of the input stack = current element in the sequence => advance
                else if (this.index < seq.length() && this.inputStack.get(0).equals(seqSplit[this.index])) {
                    this.advance();
                } else {
                    this.momentaryInsuccess();
                }
                // if i = n+1 and input stack is empty => success
            } else if (this.state.equals("b")) {
                // if head(working stack) == a - terminal
                if (this.grammar.getTerminals().contains(this.workingStack.get(this.workingStack.size() - 1))) {
                    this.back();
                } else {
                    this.anotherTry();
                }
            }
        }
        if (this.state.equals("e")) {
            System.out.println("Error at index " + this.index + "!");
        } else {
            System.out.println("Sequence " + seq + " is accepted!");
            System.out.println(this.workingStack);
        }
        this.createParsingTree();
    }


    public void createParsingTree() {
        int father = -1;

        // For every elem in working stack
        for (int index = 0; index < this.workingStack.size(); index++) {
            // If elem is a tuple -> (non-terminal, production number)
            String[] elem = this.workingStack.get(index).split(" ");
            if (elem.length == 2) {
                // Add new entry in table and set the value along with production number used
                this.tree.add(new Component(elem[0]));  // value
                this.tree.get(index).setProduction(Integer.parseInt(elem[1]));
            } else {
                // Else elem is a terminal -> add new entry in table and set the value
                this.tree.add(new Component(elem[0]));
            }
        }

        // Update father - sibling relationship
        // For every elem in working stack
        for (int index = 0; index < this.workingStack.size(); index++) {
            // If elem is a tuple -> (non-terminal, production number)
            String[] elem = this.workingStack.get(index).split(" ");
            if (elem.length == 2) {
                // Set the father and update it
                if (this.tree.get(index).getFather() == -1) {
                    this.tree.get(index).setFather(father);
                }
                father = index;

                // Get the length of the used production
                int prodLen = this.grammar.getProductionsForNonterminal(elem[0]).size();
                List<Integer> indexList = new ArrayList<>();

                // Store the indexes in a list
                for (int i = 1; i < prodLen + 1; i++) {
                    indexList.add(index + i);
                }
                ///HERE
                for (int i = 0; i < prodLen; i++) {
                    // If in the tree at position indexList[i] is a non-terminal
                    if (this.tree.get(indexList.get(i)).getProduction() != -1) {
                        // Compute the offset to update the indexes
                        int offset = getLengthDepth(indexList.get(i));

                        // For every index remained in indexList add the offset to get the right index position
                        for (int j = i + 1; j < prodLen; j++) {
                            indexList.set(j, offset);
                            if (indexList.get(j) == this.tree.size()) {
                                indexList.set(j, 1);
                            }
                        }
                    }

                    // Update the left sibling relation
                    // For every index in indexList, except last because it will not be a left sibling to anyone
                    for (i = 0; i < prodLen - 1; i++) {
                        // Now that we computed the right indexes we can surely say that the item in the tree at position
                        // indexList[i] will be a left sibling to item at pos indexList[i+1]
                        this.tree.get(indexList.get(i)).setSibling(indexList.get(i + 1));
                        if (this.tree.get(indexList.get(i)).getFather() == -1) {
                            this.tree.get(indexList.get(i)).setFather(father);
                        }
                        if (i == prodLen - 2 && this.tree.get(indexList.get(i + 1)).getFather() == -1) {
                            this.tree.get(indexList.get(i + 1)).setFather(father);
                        }
                    }
                }


            }
            else {
                if (this.tree.get(index).getFather() == -1) {
                    this.tree.get(index).setFather(father);
                }
                father = -1;
            }
        }
    }

    public int getLengthDepth(int index) {
        // Get the length of the used production
        String[] elem = this.workingStack.get(index).split(" ");
        int prodLen = this.grammar.getProductionsForNonterminal(elem[0]).size();
        int sum = prodLen;
        for (int i = 1; i <= prodLen; i++) {
            String[] elem1 = this.workingStack.get(index).split(" ");
            if (elem1.length == 2) {
                sum += getLengthDepth(index + i);
            }
        }
        return sum;
    }

}
