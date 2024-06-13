import processing.core.PApplet;
import processing.core.PImage;
//import ddf.minim.*;

import java.beans.BeanProperty;
import java.util.ArrayList;

import javax.swing.border.Border;

import inputs.Inputs;

import Objects.Ball;
import Objects.Starship;
import Objects.Target;
import Objects.Bonus; // for bonus
import Objects.Button;


/**
 * Die Hauptklasse des Spiels, die erbt von PApplet.
 * Stellt das Hauptfenster des Spiels dar und enthält die Spiellogik.
 */
public class Window extends PApplet {
    private String[] processingArgs = {"GameWindow"};
    private Ball ball; // Ball
    private Starship myStarship; // Starship
    private Bonus bonus; // Bonus
    private Inputs inputs; // Steerngseingaben
    private PImage ballSprite; // Sprite für den Ball
    private PImage starshipSprite; // Sprite für das Starship
    private boolean initialLaunch = false; // Flag, um das Spiel starten/beenden
    private boolean startMoving = false; // Flag, um die Bewegung des Balls zu ändern
    private int speedMultiplier = 1; // Multiplikator für Ballgeschwindigkeit
    private boolean isBallVisible = true; // Flag, um Sichtbarkeit des Balls zu ändern
    private int destroyedBricksCounter = 0; // Zähler für zerstörte Ziegel
    private int currentLevel = 1; // Aktuelles Level
    private int score = 0; // Score
    private ArrayList<Target> targets;
    private Button resumeButton;
    private Button restartButton;
    private Button fullscreenButton;
    private Button playgameButton;
    private Button quitgameButton;
    private Button returnIntroButton;
    private boolean isIntroActive = true; // Intro screen state
    private boolean isMenuActive = false; // Menu state 
    private boolean isFullscreen; // Fullscreen state
    Stopwatch myStopwatch;
    private boolean timeCounter = false;
    private boolean bonusActive = false; // Flag to check bonus state
    private int collectedBonuses = 0;
    private int bonusSelector = 0;

    // private Minim minim;
    // private AudioPlayer ballHitTargetSound;


    public Window(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
    }

    /**
     * Konfiguriert die Größe des Fensters.
     */
    @Override
    public void settings() {
        super.settings();
        if (isFullscreen) {
            fullScreen(); // Start in fullscreen mode
        } else {
            size(800, 600); // Start in windowed mode
        }
}


    /**
     * Initialisiert das Spiel und lädt die erforderlichen Ressourcen.
     */
    @Override
    public void setup() {
        super.setup();
        // Framerate für das Fenster
        frameRate(144);

        // Initialize stopwatch
        myStopwatch = new Stopwatch();
        // Allows to resize the window
        surface.setResizable(false);

        // Initialisiert Starship und Starship Sprite
        //starshipSprite = loadImage("starship/starship_default.png");
        myStarship = new Starship(width, height);
        myStarship.setColor(75);
        //myStarship.setSpriteImage(starshipSprite);

        // Initialisiert Ball und Ball Sprite
        ballSprite = loadImage("ball/ball.png");
        ball = new Ball(width, height);
        ball.setBallColor(200);
        ball.setSpriteImage(ballSprite);

        // Initialisiert Steerungseingaben
        inputs = new Inputs(this, myStarship);

        // Setzt Starship in die Mitte unten im Fenster, falls die Steuerungseingabe über die Tastatur erfolgt.
        int starshipInitialX = (width - myStarship.getWidth()) / 2;
        int starshipInitialY = height - myStarship.getHeight();
        myStarship.setX_position(starshipInitialX);
        myStarship.setY_position(starshipInitialY);
        
        // Inizialisierung der Ziele (Ziegel)
        targets = new ArrayList<>();
        initializeTargets();

        // Inizialisierung des Bonus
        int bonusDiameter = 30;
        int bonusSpeedY = 1;
        int bonusColor = color(255, 215, 0);
        bonus = new Bonus(width / 2, -bonusDiameter, bonusDiameter, bonusSpeedY, bonusColor);

        // Initialisierung des Spielicons
        PImage gameIcon = loadImage("icon.png");
        surface.setIcon(gameIcon);

        // Initialisierung des Spieltitels
        surface.setTitle("Arkanoid");

        // Initialisierung der Menu Buttons
        initializeMenuButtons();

        // Initialisierung der Intro Buttons
        initializeIntroButtons();

        // Load mp3 sfx file
        // ballHitTargetSound = minim.loadFile("sfx/block-destroyed.mp3");

    }


