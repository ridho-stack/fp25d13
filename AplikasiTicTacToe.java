import javax.swing.*;
import java.awt.*;

public class AplikasiTicTacToe {

    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private GameMain gamePanel;
    private MainMenuPanel mainMenuPanel;
    private HighScoresPanel highScoresPanel;
    private SettingsPanel settingsPanel;

    public AplikasiTicTacToe() {
        frame = new JFrame(GameMain.TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainMenuPanel = new MainMenuPanel(this);
        gamePanel = new GameMain(this);
        highScoresPanel = new HighScoresPanel(this);
        settingsPanel = new SettingsPanel(this);

        mainPanel.add(mainMenuPanel, "MENU");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(highScoresPanel, "SCORES");
        mainPanel.add(settingsPanel, "SETTINGS");

        frame.add(mainPanel);

        showMainMenu();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        SoundEffect.initGame();
    }

    public void showMainMenu() {
        cardLayout.show(mainPanel, "MENU");
        frame.pack();
    }

    public void showSettings() {
        cardLayout.show(mainPanel, "SETTINGS");
        frame.pack();
    }

    public void showHighScores() {
        highScoresPanel.refreshScores();
        cardLayout.show(mainPanel, "SCORES");
        frame.pack();
    }

    public void showGameSetup() {
        Object[] modeOptions = {"Lawan Menungso", "Lawan Komputer"};
        int choice = JOptionPane.showOptionDialog(frame,
                "Pengen Main ambek sopo?", "Pilih Mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, modeOptions, modeOptions[0]);

        if (choice == -1) return;

        GameMain.GameMode selectedMode = (choice == 1) ? GameMain.GameMode.VS_AI : GameMain.GameMode.VS_PLAYER;

        String playerXName = JOptionPane.showInputDialog(frame, "Lebokno Jeneng pemain MU (X):", "Pemain 1", JOptionPane.QUESTION_MESSAGE);
        if (playerXName == null || playerXName.trim().isEmpty()) {
            playerXName = "Manchester United";
        }

        String playerOName;
        if (selectedMode == GameMain.GameMode.VS_PLAYER) {
            playerOName = JOptionPane.showInputDialog(frame, "Lebokno Jeneng pemain City (O):", "Pemain 2", JOptionPane.QUESTION_MESSAGE);
            if (playerOName == null || playerOName.trim().isEmpty()) {
                playerOName = "Manchester City";
            }
        } else {
            playerOName = "AI - Manchester City";
        }

        gamePanel.startNewGame(selectedMode, playerXName, playerOName);
        cardLayout.show(mainPanel, "GAME");
        frame.pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AplikasiTicTacToe::new);
    }
}