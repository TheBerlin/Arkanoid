import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

// Interface for drawable objects
interface Drawable {
    void draw(PApplet applet);
}

// Base class for all game objects
abstract class GameObject implements Drawable {
    protected float x, y, width, height;
    protected PImage sprite;

    public GameObject(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    public void draw(PApplet applet) {
        if (sprite != null) {
            applet.image(sprite, x, y, width, height);
        } else {
            applet.rect(x, y, width, height);
        }
    }
}

// Base class for power-ups
abstract class PowerUp extends GameObject {
    public PowerUp(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public abstract void applyEffect(Starship starship);
}

class Ball extends GameObject {
    private float speedX, speedY;

    public Ball(float x, float y, float diameter, float speedX, float speedY) {
        super(x, y, diameter, diameter);
        this.speedX = speedX;
        this.speedY = speedY;
    }

    public void update() {
        x += speedX;
        y += speedY;
    }

    // Check collision with the game boundaries
    public void checkBoundaryCollision(float gameWidth, float gameHeight) {
        if (x <= 0 || x + width >= gameWidth) {
            speedX = -speedX;
        }
        if (y <= 0 || y + height >= gameHeight) {
            speedY = -speedY;
        }
    }
}

class Starship extends GameObject {
    private float speed;

    public Starship(float x, float y, float width, float height, float speed) {
        super(x, y, width, height);
        this.speed = speed;
    }

    public void moveLeft() {
        x -= speed;
    }

    public void moveRight() {
        x += speed;
    }

    public void expand() {
        width *= 1.5;
    }

    public void shrink() {
        width *= 0.75;
    }
}

class Brick extends GameObject {
    private boolean destroyed;

    public Brick(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.destroyed = false;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        destroyed = true;
    }
}

class ExpandPaddlePowerUp extends PowerUp {
    public ExpandPaddlePowerUp(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void applyEffect(Starship starship) {
        starship.expand();
    }
}

class ShrinkPaddlePowerUp extends PowerUp {
    public ShrinkPaddlePowerUp(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void applyEffect(Starship starship) {
        starship.shrink();
    }
}

// Main game window
public class ArkanoidGame extends PApplet {
    private Ball ball;
    private Starship starship;
    private ArrayList<Brick> bricks;
    private ArrayList<PowerUp> powerUps;
    private boolean isFullscreen = false;

    public static void main(String[] args) {
        PApplet.main("ArkanoidGame");
    }

    @Override
    public void settings() {
        size(800, 600);
    }

    @Override
    public void setup() {
        frameRate(60);
        ball = new Ball(width / 2, height / 2, 20, 3, 3);
        starship = new Starship(width / 2 - 50, height - 30, 100, 20, 5);
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();

        // Initialize bricks
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                bricks.add(new Brick(i * 60 + 30, j * 30 + 30, 50, 20));
            }
        }
    }

    @Override
    public void draw() {
        background(0);
        ball.update();
        ball.checkBoundaryCollision(width, height);

        // Draw ball and starship
        ball.draw(this);
        starship.draw(this);

        // Draw bricks
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                brick.draw(this);
            }
        }

        // Draw power-ups
        for (PowerUp powerUp : powerUps) {
            powerUp.draw(this);
        }
    }

    @Override
    public void keyPressed() {
        if (key == 'a' || key == 'A') {
            starship.moveLeft();
        } else if (key == 'd' || key == 'D') {
            starship.moveRight();
        } else if (key == 'f' || key == 'F') {
            toggleFullscreen();
        }
    }

    private void toggleFullscreen() {
        isFullscreen = !isFullscreen;
        surface.setVisible(false);
        noLoop();
        if (isFullscreen) {
            surface.setSize(displayWidth, displayHeight);
        } else {
            surface.setSize(800, 600);
        }
        loop();
    }
}
