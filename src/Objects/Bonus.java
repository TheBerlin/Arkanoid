package Objects;

import processing.core.PApplet;
import processing.core.PImage;

public class Bonus {
    private int x,y;
    private int diameter;
    private float speedY;
    private int color;
    private PImage spriteImage;
    private BonusType type; // type field

    public Bonus(int x, int y, int diameter, float speedY, int color, BonusType type) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.speedY = speedY;
        this.color = color;
        this.type = type;
    }

    public void draw(PApplet applet) {
        if (spriteImage != null) {
            applet.image(spriteImage, x, y, diameter, diameter);
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
        this.spriteImage = spriteImage;
    }

    public boolean isCollected(Starship myStarship) {
        // Check for collision between the bonus and the starship
        return (x + diameter / 2 > myStarship.getX_position() &&
                x - diameter / 2 < myStarship.getX_position() + myStarship.getWidth() &&
                y + diameter / 2 > myStarship.getY_position() &&
                y - diameter / 2 < myStarship.getY_position() + myStarship.getHeight());
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


    public BonusType getType() {
        return type; 
    }
}


