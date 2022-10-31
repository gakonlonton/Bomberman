package uet.oop.bomberman.scene;

import javafx.event.ActionEvent;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.controller.GameMaster;

import java.net.URL;
import java.util.ResourceBundle;

import static uet.oop.bomberman.controller.GameMaster.canvas;

public class Lobby implements Master, Initializable {
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
    private Stage stage;
    private javafx.scene.Scene scene;
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Auto constructor
    }

    public void PLAY(MouseEvent event) {
        GameMaster.gameStatus = GameMaster.gameStatus.PLAY;
        URL url = BombermanGame.class.getResource("/Playing.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);

        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((VBox)(((StackPane) root).getChildren().get(0))).getChildren().add(canvas);

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

    @FXML
    public void setMusic(MouseEvent event) {
        GameMaster.audio.setAudioOption(!GameMaster.audio.isMuted());
    }

    @FXML
    public void QUIT(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    public void RANK(MouseEvent event) {
        System.exit(0);
    }

    public static void clearScreen() {
        // Clear window screen
        GameMaster.gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    @FXML
    public void setEffect(MouseEvent event) {
        Effect shadow = new Glow();
        ((Button) event.getSource()).setEffect(shadow);
    }

    @FXML
    public void removeEffect(MouseEvent event) {
        ((Button) event.getSource()).setEffect(null);
    }
}
