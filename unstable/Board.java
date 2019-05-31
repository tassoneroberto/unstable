package unstable;

public class Board {
	public int[][] boardValues;
	public int[] ownedCells;
	public int[] unstableCells;

	public Board() {
		boardValues = new int[Utility.ROWS][Utility.COLS];
		ownedCells = new int[Utility.OWNERS];
		unstableCells = new int[Utility.OWNERS];
		ownedCells[0] = Utility.TOTAL_CELLS;
	}

	private Board(int[][] board, int ownedCells[], int unstableCells[]) {
		this.ownedCells = new int[Utility.OWNERS];
		this.unstableCells = new int[Utility.OWNERS];
		System.arraycopy(ownedCells, 0, this.ownedCells, 0, ownedCells.length);
		System.arraycopy(unstableCells, 0, this.unstableCells, 0, unstableCells.length);
		this.boardValues = new int[board.length][board[0].length];
		for (int i = 0; i < board.length; i++) {
			System.arraycopy(board[i], 0, this.boardValues[i], 0, board[i].length);
		}

	}

	Board getBoardCopy() {
		return new Board(boardValues, ownedCells, unstableCells);
	}

	Board play(Move move) {
		int ownerCell = Utility.otherPlayer(move.player);
		// Cella gia' posseduta
		if (boardValues[move.i][move.j] > 0 && move.player == 1) {
			boardValues[move.i][move.j]++;
		} else if ((boardValues[move.i][move.j] < 0 && move.player == 2)) {
			boardValues[move.i][move.j]--;
		} else if (boardValues[move.i][move.j] == 0) {
			// Cella neutra catturata
			ownedCells[0]--;
			ownedCells[move.player]++;
			if (move.player == 1) {
				boardValues[move.i][move.j]++;
			} else if (move.player == 2) {
				boardValues[move.i][move.j]--;
			}
		} else {
			// Cella avversaria catturata
			ownedCells[ownerCell]--;
			ownedCells[move.player]++;
			boardValues[move.i][move.j] = -boardValues[move.i][move.j];
			if (boardValues[move.i][move.j] > 0) {
				boardValues[move.i][move.j]++;
			} else if (boardValues[move.i][move.j] < 0) {
				boardValues[move.i][move.j]--;
			}
			if (Math.abs(boardValues[move.i][move.j]) >= Utility.COUNT_NEARS[move.i][move.j]) {
				unstableCells[ownerCell]--;
			}
		}
		if ((ownedCells[1] > 1 && ownedCells[2] == 0) || (ownedCells[2] > 1 && ownedCells[1] == 0)) {
			return this;
		}
		if (isUnstable(move.i, move.j)) {
			unstableCells[move.player]++;
		}
		if (Math.abs(boardValues[move.i][move.j]) >= Utility.COUNT_NEARS[move.i][move.j]) {
			unstableCells[move.player]--;
			if (boardValues[move.i][move.j] < 0) {
				boardValues[move.i][move.j] = boardValues[move.i][move.j] + Utility.COUNT_NEARS[move.i][move.j];

			} else if (boardValues[move.i][move.j] > 0) {
				boardValues[move.i][move.j] = boardValues[move.i][move.j] - Utility.COUNT_NEARS[move.i][move.j];

			}
			if (boardValues[move.i][move.j] == 0) {
				ownedCells[0]++;
				ownedCells[move.player]--;

			}
			explosion(move.i, move.j, move.player);
		}
		return this;
	}

	private void explosion(int i, int j, int player) {
		for (int neighbour : Utility.NEARS[i][j]) {
			play(Utility.MOVES[player][neighbour / Utility.COLS][neighbour % Utility.COLS]);
		}
	}

	int heuristic(int player) {
		int otherPlayer = Utility.otherPlayer(player);
		if (ownedCells[otherPlayer] == 0) {
			return Utility.MAX_VALUE;
		}
		return ownedCells[player] - ownedCells[otherPlayer] + unstableCells[player] - unstableCells[otherPlayer];
	}

	boolean isUnstable(int i, int j) {
		return Math.abs(boardValues[i][j]) == Utility.UNSTABILITY[i][j];
	}
}