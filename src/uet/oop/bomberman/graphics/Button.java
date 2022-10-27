package uet.oop.bomberman.graphics;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Button {
    private Text name;
    private int x;
    private int y;

    public Button(int x, int y, Text text) {
        this.x = x;
        this.y = y;
        this.name = text;
    }

    public Font getFont() {
        return name.getFont();
    }

    public Text getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void render(GraphicsContext gc) {
        gc.setFont(name.getFont());
        gc.setFill(name.getFill());
        gc.fillText(name.getText(), x, y);
    }

    public void specificRender(GraphicsContext gc) {
        gc.strokeText(name.getText(), x, y);
        gc.setFont(Graphics.font);
        gc.setFill(Color.BLACK);
        gc.fillText(name.getText(), x, y);
    }
}
