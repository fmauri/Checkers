package AlphaBeta;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Francesco Mauri on 5/15/2017.
 */
public class AI {
    private ArrayList<Node> choices = new ArrayList<>();
    private MoveScheduler game;

    public AI(MoveScheduler m) {
        this.game = m;
    }

    public Move chooseMoveRandom(ArrayList<Move> moves) {
        Random r = new Random();
        return moves.get(r.nextInt(moves.size()));
    }

    public Node minMaxRecursive(ArrayList<Move> moves, int player, int round, MoveScheduler baseGame) {
        MoveScheduler simulatorGame = new MoveScheduler();
        ArrayList<Node> scoredMoves = new ArrayList<>();
        Node best;
        for (Move m : moves) {
            if (round > 0) {
                simulatorGame.setBoard(baseGame.copyBoard(baseGame.getBoard()));
                simulatorGame.makeMove(m);
                ArrayList<Move> nextRound = simulatorGame.getLegalMoves(-player);
                if (nextRound.isEmpty()) {
                    scoredMoves.add(new Node(m, ScoreMove(m, simulatorGame, -player)));
                    continue;
                }
                best = minMaxRecursive(nextRound, -player, round - 1, simulatorGame);
                scoredMoves.add(new Node(m, ScoreMove(m, simulatorGame, -player) - best.getKey()));
            } else {
                scoredMoves.add(new Node(m, ScoreMove(m, baseGame, player)));
            }
        }
        return getMaxNode(scoredMoves);
    }

    public int ScoreMove(Move move, MoveScheduler game, int player) {
        int value = 0;
        if (move.isEat()) {
            int eaten;
            ArrayList<Move> multipleEat;
            while (move != null) {
                eaten = this.game.getBoard().getValueAt((move.getFromRow() + move.getToRow()) / 2,
                        (move.getFromCol() + move.getToCol()) / 2);
                if (eaten == Status.WHITE_PIECE_PROMOTED.getNumVal() || eaten == Status.BLACK_PIECE_PROMOTED.getNumVal()) {
                    value += 5;
                } else {
                    value += 3;
                }
                multipleEat = game.getLegalEat(player, move.getToRow(), move.getToCol());
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
            if (max < n.getKey()) {
                max = n.getKey();
            }
        }
        return max;
    }

    public Node getMaxNode(ArrayList<Node> moves) {
        Node max = moves.get(0);
        for (Node n : moves) {
            if (max.getKey() < n.getKey()) {
                max = n;
            }
        }
        return max;
    }

    public Node alphaBeta(int alpha, int beta, ArrayList<Move> moves, int player, int round, MoveScheduler baseGame) {
        MoveScheduler simulatorGame = new MoveScheduler();
        ArrayList<Node> scoredMoves = new ArrayList<>();
        Node best;
        for (Move m : moves) {
            if (round > 0) {
                simulatorGame.setBoard(baseGame.copyBoard(baseGame.getBoard()));
                simulatorGame.makeMove(m);
                ArrayList<Move> nextRound = simulatorGame.getLegalMoves(-player);
                if (nextRound.isEmpty()) {
                    scoredMoves.add(new Node(m, ScoreMove(m, simulatorGame, -player)));
                    continue;
                }
            } else {
                scoredMoves.add(new Node(m, ScoreMove(m, baseGame, player)));
            }
        }
        return getMaxNode(scoredMoves);
    }
}
