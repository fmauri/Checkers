package AlphaBeta;

/**
 * Created by Francesco Mauri on 5/14/2017.
 */
public class Move {
    private int fromRow;
    private int fromCol;
    private int toRow, toCol;

    public Move(int x1, int y1, int x2, int y2) {
        fromRow = x1;
        fromCol = y1;
        toRow = x2;
        toCol = y2;
    }

    boolean isEat() {
        return (Math.abs(fromRow - toRow) == 2);
    }

    public int getFromRow() {
        return fromRow;
    }

    public int getFromCol() {
        return fromCol;
    }

    public int getToRow() {
        return toRow;
    }

    public int getToCol() {
        return toCol;
    }
}
