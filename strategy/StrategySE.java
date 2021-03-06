package strategy;

import java.util.Arrays;

import unstable.Board;
import unstable.Configuration;
import unstable.Utility;

public class StrategySE implements StrategyIF {

	@Override
	public Configuration[] generateAllNextMoves(int player, Board board) {
		Configuration[] moves = new Configuration[board.ownedCells[player] + board.ownedCells[0]];
		int index = 0, i, j;
		if (player == 1) {
			for (i = board.boardValues.length - 1; i >= 0; i--) {
				for (j = board.boardValues[0].length - 1; j >= 0; j--) {
					if (board.boardValues[i][j] >= 0) {
						moves[index] = new Configuration(Utility.MOVES[1][i][j], board);
						if (moves[index].heuristic == Utility.MAX_VALUE)
							return moves;
						index++;
					}
				}
			}
		} else {
			for (i = board.boardValues.length - 1; i >= 0; i--) {
				for (j = board.boardValues[0].length - 1; j >= 0; j--) {
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
