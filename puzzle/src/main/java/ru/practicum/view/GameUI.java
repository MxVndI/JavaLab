package ru.practicum.view;

import ru.practicum.controller.GameController;
import ru.practicum.model.logic.GameModel;
import ru.practicum.listener.ModelListener;
import ru.practicum.model.map.*;
import ru.practicum.model.map.strategies.FiveMapStrategy;
import ru.practicum.model.map.strategies.SevenMapStrategy;
import ru.practicum.model.map.strategies.SixMapStrategy;
import ru.practicum.model.tile.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GameUI extends JFrame implements ModelListener {
    private GameModel model;
    private GameController controller;
    private JPanel boardPanel;
    private JButton[][] tileButtons;

    public GameUI() {
        initializeUI();
        showMainMenu();
    }

    private void initializeUI() {
        setTitle("Пятнашки");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(245, 245, 245));
    }

    public void initialize(GameModel model, GameController controller) {
        this.model = model;
        this.controller = controller;
        model.addListener(this);
        model.addWinListener(this::showFireworks);
    }

    private void showMainMenu() {
        getContentPane().removeAll();

        JPanel menuPanel = new JPanel(new GridLayout(4, 1, 0, 20));
        menuPanel.setBackground(new Color(245, 245, 245));

        JLabel titleLabel = new JLabel("Пятнашки", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(new Color(50, 50, 50));
        menuPanel.add(titleLabel);

        JComboBox<String> sizeComboBox = new JComboBox<>(new String[]{"5x5", "6x6", "7x7"});
        sizeComboBox.setFont(new Font("Arial", Font.PLAIN, 20));
        sizeComboBox.setBackground(Color.WHITE);
        sizeComboBox.setForeground(new Color(50, 50, 50));
        sizeComboBox.setFocusable(false);
        menuPanel.add(sizeComboBox);

        JButton startButton = createStyledButton("Начать игру");
        startButton.addActionListener(e -> {
            String selected = (String) sizeComboBox.getSelectedItem();
            MapStrategy strategy;
            switch (selected) {
                case "5x5":
                    strategy = new FiveMapStrategy();
                    break;
                case "6x6":
                    strategy = new SixMapStrategy();
                    break;
                default:
                    strategy = new SevenMapStrategy();
            }
            startGame(strategy);
        });

        JButton exitButton = createStyledButton("Выйти");
        exitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(startButton);
        menuPanel.add(exitButton);

        add(menuPanel);
        revalidate();
        repaint();
    }

    private void showFireworks() {
        getContentPane().removeAll();
        add(new FireworksPanel());
        revalidate();
        repaint();
    }

    private void createGameBoard() {
        getContentPane().removeAll();

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(createBackButton(), BorderLayout.WEST);
        topPanel.add(new JLabel("Пятнашки", SwingConstants.CENTER), BorderLayout.CENTER);

        initBoardPanel();

        add(topPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JButton createBackButton() {
        JButton button = new JButton("Назад");
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(220, 220, 220));
        button.addActionListener(e -> showMainMenu());
        return button;
    }

    private void initBoardPanel() {
        Tile[][] board = model.getBoard();
        int size = board.length;

        boardPanel = new JPanel(new GridLayout(size, size, 5, 5));
        boardPanel.setBackground(new Color(245, 245, 245));
        tileButtons = new JButton[size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Tile tile = board[row][col];
                JButton button = createTileButton(tile);
                tileButtons[row][col] = button;
                boardPanel.add(button);
            }
        }
    }

    private JButton createTileButton(Tile tile) {
        JButton button = new JButton();
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(80, 80));
        button.setBackground(tile.getValue() == 0 ?
                new Color(240, 240, 240) : new Color(70, 130, 180));
        button.setForeground(Color.WHITE);

        if (tile.getValue() != 0) {
            button.setText(String.valueOf(tile.getValue()));
            button.addActionListener(new TileClickListener(tile));
        }

        return button;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    private void startGame(MapStrategy strategy) {
        if (model != null) {
            model.removeListener(this);
        }

        GameModel newModel = new GameModel();
        newModel.start(strategy);
        GameController newController = new GameController(newModel);
        initialize(newModel, newController);

        getContentPane().removeAll();
        createGameBoard();
        revalidate();
        repaint();
    }

    @Override
    public void onModelChange() {
        updateTileButtons();
    }

    private void updateTileButtons() {
        Tile[][] board = model.getBoard();
        int size = board.length;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Tile tile = board[row][col];
                JButton button = tileButtons[row][col];

                button.setText(tile.getValue() == 0 ? "" : String.valueOf(tile.getValue()));
                button.setBackground(tile.getValue() == 0 ?
                        new Color(240, 240, 240) : new Color(70, 130, 180));

                ActionListener[] listeners = button.getActionListeners();
                if (tile.getValue() != 0) {
                    if (listeners.length == 0) {
                        button.addActionListener(new TileClickListener(tile));
                    }
                } else {
                    for (ActionListener al : listeners) {
                        button.removeActionListener(al);
                    }
                }
            }
        }
    }

    private class TileClickListener implements ActionListener {
        private final Tile tile;

        public TileClickListener(Tile tile) {
            this.tile = tile;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            controller.handleTileClick(tile);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameUI view = new GameUI();
            view.setVisible(true);
        });
    }

    private class FireworksPanel extends JPanel {
        private final ArrayList<Particle> particles = new ArrayList();
        private final Timer timer;

        public FireworksPanel() {
            setOpaque(false);
            generateParticles();
            timer = new Timer(20, e -> {
                updateParticles();
                repaint();
            });
            timer.start();
        }

        private void generateParticles() {
            Random random = new Random();
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            for (int i = 0; i < 150; i++) {
                double angle = Math.toRadians(random.nextDouble() * 360);
                double speed = 3 + random.nextDouble() * 5;
                Color color = new Color(
                        random.nextInt(256),
                        random.nextInt(256),
                        random.nextInt(256)
                );
                particles.add(new Particle(centerX, centerY, angle, speed, color));
            }
        }

        private void updateParticles() {
            Iterator<Particle> it = particles.iterator();
            while (it.hasNext()) {
                Particle p = it.next();
                p.update();
                if (p.isDead()) {
                    it.remove();
                }
            }
            if (particles.isEmpty()) {
                timer.stop();
                showMainMenu();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            for (Particle p : particles) {
                p.draw(g2d);
            }
        }
    }

    private static class Particle {
        private double x, y;
        private final double vx, vy;
        private final Color color;
        private int lifetime = 50;

        public Particle(double x, double y, double angle, double speed, Color color) {
            this.x = x;
            this.y = y;
            this.vx = Math.cos(angle) * speed;
            this.vy = Math.sin(angle) * speed;
            this.color = color;
        }

        public void update() {
            x += vx;
            y += vy;
            lifetime--;
        }

        public boolean isDead() {
            return lifetime <= 0;
        }

        public void draw(Graphics2D g2d) {
            g2d.setColor(color);
            g2d.fillOval((int) x, (int) y, 8, 8);
        }
    }
}