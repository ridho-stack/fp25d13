import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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

    public enum GameMode { VS_PLAYER, VS_AI }
    private GameMode gameMode;
    private final AplikasiTicTacToe app;

    public GameMain(AplikasiTicTacToe app) {
        this.app = app;

        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentState == State.PLAYING) {
                    int mouseX = e.getX(), mouseY = e.getY();
                    int row = mouseY / Cell.SIZE;
                    int col = mouseX / Cell.SIZE;

                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {

                        if (currentPlayer == Seed.CROSS || gameMode == GameMode.VS_PLAYER) {
                            makeMove(row, col);
                            repaint();
                        }

                        if (gameMode == GameMode.VS_AI && currentState == State.PLAYING && currentPlayer == Seed.NOUGHT) {
                            aiMove();
                            repaint();
                        }
                    }
                } else {
                    newRound();
                    repaint();
                }
            }
        });

        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(0, 30)); // Lebar diatur 0 agar fleksibel
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END);
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        initGame();
    }

    public void initGame() {
        board = new Board();
    }

    public void startNewGame(GameMode mode, String p1Name, String p2Name) {
        this.gameMode = mode;
        this.playerXName = p1Name;
        this.playerOName = p2Name;
        this.playerXScore = 0;
        this.playerOScore = 0;
        newRound();
        repaint();
    }

    public void newRound() {
        board.newGame();
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
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
                showWinnerAndExit(playerXName);
                return;
            }
        } else if (currentState == State.NOUGHT_WON) {
            playerOScore++;
            if (playerOScore == WINNING_SCORE) {
                showWinnerAndExit(playerOName);
                return;
            }
        }

        if(currentState != State.PLAYING) return;

        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }

    public void aiMove() {
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                if (board.cells[row][col].content == Seed.NO_SEED) {
                    makeMove(row, col);
                    return;
                }
            }
        }
    }

    public void showWinnerAndExit(String winnerName) {
        DatabaseManager.saveWinner(winnerName);

        repaint();

        JOptionPane.showMessageDialog(this,
                winnerName + " menang dengan skor akhir 3! Wayahe Tampil Bolo!");

        app.showMainMenu();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);
        board.paint(g);

        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            String turn = (currentPlayer == Seed.CROSS) ? playerXName : playerOName;
            statusBar.setText(turn + " wayahe Tampil | Skor: " + playerXName + " " + playerXScore + " - " + playerOScore + " " + playerOName);
        } else {
            statusBar.setForeground(Color.RED);
            if (currentState == State.DRAW) {
                statusBar.setText("Imbang! Klik untuk ronde maneh.");
            } else if (currentState == State.CROSS_WON) {
                statusBar.setText(playerXName + " Menang ronde! Klik untuk lanjut.");
            } else if (currentState == State.NOUGHT_WON) {
                statusBar.setText(playerOName + " Menang ronde! Klik untuk lanjut.");
            }
        }
    }
}