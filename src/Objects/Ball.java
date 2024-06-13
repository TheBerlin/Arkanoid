package Objects;

import processing.core.PImage;


/**
 * Die Klasse Ball repräsentiert den Spielball im Spiel.
 * Diese Klasse erweitert die Klasse Object und erbt deren Eigenschaften und Methoden.
 */
public class Ball extends Object {
    private int diameter; // Durchmesser der Balls
    private int ballColor = 255;
    private float x_position;
    private float y_position;
    private PImage spriteImage; // Sprite für Ball
    private float speedX;
    private float speedY;


     /**
     * Konstruktor für die Ball-Klasse.
     * @param screenWidth Die Breite des Bildschirms.
     * @param screenHeight Die Höhe des Bildschirms.
     */
    public Ball(int screenWidth, int screenHeight) {
        if (screenWidth <= 800) {
            this.diameter = 15;
        } else {
            this.diameter = 25;
        }
        this.x_position = (screenWidth - diameter) / 2;
        this.y_position = (screenHeight - diameter) / 2;
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    public int getBallColor() {
        return ballColor;
    }

    public void setBallColor(int ballColor) {
        this.ballColor = ballColor;
    }

    public float getX_position() {
        return x_position;
    }

    public void setX_position(float x_position) {
        this.x_position = x_position;
    }

    public float getY_position() {
        return y_position;
    }

    public void setY_position(float y_position) {
        this.y_position = y_position;
    }

    public PImage getSpriteImage() {
        return spriteImage;
    }

    public void setSpriteImage(PImage spriteImage) {
        this.spriteImage = spriteImage;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }


}
