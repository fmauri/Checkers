package AlphaBeta;

/**
 * Created by Francesco Mauri on 5/14/2017.
 */

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class CheckersCanvas extends Canvas implements ActionListener, MouseListener {
    public JButton resignButton;
    public JButton newGameButton;
    public Label message;
    public MoveScheduler board;
    public boolean gameInProgress;
    public int currentPlayer;
    public int selectedRow, selectedCol;
    private ArrayList<Move> legalMoves = new ArrayList<>();
    boolean AI_White = false;
    boolean AI_Black = true;
    public AI ai;

    public CheckersCanvas() {
        setBackground(Color.black);
        addMouseListener(this);
        setFont(new Font("Serif", Font.BOLD, 14));
        resignButton = new JButton("Resign");
        resignButton.addActionListener(this);
        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(this);
        message = new Label("", Label.CENTER);
        board = new MoveScheduler();
        ai = new AI(board);
        doNewGame();
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src == newGameButton)
            doNewGame();
        else if (src == resignButton)
            doResign();
    }

    void doNewGame() {
        if (gameInProgress) {
            message.setText("Finish the current game first!");
            return;
        }
        board.getBoard().newGame(); // Set up the pieces.
        currentPlayer = Status.WHITE.getNumVal(); // White moves first.
        legalMoves = board.getLegalMoves(currentPlayer); // Get White's legal moves.
        selectedRow = -1; // White has not yet selected a piece to move.
        message.setText("White: Make your move.");
        gameInProgress = true;
        newGameButton.setEnabled(false);
        resignButton.setEnabled(true);
        repaint();
    }

    public void doResign() {
        if (!gameInProgress) {
            message.setText("There is no game in progress!");
            return;
        }
        if (currentPlayer == Status.WHITE.getNumVal())
            gameOver("White resigns. Black wins.");
        else
            gameOver("Black resigns. White winds.");
    }

    public void gameOver(String str) {
        message.setText(str);
        newGameButton.setEnabled(true);
        resignButton.setEnabled(false);
        gameInProgress = false;
    }

    public void doClickSquare(int row, int col) {
        for (int i = 0; i < legalMoves.size(); i++)
            if (legalMoves.get(i).getFromRow() == row && legalMoves.get(i).getFromCol() == col) {
                selectedRow = row;
                selectedCol = col;
                if (currentPlayer == Status.WHITE.getNumVal())
                    message.setText("White: Make your move.");
                else
                    message.setText("Black: Make your move.");
                repaint();
                return;
            }
        if (selectedRow < 0) {
            message.setText("Click the piece you want to move.");
            return;
        }
        for (int i = 0; i < legalMoves.size(); i++)
            if (legalMoves.get(i).getFromRow() == selectedRow && legalMoves.get(i).getFromCol() == selectedCol
                    && legalMoves.get(i).getToRow() == row && legalMoves.get(i).getToCol() == col) {
                doMakeMove(legalMoves.get(i));
                return;
            }
        message.setText("Click the square you want to move to.");

    }

    public void doMakeMove(Move move) {
        board.makeMove(move);
        if (move.isEat()) {
            legalMoves = board.getLegalJumps(currentPlayer, move.getToRow(), move.getToCol());
            if (!legalMoves.isEmpty()) {
                if (currentPlayer == Status.WHITE.getNumVal()) {
                    message.setText("White: You must continue jumping.");
                } else {
                    message.setText("Black: You must continue jumping.");
                }
                selectedRow = move.getToRow();
                selectedCol = move.getToCol();
                repaint();
                if ((AI_Black && this.currentPlayer < 0) || (AI_White && this.currentPlayer > 0)) {
                    int rounds = 5;
                    if ((this.board.getBoard().isBlackWinning() && this.AI_White)
                            || (!this.board.getBoard().isBlackWinning() && this.AI_Black)) {
                        rounds = 7;
                    }
                    doMakeMove(this.ai.minMaxRecursive(legalMoves, this.currentPlayer, rounds, this.board).getMove());
                    //doMakeMove(this.ai.alphaBeta(legalMoves, this.currentPlayer));
                    //doMakeMove(this.ai.chooseMoveRandom(legalMoves));
                }
                return;
            }
        }
        if (currentPlayer == Status.WHITE.getNumVal()) {
            currentPlayer = Status.BLACK.getNumVal();
            legalMoves = board.getLegalMoves(currentPlayer);
            if (legalMoves.isEmpty()) {
                gameOver("Black has no moves. White wins.");
            } else if (legalMoves.get(0).isEat()) {
                message.setText("Black: Make your move.  You must jump.");
            } else {
                message.setText("Black: Make your move.");
            }
        } else if (currentPlayer == Status.BLACK.getNumVal()) {
            currentPlayer = Status.WHITE.getNumVal();
            legalMoves = board.getLegalMoves(currentPlayer);
            if (legalMoves.isEmpty())
                gameOver("White has no moves. Black wins.");
            else if (legalMoves.get(0).isEat()) {
                message.setText("White: Make your move. You must jump.");
            } else {
                message.setText("White: Make your move.");
            }
        }
        selectedRow = -1;
        if (!legalMoves.isEmpty()) {
            boolean sameStartSquare = true;
            for (int i = 1; i < legalMoves.size(); i++)
                if (legalMoves.get(i).getFromRow() != legalMoves.get(0).getFromRow() || legalMoves.get(i).getFromCol() != legalMoves.get(0).getFromCol()) {
                    sameStartSquare = false;
                    break;
                }
            if (sameStartSquare) {
                selectedRow = legalMoves.get(0).getFromRow();
                selectedCol = legalMoves.get(0).getFromCol();
            }
        }
        repaint();
        legalMoves = board.getLegalMoves(currentPlayer);
        if (!legalMoves.isEmpty() && (AI_Black && this.currentPlayer < 0) || (AI_White && this.currentPlayer > 0)) {
            int rounds = 5;
            if ((this.board.getBoard().isBlackWinning() && this.AI_White)
                    || (!this.board.getBoard().isBlackWinning() && this.AI_Black)) {
                rounds = 7;
            }
            doMakeMove(this.ai.minMaxRecursive(legalMoves, this.currentPlayer, rounds, this.board).getMove());
            //doMakeMove(this.ai.alphaBeta(legalMoves, this.currentPlayer));
            //doMakeMove(this.ai.chooseMoveRandom(legalMoves));
        }
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
        g.drawRect(1, 1, getSize().width - 3, getSize().height - 3);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row % 2 == col % 2)
                    g.setColor(Color.lightGray);
                else
                    g.setColor(Color.gray);
                g.fillRect(2 + col * 20, 2 + row * 20, 20, 20);
                if (board.getBoard().getValueAt(row, col) == Status.WHITE.getNumVal()) {
                    g.setColor(Color.white);
                    g.fillOval(4 + col * 20, 4 + row * 20, 16, 16);
                } else if (board.getBoard().getValueAt(row, col) == Status.BLACK.getNumVal()) {
                    g.setColor(Color.black);
                    g.fillOval(4 + col * 20, 4 + row * 20, 16, 16);
                } else if (board.getBoard().getValueAt(row, col) == Status.WHITE_PIECE_PROMOTED.getNumVal()) {
                    g.setColor(Color.white);
                    g.fillOval(4 + col * 20, 4 + row * 20, 16, 16);
                    g.setColor(Color.black);
                    g.drawString("K", 7 + col * 20, 16 + row * 20);
                } else if (board.getBoard().getValueAt(row, col) == Status.BLACK_PIECE_PROMOTED.getNumVal()) {
                    g.setColor(Color.black);
                    g.fillOval(4 + col * 20, 4 + row * 20, 16, 16);
                    g.setColor(Color.white);
                    g.drawString("K", 7 + col * 20, 16 + row * 20);
                }
            }
        }
        if (gameInProgress) {
            g.setColor(Color.cyan);
            for (int i = 0; i < legalMoves.size(); i++) {
                g.drawRect(2 + legalMoves.get(i).getFromCol() * 20, 2 + legalMoves.get(i).getFromRow() * 20, 19, 19);
            }
            if (selectedRow >= 0) {
                g.setColor(Color.white);
                g.drawRect(2 + selectedCol * 20, 2 + selectedRow * 20, 19, 19);
                g.drawRect(3 + selectedCol * 20, 3 + selectedRow * 20, 17, 17);
                g.setColor(Color.green);
                for (int i = 0; i < legalMoves.size(); i++) {
                    if (legalMoves.get(i).getFromCol() == selectedCol && legalMoves.get(i).getFromRow() == selectedRow)
                        g.drawRect(2 + legalMoves.get(i).getToCol() * 20, 2 + legalMoves.get(i).getToRow() * 20, 19, 19);
                }
            }
        }

    }

    public Dimension getPreferredSize() {
        return new Dimension(164, 164);
    }

    public Dimension getMinimumSize() {
        return new Dimension(164, 164);
    }

    public void mousePressed(MouseEvent evt) {
        if (!gameInProgress)
            message.setText("Click \"New Game\" to start a new game.");
        else {
            int col = (evt.getX() - 2) / 20;
            int row = (evt.getY() - 2) / 20;
            if (col >= 0 && col < 8 && row >= 0 && row < 8)
                doClickSquare(row, col);
        }
    }

    public void mouseReleased(MouseEvent evt) {
    }

    public void mouseClicked(MouseEvent evt) {
    }

    public void mouseEntered(MouseEvent evt) {
    }

    public void mouseExited(MouseEvent evt) {
    }
}
