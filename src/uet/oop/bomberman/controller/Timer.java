package uet.oop.bomberman.controller;

import javafx.animation.AnimationTimer;
import uet.oop.bomberman.BombermanGame;

public class Timer {
    private static final int FPS = 30;
    private static final long TIME_PER_FRAME = 1000000000 / FPS;
    public static final long TIME_FOR_DELAY_BOMB = 200000000L;
    public static final long TIME_FOR_SINGLE_INPUT = TIME_PER_FRAME * 5;
    public static final long TIME_FOR_BOMB_EXPLODE = 2000000000;
    private long lastTime;
    private BombermanGame game;
    private AnimationTimer timer;

    public Timer(BombermanGame game) {
        this.game = game;
        lastTime = System.nanoTime();
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                game.gameLoop();
            }
        };
        timer.start();
    }

    public static long now() {
        return System.nanoTime();
    }

    public long delay() {
        long endTime = System.nanoTime();
        long delayTime = endTime - lastTime;
        lastTime = endTime;
        if (delayTime < TIME_PER_FRAME) {
            return TIME_PER_FRAME - delayTime;
        }
        else return 0;
    }
}
