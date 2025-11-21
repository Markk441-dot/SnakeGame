import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainMenuJewelChase extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;

    // menu options
    String[] options = {"New Game", "Load Game", "Help", "Settings", "Exit Game"};
    int currentSelection = 0;

    // Game State: 0 = Menu, 1 = Game
    int gameState = 0;

    MainMenuJewelChase() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new SelectionKeys());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Clears the screen

        if (gameState == 0) {
            drawMainMenu(g);
        } else if (gameState == 1) {
            drawGameScreen(g);
        }
    }

    public void drawMainMenu(Graphics g) {
        // 1. Draw Title
        g.setColor(Color.CYAN);
        g.setFont(new Font("Ink Free", Font.BOLD, 50));
        FontMetrics fm = g.getFontMetrics();
        String title = "Jewel Chase";
        g.drawString(title, (SCREEN_WIDTH - fm.stringWidth(title)) / 2, 100);

        // 2. Draw Menu Options
        g.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics fm2 = g.getFontMetrics();

        for (int i = 0; i < options.length; i++) {
            if (i == currentSelection) {
                g.setColor(Color.YELLOW);
                String selection = "> " + options[i] + " <";
                g.drawString(selection, (SCREEN_WIDTH - fm2.stringWidth(selection)) / 2, 200 + (i * 50));
            } else {
                g.setColor(Color.GRAY);
                g.drawString(options[i], (SCREEN_WIDTH - fm2.stringWidth(options[i])) / 2, 200 + (i * 50));
            }
        }

        // 3. Draw Instructions
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Use W/S or Arrow Keys to Select. Press Enter to Confirm.", 140, SCREEN_HEIGHT - 50);
    }

    public void drawGameScreen(Graphics g) {
        // Visual confirmation we are in the game
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Game is running", 200, 300);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.drawString("Press ESCAPE to return to Main Menu", 180, 350);
    }

    public void selectOption() {
        switch (currentSelection) {
            case 0: // New Game
                System.out.println("Action: Starting New Game...");
                gameState = 1; // Switch state to game
                break;
            case 1: // Load Game
                System.out.println("Action: Loading Game...");
                break;
            case 2: // Help
                JOptionPane.showMessageDialog(this, "67");
                break;
            case 3: // Settings
                System.out.println("Action: Opening Settings...");
                break;
            case 4: // Exit
                System.exit(0);
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public class SelectionKeys extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            // --- MENU NAVIGATION ---
            if (gameState == 0) {
                switch (key) {
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                        if (currentSelection > 0) {
                            currentSelection--;
                        } else {
                            currentSelection = options.length - 1;
                        }
                        break;

                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        if (currentSelection < options.length - 1) {
                            currentSelection++;
                        } else {
                            currentSelection = 0;
                        }
                        break;

                    case KeyEvent.VK_ENTER:
                        selectOption();
                        break;
                }
            }
            // --- GAME NAVIGATION (ESCAPE) ---
            else if (gameState == 1) {
                if (key == KeyEvent.VK_ESCAPE) {
                    gameState = 0; // Simply set state back to 0 to return to menu
                }
            }

            // Update screen
            repaint();
        }
    }

    // --- MAIN METHOD ---
    public static void main(String[] args) {
        JFrame frame = new JFrame("Jewel Chase");
        MainMenuJewelChase gamePanel = new MainMenuJewelChase();

        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}