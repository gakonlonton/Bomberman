package uet.oop.bomberman.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomber.Bomber;
import uet.oop.bomberman.graphics.sprite.Sprite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Graphics {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 448;

    public static Font default_font;
    public static Font font;
    public static Font thin_font;
    public static Image menu_image;

    private GraphicsContext gc;

    public Graphics(Canvas canvas) {
        gc = canvas.getGraphicsContext2D();
        try {
            default_font = Font.loadFont(Files.newInputStream(Paths.get("res/font/PixelGameFont.ttf")), 30);
            font = Font.loadFont(Files.newInputStream(Paths.get("res/font/PixelGameFont.ttf")), 25);
            thin_font = Font.loadFont(Files.newInputStream(Paths.get("res/font/PixelGameFont.ttf")), 15);
            menu_image = new Image(Files.newInputStream(Paths.get("res/textures/menu.png")));
        } catch (IOException e) {
            System.out.println("Wrong file path");
        }
    }

    public void clearScreen(Canvas canvas) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void renderText(Font font, Text text, int x, int y) {
        gc.setFont(font);
        gc.setFill(text.getFill());
        gc.fillText(text.getText(), x, y);
    }

    public void renderButton(Button button) {
        renderText(button.getFont(), button.getName(), button.getX(), button.getY());
    }
}
