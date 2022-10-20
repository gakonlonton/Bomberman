package uet.oop.bomberman.scene;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import uet.oop.bomberman.controller.GameMaster;

public class InGameScene extends RootScene {
    private static final int WIDTH_PIXEL_STATUS_FRAME = WINDOW_HEIGHT_PIXEL;
    private static final int HEIGHT_PIXEL_STATUS_FRAME = 30;
    private static final int WIDTH_PIXEL_PLAYING_FRAME = WIDTH_PIXEL_STATUS_FRAME;
    private static final int HEIGHT_PIXEL_PLAYING_FRAME = WINDOW_HEIGHT_PIXEL - HEIGHT_PIXEL_STATUS_FRAME;
    Canvas statusFrame;
    Canvas playingFrame;
    GraphicsContext statusGc;
    GraphicsContext playingGc;

    public InGameScene() {
        statusFrame = new Canvas(WIDTH_PIXEL_STATUS_FRAME, HEIGHT_PIXEL_STATUS_FRAME);
        playingFrame = new Canvas(WIDTH_PIXEL_PLAYING_FRAME, HEIGHT_PIXEL_PLAYING_FRAME);
        playingGc = playingFrame.getGraphicsContext2D();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                GameMaster.entities.get(GameMaster.level).get(0).isPressed(keyEvent.getCode(), true);
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                GameMaster.entities.get(GameMaster.level).get(0).isPressed(keyEvent.getCode(), false);
            }
        });
    }

    public static int getWidthPixelPlayingFrame() {
        return WIDTH_PIXEL_PLAYING_FRAME;
    }
    public static int getHeightPixelPlayingFrame() {
        return HEIGHT_PIXEL_PLAYING_FRAME;
    }

    public GraphicsContext getPlayingGc() {
        return playingGc;
    }

    public void clearScreen() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
