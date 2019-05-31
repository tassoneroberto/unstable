package unstable;

public class Move {
	final int i, j, player;

	Move(int i, int j, int player) {
		this.i = i;
		this.j = j;
		this.player = player;
	}

	public String toString() {
		return "" + (char) (i + 65) + (j + 1);
	}
}