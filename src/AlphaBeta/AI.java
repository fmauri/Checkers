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
                if (MyThirdMoves.isEmpty()) {
                    continue;
                }
                for (Move t : MyThirdMoves) {
                    thirdMe.add(new Node(t, ScoreMove(t, forEnemy, player)));
                }
                secondEnemy.add(new Node(e, ScoreMove(e, forEnemy, -player) - getMaxKey(thirdMe)));
            }
            firstMe.add(new Node(m, ScoreMove(m, forMe, player) - getMaxKey(secondEnemy)));
        }
        return getMaxNode(firstMe).move;
    }

    public int ScoreMove(Move move, MoveScheduler game, int player) {
        int value = 0;
        if (move.isEat()) {
            int eaten;
            ArrayList<Move> multipleEat;
            while (move != null) {
                eaten = this.game.getBoard().getValueAt(Math.abs(move.getFromRow() - move.getToRow()), Math.abs(move.getFromCol() - move.getToCol()));
                if (eaten == Status.WHITE_PIECE_PROMOTED.getNumVal() || eaten == Status.BLACK_PIECE_PROMOTED.getNumVal()) {
                    value += 3;
                } else {
                    value += 2;
                }
                multipleEat = game.getLegalJumps(player, move.getToRow(), move.getToCol());
                move = multipleEat.isEmpty() ? null : multipleEat.get(0);
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

    public int getMaxKey(ArrayList<Node> moves) {
        int max = 0;
        for (Node n : moves) {
            if (max < n.key) {
                max = n.key;
            }
        }
        return max;
    }

    public Node getMaxNode(ArrayList<Node> moves) {
        Node max = moves.get(0);
        for (Node n : moves) {
            if (max.key < n.key) {
                max = n;
            }
        }
        return max;
    }
}
