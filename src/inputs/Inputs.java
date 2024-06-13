package inputs;

import Objects.Starship;
import processing.core.PApplet;

/**
 * Die Inputs-Klasse verwaltet die Steuereingaben für das Raumschiff im Spiel.
 */
public class Inputs {
    private PApplet parent; // Die PApplet-Instanz
    private Starship starship; // Das Raumschiff
    private boolean useMouseControl; // Schalter für die Steuerungsart
    private int movingSpeed = 3; // Bewegungsgeschwindigkeit des Raumschiffs

    /**
     * Konstruktor für die Inputs-Klasse.
     * @param parent Die PApplet-Instanz.
     * @param starship Das Raumschiff-Objekt.
     */
    public Inputs(PApplet parent, Starship starship) {
        this.parent = parent;
        this.starship = starship;
        this.useMouseControl = true; // Standardmäßig Maussteuerung aktiviert
    }

    /**
     * Gibt die Bewegungsgeschwindigkeit des Raumschiffs zurück.
     * @return Die Bewegungsgeschwindigkeit des Raumschiffs.
     */
    public int getMovingSpeed() {
        return movingSpeed;
    }

    /**
     * Setzt die Bewegungsgeschwindigkeit des Raumschiffs.
     * @param movingSpeed Die Bewegungsgeschwindigkeit des Raumschiffs.
     */
    public void setMovingSpeed(int movingSpeed) {
        this.movingSpeed = movingSpeed;
    }

    /**
     * Setzt die Steuerungsart (Maus oder Tastatur).
     * @param useMouseControl True, wenn Maussteuerung aktiviert werden soll, ansonsten False.
     */
    public void setUseMouseControl(boolean useMouseControl) {
        this.useMouseControl = useMouseControl;
    }

    /**
     * Aktualisiert die Position des Raumschiffs basierend auf den Eingaben.
     */
    public void updateStarshipPosition() {
        int y = parent.height - starship.getHeight(); // Die y-Position des Raumschiffs (am unteren Rand des Bildschirms)

        if (useMouseControl) {
            // Maussteuerung: Das Raumschiff folgt der horizontalen Position der Maus
            int x = parent.mouseX - starship.getWidth() / 2; // Berechnung der x-Position basierend auf der Mausposition
            x = Math.max(0, Math.min(parent.width - starship.getWidth(), x)); // Begrenzen der x-Position innerhalb des Bildschirms
            starship.setX_position(x); // Setzen der neuen x-Position des Raumschiffs
        } else {
            // Tastatursteuerung: A und D (oder Pfeiltasten) zum Bewegen des Raumschiffs nach links und rechts
            if (parent.keyPressed) {
                if (parent.key == 'a' || parent.key == 'A' || parent.keyCode == PApplet.LEFT) {
                    float newX = starship.getX_position() - getMovingSpeed(); // Bewegung nach links
                    starship.setX_position(Math.max(0, newX)); // Begrenzen der x-Position innerhalb des Bildschirms
                } else if (parent.key == 'd' || parent.key == 'D' || parent.keyCode == PApplet.RIGHT) {
                    float newX = starship.getX_position() + getMovingSpeed(); // Bewegung nach rechts
                    starship.setX_position(Math.min(parent.width - starship.getWidth(), newX)); // Begrenzen der x-Position innerhalb des Bildschirms
                }
            }
        }

        starship.setY_position(y); // Setzen der y-Position des Raumschiffs
    }

}
