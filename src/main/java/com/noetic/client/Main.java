package com.noetic.client;

public class Main {
    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%4$s: %5$s [%1$tc]%n");

        UOEngine engine = new UOEngine();
        engine.run();
    }
}
