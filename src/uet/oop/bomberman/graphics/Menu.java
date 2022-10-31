package uet.oop.bomberman.graphics;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import uet.oop.bomberman.controller.GameMaster;
import uet.oop.bomberman.controller.KeyListener;
import uet.oop.bomberman.controller.Timer;
import uet.oop.bomberman.controller.audio.Audio;
import uet.oop.bomberman.graphics.sprite.Sprite;

import javafx.scene.input.KeyCode;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static uet.oop.bomberman.controller.GameMaster.*;
import static uet.oop.bomberman.graphics.Graphics.*;

public class Menu {
    public enum MenuState {
        MENU, SINGLE_PLAY, MULTIPLAYER, OPTION, END, LOSE, UNFINISHED, WIN, MAP_RELOAD
    }
    public static MenuState menuState;
    private KeyListener keyListener;
    /*
        Buttons
     */
    List<Button> buttons = new ArrayList<>();
    Button ButtonStart, ButtonReady, ButtonPause, ButtonReturn;
    private int choseButton;

    /*
        Game State code
     */

    private final int SingleGameCode = 0;
    private final int MultiplayerCode = 1;
    private final int OptionCode = 2;
    private final int AutoPlayCode = 3;
    private final int ExitCode = 4;

    /*
        Constructor
     */

    public Menu(KeyListener keyListener) {
        audio.playAlone(Audio.AudioType.LOBBY, -1);
        this.menuState = MenuState.MENU;
        this.keyListener = keyListener;

        Text text = new Text("NEW GAME");
        text.setFont(Graphics.font);
        text.setFill(Color.WHITE);
        buttons.add(new Button(Graphics.SCREEN_WIDTH / 2 - (int) text.getLayoutBounds().getWidth() / 2,
                Graphics.SCREEN_HEIGHT / 2 + (int) text.getLayoutBounds().getHeight() / 2, text));

        text = new Text("MULTIPLAYER");
        text.setFont(Graphics.font);
        text.setFill(Color.WHITE);
        buttons.add(new Button(Graphics.SCREEN_WIDTH / 2 - (int) text.getLayoutBounds().getWidth() / 2,
                Graphics.SCREEN_HEIGHT / 2 + 4 * (int) text.getLayoutBounds().getHeight() / 2, text));

        text = new Text("AUDIO OPTIONS");
        text.setFont(Graphics.font);
        text.setFill(Color.WHITE);

        buttons.add(new Button(Graphics.SCREEN_WIDTH / 2 - (int) text.getLayoutBounds().getWidth() / 2,
                Graphics.SCREEN_HEIGHT / 2 + 7 * (int) text.getLayoutBounds().getHeight() / 2, text));
        text = new Text("BOT PLAY");
        text.setFont(Graphics.font);
        text.setFill(Color.WHITE);
        buttons.add(new Button(Graphics.SCREEN_WIDTH / 2 - (int) text.getLayoutBounds().getWidth() / 2,
                Graphics.SCREEN_HEIGHT / 2 + 10 * (int) text.getLayoutBounds().getHeight() / 2, text));

        text = new Text("EXIT");
        text.setFont(Graphics.font);
        text.setFill(Color.WHITE);
        buttons.add(new Button(Graphics.SCREEN_WIDTH / 2 - (int) text.getLayoutBounds().getWidth() / 2,
                Graphics.SCREEN_HEIGHT / 2 + 13 * (int) text.getLayoutBounds().getHeight() / 2, text));
        choseButton = SingleGameCode;
    }

    public void menuRender(GraphicsContext gc) {
        gc.drawImage(menu_image, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        render(gc);
    }

    public void winRender(GraphicsContext gc) {
        gc.drawImage(win_image, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        render(gc);
    }

    public void loseRender(GraphicsContext gc) {
        gc.drawImage(lose_image, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        render(gc);
    }

    /*
        Getter and Setter
     */

    public MenuState getMenuState() {
        return menuState;
    }

    public void setMenuState(MenuState state) {
        menuState = state;
    }

    /*
        Update and render menu
     */

    private long delay = 0;
    private boolean enter, goDown, goUp;

    public void isPressed(KeyCode keyCode, boolean isPress) {
        switch (keyCode) {
            case ENTER:
                enter = isPress;
                break;
            case S:
                goDown = isPress;
                break;
            case W:
                goUp = isPress;
                break;
            case SPACE:
                enter = isPress;
                break;
            default:
                break;
        }
    }

    public void update() {
        long now = Timer.now();
        switch (menuState) {
            case MENU:
                if (now - delay > Timer.INPUT_TIME) {
                    delay = now;
                    if (enter) {
                        switch (choseButton) {
                            case SingleGameCode:
                                audio.playAlone(Audio.AudioType.PLAYING, -1);
                                menuState = MenuState.SINGLE_PLAY;
                                break;
                            case MultiplayerCode:
                                menuState = MenuState.MULTIPLAYER;
                                // Do something
                                break;
                            case OptionCode:
                                menuState = MenuState.OPTION;
                                setAudio = true;
                                break;
                            case AutoPlayCode:
                                menuState = MenuState.UNFINISHED;
                                unfinished = true;
                                break;
                            case ExitCode:
                                menuState = MenuState.END;
                                break;
                        }
                    } else {
                        if (goDown) {
                            audio.playOnBackground(Audio.AudioType.CHOOSE, 1);
                            choseButton++;
                            if (choseButton == buttons.size()) choseButton = 0;
                        } else if (goUp) {
                            audio.playOnBackground(Audio.AudioType.CHOOSE, 1);
                            choseButton--;
                            if (choseButton == -1) choseButton = buttons.size() - 1;
                        }
                    }
                }
                break;
            case UNFINISHED:
            case OPTION:
                menuState = MenuState.MENU;
                enter = false;
                break;
            case WIN:
            case LOSE:
                if (now - delay > Timer.INPUT_TIME) {
                    delay = now;
                    if (enter) {
                        audio.playAlone(Audio.AudioType.LOBBY, -1);
                        menuState = MenuState.MENU;
                    }
                }
                break;
            case END:
                Platform.exit();
                break;
        }
    }

    public void render(GraphicsContext gc) {
        switch (menuState) {
            case MENU:
                for (int i = 0; i < buttons.size(); i++) {
                    if (choseButton == i) {
                        buttons.get(i).specificRender(gc);
                    } else {
                        buttons.get(i).render(gc);
                    }
                }
                break;
            case LOSE:
                break;
            case END:
                break;
        }
    }
}
