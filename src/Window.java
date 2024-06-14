import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;
import ddf.minim.Minim;
import ddf.minim.AudioPlayer;

import java.io.File;
import java.util.ArrayList;

import inputs.Inputs;

import Objects.Ball;
import Objects.Starship;
import Objects.Target;
import Objects.Bonus;
import Objects.Button;


/**
 * Die Hauptklasse des Spiels, die erbt von PApplet.
 * Stellt das Hauptfenster des Spiels dar und enthält die Spiellogik.
 */
public class Window extends PApplet {
    private String[] processingArgs = {"GameWindow"};
    private Ball ball; // Ball
    private Starship myStarship; // Starship
    private Bonus bonus;
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
    private Button tryAgainButton;
    private boolean isIntroActive = true; // Intro screen state
    private boolean isMenuActive = false; // Menu state 
    private boolean isGameOver = false;
    private boolean isFullscreen; // Fullscreen state
    private boolean gameOverSoundplayed; // Gameover state for play sound
    private boolean isGameEnd = false;
    
    // Bonuses
    private boolean isBonusActive = false;
    private int bonusSelector = 0;
    private int colorSelektor = 0; // from 0 to 9
    
    //For timer
    private int bonusTimings[] = {
        5, 10, 15, 20, 30
    };
    private int spawnTimings[] = {
        7, 15, 25, 30
    };
    TimeManager spawnManager = new TimeManager(spawnTimings, bonusTimings);
    private CountdownTimer bonusDurationTimer;
    private CountdownTimer bonusSpawnTimer;
    int bonusDuration;
    int spawnCountDown;







    private Minim minim;
    private AudioPlayer ballHitTargetSound;
    private AudioPlayer gameOverSound;
    private AudioPlayer gameEndSound;
    private AudioPlayer hitStarshipSound;
    private AudioPlayer backgroundMusic;

    private PFont gameFont;


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

        // Allows to resize the window
        surface.setResizable(false);

        // Font
        gameFont = createFont("data/PixelEmulator-xq08.ttf", 32);

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
        
        // Inizialisierng der Ziele (Ziegel)
        targets = new ArrayList<>();
        initializeTargets();

        // Timer for bonus duration
        

        int bonusDiameter = 30;
        int bonusSpeedY = 1;
        int bonusColor[] = {
            color(127, 0, 0), // dark red
            color(255, 200, 200),// pink
            color(255, 255, 0),   // yellow
            color(0, 255, 255),   // aqua
            color(0, 0, 0),   // black
        };

        int randomX = (int)(Math.random() * 1000);
        System.out.println(randomX);
        bonus = new Bonus(randomX, -bonusDiameter, bonusDiameter, bonusSpeedY, bonusColor[colorSelektor]);


        // Initialisierung des Spielicons
        PImage gameIcon = loadImage("icon.png");
        surface.setIcon(gameIcon);

        // Initialisierung des Spieltitels
        surface.setTitle("Arkanoid");

        // Initialisierung der Menu Buttons
        initializeMenuButtons();

        // Initialisierung der Intro Buttons
        initializeIntroButtons();

        // Initialisierung der Gameover Buttons
        initializeGameOverButtons();

        // Initialize Minim
        minim = new Minim(this);

        // Load sound effects
        ballHitTargetSound = minim.loadFile("sfx/block-destroyed.wav");
        gameOverSound = minim.loadFile("sfx/game-over.wav");
        gameEndSound = minim.loadFile("sfx/game-end.wav");
        hitStarshipSound = minim.loadFile("sfx/paddle-hit.wav");
        backgroundMusic = minim.loadFile("sfx/bg-music1.wav");

        gameOverSound.setGain(5);
        backgroundMusic.setGain(-10);
        backgroundMusic.loop();
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
            if (isBonusActive) {
                bonus.draw(this);
                bonus.update();

                // if (bonus.isCollected(myStarship)) {
                //     isBonusActive = false;
                //     myStarship.setWidth(200);
                //     //countdownTimer.start();
                // }


                if (bonus.getY() > height); {
                    isBonusActive = false;
                }
            }

            // Check score and spawn a bonus
            // if (&& !isBonusActive) {
            //     isBonusActive = true;
            //     // Selektor for a random bonus (0 to 10)
            //     // bonusSelector = (int)(Math.random() * 5);
            //     // colorSelektor = bonusSelector;
            //     // System.out.println("BONUS: " + bonusSelector);  
            // }

            // Aktualisieren der Position des Raumschiffs basierend auf den Steuerungseingaben
            if(isBallVisible) {
                inputs.updateStarshipPosition();
            }
            
