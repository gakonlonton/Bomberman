package uet.oop.bomberman.controller;

import javafx.animation.AnimationTimer;

public class Timer extends AnimationTimer {
    private boolean running = false;

    boolean isRunning() {
        return running;
    }

    @Override
    public void start() {
        super.start();
        running = true;
    }

    @Override
    public void stop() {
        super.stop();
        running = false;
    }

    @Override
    public void handle(long l) {

    }
}
