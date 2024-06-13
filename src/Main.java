import processing.core.PApplet;
/**
 * Die Main-Klasse enthält die main-Methode, um das Spiel zu starten.
 */
public class Main extends PApplet{
     /**
     * Die main-Methode, um das Spiel zu starten.
     * @param args Die Eingabeargumente für das Programm (nicht verwendet).
     */
    public static void main(String[] args) {
        Window gameWindow = new Window(false);
        PApplet.runSketch(gameWindow.getProcessingArgs(), gameWindow);
    }
}
