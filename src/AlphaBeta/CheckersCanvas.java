package AlphaBeta;

/**
 * Created by Francesco Mauri on 5/14/2017.
 */

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class CheckersCanvas extends Canvas implements ActionListener, MouseListener {
    public Button resignButton;
    public Button newGameButton;
    public Label message;
    public MoveScheduler board;
    public boolean gameInProgress;
    public int currentPlayer;
    public int selectedRow, selectedCol;
    private ArrayList<Move> legalMoves = new ArrayList<>();

    public CheckersCanvas() {
        // Constructor. Create the buttons and lable. Listen for mouse
        // clicks and for clicks on the buttons. Create the board and
        // start the first game.
        setBackground(Color.black);
        addMouseListener(this);
        setFont(new Font("Serif", Font.BOLD, 14));
        resignButton = new Button("Resign");
        resignButton.addActionListener(this);
        newGameButton = new Button("New Game");
        newGameButton.addActionListener(this);
        message = new Label("", Label.CENTER);
        board = new MoveScheduler();
        doNewGame();
    }

    public void actionPerformed(ActionEvent evt) {
        // Respond to user's click on one of the two buttons.
        Object src = evt.getSource();
        if (src == newGameButton)
            doNewGame();
        else if (src == resignButton)
            doResign();
    }

    void doNewGame() {
        // Begin a new game.
        if (gameInProgress) {
            message.setText("Finish the current game first!");
            return;
        }
        board.getBoard().newGame(); // Set up the pieces.
        currentPlayer = Status.WHITE.getNumVal(); // White moves first.
        legalMoves = board.getLegalMoves(currentPlayer); // Get White's legal moves.
        selectedRow = -1; // White has not yet selected a piece to move.
        message.setText("White:  Make your move.");
        gameInProgress = true;
        newGameButton.setEnabled(false);
        resignButton.setEnabled(true);
        repaint();
    }

    public void doResign() {
        // Current player resigns. Game ends. Opponent wins.
        if (!gameInProgress) {
            message.setText("There is no game in progress!");
            return;
        }
        if (currentPlayer == Status.WHITE.getNumVal())
            gameOver("White resigns.  BLACK wins.");
        else
            gameOver("BLACK resigns.  White winds.");
    }

    public void gameOver(String str) {
        // The game ends. The parameter, str, is displayed as a message
        // to the user. The states of the buttons are adjusted so players
        // can start a new game.
        message.setText(str);
        newGameButton.setEnabled(true);
        resignButton.setEnabled(false);
        gameInProgress = false;
    }

    public void doClickSquare(int row, int col) {
        // This is called by mousePressed() when a player clicks on the
        // square in the specified row and col. It has already been checked
        // that a game is, in fact, in progress.

		/*
         * If the player clicked on one of the pieces that the player can move,
		 * mark this row and col as selected and return. (This might change a
		 * previous selection.) Reset the message, in case it was previously
		 * displaying an error message.
		 */

        for (int i = 0; i < legalMoves.size(); i++)
            if (legalMoves.get(i).getFromRow() == row && legalMoves.get(i).getFromCol() == col) {
                selectedRow = row;
                selectedCol = col;
                if (currentPlayer == Status.WHITE.getNumVal())
                    message.setText("White:  Make your move.");
                else
                    message.setText("BLACK:  Make your move.");
                repaint();
                return;
            }

		/*
         * If no piece has been selected to be moved, the user must first select
		 * a piece. Show an error message and return.
		 */

        if (selectedRow < 0) {
            message.setText("Click the piece you want to move.");
            return;
        }

		/*
         * If the user clicked on a square where the selected piece can be
		 * legally moved, then make the move and return.
		 */

        for (int i = 0; i < legalMoves.size(); i++)
            if (legalMoves.get(i).getFromRow() == selectedRow && legalMoves.get(i).getFromCol() == selectedCol
                    && legalMoves.get(i).getToRow() == row && legalMoves.get(i).getToCol() == col) {
                doMakeMove(legalMoves.get(i));
                return;
            }

		/*
         * If we get to this point, there is a piece selected, and the square
		 * where the user just clicked is not one where that piece can be
		 * legally moved. Show an error message.
		 */

        message.setText("Click the square you want to move to.");

    } // end doClickSquare()

    public void doMakeMove(Move move) {
        // Thiis is called when the current player has chosen the specified
        // move. Make the move, and then either end or continue the game
        // appropriately.

        board.makeMove(move);

		/*
         * If the move was a jump, it's possible that the player has another
		 * jump. Check for legal jumps starting from the square that the player
		 * just moved to. If there are any, the player must jump. The same
		 * player continues moving.
		 */

        if (move.isEat()) {
            legalMoves = board.getLegalJumps(currentPlayer, move.getToRow(), move.getToCol());
            if (!legalMoves.isEmpty()) {
                if (currentPlayer == Status.WHITE.getNumVal())
                    message.setText("White:  You must continue jumping.");
                else
                    message.setText("BLACK:  You must continue jumping.");
                selectedRow = move.getToRow(); // Since only one piece can be moved, select it.
                selectedCol = move.getToCol();
                repaint();
                return;
            }
        }

		/*
         * The current player's turn is ended, so change to the other player.
		 * Get that player's legal moves. If the player has no legal moves, then
		 * the game ends.
		 */

        if (currentPlayer == Status.WHITE.getNumVal()) {
            currentPlayer = Status.BLACK.getNumVal();
            legalMoves = board.getLegalMoves(currentPlayer);
            if (legalMoves == null)
                gameOver("BLACK has no moves.  RED wins.");
            else if (legalMoves.get(0).isEat())
                message.setText("BLACK:  Make your move.  You must jump.");
            else
                message.setText("BLACK:  Make your move.");
        } else {
            currentPlayer = Status.WHITE.getNumVal();
            legalMoves = board.getLegalMoves(currentPlayer);
            if (legalMoves == null)
                gameOver("White has no moves.  BLACK wins.");
            else if (legalMoves.get(0).isEat())
                message.setText("White:  Make your move.  You must jump.");
            else
                message.setText("White:  Make your move.");
        }

		/*
         * Set selectedRow = -1 to record that the player has not yet selected a
		 * piece to move.
		 */

        selectedRow = -1;

		/*
         * As a courtesy to the user, if all legal moves use the same piece,
		 * then select that piece automatically so the use won't have to click
		 * on it to select it.
		 */

        if (legalMoves != null) {
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

		/* Make sure the board is redrawn in its new state. */

        repaint();

    } // end doMakeMove();

    public void update(Graphics g) {
        // The paint method completely redraws the canvas, so don't erase
        // before calling paint().
        paint(g);
    }

    public void paint(Graphics g) {
        // Draw checkerboard pattern in gray and lightGray. Draw the
        // checkers. If a game is in progress, highlight the legal moves.

		/* Draw a two-pixel black border around the edges of the canvas. */

        g.setColor(Color.black);
        g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
        g.drawRect(1, 1, getSize().width - 3, getSize().height - 3);

		/* Draw the squares of the checkerboard and the checkers. */

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
        /*
         * If a game is in progress, highlight the legal moves. Note that
		 * legalMoves is never null while a game is in progress.
		 */
        if (gameInProgress) {
            // First, draw a cyan border around the pieces that can be moved.
            g.setColor(Color.cyan);
            for (int i = 0; i < legalMoves.size(); i++) {
                g.drawRect(2 + legalMoves.get(i).getFromCol() * 20, 2 + legalMoves.get(i).getFromRow() * 20, 19, 19);
            }
            // If a piece is selected for moving (i.e. if selectedRow >= 0),
            // then
            // draw a 2-pixel white border around that piece and draw green
            // borders
            // around each square that that piece can be moved to.
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

    } // end paint()

    public Dimension getPreferredSize() {
        // Specify desired size for this component. Note:
        // the size MUST be 164 by 164.
        return new Dimension(164, 164);
    }

    public Dimension getMinimumSize() {
        return new Dimension(164, 164);
    }

    public void mousePressed(MouseEvent evt) {
        // Respond to a user click on the board. If no game is
        // in progress, show an error message. Otherwise, find
        // the row and column that the user clicked and call
        // doClickSquare() to handle it.
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
