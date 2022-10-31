package uet.oop.bomberman.scene;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.controller.GameMaster;

import java.net.URL;
import javafx.util.Duration;
import java.util.ResourceBundle;

public class GameScene implements Master, Initializable {
    @FXML
    private Text levelText;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Line muteLine;
    @FXML
    private Button ButtonSettings;
    @FXML
    private Button ButtonPause;
    @FXML
    private Button ButtonReturn;
    @FXML
    private StackPane root;
    @FXML
    private Button continueButton;
    private Scene scene;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL playingURL;
        playingURL = BombermanGame.class.getResource("Playing.fxml");
        if (url.equals(playingURL)) {
            levelText.setText("LEVEL " + (GameMaster.level + 1));

            muteLine.setVisible(GameMaster.audio.isMuted());

            // Set status for progress bar.
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                    new KeyFrame(Duration.minutes(1), e -> {
                        //time out
                    }, new KeyValue(progressBar.progressProperty(), 1))
            );
            timeline.play();

            ButtonPause.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    GameMaster.gameStatus = GameMaster.inGameStatus.PAUSE;
                    root.getChildren().get(0).setEffect(new BoxBlur());
                    continueButton.setVisible(true);
                    continueButton.setDisable(false);
                }
            });

            continueButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    root.getChildren().get(0).setEffect(null);
                    continueButton.setVisible(false);
                    continueButton.setDisable(true);
                    GameMaster.gameStatus = GameMaster.inGameStatus.PLAY;
                }
            });

            ButtonSettings.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    GameMaster.audio.setAudioOption(!GameMaster.audio.isMuted());
                    muteLine.setVisible(GameMaster.audio.isMuted());
                }
            });
        }
    }

    @FXML
    public void QUIT(MouseEvent event) {
        GameMaster.gameStatus = GameMaster.inGameStatus.LOBBY;
        URL url = BombermanGame.class.getResource("/UI_fxml/LobbyScene.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }
}
