package ru.practicum;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class KeyboardTrainer extends JFrame {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;
    private static final int LANES = 3;
    private static final int LANE_HEIGHT = HEIGHT / LANES;
    private static final int LETTER_SPEED = 15;
    private static final int SPAWN_RATE = 1000;
    private static final int HIT_ZONE_START = 0;
    private static final Font LETTER_FONT = new Font("Arial", Font.BOLD, 36);

    private final List<Letter>[] laneLetters = new List[LANES];
    private int score = 0;
    private JLabel scoreLabel;

    public KeyboardTrainer() {
        for (int i = 0; i < LANES; i++) {
            laneLetters[i] = Collections.synchronizedList(new ArrayList<>());
        }

        setupUI();
        setupKeyListener();
        startLaneThreads();
        spawnLetters();
    }

    private void setupUI() {
        setTitle("Keyboard Trainer");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(scoreLabel, BorderLayout.NORTH);

        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < LANES; i++) {
                    synchronized (laneLetters[i]) {
                        laneLetters[i].forEach(letter -> letter.draw(g));
                    }
                }
            }
        };
        gamePanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        add(gamePanel, BorderLayout.CENTER);
    }

    private void setupKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char pressedChar = Character.toUpperCase(e.getKeyChar());
                for (int i = 0; i < LANES; i++) {
                    synchronized (laneLetters[i]) {
                        boolean hit = laneLetters[i].removeIf(letter ->
                                letter.getX() >= HIT_ZONE_START &&
                                        letter.getChar() == pressedChar
                        );
                        if (hit) {
                            score += 10;
                            scoreLabel.setText("Score: " + score);
                        }
                    }
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }

    private void startLaneThreads() {
        for (int i = 0; i < LANES; i++) {
            final int laneIndex = i;
            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        synchronized (laneLetters[laneIndex]) {
                            laneLetters[laneIndex].removeIf(letter -> letter.move() > WIDTH);
                        }
                        SwingUtilities.invokeLater(this::repaint);
                        Thread.sleep(16);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        }
    }

    private void spawnLetters() {
        new Timer(SPAWN_RATE, e -> {
            Random rand = new Random();
            char c = (char) ('A' + rand.nextInt(26));
            int lane = rand.nextInt(LANES);
            int y = lane * LANE_HEIGHT + 50;
            synchronized (laneLetters[lane]) {
                laneLetters[lane].add(new Letter(c, y));
            }
        }).start();
    }

    private static class Letter {
        private int x = 0;
        private final int y;
        private final char ch;

        public Letter(char ch, int y) {
            this.ch = ch;
            this.y = y;
        }

        public int move() {
            return x += LETTER_SPEED;
        }

        public char getChar() {
            return ch;
        }

        public int getX() {
            return x;
        }

        public void draw(Graphics g) {
            g.setColor(Color.BLUE);
            g.setFont(LETTER_FONT);
            g.drawString(String.valueOf(ch), x, y);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KeyboardTrainer().setVisible(true));
    }
}