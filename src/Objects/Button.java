package Objects;

import processing.core.PApplet;

public class Button {
    private float x, y, width, height;
    private String label;
    private int baseColor, hoverColor, currentColor;

    public Button(float x, float y, float width, float height, String label) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.baseColor = 255;
        this.hoverColor = 200;
        this.currentColor = baseColor;
    }

    public void draw(PApplet app) {
        app.fill(currentColor);
        app.rect(x, y, width, height);
        app.fill(0);
        app.textSize(18);
        app.textAlign(PApplet.CENTER, PApplet.CENTER);
        app.text(label, x + width / 2, y + height / 2);
    }

    public void update(PApplet app) {
        if (isMouseOver(app)) {
            currentColor = hoverColor;
        } else {
            currentColor = baseColor;
        }
    }

    public boolean isMouseOver(PApplet app) {
        return app.mouseX > x && app.mouseX < x + width && app.mouseY > y && app.mouseY < y + height;
    }

    public boolean isClicked(PApplet app) {
        return isMouseOver(app) && app.mousePressed && app.mouseButton == PApplet.LEFT;
    }
}
