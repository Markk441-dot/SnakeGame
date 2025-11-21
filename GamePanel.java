import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int Unit_Size = 25;
    static final int Game_Units = (SCREEN_WIDTH * SCREEN_HEIGHT) / Unit_Size;
    static final int Delay = 75;
    final int x[] = new int[Game_Units];
    final int y[] = new int[Game_Units];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';

    // Game States: 0 = Menu, 1 = Running, 2 = Game Over
    int gameState = 0;
    int menuOption = 0; // 0 = Start Game,  = Exit Game

    Timer timer;
    Random rand;

    GamePanel() {
        rand = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.CYAN);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        // Do not start game immediately, let the menu handle it
    }

    public void startGame() {
        newApple();
        gameState = 1; // Set state to Running
        bodyParts = 6;
        applesEaten = 0;
        direction = 'R';

        // Reset snake position (optional, prevents instant death if restarting)
        for(int i=0; i<bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }

        if (timer == null) {
            timer = new Timer(Delay, this);
            timer.start();
        } else {
            timer.restart();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (gameState == 0) {
            drawMenu(g);
        } else if (gameState == 1) {
            // Draw Game Elements
            for (int i = 0; i < SCREEN_HEIGHT / Unit_Size; i++) {
                g.drawLine(i * Unit_Size, 0, i * Unit_Size, SCREEN_HEIGHT);
                g.drawLine(0, i * Unit_Size, SCREEN_WIDTH, i * Unit_Size);
            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, Unit_Size, Unit_Size);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], Unit_Size, Unit_Size);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], Unit_Size, Unit_Size);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("INk Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void drawMenu(Graphics g) {
        // Title
        g.setColor(Color.red);
        g.setFont(new Font("INk Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("SNAKE GAME", (SCREEN_WIDTH - metrics.stringWidth("SNAKE GAME")) / 2, SCREEN_HEIGHT / 3);

        // Menu Options
        g.setFont(new Font("INk Free", Font.BOLD, 40));
        FontMetrics metrics3 = getFontMetrics(g.getFont());

        // Option 1: Start Game
        if (menuOption == 0) {
            g.setColor(Color.yellow); // Selected color
            g.drawString("> Start Game <", (SCREEN_WIDTH - metrics3.stringWidth("> Start Game <")) / 2, SCREEN_HEIGHT / 2);
        } else {
            g.setColor(Color.red); // Unselected color
            g.drawString("Start Game", (SCREEN_WIDTH - metrics3.stringWidth("Start Game")) / 2, SCREEN_HEIGHT / 2);
        }

        // Option 2: Exit Game
        if (menuOption == 1) {
            g.setColor(Color.yellow);
            g.drawString("> Exit Game <", (SCREEN_WIDTH - metrics3.stringWidth("> Exit Game <")) / 2, SCREEN_HEIGHT / 2 + 60);
        } else {
            g.setColor(Color.red);
            g.drawString("Exit Game", (SCREEN_WIDTH - metrics3.stringWidth("Exit Game")) / 2, SCREEN_HEIGHT / 2 + 60);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.drawString("Use Up/Down arrows to select, Enter to confirm", 140, SCREEN_HEIGHT - 50);
    }

    public void newApple() {
        appleX = rand.nextInt((int) (SCREEN_WIDTH / Unit_Size)) * Unit_Size;
        appleY = rand.nextInt((int) (SCREEN_HEIGHT / Unit_Size)) * Unit_Size;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'R':
                x[0] = x[0] + Unit_Size;
                break;
            case 'L':
                x[0] = x[0] - Unit_Size;
                break;
            case 'U':
                y[0] = y[0] - Unit_Size;
                break;
            case 'D':
                y[0] = y[0] + Unit_Size;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollision() {
        // check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i] && y[0] == y[i])) {
                gameState = 2; // Game Over
            }
        }
        // check if head touches left border
        if (x[0] < 0) {
            gameState = 2;
        }
        // check right border
        if (x[0] > SCREEN_WIDTH) {
            gameState = 2;
        }
        // check if head touches top
        if (y[0] < 0) {
            gameState = 2;
        }
        // check bottom
        if (y[0] > SCREEN_HEIGHT) {
            gameState = 2;
        }

        if (gameState == 2) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        // game over text
        g.setColor(Color.red);
        g.setFont(new Font("INk Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GameOver", (SCREEN_WIDTH - metrics.stringWidth("GameOver")) / 2, SCREEN_HEIGHT / 2);
        g.setColor(Color.red);
        g.setFont(new Font("INk Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());

        // Instruction to restart (Optional)
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Press Enter to Return to Menu", (SCREEN_WIDTH - metrics.stringWidth("GameOver")) / 2 + 40, SCREEN_HEIGHT / 2 + 100);
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (gameState == 0) {
                // MENU NAVIGATION
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (menuOption > 0) {
                            menuOption--;
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (menuOption < 1) {
                            menuOption++;
                        }
                        break;
                    case KeyEvent.VK_ENTER:
                        if (menuOption == 0) {
                            startGame();
                        } else if (menuOption == 1) {
                            System.exit(0);
                        }
                        break;
                }
                repaint(); // Repaint to show selection change
            } else if (gameState == 1) {
                // GAME CONTROLS
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') {
                            direction = 'L';
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') {
                            direction = 'R';
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (direction != 'D') {
                            direction = 'U';
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') {
                            direction = 'D';
                        }
                        break;
                }
            } else if (gameState == 2) {
                // GAME OVER CONTROLS
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    gameState = 0; // Go back to menu
                    repaint();
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameState == 1) { // Only run game logic if state is Running
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }
}