            // Zeichnen des Raumschiffs
            if (myStarship.getSpriteImage() != null) {
                image(myStarship.getSpriteImage(), myStarship.getX_position(), myStarship.getY_position(), myStarship.getWidth(), myStarship.getHeight());
            } else {
                fill(myStarship.getColor());
                rect(myStarship.getX_position(), myStarship.getY_position(), myStarship.getWidth(), myStarship.getHeight());
            }

            // Zeichnen des Balls
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

                hitStarshipSound.rewind();
                hitStarshipSound.play();
                System.out.println(ball.getSpeedX());

                // The ball hit the left side of the starship.
                if (ball.getX_position() >= myStarship.getX_position() && 
                    ball.getX_position() < myStarship.getX_position() + myStarship.getWidth() / 3) {

                    if (ball.getSpeedX() > 0) {
                        ball.setSpeedX(-abs((float)(ball.getSpeedX())));
                        System.out.println("It hit left");
                    } 
                    ball.setSpeedY(-abs((float)(ball.getSpeedY() * 1.1)));
                }
                // The ball hit the middle of the starship. 
                else if (ball.getX_position() > myStarship.getX_position() + myStarship.getWidth() / 3 &&
                        ball.getX_position() <= myStarship.getX_position() + myStarship.getWidth() * 2 / 3) {       
                        System.out.println("It hit middle");
                        ball.setSpeedY(-abs((float)(ball.getSpeedY() * 0.9)));

                } 
                // The ball hit the right side of the starship.
                else if (ball.getX_position() > myStarship.getX_position() + myStarship.getWidth() * 2 / 3 &&
                        ball.getX_position() <= myStarship.getX_position() + myStarship.getWidth()) {

                    if (ball.getSpeedX() < 0) {
                        ball.setSpeedX(abs((float)(ball.getSpeedX())));
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
            if (!gameOverSoundplayed) {
                gameOverSoundplayed = true;
                gameOverSound.rewind();
                gameOverSound.play();
            }
        }

            // Kollisionserkennung mit den Zielen (Ziegeln)
            for (Target target : targets) {
                switch(target.hit(ball.getX_position(), ball.getY_position(), ball.getDiameter())) {
                    case "left":
                        ball.setSpeedX(-abs(ball.getSpeedX()));
                        ballHitTargetSound.rewind();
                        ballHitTargetSound.play();
                        score += 100;
                        break;
                    case "right":
                        ball.setSpeedX(abs(ball.getSpeedX()));
                        ballHitTargetSound.rewind();
                        ballHitTargetSound.play();
                        score += 100;
                        break;
                    case "top":
                        ball.setSpeedY(-abs(ball.getSpeedY()));
                        ballHitTargetSound.rewind();
                        ballHitTargetSound.play();
                        score += 100;
                        break;
                    case "bottom":
                        ball.setSpeedY(abs(ball.getSpeedY()));
                        ballHitTargetSound.rewind();
                        ballHitTargetSound.play();
                        score += 100;

                        break;
                    default:        
                }
                displayScore(); // Anzeige der Punktzahl
            }

            // Überprüfen, ob alle Ziele (Ziegel) zerstört wurden, um die Gewinnbedingung zu überprüfen
            if (checkWinConditions()) {
                fill(0, 255, 0); // Grün für Gewinnnachricht
                textSize(20);
                textAlign(CENTER, CENTER);
                text("Level abgeschlossen", width / 2, (height / 2 - 10));
                text("Linksklick, um fortzufahren", width / 2, (height / 2 + 10));
                noLoop(); // Spiel pausieren
            }

            // Anzeige der Spielende-Nachricht, wenn der Ball nicht mehr sichtbar ist
            if (!isBallVisible) {
                isGameOver = true;
                drawGameOver();
                //stopTimer();
                spawnManager.stop();
            }
        }      
    }

    @Override
    public void stop() {
        ballHitTargetSound.close();
        gameOverSound.close();
        //winSound.close();
        minim.stop();
        super.stop();
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
                    for (int j = 0; j < 6; j++) {
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
        if (isFullscreen) {
            resumeButton = new Button(width / 2 - 200, height / 2 - 60, 400, 40, "Resume");
            restartButton = new Button(width / 2 - 200, height / 2 - 10, 400, 40, "Restart");
            returnIntroButton = new Button(width / 2 - 200, height / 2 + 40, 400, 40, "Back to main menu");
        }
        else {
            resumeButton = new Button(width / 2 - 100, height / 2 - 60, 200, 40, "Resume");
            restartButton = new Button(width / 2 - 100, height / 2 - 10, 200, 40, "Restart");
            returnIntroButton = new Button(width / 2 - 100, height / 2 + 40, 200, 40, "Back to main menu");
            //returnIntroButton = new Button(width / 2 - 100, height / 2 + 40, 200, 40, "Back to main menu");
        }
        
    }

    public void initializeIntroButtons () {
        if (isFullscreen) {
            playgameButton = new Button(width / 2 - 200, height / 2 - 60, 400, 40, "Play");
            quitgameButton = new Button(width / 2 - 200, height / 2 + 40, 400, 40, "Quit game");    
        }
        else {
            playgameButton = new Button(width / 2 - 100, height / 2 - 60, 200, 40, "Play");
            quitgameButton = new Button(width / 2 - 100, height / 2 + 40, 200, 40, "Quit game");
        }

        if (isFullscreen) {
            fullscreenButton = new Button(width / 2 - 200, height / 2 - 10, 400, 40, "Fullscreen: On");
        } else {
            fullscreenButton = new Button(width / 2 - 100, height / 2 - 10, 200, 40, "Fullscreen: Off");
        }
    }

    public void initializeGameOverButtons () {
        tryAgainButton = new Button(width / 2 - 100, height / 2 - 60, 200, 40, "Try again");
    }

    /**
     * Überprüft, ob alle Ziele (Ziegel) zerstört wurden, um die Gewinnbedingung zu überprüfen.
     * @return True, wenn alle Ziele zerstört wurden, ansonsten False.
     */
    public boolean checkWinConditions() {
        boolean allDestroyed = true;
        for (Target target: targets) {
            if (!target.isDestroyed()) {
                allDestroyed = false;
                break;
            }
        }
        
        if (allDestroyed) {
            targets.clear();
            destroyedBricksCounter = 0;
            currentLevel++;
            initializeTargets();
        }
        
        return allDestroyed;
        
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
        // clear targets
        targets.clear();

        // reset destroyed bricks conuter
        destroyedBricksCounter = 0;
        score = 0;

        // Initialize new targets for the next level
        initializeTargets();

        // Reset starship and ball positions
        resetStarshipAndBall();

        // Reset the initial launch
        initialLaunch = false;

        // Start moving is false initially
        startMoving = false;

        gameOverSoundplayed = false;

    }
    

    /**
     * Behandelt den Mausklick, um das Spiel zu starten und fortzusetzen.
     */ 

    @Override
    public void mousePressed() {
        super.mousePressed();
        if (isIntroActive) {
            if (playgameButton.isClicked(this)) {
                isIntroActive = false; 
            } else if (fullscreenButton.isClicked(this)) {
                toggleFullscreen();
            } else if (quitgameButton.isClicked(this)) {
                exit();
            }
        } else if (isMenuActive) {
            if (resumeButton.isClicked(this)) {
                isMenuActive = false; 
            } else if (restartButton.isClicked(this)) {
                resetGame(); 
                isMenuActive = false;
            } else if (returnIntroButton.isClicked(this)) {
                resetGame();
                isMenuActive = false;
                isIntroActive = true;
            } 
        } else if (isGameOver) {
            if (tryAgainButton.isClicked(this)) {
                isGameOver = false;
                resetGame();
            }
        } else {
            if (mouseButton == LEFT) {
                if (!initialLaunch) {
                    // Start the game if it hasn't started yet
                    initialLaunch = true;
                    startMoving = true; // Start moving the ball

                    // Start spawn timers
                    spawnManager.reset();


                    ball.setSpeedX(2 * speedMultiplier); // Set constant speed for the ball
                    ball.setSpeedY(2 * speedMultiplier);
                } else if (checkWinConditions()) {
                    // Reset the game for the next level if all targets are destroyed
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
        textFont(gameFont);
        text("ARKANOID", width / 2, height / 2 - 150);

        playgameButton.update(this);
        fullscreenButton.update(this);
        quitgameButton.update(this);

        playgameButton.draw(this);
        fullscreenButton.draw(this);
        quitgameButton.draw(this);
    }

    public void drawGameOver() {
        background(0, 0, 0, 150); 
        textFont(gameFont); 
        fill(255,0 ,0); 
        textSize(50); 
        textAlign(CENTER, CENTER); 
        text("GAME OVER", width / 2, height / 2 - 150); 

        tryAgainButton.update(this);
        returnIntroButton.update(this);

        tryAgainButton.draw(this);
        textSize(10);
        returnIntroButton.draw(this);
    }


    public void toggleFullscreen () {
        isFullscreen = !isFullscreen; // Toggle the fullscreen state
        surface.setVisible(false); // Hide the current window
        backgroundMusic.close();
        noLoop(); // Stop the current sketch
        Window gameWindow = new Window(isFullscreen);
        PApplet.runSketch(gameWindow.getProcessingArgs(), gameWindow);
    }


    public void stopTimer() {
        bonusSpawnTimer.stop();
    }

}
