import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HighScoresPanel extends JPanel {
    private final AplikasiTicTacToe app;
    private final DefaultListModel<String> listModel;

    public HighScoresPanel(AplikasiTicTacToe app) {
        this.app = app;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Skor Paling Dhuwur", SwingConstants.CENTER);
        title.setFont(new Font("OCR A Extended", Font.BOLD, 24));

        listModel = new DefaultListModel<>();
        JList<String> scoreList = new JList<>(listModel);
        scoreList.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(scoreList);

        JButton backButton = new JButton("Balik");
        backButton.addActionListener(e -> app.showMainMenu());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);

        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Method ini dipanggil setiap kali panel ditampilkan
    public void refreshScores() {
        listModel.clear();
        List<String> scores = DatabaseManager.getHighScores();
        if (scores.isEmpty()) {
            listModel.addElement("Durung onok sing menang, maino sek!");
        } else {
            for (String score : scores) {
                listModel.addElement(score);
            }
        }
    }
}