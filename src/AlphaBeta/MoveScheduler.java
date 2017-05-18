package AlphaBeta;

import java.util.ArrayList;

/**
 * Created by Francesco Mauri on 5/14/2017.
 */
public class MoveScheduler {
    private Board board = new Board();
    private Board test = new Board();

    public MoveScheduler() {
        this.board.newGame();
        this.test.newGame();
    }

    public void makeMove(Move move) {
        makeMove(move.getFromRow(), move.getFromCol(), move.getToRow(), move.getToCol());
    }

    public void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        this.board.setValueAt(toRow, toCol, this.board.getValueAt(fromRow, fromCol));
        this.board.setValueAt(fromRow, fromCol, Status.EMPTY.getNumVal());
        if (Math.abs(fromRow - toRow) == 2) {
            int jumpedRow = (fromRow + toRow) / 2;
            int jumpedCol = (fromCol + toCol) / 2;
            this.board.setValueAt(jumpedRow, jumpedCol, Status.EMPTY.getNumVal());
        }
        if (toRow == 0 && this.board.getValueAt(toRow, toCol) == Status.WHITE.getNumVal()) {
            this.board.setValueAt(toRow, toCol, Status.WHITE_PIECE_PROMOTED.getNumVal());
        } else if (toRow == 7 && this.board.getValueAt(toRow, toCol) == Status.BLACK.getNumVal()) {
            this.board.setValueAt(toRow, toCol, Status.BLACK_PIECE_PROMOTED.getNumVal());
        }
    }

    public ArrayList<Move> getLegalMoves(int player) {
        ArrayList<Move> moves = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                moves.addAll(getLegalEat(player, row, col));
            }
        }
        if (moves.isEmpty()) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if ((player < 0 && this.board.getValueAt(row, col) < 0) || (player > 0 && this.board.getValueAt(row, col) > 0)) {
                        if (canMove(player, row, col, row + 1, col + 1)) {
                            moves.add(new Move(row, col, row + 1, col + 1));
                        }
                        if (canMove(player, row, col, row - 1, col + 1)) {
                            moves.add(new Move(row, col, row - 1, col + 1));
                        }
                        if (canMove(player, row, col, row + 1, col - 1)) {
                            moves.add(new Move(row, col, row + 1, col - 1));
                        }
                        if (canMove(player, row, col, row - 1, col - 1)) {
                            moves.add(new Move(row, col, row - 1, col - 1));
                        }
                    }
                }
            }
        }
        return moves;
    }

    public boolean canEat(int player, int r1, int c1, int r2, int c2, int r3, int c3) {
        if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8 || this.board.getValueAt(r3, c3) != Status.EMPTY.getNumVal()) {
            return false;
        }
        if ((player > 0) && ((this.board.getValueAt(r1, c1) == Status.WHITE.getNumVal() && r3 > r1)
                || this.board.getValueAt(r2, c2) >= 0)) {
            return false;
        }
        if ((player < 0) && ((this.board.getValueAt(r1, c1) == Status.BLACK.getNumVal() && r3 < r1)
                || this.board.getValueAt(r2, c2) <= 0)) {
            return false;
        }
        return true;
    }

    public boolean canMove(int player, int r1, int c1, int r2, int c2) {
        if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8 || this.board.getValueAt(r2, c2) != Status.EMPTY.getNumVal()) {
            return false;
        }
        if (player > 0) {
            return ((this.board.getValueAt(r1, c1) == Status.WHITE.getNumVal() && r2 < r1)
                    || (this.board.getValueAt(r1, c1) == Status.WHITE_PIECE_PROMOTED.getNumVal()));
        } else {
            return ((this.board.getValueAt(r1, c1) == Status.BLACK.getNumVal() && r2 > r1)
                    || (this.board.getValueAt(r1, c1) == Status.BLACK_PIECE_PROMOTED.getNumVal()));
        }
    }

    public ArrayList<Move> getLegalEat(int player, int row, int col) {
        ArrayList<Move> moves = new ArrayList<>();
        if ((player < 0 && this.board.getValueAt(row, col) < 0) || (player > 0 && this.board.getValueAt(row, col) > 0)) {
            if (canEat(player, row, col, row + 1, col + 1, row + 2, col + 2)) {
                moves.add(new Move(row, col, row + 2, col + 2));
            }
            if (canEat(player, row, col, row - 1, col + 1, row - 2, col + 2)) {
                moves.add(new Move(row, col, row - 2, col + 2));
            }
            if (canEat(player, row, col, row + 1, col - 1, row + 2, col - 2)) {
                moves.add(new Move(row, col, row + 2, col - 2));
            }
            if (canEat(player, row, col, row - 1, col - 1, row - 2, col - 2)) {
                moves.add(new Move(row, col, row - 2, col - 2));
            }
        }
        return moves;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board b) {
        this.board = b;
    }

    public Board copyBoard(Board b) {
        Board copy = new Board();
        for (int i = 0; i < b.getBoard().length; i++) {
            for (int j = 0; j < b.getBoard()[0].length; j++) {
                copy.setValueAt(i, j, b.getValueAt(i, j));
            }
        }
        return copy;
    }
}
