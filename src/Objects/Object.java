package Objects;

/**
 * Die Object-Klasse stellt eine abstrakte Basisklasse für Spielobjekte dar.
 */
public abstract class Object {
    private float x_position, y_position; // Die x- und y-Position des Objekts
    private int color; // Die Farbe des Objekts

    
    /**
     * Gibt die x-Position des Objekts zurück.
     * @return Die x-Position des Objekts.
     */
    public float getX_position() {
        return x_position;
    }

    /**
     * Setzt die x-Position des Objekts.
     * @param x_position Die neue x-Position des Objekts.
     */
    public void setX_position(float x_position) {
        this.x_position = x_position;
    }

    /**
     * Gibt die y-Position des Objekts zurück.
     * @return Die y-Position des Objekts.
     */
    public float getY_position() {
        return y_position;
    }

    /**
     * Setzt die y-Position des Objekts.
     * @param y_position Die neue y-Position des Objekts.
     */
    public void setY_position(float y_position) {
        this.y_position = y_position;
    }

    /**
     * Gibt die Farbe des Objekts zurück.
     * @return Die Farbe des Objekts.
     */
    public int getColor() {
        return color;
    }

    /**
     * Setzt die Farbe des Objekts.
     * @param color Die neue Farbe des Objekts.
     */
    public void setColor(int color) {
        this.color = color;
    }
}
