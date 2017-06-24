package AlphaBeta;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Francesco Mauri on 5/15/2017.
 */
public class AI {
    private MoveScheduler game;


    public AI(MoveScheduler m) {
        this.game = m;
    }

    public Move chooseMoveRandom(ArrayList<Move> moves) {
        Random r = new Random();
        return moves.get(r.nextInt(moves.size()));
    }

    public Node searchRecursive(ArrayList<Move> moves, int player, int round, MoveScheduler baseGame) {
        MoveScheduler simulatorGame = new MoveScheduler();
        ArrayList<Node> scoredMoves = new ArrayList<>();
        Node best;
        for (Move m : moves) {
            simulatorGame.setBoard(baseGame.copyBoard(baseGame.getBoard()));
            simulatorGame.applyMove(m);
            if (round > 0) {
                ArrayList<Move> nextRound = simulatorGame.getLegalMoves(-player);
                if (nextRound.isEmpty()) {
                    scoredMoves.add(new Node(m, Integer.MAX_VALUE));
                    continue;
                }
                best = searchRecursive(nextRound, -player, round - 1, simulatorGame);
                scoredMoves.add(new Node(m, ScoreMove(m, simulatorGame, player) - best.getKey()));
            } else {
                scoredMoves.add(new Node(m, ScoreMove(m, simulatorGame, player)));
            }
        }
        return getMaxNode(scoredMoves);
    }

    public Node minMax(ArrayList<Move> moves, int player, int round, MoveScheduler baseGame) {
        MoveScheduler simulatorGame = new MoveScheduler();
        ArrayList<Node> scoredMoves = new ArrayList<>();
        Node chosen;
        for (Move m : moves) {
            if (round > 0) {
                simulatorGame.setBoard(baseGame.copyBoard(baseGame.getBoard()));
                simulatorGame.applyMove(m);
                ArrayList<Move> nextRound = simulatorGame.getLegalMoves(-player);
                if (nextRound.isEmpty()) {
                    if (round % 2 == 0) {
                        scoredMoves.add(new Node(m, Integer.MAX_VALUE));
                    } else {
                        scoredMoves.add(new Node(m, Integer.MIN_VALUE));
                    }
                    continue;
                }
                chosen = minMax(nextRound, -player, round - 1, simulatorGame);
                scoredMoves.add(new Node(m, chosen.getKey()));
            } else {
                scoredMoves.add(new Node(m, simulatorGame.getBoard().countPieces(player)));
            }
        }
        return round % 2 == 0 ? getMaxNode(scoredMoves) : getMinNode(scoredMoves);
    }

    public Node alphaBetaPruning(ArrayList<Move> moves, int player, int round, MoveScheduler baseGame, Node alpha, Node beta) {
        MoveScheduler simulatorGame = new MoveScheduler();
        ArrayList<Node> scoredMoves = new ArrayList<>();
        Node chosen;
        for (Move m : moves) {
            simulatorGame.setBoard(baseGame.copyBoard(baseGame.getBoard()));
            simulatorGame.applyMove(m);
            if (round > 0) {
                ArrayList<Move> nextRound = simulatorGame.getLegalMoves(-player);
                if (nextRound.isEmpty()) {
                    if (round % 2 == 0) {
                        scoredMoves.add(new Node(m, Integer.MAX_VALUE));
                    } else {
                        scoredMoves.add(new Node(m, Integer.MIN_VALUE));
                    }
                    continue;
                }
                if (round % 2 == 0) {
                    chosen = alphaBetaPruning(nextRound, -player, round - 1, simulatorGame, alpha, beta);
                    alpha = alpha.getKey() >= chosen.getKey() ? alpha : new Node(m, chosen.getKey());
                    if (beta.getKey() <= alpha.getKey()) {
                        break;
                    }
                } else {
                    chosen = alphaBetaPruning(nextRound, -player, round - 1, simulatorGame, alpha, beta);
                    beta = beta.getKey() <= chosen.getKey() ? beta : new Node(m, chosen.getKey());
                    if (beta.getKey() <= alpha.getKey()) {
                        break;
                    }
                }
            } else {
                scoredMoves.add(new Node(m, simulatorGame.getBoard().countPieces(player)));
            }
        }
        if (round == 0) {
            return getMaxNode(scoredMoves);
        } else if (round % 2 == 0) {
            return alpha;
        } else {
            return beta;
        }
    }

    public int ScoreMove(Move move, MoveScheduler game, int player) {
        int value = 0;
        if (move.isEat()) {
            int eaten;
            ArrayList<Move> multipleEat;
            while (move != null) {
                eaten = this.game.getBoard().getValueAt((move.getFromRow() + move.getToRow()) / 2,
                        (move.getFromCol() + move.getToCol()) / 2);
                if (eaten == Status.WHITE_PROMOTED.getNumVal() || eaten == Status.BLACK_PROMOTED.getNumVal()) {
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

    public Node getMaxNode(ArrayList<Node> moves) {
        Node max = moves.get(0);
        for (Node n : moves) {
            if (max.getKey() < n.getKey()) {
                max = n;
            }
        }
        return max;
    }

    public Node getMinNode(ArrayList<Node> moves) {
        Node min = moves.get(0);
        for (Node n : moves) {
            if (min.getKey() > n.getKey()) {
                min = n;
            }
        }
        return min;
    }
}
