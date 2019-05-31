package unstable;

import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import strategy.StrategyIF;
import strategy.StrategyNE;
import strategy.StrategyNW;
import strategy.StrategySE;
import strategy.StrategySW;

public class Unstable {
	Board currentBoard;
	private boolean timeOut;
	private long initialTime;
	private int depth;
	private Configuration[] nextMoves;
	public int player; // black=1, white=2 [1=positivo; 2=negativo]
	private boolean gameOver;
	public StrategyIF strategy;
	public StringBuilder initialMoves;

	public Unstable() {
		initialMoves = new StringBuilder();
		strategy = null;
		Utility.generateAllMoves();
		Utility.calculateNearCells();
		currentBoard = new Board();

	}

	public void updateBoard(String move, int player) {
		currentBoard.play(Utility.moveFromServer(move, player));
	}

	public int[][] getCurrentBoard() {
		return currentBoard.boardValues;
	}

	public void reset() {
		depth = 4;
		timeOut = false;
		initialTime = System.currentTimeMillis();
	}

	public Move calculateInitialMove() {
		String sequence = initialMoves.toString();
		if (player == 1) {
			if (sequence.equals("J6J1")) {
				strategy = new StrategyNE();
				// strategy[2] = new StrategyNE();
				return Utility.MOVES[player][0][Utility.LAST_COL];
			} else {
				// strategy[1] = new StrategySW();
				strategy = new StrategySW();
				return Utility.MOVES[player][Utility.LAST_ROW][0];
			}
		} else {
			if (sequence.length() == 2) {
				if (sequence.equals("A1")) {
					return Utility.MOVES[player][0][Utility.LAST_COL];
				}
				if (sequence.equals("A6")) {
					return Utility.MOVES[player][0][0];
				}
				if (sequence.equals("J1")) {
					return Utility.MOVES[player][Utility.LAST_ROW][Utility.LAST_COL];
				}
				if (sequence.equals("J6")) {
					return Utility.MOVES[player][Utility.LAST_ROW][0];
				}
				return Utility.MOVES[player][Utility.LAST_ROW][Utility.LAST_COL];

			} else {
				String twoMoves = sequence.substring(0, 4);
				if (twoMoves.equals("A1A6")) {
					// strategy[1] = new StrategyNW();
					if (sequence.equals("A1A6J6")) {
						strategy = new StrategySE();
						return Utility.MOVES[player][Utility.LAST_ROW][0];
					} else if (sequence.equals("A1A6J1")) {
						strategy = new StrategyNW();
						// strategy[2] = new StrategySE();
						return Utility.MOVES[player][Utility.LAST_ROW][Utility.LAST_COL];
					} else {
						strategy = new StrategySE();
						// strategy[2] = new StrategySE();
						return Utility.MOVES[player][Utility.LAST_ROW][Utility.LAST_COL];
					}
				}
				if (twoMoves.equals("A6A1")) {
					// strategy[1] = new StrategyNE();
					if (sequence.equals("A6A1J1")) {
						strategy = new StrategySE();
						return Utility.MOVES[player][Utility.LAST_ROW][Utility.LAST_COL];
					} else {
						strategy = new StrategyNE();
						// strategy[2] = new StrategySW();
						return Utility.MOVES[player][Utility.LAST_ROW][0];
					}
				}
				if (twoMoves.equals("J1J6")) {
					// strategy[1] = new StrategySW();
					if (sequence.equals("J1J6A6")) {
						strategy = new StrategyNE();
						return Utility.MOVES[player][0][0];
					} else if (sequence.equals("J1J6A1")) {
						strategy = new StrategySW();
						// strategy[] = new StrategyNE();
						return Utility.MOVES[player][0][Utility.LAST_COL];
					} else {
						strategy = new StrategyNE();
						return Utility.MOVES[player][0][Utility.LAST_COL];
					}
				}
				if (twoMoves.equals("J6J1")) {
					// strategy[1] = new StrategySE();
					if (sequence.equals("J6J1A1")) {
						strategy = new StrategyNW();
						return Utility.MOVES[player][0][Utility.LAST_COL];
					} else {
						strategy = new StrategySE();
						// strategy[2] = new StrategyNW();
						return Utility.MOVES[player][0][0];
					}
				}
				// strategy[1] = new StrategyNW();
				if (currentBoard.boardValues[Utility.LAST_ROW][0] == 0) {
					strategy = new StrategySE();
					return Utility.MOVES[player][Utility.LAST_ROW][0];
				} else if (currentBoard.boardValues[0][Utility.LAST_COL] == 0) {
					strategy = new StrategySE();
					return Utility.MOVES[player][0][Utility.LAST_COL];

				}
			}
		}
		return null;

	}

	public Move calculateMove() {
		nextMoves = strategy.generateAllNextMoves(player, currentBoard);
		Move bestMove = iterativeDeepening(nextMoves[0].move);
		return bestMove;
	}

	private Move iterativeDeepening(Move currentBestMove) {
		while (depth < Utility.MAX_DEPTH && !timeOut) {
			int alpha = Utility.MIN_VALUE, beta = Utility.MAX_VALUE;
			int max = Utility.MIN_VALUE;
			try {
				int value;
				for (Configuration move : nextMoves) {
					if (move.heuristic == Utility.MAX_VALUE)
						return move.move;
					value = -negaMax(move.board.getBoardCopy(), 0, -beta, -alpha, -move.heuristic,
							Utility.otherPlayer(player));
					/*
					 * if (value == Utility.MAX_VALUE) return move.move;
					 */
					move.heuristic = value;
					if (value > max) {
						max = value;
						currentBestMove = move.move;
					}
					if (alpha < value) {
						alpha = value;
					}
				}
			} catch (TimeoutException e) {
				timeOut = true;
			}
			Arrays.sort(nextMoves);
			depth++;
		}
		return currentBestMove;
	}

	private int negaMax(Board board, int level, int alpha, int beta, int heuristicValue, int player)
			throws TimeoutException {
		if (System.currentTimeMillis() - initialTime >= Utility.MAX_TIME) {
			throw new TimeoutException();
		}
		int bestValue = Utility.MIN_VALUE;
		if (level >= depth) {
			return heuristicValue;
		} else {
			Configuration[] moves = strategy.generateAllNextMoves(player, board);
			int v;
			int otherPlayer = Utility.otherPlayer(player);
			int nextLevel = level + 1;
			for (Configuration move : moves) {
				if (move.heuristic == Utility.MAX_VALUE)
					return Utility.MAX_VALUE - level;
				v = -negaMax(move.board, nextLevel, -beta, -alpha, -move.heuristic, otherPlayer);
				if (v > bestValue)
					bestValue = v;
				if (alpha < v) {
					alpha = v;
				}
				if (beta <= alpha)
					return bestValue;
			}
		}
		return bestValue;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

}