    /**
     * Zeichnet den Spielbildschirm.
     */
    public void draw() {
        
        if (isIntroActive) {
            drawIntro();
        } else if (isMenuActive) {
            drawMenu();
        } else {
            
            background(193, 232, 255); // Hintergrundfarbe

            // Spawn bonus
            if (bonusActive) {
                bonus.draw(this);
                bonus.update();

                if (bonus.isCollected(myStarship)) {
                    //collectedBonuses++;
                    bonusActive = false;
                    switch (bonusSelector) {
                        case 0:
                            myStarship.setWidth(125);
                            System.out.println("Bonus " + bonusSelector + " collected");
                            break;
                        case 1:
                            ball.setSpeedX(1);
                            ball.setSpeedY(1);
                            System.out.println("Bonus " + bonusSelector + " collected");
                            break;

                    }
                }

                if (bonus.getY() > height) {
                    bonusActive = false;
                }
            }

            //Check score and activate bonus
            if (score == 100 && !bonusActive || score == 500 && !bonusActive || score == 1500 && !bonusActive || score == 2200 && !bonusActive || score == 3200 && !bonusActive || score == 6100 && !bonusActive) {
                bonusActive = true;
                // Selektor for a random bonus (0 to 10)
                bonusSelector = (int)(Math.random() * 10);
                System.out.println("[ " + bonusSelector + " ]");
                switch (bonusSelector) {
                    case 0:
                        bonus = new Bonus(width / 2, -bonus.getDiameter(), bonus.getDiameter()-10, bonus.getSpeedY(), bonus.getColor());
                        // Clear bonus selektor
                        bonusSelector = 0;
                        break;
                    case 1:
                        bonus = new Bonus(width / 2, -bonus.getDiameter(), bonus.getDiameter()+10, bonus.getSpeedY(), bonus.getColor());
                        // Clear bonus selektor
                        bonusSelector = 0;
                        break;
                    case 2:
                        bonus = new Bonus(width / 2, -bonus.getDiameter(), bonus.getDiameter(), bonus.getSpeedY(), bonus.getColor());
                        // Clear bonus selektor
                        bonusSelector = 0;
                        break;
                    case 3:
                        bonus = new Bonus(width / 2, -bonus.getDiameter(), bonus.getDiameter()+20, bonus.getSpeedY(), bonus.getColor());
                        // Clear bonus selektor
                        bonusSelector = 0;
                        break;
                    case 4:
                        bonus = new Bonus(width / 2, -bonus.getDiameter(), bonus.getDiameter()-20, bonus.getSpeedY(), bonus.getColor());
                        // Clear bonus selektor
                        bonusSelector = 0;
                        break;
                    case 5:
                        bonus = new Bonus(width / 2, -bonus.getDiameter(), bonus.getDiameter(), bonus.getSpeedY(), bonus.getColor());
                        // Clear bonus selektor
                        bonusSelector = 0;
                        break;
                    case 6:
                    bonus = new Bonus(width / 2, -bonus.getDiameter(), bonus.getDiameter(), bonus.getSpeedY(), bonus.getColor());
                    // Clear bonus selektor
                    bonusSelector = 0;
                    break;    
                    case 7:
                        bonus = new Bonus(width / 2, -bonus.getDiameter(), bonus.getDiameter()+100, bonus.getSpeedY(), bonus.getColor());
                        // Clear bonus selektor
                        bonusSelector = 0;
                        break;
                    case 8:
                        bonus = new Bonus(width / 2, -bonus.getDiameter(), bonus.getDiameter()-5, bonus.getSpeedY(), bonus.getColor());
                        // Clear bonus selektor
                        bonusSelector = 0;
                        break;
                    case 9:
                        bonus = new Bonus(width / 2, -bonus.getDiameter()+50, bonus.getDiameter()+25, bonus.getSpeedY(), bonus.getColor());
                        // Clear bonus selektor
                        bonusSelector = 0;
                        break;
                    case 10:
                    bonus = new Bonus(width / 2, -bonus.getDiameter(), bonus.getDiameter()+50, bonus.getSpeedY(), bonus.getColor());
                    // Clear bonus selektor
                    bonusSelector = 0;
                    break;                    
                }
                                

            }

            // Aktualisieren der Position des Raumschiffs basierend auf den Steuerungseingaben
            inputs.updateStarshipPosition();

            // Zeichnen des Raumschiffs
            if (myStarship.getSpriteImage() != null) {
                image(myStarship.getSpriteImage(), myStarship.getX_position(), myStarship.getY_position(), myStarship.getWidth(), myStarship.getHeight());
            } else {
                fill(myStarship.getColor());
                rect(myStarship.getX_position(), myStarship.getY_position(), myStarship.getWidth(), myStarship.getHeight());
            }

            // Zeichnen des Balls
            // If super bonus is collected, spawn 2 more  balls within an array
            if (ball.getSpriteImage() != null) {
                image(ball.getSpriteImage(), ball.getX_position(), ball.getY_position(), ball.getDiameter(), ball.getDiameter());

            } else {
                fill(ball.getBallColor());
                circle(ball.getX_position(), ball.getY_position(), ball.getDiameter());
            }

            // Zeichnen der Ziele (Ziegel)
            for (Target target : targets) {
                target.draw(this);
            }

            // Bewegen des Balls, wenn das Spiel gestartet wurde
            if (startMoving && isBallVisible) {
                ball.setY_position(ball.getY_position() + ball.getSpeedY());
                ball.setX_position(ball.getX_position() + ball.getSpeedX());
            }

            // Kollisionserkennung mit dem Raumschiff am unteren Bildschirmrand
            // Wechsel der Ballrichtung, um nach oben zu bewegen
            if (ball.getY_position() + ball.getDiameter() >= myStarship.getY_position() &&
            ball.getX_position() >= myStarship.getX_position() &&
            ball.getX_position() <= myStarship.getX_position() + myStarship.getWidth()) {

            // ballHitTargetSound.rewind();
            // ballHitTargetSound.play();
            System.out.println(ball.getSpeedX());

            if (ball.getX_position() >= myStarship.getX_position() && 
                ball.getX_position() < myStarship.getX_position() + myStarship.getWidth() / 3) {

                if (ball.getSpeedX() > 0) {
                    ball.setSpeedX(-abs(ball.getSpeedX()));
                    System.out.println("It hit left");
                } 
                ball.setSpeedY(-abs((float)(ball.getSpeedY() * 1.1)));

            } else if (ball.getX_position() > myStarship.getX_position() + myStarship.getWidth() / 3 &&
                    ball.getX_position() <= myStarship.getX_position() + myStarship.getWidth() * 2 / 3) {       
                    System.out.println("It hit middle");
                    ball.setSpeedY(-abs(ball.getSpeedY()));

            } else if (ball.getX_position() > myStarship.getX_position() + myStarship.getWidth() * 2 / 3 &&
                    ball.getX_position() <= myStarship.getX_position() + myStarship.getWidth()) {

                if (ball.getSpeedX() < 0) {
                    ball.setSpeedX(abs(ball.getSpeedX()));
                    System.out.println("It hit right");
                } 
                ball.setSpeedY(-abs((float)(ball.getSpeedY() * 1.1)));

            } 
        }

            // Wechsel der Ballrichtung, um nach unten zu bewegen, wenn der obere Bildschirmrand erreicht ist
            if (ball.getY_position() <= 0) {
                ball.setSpeedY(abs(ball.getSpeedY()));
            }
                
            // Wechsel der Ballrichtung, um nach rechts zu bewegen, wenn der linke Bildschirmrand erreicht ist
            if (ball.getX_position() <= 0) {
                ball.setSpeedX(abs(ball.getSpeedX()));
            }

            // Wechsel der Ballrichtung, um nach links zu bewegen, wenn der rechte Bildschirmrand erreicht ist
            if (ball.getX_position() + ball.getDiameter() >= width) {
                ball.setSpeedX(-abs(ball.getSpeedX()));
            }

            // Überprüfen, ob der Ball den unteren Bildschirmrand erreicht hat
            // Das Spiel endet, wenn der Ball den unteren Bildschirmrand erreicht
            if (ball.getY_position() + ball.getDiameter() >= height) {
                startMoving = false;
                isBallVisible = false;
                // TEMP
                //isMenuActive = true;
                timeCounter = true;

            }

            // Kollisionserkennung mit den Zielen (Ziegeln)
            for (Target target : targets) {
                if (target.hit(ball.getX_position(), ball.getY_position(), ball.getDiameter())) {
                    ball.setSpeedY(abs(ball.getSpeedY()));// Wechsel der Ballrichtung nach dem Treffen des Ziels
                    System.out.println(ball.getSpeedY());
                    destroyedBricksCounter++; // Zähler für zerstörte Ziegel erhöhen
                    score += 100; // Score erhöhen
                }
                displayScore(); // Anzeige der Punktzahl
            }

            // Überprüfen, ob alle Ziele (Ziegel) zerstört wurden, um die Gewinnbedingung zu überprüfen
            if (checkWinConditions()) {
                fill(0, 255, 0); // Grün für Gewinnnachricht
                textSize(20);
                textAlign(CENTER, CENTER);
                text("Level completed", width / 2, (height / 2 - 10));
                text("Left click to continue", width / 2, (height / 2 + 10));
                noLoop(); // Spiel pausieren
            }

            // Anzeige der Spielende-Nachricht, wenn der Ball nicht mehr sichtbar ist
            if (!isBallVisible) {
                fill(255, 0, 0); // Rot für Spielende-Nachricht
                textSize(20);
                textAlign(CENTER, CENTER);
                text("Game Over!", width / 2, height / 2);

            }
        }

        // Stopwatch
        displayStopwatchTime();

    }


