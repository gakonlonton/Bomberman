package uet.oop.bomberman.scene;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import uet.oop.bomberman.controller.GameMaster;

public class IngameScene extends RootScene {
    public IngameScene() {
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

    public void clearScreen() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
