package strategy;

import unstable.Board;
import unstable.Configuration;

public interface StrategyIF {
	public Configuration[] generateAllNextMoves(int player, Board board);
}