    /**
     * Initialisiert die Ziele (Ziegel) basierend auf dem aktuellen Level.
     */
    public void initializeTargets() {
        int targetWidth = width / 16;
        int targetHeight = height / 30;
        int borderBuffer;
        
        if(width <= 800) {
            borderBuffer = 10;
        } else {
            borderBuffer = 45;
        }

        
        // Level counter
        // Define colors for each row
        int[] rowColors = {
            color(255, 0, 0), // red
            color(0, 255, 0), // green
            color(0, 255, 255), // light blue
            color(255, 255, 0), // yellow
            color(255, 0, 255), // magenta
            color(0, 0, 255), // blue
            color(0,0,0)
        };
        switch (currentLevel) {
            case 1:
                // Targets quanttiy is fixed to the current level
                // Targets quantity cannot be changed with window resolution
                // Use this to set up a targets quantity for single row
                for (int i = 0; i < 15; i++) {
                    // Use this to set up number of rows
                    for (int j = 0; j < 7; j++) {
                        int color = rowColors[j % rowColors.length]; // Assign color based on row index
                        targets.add(new Target(borderBuffer + i * (targetWidth+2), targetWidth + j * (targetHeight+2), targetWidth, targetHeight, color)); // Assign color to targets
                    }
                }
                break;
        
            case 2:
                // Define colors for each row
                rowColors[0] = color(255,255,0); // yellow
                rowColors[1] = color(0,255,0); // green
                rowColors[2] = color(0,0,255); // blue
                rowColors[3] = color(255,0,0); // red
                rowColors[4] = color(255,0,255); // magenta
                rowColors[5] = color(0,255,255); // aqua
                rowColors[6] = color(255,255,255); // beige
                ball.setSpeedX(10);
                ball.setSpeedY(10);

                // Targets quanttiy is fixed to the current level
                // Targets quantity cannot be changed with window resolution
                // Use this to set up a targets quantity for single row
                for (int i = 0; i < 15; i++) {
                    // Use this to set up number of rows
                    if (i % 3 == 0) {
                        continue;
                    }
                    for (int j = 0; j < 7; j++) {
                        int color = rowColors[j % rowColors.length]; // Assign color based on row index
                        targets.add(new Target(borderBuffer + i * (targetWidth-1), targetWidth + j * (targetHeight+2), targetWidth, targetHeight, color)); // Assign color to targets
                    }
                }
                break;
            
            case 3:
            // Targets quanttiy is fixed to the current level
            // Targets quantity cannot be changed with window resolution
            // Use this to set up a targets quantity for single row
                for (int i = 0; i < 6; i++) {
                    // Use this to set up number of rows
                    for (int j = 0; j < 5; j++) {
                        int color = rowColors[j % rowColors.length]; // Assign color based on row index
                        targets.add(new Target(borderBuffer + i * (targetWidth+2), targetWidth + j * (targetHeight+2), targetWidth, targetHeight, color)); // Assign color to targets
                    }
                }
        }
    }

