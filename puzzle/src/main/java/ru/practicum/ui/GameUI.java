package ru.practicum.ui;

import ru.practicum.Game;
import ru.practicum.map.MapStrategy;
import ru.practicum.map.FiveMapStrategy;
import ru.practicum.map.SevenMapStrategy;
import ru.practicum.map.SixMapStrategy;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameUI extends JFrame {
    private Game game;
    private MapStrategy currentStrategy;
    private JPanel boardPanel;
    private JButton[][] tiles;

    public GameUI() {
        setTitle("Пятнашки");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        showMainMenu();
    }

    private void showMainMenu() {
        getContentPane().removeAll();

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(4, 1, 0, 20));
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

        JButton exitButton = createStyledButton("Выход");
        exitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(startButton);
        menuPanel.add(exitButton);

        add(menuPanel);
        revalidate();
        repaint();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void startGame(MapStrategy strategy) {
        this.currentStrategy = strategy;
        this.game = new Game();
        this.game.start(strategy);
        createBoard();
    }

    private void createBoard() {
        getContentPane().removeAll();

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setPreferredSize(new Dimension(600, 100));

        JLabel titleLabel = new JLabel("Пятнашки", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(50, 50, 50));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        JButton backButton = createStyledButton("Назад");
        backButton.addActionListener(e -> showMainMenu());
        topPanel.add(backButton, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        boardPanel = new JPanel(new GridLayout(
                currentStrategy.getBoard().length,
                currentStrategy.getBoard()[0].length
        ));
        boardPanel.setBackground(new Color(245, 245, 245));

        int size = currentStrategy.getBoard().length;
        tiles = new JButton[size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int value = game.getBoard()[row][col];
                JButton tile = new JButton(value == 0 ? "" : String.valueOf(value));
                tile.setFont(new Font("Arial", Font.BOLD, 24));
                tile.setFocusPainted(false);

                if (value != 0) {
                    tile.setBackground(new Color(70, 130, 180));
                    tile.setForeground(Color.WHITE);
                    tile.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50), 2));
                    tile.addActionListener(new TileClickListener(row, col));
                } else {
                    tile.setBackground(new Color(240, 240, 240));
                    tile.setBorderPainted(false);
                }

                tiles[row][col] = tile;
                boardPanel.add(tile);
            }
        }

        add(boardPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void updateBoard() {
        int size = currentStrategy.getBoard().length;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int value = game.getBoard()[row][col];
                JButton tile = tiles[row][col];

                tile.setText(value == 0 ? "" : String.valueOf(value));
                tile.setBackground(value == 0 ?
                        new Color(240, 240, 240) :
                        new Color(70, 130, 180));

                ActionListener[] listeners = tile.getActionListeners();
                for (ActionListener listener : listeners) {
                    tile.removeActionListener(listener);
                }

                if (value != 0) {
                    tile.addActionListener(new TileClickListener(row, col));
                }
            }
        }

        if (game.isSolved()) {
            JOptionPane.showMessageDialog(this,
                    "Поздравляем! Вы собрали головоломку!",
                    "Победа",
                    JOptionPane.INFORMATION_MESSAGE);
            int response = JOptionPane.showConfirmDialog(this,
                    "Хотите сыграть снова?", "Новая игра",
                    JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                showMainMenu();
            } else {
                System.exit(0);
            }
        }
    }

    private class TileClickListener implements ActionListener {
        private int row;
        private int col;

        public TileClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            game.swapTiles(row, col);
                updateBoard();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameUI gameUI = new GameUI();
            gameUI.setVisible(true);
        });
    }
}