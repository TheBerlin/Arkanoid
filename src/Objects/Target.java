package Objects;

import processing.core.PApplet;


/**
 * Die Klasse repräsentiert ein Ziel (Ziegel) im Spiel.
 * Sie erbt von der Object.
 */
public class Target extends Object {
    private int width, height;
    private boolean destroyed;

    
    /**
     * Konstruktor für die Initialisierung eines Ziels.
     * @param x_position Die x-Position des Ziels.
     * @param y_position Die y-Position des Ziels.
     * @param width Die Breite des Ziels.
     * @param height Die Höhe des Ziels.
     * @param color Die Farbe des Ziels.
     */    
    public Target(int x_position, int y_position, int width, int height, int color) {
        setX_position(x_position);
        setY_position(y_position);
        this.width = width;
        this.height = height;
        setColor(color);
        this.destroyed = false;
    }


    /**
     * Gibt die Breite des Ziels zurück.
     * @return Die Breite des Ziels.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Setzt die Breite des Ziels.
     * @param width Die Breite des Ziels.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gibt die Höhe des Ziels zurück.
     * @return Die Höhe des Ziels.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Setzt die Höhe des Ziels.
     * @param height Die Höhe des Ziels.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Überprüft, ob das Ziel zerstört wurde.
     * @return True, wenn das Ziel zerstört wurde, ansonsten False.
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Setzt den Zerstörungsstatus des Ziels.
     * @param destroyed True, wenn das Ziel zerstört wurde, ansonsten False.
     */
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    /**
     * Überprüft, ob das Ziel von einem Ball getroffen wurde.
     * @param ballX Die x-Position des Balls.
     * @param ballY Die y-Position des Balls.
     * @param ballDiameter Der Durchmesser des Balls.
     * @return True, wenn das Ziel getroffen wurde, ansonsten False.
     */
    // public boolean hit(float ballX, float ballY, float ballDiameter) {
    //     if (!destroyed && ballX + ballDiameter >= getX_position() && ballX <= getX_position() + width &&
    //         ballY + ballDiameter >= getY_position() && ballY <= getY_position() + height) {
    //         this.destroyed = true; // Ziel wurde getroffen und ist zerstört
    //         return true;
    //     }
    //     return false;
    // }

    public String hit(float ballX, float ballY, float ballDiameter) {
    if (!destroyed && ballX + ballDiameter >= getX_position() && ballX <= getX_position() + width &&
        ballY + ballDiameter >= getY_position() && ballY <= getY_position() + height) {
        
        this.destroyed = true; // Ziel wurde getroffen und ist zerstört
        
        // Calculate the distances to each side of the block
        float leftDist = ballX + ballDiameter - getX_position();
        float rightDist = getX_position() + width - ballX;
        float topDist = ballY + ballDiameter - getY_position();
        float bottomDist = getY_position() + height - ballY;
        
        // Determine the side that was hit
        float minDist = Math.min(Math.min(leftDist, rightDist), Math.min(topDist, bottomDist));
        if (minDist == leftDist) {
            return "left";
        } else if (minDist == rightDist) {
            return "right";
        } else if (minDist == topDist) {
            return "top";
        } else if (minDist == bottomDist) {
            return "bottom";
        }
    }
    return "none";
}

    /**
     * Zeichnet das Ziel auf den Bildschirm.
     * @param app Die PApplet-Instanz.
     */
    public void draw(PApplet app) {
        if (!destroyed) {
            app.fill(getColor()); // Festlegen der Farbe
            app.rect(getX_position(), getY_position(), width, height); // Zeichnen des Rechtecks
        }
    }
}