    public void initializeMenuButtons () {
        resumeButton = new Button(width / 2 - 100, height / 2 - 60, 200, 40, "Resume");
        restartButton = new Button(width / 2 - 100, height / 2 - 10, 200, 40, "Restart");
        returnIntroButton = new Button(width / 2 - 100, height / 2 + 40, 200, 40, "Back to main menu");
    }

    public void initializeIntroButtons () {
        playgameButton = new Button(width / 2 - 100, height / 2 - 60, 200, 40, "Play");
        quitgameButton = new Button(width / 2 - 100, height / 2 + 40, 200, 40, "Quit game");

        if (isFullscreen) {
            fullscreenButton = new Button(width / 2 - 100, height / 2 - 10, 200, 40, "Fullscreen: On");
        } else {
            fullscreenButton = new Button(width / 2 - 100, height / 2 - 10, 200, 40, "Fullscreen: Off");
        }
    }

    /**
     * Überprüft, ob alle Ziele (Ziegel) zerstört wurden, um die Gewinnbedingung zu überprüfen.
     * @return True, wenn alle Ziele zerstört wurden, ansonsten False.
     */
    public boolean checkWinConditions() {
        for (Target target : targets) {
            if (!target.isDestroyed()) {
                return false;
            }
        }
        return true;
    }
    


