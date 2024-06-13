package Objects;

import processing.core.PApplet;
import processing.core.PImage;

public class Bonus {
    private int x,y;
    private int diameter;
    private float speedY;
    private int color;
    private PImage spritImage;

    public Bonus(int x, int y, int diameter, float speedY, int color) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.speedY = speedY;
        this.color = color;
    }

    public void draw(PApplet applet) {
        if (spritImage != null) {
            applet.image(spritImage, x, y, diameter, diameter);
        }
        else {
            applet.fill(color);
            applet.circle(x, y, diameter);
        }
    }


    public void update() {
        y += speedY;
    }


    public void setSpriteImage(PImage spriteImage) {
        this.spritImage = spriteImage;
    }

    public boolean isCollected(Starship myStarship) {
        return x > myStarship.getX_position() && x < myStarship.getX_position() + myStarship.getWidth() &&
        y > myStarship.getY_position() && y < myStarship.getY_position() + myStarship.getHeight();

    }

    public int getY() {
        return y;
    }

    public int getDiameter() {
        return diameter;
    }

    public float getSpeedY() {
        return speedY;
    }

    public int getColor() {
        return color;
    }
}


