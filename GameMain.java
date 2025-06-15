import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Tic-Tac-Toe with Player vs Player or vs AI mode.
 */
public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Tic Tac Toe - Manchester Derby";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225);
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;

    private String playerXName;
    private String playerOName;
    private int playerXScore = 0, playerOScore = 0;
    private final int WINNING_SCORE = 3;

    private enum GameMode { VS_PLAYER, VS_AI }
    private GameMode gameMode;

    public GameMain() {
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentState != State.PLAYING) {
                    newGame();
                    repaint();
                    return;
                }

                int mouseX = e.getX(), mouseY = e.getY();
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                        && board.cells[row][col].content == Seed.NO_SEED) {

                    if (currentPlayer == Seed.CROSS || gameMode == GameMode.VS_PLAYER) {
                        makeMove(row, col);
                    }
                }

                if (gameMode == GameMode.VS_AI && currentState == State.PLAYING && currentPlayer == Seed.NOUGHT) {
                    aiMove();
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
        chooseGameMode();
        inputPlayerNames();
        newGame();
    }

    public void initGame() {
        board = new Board();
    }

    public void chooseGameMode() {
        Object[] options = {"Menungso", "Komputer"};
        int choice = JOptionPane.showOptionDialog(null,
                "Pengen Main ambek sopo ?", "Main Ambek Sopo?",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        gameMode = (choice == 1) ? GameMode.VS_AI : GameMode.VS_PLAYER;
    }

    public void inputPlayerNames() {
        playerXName = JOptionPane.showInputDialog(null,
                "Lebokno Jeneng  pemain MU:",
                "Jeneng Pemain", JOptionPane.QUESTION_MESSAGE);
        if (playerXName == null || playerXName.trim().isEmpty()) {
            playerXName = "Manchester United";
        }

        if (gameMode == GameMode.VS_PLAYER) {
            playerOName = JOptionPane.showInputDialog(null,
                    "Lebokno Jeneng  pemain City:",
                    "Jeneng Pemain", JOptionPane.QUESTION_MESSAGE);
            if (playerOName == null || playerOName.trim().isEmpty()) {
                playerOName = "Manchester City";
            }
        } else {
            playerOName = "AI - Manchester City";
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

        if (gameMode == GameMode.VS_AI && currentPlayer == Seed.NOUGHT) {
            aiMove();
        }

        repaint();
    }

    public void resetGame() {
        playerXScore = 0;
        playerOScore = 0;
        chooseGameMode();
        inputPlayerNames();
        newGame();
    }

    public void makeMove(int row, int col) {
        if (board.cells[row][col].content != Seed.NO_SEED) return;

        currentState = board.stepGame(currentPlayer, row, col);
        if (currentState == State.PLAYING) {
            SoundEffect.EAT_FOOD.play();
        } else {
            SoundEffect.DIE.play();
        }

        if (currentState == State.CROSS_WON) {
            playerXScore++;
            if (playerXScore == WINNING_SCORE) {
                showWinner(playerXName);
                return;
            }
        } else if (currentState == State.NOUGHT_WON) {
            playerOScore++;
            if (playerOScore == WINNING_SCORE) {
                showWinner(playerOName);
                return;
            }
        }

        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }

    public void aiMove() {
        // Simple AI: choose first available cell
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                if (board.cells[row][col].content == Seed.NO_SEED) {
                    makeMove(row, col);
                    return;
                }
            }
        }
    }

    public void showWinner(String winnerName) {
        JOptionPane.showMessageDialog(this,
                winnerName + " menang,skor e 3, Wayahe Tampil Bolo!");
        resetGame();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);

        board.paint(g);

        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS ? playerXName : playerOName) + " wayahe Tampil | "
                    + playerXName + ": " + playerXScore + " vs " + playerOName + ": " + playerOScore);
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("Imbang! Main Maneh.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(playerXName + " Menang,Wayahe Lanjut!!.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(playerOName + " Menang,Wayahe Lanjut!!.");
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