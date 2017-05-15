package AlphaBeta;

/**
 * Created by Francesco Mauri on 5/14/2017.
 */
public enum Status {
    EMPTY(0), WHITE(1), BLACK(-1), WHITE_PIECE_PROMOTED(2), BLACK_PIECE_PROMOTED(-2);

    private int numVal;

    Status(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }

    public static Status fromInteger(int x) {
        switch (x) {
            case 0:
                return EMPTY;
            case 1:
                return WHITE;
            case -1:
                return BLACK;
            case 2:
                return WHITE_PIECE_PROMOTED;
            case -2:
                return BLACK_PIECE_PROMOTED;
            default:
                return EMPTY;
        }
    }

    public static int toInt(Status status) {
        switch (status) {
            case EMPTY:
                return 0;
            case WHITE:
                return 1;
            case BLACK:
                return -1;
            case WHITE_PIECE_PROMOTED:
                return 2;
            case BLACK_PIECE_PROMOTED:
                return -2;
            default:
                return 0;
        }
    }
}
