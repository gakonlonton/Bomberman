package uet.oop.bomberman.scene;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.controller.GameMaster;

import java.net.URL;
import java.util.ResourceBundle;

import static uet.oop.bomberman.controller.GameMaster.canvas;

public class SceneMaster implements Initializable {
    public static final int SCREEN_WIDTH = 700;
    public static final int SCREEN_HEIGHT = 400;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button ButtonOption;
    @FXML
    private Button ButtonPlay;
    @FXML
    private Button ButtonReturn;
    @FXML
    private Button ButtonQuit;
    @FXML
    private Button ButtonHighScore;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL lobby, playing;
        lobby = BombermanGame.class.getResource("/Lobby.fxml");
        playing = BombermanGame.class.getResource("/Playing.fxml");
        if (url.equals(lobby)) {
            ButtonPlay.setFocusTraversable(false);
            ButtonOption.setFocusTraversable(false);
            ButtonQuit.setFocusTraversable(false);
            ButtonHighScore.setFocusTraversable(false);
        }
        if (url.equals(playing)) {
            ButtonReturn.setFocusTraversable(false);
        }
    }

    public void SwitchToLobby(MouseEvent event) {
        URL url = BombermanGame.class.getResource("/Lobby.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        scene = new Scene(root);
        stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void SwitchToInGame(ActionEvent event) {
        GameMaster.gameStatus = GameMaster.gameStatus.PLAY;
        URL url = BombermanGame.class.getResource("/Playing.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);

        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((VBox) root).getChildren().add(canvas);

        scene = new Scene(root);

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

        stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void setEffect(MouseEvent event) {
        Effect shadow = new Glow();
        ((Button) event.getSource()).setEffect(shadow);
    }

    public void removeEffect(MouseEvent event) {
        ((Button) event.getSource()).setEffect(null);
    }

    public static void clearScreen() {
        GameMaster.gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void setMusic(MouseEvent event) {
        GameMaster.audio.setAudioOption(!GameMaster.audio.isMuted());
    }
}
