package unstable;

public class Utility {
	public static final int ROWS = 10;
	public static final int COLS = 6;
	public static final int LAST_ROW = ROWS - 1;
	public static final int LAST_COL = COLS - 1;
	public static final int TOTAL_CELLS = ROWS * COLS;
	public static final int NEARS[][][] = new int[ROWS][COLS][];
	public static final int COUNT_NEARS[][] = new int[ROWS][COLS];
	public static final int UNSTABILITY[][] = new int[ROWS][COLS];
	public static final int OWNERS = 3; // 0=Neutral, 1=Black, 2=White
	public static final Move[][][] MOVES = new Move[OWNERS][ROWS][COLS];
	public static final int MAX_DEPTH = 148;
	public static final int MAX_TIME = 4990;
	public static final int MAX_VALUE = TOTAL_CELLS * 2 + MAX_DEPTH;
	public static final int MIN_VALUE = -MAX_VALUE;

	public static int otherPlayer(int player) {
		return ~player & 3;
	}

	public static void calculateNearCells() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				int x = i * COLS + j;
				if (i == 0) {// prima riga
					if (j == 0) {// angolo alto sinistra
						COUNT_NEARS[i][j] = 2;
						NEARS[i][j] = new int[2];
						NEARS[i][j][0] = 1;
						NEARS[i][j][1] = COLS;
					} else if (j == COLS - 1) {// angolo alto destra
						COUNT_NEARS[i][j] = 2;
						NEARS[i][j] = new int[2];
						NEARS[i][j][0] = x - 1;
						NEARS[i][j][1] = x + COLS;
					} else {// bordo superiore
						COUNT_NEARS[i][j] = 3;
						NEARS[i][j] = new int[3];
						NEARS[i][j][0] = x - 1;
						NEARS[i][j][1] = x + 1;
						NEARS[i][j][2] = x + COLS;
					}
				} else if (i == ROWS - 1) { // ultima riga
					if (j == 0) {// angolo basso sinistra
						COUNT_NEARS[i][j] = 2;
						NEARS[i][j] = new int[2];
						NEARS[i][j][0] = x - COLS;
						NEARS[i][j][1] = x + 1;
					} else if (j == COLS - 1) {// angolo basso destra
						COUNT_NEARS[i][j] = 2;
						NEARS[i][j] = new int[2];
						NEARS[i][j][0] = x - 1;
						NEARS[i][j][1] = x - COLS;
					} else {// bordo inferiore
						COUNT_NEARS[i][j] = 3;
						NEARS[i][j] = new int[3];
						NEARS[i][j][0] = x - 1;
						NEARS[i][j][1] = x + 1;
						NEARS[i][j][2] = x - COLS;
					}
				} else {
					if (j == 0) {// prima colonna - bordo sinistra
						COUNT_NEARS[i][j] = 3;
						NEARS[i][j] = new int[3];
						NEARS[i][j][0] = x - COLS;
						NEARS[i][j][1] = x + 1;
						NEARS[i][j][2] = x + COLS;

					} else if (j == COLS - 1) {// ultima colonna - bordo
												// destra
						COUNT_NEARS[i][j] = 3;
						NEARS[i][j] = new int[3];
						NEARS[i][j][0] = x - COLS;
						NEARS[i][j][1] = x - 1;
						NEARS[i][j][2] = x + COLS;
					} else {// celle centrali
						COUNT_NEARS[i][j] = 4;
						NEARS[i][j] = new int[4];
						NEARS[i][j][0] = x - 1;
						NEARS[i][j][1] = x + 1;
						NEARS[i][j][2] = x - COLS;
						NEARS[i][j][3] = x + COLS;
					}
				}
				UNSTABILITY[i][j] = COUNT_NEARS[i][j] - 1;
			}
		}
	}

	public static void generateAllMoves() {
		for (int player = 1; player < OWNERS; player++) {
			for (int i = 0; i < ROWS; i++) {
				for (int j = 0; j < COLS; j++) {
					MOVES[player][i][j] = new Move(i, j, player);
				}
			}
		}
	}

	public static String moveToServer(Move move) {
		return "" + (char) (move.i + 65) + (move.j + 1);
	}

	public static Move moveFromServer(String move, int player) {
		int row = (int) (move.charAt(0)) - 65;
		int col = Integer.parseInt("" + move.charAt(1)) - 1;
		return MOVES[player][row][col];
	}
}
