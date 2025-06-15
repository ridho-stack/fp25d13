import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Tic-Tac-Toe: Manchester Derby Edition with Player Name Input and Point System
 */
public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Tic Tac Toe - Manchester Derby";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);   // MU Red
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225);  // City Blue
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;

    private String playerXName;
    private String playerOName;
    private int playerXScore = 0, playerOScore = 0;
    private final int WINNING_SCORE = 3;

    public GameMain() {
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {

                        currentState = board.stepGame(currentPlayer, row, col);

                        if (currentState == State.PLAYING) {
                            SoundEffect.EAT_FOOD.play();
                        } else {
                            SoundEffect.DIE.play();
                        }

                        if (currentState == State.CROSS_WON) {
                            playerXScore++;
                            if (playerXScore == WINNING_SCORE) {
                                JOptionPane.showMessageDialog(GameMain.this,
                                        playerXName + " menang dengan skor 3 dan jadi pemenang utama!");
                                resetGame();
                                return;
                            }
                        } else if (currentState == State.NOUGHT_WON) {
                            playerOScore++;
                            if (playerOScore == WINNING_SCORE) {
                                JOptionPane.showMessageDialog(GameMain.this,
                                        playerOName + " menang dengan skor 3 dan jadi pemenang utama!");
                                resetGame();
                                return;
                            }
                        }

                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                    }
                } else {
                    newGame();
                }

                repaint();
            }
        });

        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END);
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        initGame();
        inputPlayerNames();
        newGame();
    }

    public void initGame() {
        board = new Board();
    }

    public void inputPlayerNames() {
        playerXName = JOptionPane.showInputDialog(null,
                "Masukkan nama pemain untuk Manchester United (X):", "Input Nama Pemain",
                JOptionPane.QUESTION_MESSAGE);
        if (playerXName == null || playerXName.trim().isEmpty()) {
            playerXName = "Manchester United";
        }

        playerOName = JOptionPane.showInputDialog(null,
                "Masukkan nama pemain untuk Manchester City (O):", "Input Nama Pemain",
                JOptionPane.QUESTION_MESSAGE);
        if (playerOName == null || playerOName.trim().isEmpty()) {
            playerOName = "Manchester City";
        }
    }

    public void newGame() {
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED;
            }
        }

        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
        repaint();
    }

    private void resetGame() {
        playerXScore = 0;
        playerOScore = 0;
        inputPlayerNames();
        newGame();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);

        board.paint(g);

        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS ? playerXName : playerOName) + "'s Turn | "
                    + playerXName + ": " + playerXScore + " vs "
                    + playerOName + ": " + playerOScore);
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("Imbang! Klik untuk main lagi.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(playerXName + " menang ronde ini! Klik untuk lanjut.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(playerOName + " menang ronde ini! Klik untuk lanjut.");
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            frame.setContentPane(new GameMain());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
