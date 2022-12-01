package net.htlgkr.parser;

public class ParserRunnable implements Runnable{

    private String line;

    public ParserRunnable(String line) {
        this.line = line;
    }

    @Override
    public void run() {
        Parser parser = new Parser(false);
        parser.readLine(line);
    }
}
