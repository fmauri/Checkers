package AlphaBeta;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Francesco Mauri on 5/15/2017.
 */
public class AI {
    private ArrayList<Move> moves = new ArrayList<>();
    private ArrayList<Node> choices = new ArrayList<>();
    private MoveScheduler game;

    public AI(MoveScheduler m) {
        this.game = m;
    }

    public Move chooseMoveRandom() {
        Random r = new Random();
        return this.moves.get(r.nextInt(this.moves.size()));
    }

    public Move chooseMoveRandom(ArrayList<Move> moves) {
        Random r = new Random();
        return moves.get(r.nextInt(moves.size()));
    }

    public Move alphaBeta(ArrayList<Move> MyMoves, int player) {
        MoveScheduler forEnemy = new MoveScheduler();
        MoveScheduler forMe = new MoveScheduler();
        ArrayList<Node> firstMe = new ArrayList<>();
        ArrayList<Node> secondEnemy = new ArrayList<>();
        ArrayList<Node> thirdMe = new ArrayList<>();
        for (Move m : MyMoves) {
            forMe.setBoard(this.game.copyBoard(this.game.getBoard()));
            forMe.makeMove(m);
            ArrayList<Move> enemyMoves = forMe.getLegalMoves(-player);
            for (Move e : enemyMoves) {
                forEnemy.setBoard(this.game.copyBoard(forMe.getBoard()));
                forEnemy.makeMove(e);
                ArrayList<Move> MyThirdMoves = forEnemy.getLegalMoves(player);
                for (Move t : MyThirdMoves) {
                    thirdMe.add(new Node(t, ScoreMove(t)));
                }
                secondEnemy.add(new Node(e, ScoreMove(e) - getMax(thirdMe).key));
            }
            firstMe.add(new Node(m, ScoreMove(m) - getMax(secondEnemy).key));
        }
        return getMax(firstMe).move;
    }

    public int ScoreMove(Move move) {
        int value = 0;
        if (move.isEat()) {
            int eaten = this.game.getBoard().getValueAt(Math.abs(move.getFromRow() - move.getToRow()), Math.abs(move.getFromCol() - move.getToCol()));
            if (eaten == Status.WHITE_PIECE_PROMOTED.getNumVal() || eaten == Status.BLACK_PIECE_PROMOTED.getNumVal()) {
                value += 3;
            } else {
                value += 2; //maybe problem due to multiple eating
            }
        } else {
            int piece = this.game.getBoard().getValueAt(move.getFromRow(), move.getFromCol());
            if (piece == Status.WHITE.getNumVal() && move.getToRow() == 0 ||
                    piece == Status.BLACK.getNumVal() && move.getToRow() == 7) {
                value += 5;
            }
        }
        return value;
    }

    public Node getMax(ArrayList<Node> moves) {
        Node max = moves.get(0);
        for (Node n : moves) {
            if (max.key < n.key) {
                max = n;
            }
        }
        return max;
    }
}
