import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainMenuPanel extends JPanel {

    public MainMenuPanel(AplikasiTicTacToe app) {
        setLayout(new GridBagLayout());
        setBackground(Color.DARK_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Tic-Tac-Toe: Derby Manchester", SwingConstants.CENTER);
        title.setFont(new Font("OCR A Extended", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        JButton startButton = createMenuButton("Mulai Main");
        JButton scoresButton = createMenuButton("Skor Paling Dhuwur");
        JButton settingsButton = createMenuButton("Pengaturan");
        JButton exitButton = createMenuButton("Metu");

        startButton.addActionListener(e -> app.showGameSetup());
        scoresButton.addActionListener(e -> app.showHighScores());
        settingsButton.addActionListener(e -> app.showSettings());
        exitButton.addActionListener(e -> System.exit(0));

        add(title, gbc);
        add(Box.createVerticalStrut(20), gbc); // Spasi
        add(startButton, gbc);
        add(scoresButton, gbc);
        add(settingsButton, gbc);
        add(exitButton, gbc);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusPainted(false);
        return button;
    }
}