    private void resetStarshipAndBall() {
        // Reset the starship position
        int starshipInitialX = ((width - myStarship.getWidth()) / 2);
        int starshipInitialY = height - myStarship.getHeight();
        myStarship.setX_position(starshipInitialX);
        myStarship.setY_position(starshipInitialY);

        // Reset the ball position
        ball.setX_position(starshipInitialX + (myStarship.getWidth() / 2) - ball.getDiameter() / 2);
        ball.setY_position(starshipInitialY - ball.getDiameter() - 250);

        // Reset the ball visibility
        isBallVisible = true;
    }


    /**
     * Setzt das Spiel und die Spielobjekte zurück, um ein neues Spiel zu starten.
     */
    public void resetGame() {
        // Reset the time
        myStopwatch.stop();
        myStopwatch.reset();

        // clear targets
        targets.clear();

        // reset destroyed bricks conuter
        destroyedBricksCounter = 0;

        // Initialize new targets for the next level
        initializeTargets();

        // Reset starship and ball positions
        resetStarshipAndBall();

        // Reset the initial launch
        initialLaunch = false;

        // Start moving is false initially
        startMoving = false;

    }
    

    /**
     * Behandelt den Mausklick, um das Spiel zu starten und fortzusetzen.
     */ 

    @Override
    public void mousePressed() {
        super.mousePressed();
        // If intro is active
        if (isIntroActive) {
            // Starts the game
            if (playgameButton.isClicked(this)) {
                isIntroActive = false; 
            // Toggle on/off fullscreen mode
            } else if (fullscreenButton.isClicked(this)) {
                toggleFullscreen();
            // Exit from the game
            } else if (quitgameButton.isClicked(this)) {
                exit();
            }
        // If game is paused (menu is opened)
        } else if (isMenuActive) {
            System.out.println("Time paused");
            // Resumes the game
            if (resumeButton.isClicked(this)) {
                isMenuActive = false; 
            // Restarts the game
            } else if (restartButton.isClicked(this)) {
                resetGame(); 
                isMenuActive = false;
                //myStopwatch.reset();
                System.out.println("RESET");
            // Back to menu
            } else if (returnIntroButton.isClicked(this)) {
                isIntroActive = true;
            } 
        } else {
            if (mouseButton == LEFT) {
                if (!initialLaunch) {
                    // Start the game if it hasn't started yet
                    initialLaunch = true;
                    startMoving = true; // Start moving the ball
                    myStopwatch.start();
                    ball.setSpeedX(2 * speedMultiplier); // Set constant speed for the ball
                    ball.setSpeedY(2 * speedMultiplier);
                } else if (checkWinConditions()) {
                    // Reset the game for the next level if all targets are destroyed
                    currentLevel++;
                    resetGame();
                    loop(); // Resume the game loop
                }
            }
        }
    }
    

