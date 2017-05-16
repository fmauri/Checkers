package AlphaBeta;

/**
 * Created by Francesco Mauri on 5/14/2017.
 */
public class Board {
    private int[][] board = new int[8][8];

    public Board() {
        newGame();
    }

    public void newGame() {
        for (int row = 0; row < this.board.length; row++) {
            for (int col = 0; col < this.board[row].length; col++) {
                if (row % 2 == col % 2) {
                    if (row <= 2) {
                        this.board[row][col] = Status.BLACK.getNumVal();
                    } else if (row >= 5) {
                        this.board[row][col] = Status.WHITE.getNumVal();
                    } else {
                        this.board[row][col] = Status.EMPTY.getNumVal();
                    }
                } else {
                    this.board[row][col] = Status.EMPTY.getNumVal();
                }
            }
        }
    }

    public int getValueAt(int x, int y) {
        return this.board[x][y];
    }

    public void setValueAt(int x, int y, int value) {
        this.board[x][y] = value;
    }

    public int[][] getBoard() {
        return board;
    }

    public boolean isBlackWinning() {
        int w = 0;
        int b = 0;
        for (int row = 0; row < this.board.length; row++) {
            for (int col = 0; col < this.board[row].length; col++) {
                if(this.board[row][col]>0){
                    w++;
                }else{
                    b++;
                }
            }
        }
        return b>w;
    }
}
