import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    public SettingsPanel(AplikasiTicTacToe app) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Pengaturan", SwingConstants.CENTER);
        title.setFont(new Font("OCR A Extended", Font.BOLD, 24));

        JPanel volumePanel = new JPanel();
        volumePanel.setLayout(new FlowLayout());
        JLabel volumeLabel = new JLabel("Volume Suara:");
        JSlider volumeSlider = new JSlider(0, 3, SoundEffect.volume.ordinal());
        volumeSlider.setMajorTickSpacing(1);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setSnapToTicks(true);

        java.util.Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<>();
        labelTable.put(0, new JLabel("Mati"));
        labelTable.put(1, new JLabel("Cilik"));
        labelTable.put(2, new JLabel("Sedeng"));
        labelTable.put(3, new JLabel(" banter"));
        volumeSlider.setLabelTable(labelTable);

        volumeSlider.addChangeListener(e -> {
            int value = ((JSlider)e.getSource()).getValue();
            SoundEffect.volume = SoundEffect.Volume.values()[value];
        });

        volumePanel.add(volumeLabel);
        volumePanel.add(volumeSlider);

        JButton backButton = new JButton("Balik");
        backButton.addActionListener(e -> app.showMainMenu());

        JPanel centerPanel = new JPanel();
        centerPanel.add(volumePanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);

        add(title, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}