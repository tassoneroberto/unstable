package strategy;

import java.util.Arrays;

import unstable.Board;
import unstable.Configuration;
import unstable.Utility;

public class StrategyWN implements StrategyIF {

	@Override
	public Configuration[] generateAllNextMoves(int player, Board board) {
		Configuration[] moves = new Configuration[board.ownedCells[player] + board.ownedCells[0]];
		int index = 0, i, j;
		if (player == 1) {
			for (j = 0; j < board.boardValues[0].length; j++) {
				for (i = 0; i < board.boardValues.length; i++) {
					if (board.boardValues[i][j] >= 0) {
						moves[index] = new Configuration(Utility.MOVES[1][i][j], board);
						if (moves[index].heuristic == Utility.MAX_VALUE)
							return moves;
						index++;
					}
				}
			}
		} else {
			for (j = 0; j < board.boardValues[0].length; j++) {
				for (i = 0; i < board.boardValues.length; i++) {
					if (board.boardValues[i][j] <= 0) {
						moves[index] = new Configuration(Utility.MOVES[2][i][j], board);
						if (moves[index].heuristic == Utility.MAX_VALUE)
							return moves;
						index++;
					}
				}
			}
		}
		Arrays.sort(moves);
		return moves;
	}
}