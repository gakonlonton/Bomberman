package uet.oop.bomberman.controller;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

public class KeyListener implements EventHandler<KeyEvent> {
    private Set<KeyCode> pressedKeys = new HashSet<>();
    public KeyListener(Scene scene) {
        scene.setOnKeyPressed(this);
        scene.setOnKeyReleased(this);
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        if (KeyEvent.KEY_PRESSED.equals(keyEvent.getEventType())) {
            GameMaster.entities.get(GameMaster.level).get(0).isPressed(keyEvent.getCode(), true);
            pressedKeys.add(keyEvent.getCode());
        } else if (KeyEvent.KEY_RELEASED.equals(keyEvent.getEventType())) {
            GameMaster.entities.get(GameMaster.level).get(0).isPressed(keyEvent.getCode(), false);
            pressedKeys.remove(keyEvent.getCode());
        }
    }

    public boolean pressed(KeyCode keyCode) {
        return pressedKeys.contains(keyCode);
    }
}
