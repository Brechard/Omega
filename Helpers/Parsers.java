package Helpers;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;

import com.um.omega.game.Game;
import com.um.omega.game.Main;
import com.um.omega.game.UserInterface;

public class Parsers {

	private static String gameHistoryProvisional = "";
	static JFrame frame = new JFrame("DEBUG BOARD");

	public static void parseNextMove(String s, GameController gameController) {
		String[] movesReceived = s.split("\\.");
		ArrayList<String> newMoves = Moves.getMoveToDo(movesReceived, gameController.getGameHistory());
		if(newMoves.size() >= 2) {
			for(int i = 0; i < 2; i++) {
				String cell = newMoves.get(i);
				String[] cellData = cell.replace("(", "").replace(")", "").replace(" ", "").split(",");
				gameController.moveAI(Integer.valueOf(cellData[0]), Integer.valueOf(cellData[1]), Integer.valueOf(cellData[2]));
				System.out.println("The next move for player " +cellData[0]+ ", is: (" +cellData[1]+ ", " +cellData[2]+ ")");
			}
			
		} else {
			System.out.println();
			System.out.println("-------------");
			System.out.println("There are no new moves");
			System.out.println("-------------");
			System.out.println();
		}
		Moves.deleteMovesNotDone(newMoves);
	}
	
	/**
	 * This method is only for debug it should never be used during the actual game
	 * @param gameHistory
	 */
	public static void parseGameDebug(String gameHistory, UserInterface ui) {
		String[] list = gameHistory.split("\\.");
		Game newGame = new Game(Main.numberOfHexagonsCenterRow, Main.game.playerToPlay);
		for(String cell: list) {
			String[] cellData = cell.replace("(", "").replace(")", "").replace(" ", "").split(",");
			newGame.setCellToPlayer(Integer.valueOf(cellData[0]), Integer.valueOf(cellData[1]), Integer.valueOf(cellData[2]));
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(ui);
		frame.setSize(Main.boardSize, Main.boardSize);
		frame.setVisible(true);
		long p1 = newGame.getPunctuation(1);
		long p2 = newGame.getPunctuation(2);
		System.out.println("DEBUG History = " +gameHistory+ ".");

		System.out.println("DEBUG Player 1 = " +p1+ ".");
		System.out.println("DEBUG Player 2 = " +p2+ ".");
		System.out.println("DEBUG RIGHT NOW THE WINNER IS = " +(p1 > p2 ? 1 : 2)+ ".");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
