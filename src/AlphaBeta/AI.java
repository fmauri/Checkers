package AlphaBeta;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Francesco Mauri on 5/15/2017.
 */
public class AI {
    private ArrayList<Move> moves = new ArrayList<>();

    public AI(ArrayList<Move> m) {
        this.moves = moves;
    }

    public Move chooseMoveRandom() {
        Random r = new Random();
        return this.moves.get(r.nextInt(this.moves.size()));
    }

    public Move chooseMoveRandom(ArrayList<Move> moves) {
        Random r = new Random();
        return moves.get(r.nextInt(moves.size()));
    }
}
