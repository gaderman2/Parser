package net.htlgkr;

import net.htlgkr.parser.Parser;

public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser(true);

        System.out.println("---simple1.txt---");
        parser.readFile("./tag_files/simple1.txt");
        System.out.println();
        System.out.println("---sample1.html--");
        parser.readFile("./tag_files/sample1.html");
        System.out.println();
        System.out.println("---sample2.html--");
        parser.readFile("./tag_files/sample2.html");
        System.out.println();
        System.out.println("---sample3.html--");
        parser.readFile("./tag_files/sample3.html");
        System.out.println();
        System.out.println("---nothing.html--");
        parser.readFile("./tag_files/nothing.html");
        System.out.println();
    }
}