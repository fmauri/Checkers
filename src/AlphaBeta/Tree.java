package AlphaBeta;

import AlphaBeta.Move;
import AlphaBeta.Node;

/**
 * Created by Francesco Mauri on 5/15/2017.
 */
public class Tree {
    Node root;

    public void addNode(int key, Move move) {
        Node newNode = new Node(move, key);

    }
}