     /**
     * Anzeige der Spielerpunktzahl.
     */
    public void displayScore() {
        fill(0);
        textSize(20);
        textAlign(RIGHT, BOTTOM);
        text("Score: " + score, width - 10, height - 40);
    }

    public void displayStopwatchTime() {
        int countedTime = myStopwatch.getTimeInSeconds();
        // if game over
        if (timeCounter) {
            myStopwatch.stop();
            if (isFullscreen) {
                fill(0);
                textSize(20);
                textAlign(CENTER, TOP);
                text("Time: " + countedTime + " s", displayWidth / 2, 5);
                }
            else {
                fill(0);
                textSize(20);
                textAlign(CENTER, TOP);
                text("Time: " + countedTime + " s", width / 2, 5);
                System.out.println(countedTime);
            }
        }
        else {
            if (isFullscreen) {
                fill(0);
                textSize(20);
                textAlign(CENTER, TOP);
                text("Time: " + myStopwatch.getTimeInSeconds() + " s", displayWidth / 2, 5);            
            }
            else {
                fill(0);
                textSize(20);
                textAlign(CENTER, TOP);
                text("Time: " + myStopwatch.getTimeInSeconds() + " s", width / 2, 5);
            }
        }

    }

    public String[] getProcessingArgs() {
        return processingArgs; 
    }

    @Override
    public void keyPressed() {
        super.keyPressed();
        if (key == ESC) {
            key = 0;
            isMenuActive = !isMenuActive;
        } 
    }

    // With mouse input
    public void drawMenu () {
        background(0, 0, 0, 150); 
        fill(255);
        textSize(32);
        textAlign(CENTER, CENTER);
        text("Menu", width / 2, height / 2 - 150);

        resumeButton.update(this);
        restartButton.update(this);
        returnIntroButton.update(this);

        resumeButton.draw(this);
        restartButton.draw(this);
        returnIntroButton.draw(this);
        
    }

    public void drawIntro () {
        background(0, 0, 0, 150); 
        fill(255);
        textSize(50);
        textAlign(CENTER, CENTER);
        text("ARKANOID", width / 2, height / 2 - 150);

        playgameButton.update(this);
        fullscreenButton.update(this);
        quitgameButton.update(this);

        playgameButton.draw(this);
        fullscreenButton.draw(this);
        quitgameButton.draw(this);
    }

    public void toggleFullscreen () {
        isFullscreen = !isFullscreen; // Toggle the fullscreen state
        surface.setVisible(false); // Hide the current window
        noLoop(); // Stop the current sketch
        Window gameWindow = new Window(isFullscreen);
        PApplet.runSketch(gameWindow.getProcessingArgs(), gameWindow);
    }
    

}
