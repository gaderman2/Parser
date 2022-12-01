package net.htlgkr.parser;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Parser {

    private boolean parallel;

    private Stack<String> tagStack;
    private Stack<String> textStack;

    private boolean readingTag;
    private boolean endTag;
    private String text;
    private boolean lineError;

    public Parser(boolean parallel){
        this.parallel = parallel;

        tagStack = new Stack<>();
        textStack = new Stack<>();
        readingTag = false;
        endTag = false;
        lineError = false;
        text = "";
    }

    public void readFile(String name){
        if(!parallel) readSequentiell(name);

        else readParallel(name);
    }

    private void readSequentiell(String name){
        try {
            Files.lines(new File(name).toPath()).forEach(line -> {
                readLine(line);
            });
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            tagStack = new Stack<>();
            textStack = new Stack<>();
            readingTag = false;
            endTag = false;
            text = "";
        }
    }

    private void readParallel(String name){
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        try {
            Files.lines(new File(name).toPath()).forEach(line -> {
                executor.execute(new ParserRunnable(line));
            });
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            tagStack = new Stack<>();
            textStack = new Stack<>();
            readingTag = false;
            endTag = false;
            text = "";
        }
    }

    public void readLine(String line){
        line.chars().mapToObj(x -> (char) x).forEach(c -> {
            if(lineError) return;

            if(c == '<'){
                textStack.push(text);
                text = "";
                readingTag = true;
                return;
            }

            if(c == '/' && readingTag){
                endTag = true;
                return;
            }

            if(c == '>' && !endTag){
                tagStack.push(text);
                //System.out.println("Tag added: " + text);
                text = "";
                readingTag = false;
                return;
            }

            if(c == '>' && endTag) {
                try {
                    String beginTag = tagStack.pop();

                    if (!beginTag.equals(text)){
                        System.err.println(text + " was closed before " + beginTag + " -> " + line);
                        lineError = true;
                        return;
                    }

                    printText(beginTag);
                }catch(EmptyStackException e) {
                    System.err.println(text + " without BeginTag! -> " + line);
                    lineError = true;
                }finally {
                    text = "";
                    readingTag = false;
                    endTag = false;
                }
                return;
            }

            text = text + c;
        });
        if(!tagStack.isEmpty()) System.err.println(tagStack.pop() + " has no EndTag! -> " + line);

        lineError = false;
    }

    private void printText(String tag){
        String line = textStack.pop();
        System.out.println(!line.equals("") ?  "<" + tag + ">" + line + "</" + tag + ">" : tag + " kein Inhalt");
    }

}
