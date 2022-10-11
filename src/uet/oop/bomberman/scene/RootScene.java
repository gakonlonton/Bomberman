package uet.oop.bomberman.scene;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.graphics.Sprite;


import static uet.oop.bomberman.BombermanGame.WINDOW_HEIGHT;
import static uet.oop.bomberman.BombermanGame.WINDOW_WIDTH;

public class RootScene {
    Canvas canvas;
    GraphicsContext gc;
    Group root;
    Scene scene;

    public RootScene() {
        canvas = new Canvas(WINDOW_WIDTH * Sprite.SCALED_SIZE, WINDOW_HEIGHT * Sprite.SCALED_SIZE);
        gc = canvas.getGraphicsContext2D();
        root = new Group();
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
