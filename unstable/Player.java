package unstable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Player {
	public static void main(String[] args) throws IOException {
		System.out.println("\t _____________\r\n" + "\t|=============|\r\n" + "\t| .---------. |\r\n" + "\t| | HAL-9000| |\r\n"
				+ "\t| '---------' |\r\n" + "\t|             |\r\n" + "\t|             |\r\n" + "\t|             |\r\n"
				+ "\t|             |\r\n" + "\t|      _      |\r\n" + "\t|   ,`   `.   |\r\n" + "\t|   : (o) :   |\r\n"
				+ "\t|   `. _ ,`   |\r\n" + "\t|             |\r\n" + "\t|_____________|\r\n" + "\t|=============|\r\n"
				+ "\t|*%*%*%*%*%*%*|\r\n" + "\t|%*%*%*%*%*%*%|\r\n" + "\t|*%*%*%*%*%*%*|\r\n" + "\t|%*%*%*%*%*%*%|\r\n"
				+ "\t'============='\r\n");
		Unstable game = new Unstable();
		try {
			Socket s = new Socket(args[0], Integer.parseInt(args[1]));
			PrintWriter out = new PrintWriter(s.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String line = in.readLine();
			String moveServer = "J6";
			if (line.equals("WELCOME Black")) {
				game.player = 1;
			} else if (line.equals("WELCOME White")) {
				game.player = 2;
			}
			Move move;
			while (game.strategy == null) {
				line = in.readLine();
				if (line.equals("YOUR_TURN")) {
					out.println("MOVE " + moveServer);
					out.flush();
					game.initialMoves.append(moveServer);
					game.updateBoard(moveServer, game.player);
				} else if (line.contains("OPPONENT_MOVE")) {
					moveServer = line.replace("OPPONENT_MOVE ", "");
					game.initialMoves.append(moveServer);
					game.updateBoard(moveServer, Utility.otherPlayer(game.player));
					move = game.calculateInitialMove();
					moveServer = Utility.moveToServer(move);
				} else if (line.equals("VICTORY") || line.equals("DEFEAT") || line.equals("TIE")
						|| line.equals("ILLEGAL_MOVE")) {
					game.setGameOver(true);
					s.close();
					if (line.equals("DEFEAT"))
						System.out.println("\"I'm sorry, Dave.\"");
					else if (line.equals("VICTORY"))
						System.out.println(
								"\"I am, by any practical definition of the words, foolproof and incapable of error.\"");
				}
			}
			while (!game.isGameOver()) {
				line = in.readLine();
				if (line.equals("YOUR_TURN")) {
					out.println("MOVE " + moveServer);
					out.flush();
					game.updateBoard(moveServer, game.player);
				} else if (line.contains("OPPONENT_MOVE")) {
					game.reset();
					moveServer = line.replace("OPPONENT_MOVE ", "");
					game.updateBoard(moveServer, Utility.otherPlayer(game.player));
					move = game.calculateMove();
					moveServer = Utility.moveToServer(move);
				} else if (line.equals("VICTORY") || line.equals("DEFEAT") || line.equals("TIE")
						|| line.equals("ILLEGAL_MOVE")) {
					game.setGameOver(true);
					s.close();
					if (line.equals("DEFEAT"))
						System.out.println("\"I'm sorry, Dave.\"");
					else if (line.equals("VICTORY"))
						System.out.println("\"I am, by any practical definition of the words, foolproof and incapable of error.\"");
				}
			}
		} catch (Exception e) {
			System.out.println("Connection failed.");
		}
	}

}
