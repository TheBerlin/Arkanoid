package Objects;

import processing.core.PImage;

/**
 * Representiert Vaus as dem Originalspiel Arkanoid.
 * Die Klasse erweitert Object und erbt deren Attributen und Methoden.
 */
public class Starship extends Object {
    private int width; // Veränderung der Breite des Starships
    private int height = 15;
    private PImage spriteImage; // Sprite/Image für Starship

    public Starship(int screenWidth, int screenHeight) {
        if (screenWidth <= 800) {
            this.width = 75;
            this.height = 15;
        } else {
            this.width = 125;
            this.height = 25;
        }
    }

    /**
     * Gibt die Breite des Raumschiffs zurück.
     * @return Die Breite des Raumschiffs.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Setzt die Breite des Raumschiffs.
     * @param width Die zu setzende Breite.
     */
    public void setWidth(int width) {
        this.width = width;
    }


    /**
     * Gibt die Höhe des Raumschiffs zurück.
     * @return Die Höhe des Raumschiffs.
     */
    public int getHeight() {
        return height;
    }


    /**
     * Setzt die Höhe des Raumschiffs.
     * @param height Die zu setzende Höhe.
     */
    public void setHeight(int height) {
        this.height = height;
    }


    /**
     * Gibt das Spritebild des Raumschiffs zurück.
     * @return Das Spritebild des Raumschiffs.
     */
    public PImage getSpriteImage() {
        return spriteImage;
    }


    /**
     * Setzt das Spritebild des Raumschiffs.
     * @param spriteImage Das zu setzende Spritebild.
     */
    public void setSpriteImage(PImage spriteImage) {
        this.spriteImage = spriteImage;
    }
}
