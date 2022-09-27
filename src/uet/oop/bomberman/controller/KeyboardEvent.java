package uet.oop.bomberman.controller;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

public class KeyboardEvent implements EventHandler<KeyEvent> {
    private Set<KeyCode> pressedKeys = new HashSet<>();

    public KeyboardEvent(Scene scene) {
        scene.setOnKeyPressed(this);
        scene.setOnKeyReleased(this);
    }

    public boolean isPressed(KeyCode keys) {
        return pressedKeys.contains(keys);
    }

    @Override
    public void handle(KeyEvent event) {
        if (KeyEvent.KEY_PRESSED.equals(event.getEventType())) {
            pressedKeys.add(event.getCode());
        }
        else if (KeyEvent.KEY_RELEASED.equals(event.getEventType())) {
            pressedKeys.remove(event.getCode());
        }
    }
}
