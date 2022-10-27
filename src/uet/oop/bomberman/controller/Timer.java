package uet.oop.bomberman.controller;

import javafx.animation.AnimationTimer;
import java.util.concurrent.TimeUnit;

public class Timer {
    private static final int FPS = 30;
    private static final long PER_FRAME = 1000000000 / FPS;

    public static final long INPUT_TIME = PER_FRAME * 5;


    private AnimationTimer timer;
    private long last;
    private GameMaster gameMaster;

    public Timer(GameMaster gameMaster) {
        this.gameMaster = gameMaster;
        last = System.nanoTime();
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
            gameMaster.update();
            gameMaster.render();
            try {
                TimeUnit.NANOSECONDS.sleep(delay());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            }
        };
        timer.start();
    }

    public long delay() {
        long end = System.nanoTime();
        long delay = end - last;
        last = end;
        if (delay < PER_FRAME) {
            return PER_FRAME - delay;
        }
        return 0;
    }

    public static long now() {
        return System.nanoTime();
    }
}
