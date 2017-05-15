package AlphaBeta;

/**
 * Created by Francesco Mauri on 5/15/2017.
 */
public class Node {
    int key;
    Move move;
    Node leftChild;
    Node rightChild;
    Node parent;

    Node(Move m, int key) {
        this.key = key;
        this.move = m;
    }

    public String toString() {
        return move + " has the key " + key;
    }
}