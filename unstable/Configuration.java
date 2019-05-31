package unstable;

public class Configuration implements Comparable<Configuration> {
	Move move;
	Board board;
	public int heuristic;

	public Configuration(Move move, Board board) {
		this.board = board.getBoardCopy().play(move);
		this.heuristic = this.board.heuristic(move.player);
		this.move = move;
	}

	@Override
	public int compareTo(Configuration o) {
		return o.heuristic - heuristic;
	}

}
