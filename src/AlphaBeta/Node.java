package AlphaBeta;

/**
 * Created by Francesco Mauri on 5/15/2017.
 */
public class Node {
    private int key;
    private Move move;

    Node(Move m, int key) {
        this.key = key;
        this.move = m;
    }
    Node() {
        this.key = 0;
    }

    public int getKey() {
        return this.key;
    }

    public Move getMove() {
        return this.move;
    }

    public String toString() {
        return move + " has the key " + key;
    }
}