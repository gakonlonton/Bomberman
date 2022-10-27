package uet.oop.bomberman.graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import uet.oop.bomberman.controller.GameMaster;
import uet.oop.bomberman.controller.KeyListener;
import uet.oop.bomberman.controller.Timer;
import uet.oop.bomberman.graphics.sprite.Sprite;

import javafx.scene.input.KeyCode;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static uet.oop.bomberman.graphics.Graphics.menu_image;

public class Menu {
    public enum MenuState {
        MENU, SINGLE_PLAY, MULTIPLAYER, PAUSE, END, END_STATE
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
    private final int AutoPlayCode = 2;
    private final int ExitCode = 3;

    /*
        Constructor
     */

    public Menu(KeyListener keyListener) {
        this.menuState = MenuState.MENU;
        this.keyListener = keyListener;

        Text text = new Text("SINGLE PLAY");
        text.setFont(Graphics.font);
        text.setFill(Color.WHITE);
        buttons.add(new Button(Graphics.SCREEN_WIDTH / Sprite.SCALED_SIZE / 2 * Sprite.SCALED_SIZE - (int) text.getLayoutBounds().getWidth() / 2,
                Graphics.SCREEN_HEIGHT / Sprite.SCALED_SIZE / 2 * Sprite.SCALED_SIZE + (int) text.getLayoutBounds().getHeight() / 2, text));

        text = new Text("MULTIPLAYER");
        text.setFont(Graphics.font);
        text.setFill(Color.WHITE);
        buttons.add(new Button(Graphics.SCREEN_WIDTH / Sprite.SCALED_SIZE / 2 * Sprite.SCALED_SIZE - (int) text.getLayoutBounds().getWidth() / 2,
                Graphics.SCREEN_HEIGHT / Sprite.SCALED_SIZE / 2 * Sprite.SCALED_SIZE + 3 * (int) text.getLayoutBounds().getHeight() / 2, text));

        text = new Text("SURVIVAL PLAY");
        text.setFont(Graphics.font);
        text.setFill(Color.WHITE);

        buttons.add(new Button(Graphics.SCREEN_WIDTH / Sprite.SCALED_SIZE / 2 * Sprite.SCALED_SIZE - (int) text.getLayoutBounds().getWidth() / 2,
                Graphics.SCREEN_HEIGHT / Sprite.SCALED_SIZE / 2 * Sprite.SCALED_SIZE + 5 * (int) text.getLayoutBounds().getHeight() / 2, text));
        text = new Text("BOT PLAY");
        text.setFont(Graphics.font);
        text.setFill(Color.WHITE);
        buttons.add(new Button(Graphics.SCREEN_WIDTH / Sprite.SCALED_SIZE / 2 * Sprite.SCALED_SIZE - (int) text.getLayoutBounds().getWidth() / 2,
                Graphics.SCREEN_HEIGHT / Sprite.SCALED_SIZE / 2 * Sprite.SCALED_SIZE + 7 * (int) text.getLayoutBounds().getHeight() / 2, text));

        text = new Text("EXIT");
        text.setFont(Graphics.font);
        text.setFill(Color.WHITE);
        buttons.add(new Button(Graphics.SCREEN_WIDTH / Sprite.SCALED_SIZE / 2 * Sprite.SCALED_SIZE - (int) text.getLayoutBounds().getWidth() / 2,
                Graphics.SCREEN_HEIGHT / Sprite.SCALED_SIZE / 2 * Sprite.SCALED_SIZE + 9 * (int) text.getLayoutBounds().getHeight() / 2, text));

        text = new Text("START");
        text.setFont(Graphics.font);
        text.setFill(Color.WHITE);

        ButtonStart = new Button(Graphics.SCREEN_WIDTH / Sprite.SCALED_SIZE / 2 * Sprite.SCALED_SIZE - (int) text.getLayoutBounds().getWidth() / 2,
                Graphics.SCREEN_HEIGHT / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE - (int) text.getLayoutBounds().getHeight() / 3 + 4, text);

        text = new Text("READY");
        text.setFont(Graphics.font);
        text.setFill(Color.WHITE);

        ButtonReady = new Button(Graphics.SCREEN_WIDTH / Sprite.SCALED_SIZE / 2 * Sprite.SCALED_SIZE - (int) text.getLayoutBounds().getWidth() / 2,
                Graphics.SCREEN_HEIGHT / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE - (int) text.getLayoutBounds().getHeight() / 3 + 4, text);

        text = new Text("CONTINUE GAME");
        text.setFont(Graphics.font);
        text.setFill(Color.WHITE);

        ButtonPause = new Button(Graphics.SCREEN_WIDTH / Sprite.SCALED_SIZE / 7 * Sprite.SCALED_SIZE,
                Graphics.SCREEN_HEIGHT / Sprite.SCALED_SIZE / 8 * 10 * Sprite.SCALED_SIZE - (int) text.getLayoutBounds().getHeight() / 3, text);

        text = new Text("GO TO MENU");
        text.setFont(Graphics.font);
        text.setFill(Color.WHITE);
        ButtonReturn = new Button(Graphics.SCREEN_WIDTH / Sprite.SCALED_SIZE / 7 * Sprite.SCALED_SIZE,
                Graphics.SCREEN_HEIGHT / Sprite.SCALED_SIZE / 8 * 10 * Sprite.SCALED_SIZE - (int) text.getLayoutBounds().getHeight() / 2, text);

        choseButton = SingleGameCode;
    }

    public void menuRender(GraphicsContext gc) {
        gc.drawImage(menu_image, 0, 0, Graphics.SCREEN_WIDTH, Graphics.SCREEN_HEIGHT);
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

    public void update() {
        switch (menuState) {
            case MENU:
                long now = Timer.now();
                if (now - delay > Timer.INPUT_TIME) {
                    delay = now;
                    if (keyListener.pressed(KeyCode.ENTER)) {
                        // playAudio
                        switch (choseButton) {
                            case SingleGameCode:
                                // playAudio
                                menuState = MenuState.SINGLE_PLAY;
                                // GameMaster.mapList.get() = new Map(0);
                                break;
                            case MultiplayerCode:
                                menuState = MenuState.MULTIPLAYER;
                                // Do something
                                System.exit(0);
                                break;
                            case AutoPlayCode:
                                menuState = MenuState.END;
                                break;
                            case ExitCode:
                                menuState = MenuState.END;
                                break;
                        }
                    } else {
                        if (keyListener.pressed(KeyCode.S)) {
                            // playAudio
                            choseButton++;
                            if (choseButton == buttons.size()) choseButton = 0;
                        } else if (keyListener.pressed(KeyCode.W)) {
                            // playAudio
                            choseButton--;
                            if (choseButton == -1) choseButton = buttons.size() - 1;
                        } else if (keyListener.pressed(KeyCode.ESCAPE)) {
                            choseButton = ExitCode;
                        }
                    }
                }
                break;
            case PAUSE:
                now = Timer.now();
                if (now - delay > Timer.INPUT_TIME) {
                    delay = now;
                    if (keyListener.equals(KeyCode.ENTER)) {
                        menuState = MenuState.SINGLE_PLAY;
                    }
                }
                break;
            case END_STATE:
                now = Timer.now();
                if (now - delay > Timer.INPUT_TIME) {
                    delay = now;
                    if (keyListener.equals(KeyCode.ENTER)) {
                        menuState = MenuState.MENU;
                    }
                }
                break;
            case END:
                System.exit(0);
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
            case PAUSE:
                ButtonPause.render(gc);
                break;
            case END_STATE:
                break;
            case END:
                break;
        }
    }
}
