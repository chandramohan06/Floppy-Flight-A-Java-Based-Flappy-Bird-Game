import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    int birdY = 200, velocity = 0, gravity = 1, jumpStrength = -12;
    ArrayList<Rectangle> pipes;
    int pipeSpeed = 5, pipeGap = 150;
    Timer timer;
    int score = 0;
    boolean gameOver = false;

    public GamePanel() {
        this.setPreferredSize(new Dimension(400, 600));
        this.setBackground(Color.cyan);
        this.setFocusable(true);
        this.addKeyListener(this);

        pipes = new ArrayList<>();
        timer = new Timer(20, this);  // ~50 FPS
        addPipe(true); addPipe(true); addPipe(true); addPipe(true);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Ground
        g.setColor(Color.orange);
        g.fillRect(0, 550, 400, 50);

        // Bird
        g.setColor(Color.red);
        g.fillOval(100, birdY, 20, 20);

        // Pipes
        g.setColor(Color.green);
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        // Score
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 10, 30);

        if (gameOver) {
            g.drawString("Game Over!", 120, 300);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            velocity += gravity;
            birdY += velocity;

            for (int i = 0; i < pipes.size(); i++) {
                Rectangle pipe = pipes.get(i);
                pipe.x -= pipeSpeed;

                if (pipe.intersects(new Rectangle(100, birdY, 20, 20))) {
                    gameOver = true;
                }
            }

            if (pipes.get(0).x + pipes.get(0).width < 0) {
                pipes.remove(0);
                pipes.remove(0);
                addPipe(false);
            }

            for (Rectangle pipe : pipes) {
                if (pipe.x == 100) {
                    score++;
                }
            }

            if (birdY > 530 || birdY < 0) {
                gameOver = true;
            }
        }

        repaint();
    }

    public void addPipe(boolean start) {
        int height = 50 + new Random().nextInt(300);
        int space = pipeGap;
        int width = 60;

        if (start) {
            pipes.add(new Rectangle(400 + pipes.size() * 200, 0, width, height));
            pipes.add(new Rectangle(400 + pipes.size() * 200, height + space, width, 600 - height - space));
        } else {
            pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x + 200, 0, width, height));
            pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x, height + space, width, 600 - height - space));
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
            velocity = jumpStrength;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE && gameOver) {
            resetGame();
        }
    }

    public void resetGame() {
        birdY = 200;
        velocity = 0;
        pipes.clear();
        addPipe(true); addPipe(true); addPipe(true); addPipe(true);
        score = 0;
        gameOver = false;
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
