package uet.oop.bomberman.scene;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import uet.oop.bomberman.graphics.Sprite;

import static uet.oop.bomberman.BombermanGame.WINDOW_HEIGHT;
import static uet.oop.bomberman.BombermanGame.WINDOW_WIDTH;

public class RootScene {
    protected static final int WINDOW_WIDTH_PIXEL = 700;
    protected static final int WINDOW_HEIGHT_PIXEL = 400;
    Canvas canvas, scoreCanvas;
    GraphicsContext gc;
    VBox root;
    Scene scene;

    public RootScene() {
        canvas = new Canvas(WINDOW_WIDTH_PIXEL, WINDOW_HEIGHT_PIXEL - 30);
        scoreCanvas = new Canvas(WINDOW_WIDTH_PIXEL, 30);
        gc = canvas.getGraphicsContext2D();
        root = new VBox();
        root.getChildren().add(scoreCanvas);
        root.getChildren().add(canvas);
        scene = new Scene(root);
    }

    public Scene getScene() {
        return scene;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public GraphicsContext getGc() {
        return gc;
    }
}
