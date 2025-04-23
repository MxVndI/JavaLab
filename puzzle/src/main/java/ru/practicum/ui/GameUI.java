package ru.practicum.ui;

import ru.practicum.GameController;
import ru.practicum.GameModel;
import ru.practicum.ModelListener;
import ru.practicum.map.*;
import ru.practicum.model.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        createGameBoard();
    }

    private void showMainMenu() {
        getContentPane().removeAll(); // Очистка предыдущего содержимого

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
        // Удаляем старую модель и контроллер
        if (model != null) {
            model.removeListener(this);
        }

        GameModel newModel = new GameModel();
        newModel.start(strategy);
        GameController newController = new GameController(newModel);
        initialize(newModel, newController);

        // Пересоздаем UI
        getContentPane().removeAll();
        createGameBoard();
        revalidate();
        repaint();
    }

    @Override
    public void onModelChange() {
        updateTileButtons();
        checkWinCondition();
    }

    // В методе updateTileButtons добавьте обновление ActionListener
    private void updateTileButtons() {
        Tile[][] board = model.getBoard();
        int size = board.length;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Tile tile = board[row][col];
                JButton button = tileButtons[row][col];

                // Обновление текста и цвета
                button.setText(tile.getValue() == 0 ? "" : String.valueOf(tile.getValue()));
                button.setBackground(tile.getValue() == 0 ?
                        new Color(240, 240, 240) : new Color(70, 130, 180));

                // Обновление ActionListener
                ActionListener[] listeners = button.getActionListeners();
                if (tile.getValue() != 0) {
                    // Если плитка не пустая, добавляем обработчик (если его нет)
                    if (listeners.length == 0) {
                        button.addActionListener(new TileClickListener(tile));
                    }
                } else {
                    // Если плитка пустая, удаляем все обработчики
                    for (ActionListener al : listeners) {
                        button.removeActionListener(al);
                    }
                }
            }
        }
    }

    private void checkWinCondition() {
        if (model.isSolved()) {
            JOptionPane.showMessageDialog(this,
                    "Поздравляем! Вы собрали головоломку!",
                    "Победа",
                    JOptionPane.INFORMATION_MESSAGE);
            showMainMenu();
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
}