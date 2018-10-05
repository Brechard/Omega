package Helpers;

import java.util.ArrayList;

import javax.swing.JFrame;

import com.um.omega.game.Game;
import com.um.omega.game.Main;
import com.um.omega.game.UserInterface;

public class Parsers {

	private static String gameHistoryProvisional = "";
	static JFrame frame = new JFrame("DEBUG BOARD");

	public static void parseNextMove(String s) {
		String[] movesReceived = s.split("\\.");
		String[] alreadyMadeMoves = Main.gameHistory.split("\\.");
		ArrayList<String> newMoves = Moves.getMoveToDo(movesReceived, alreadyMadeMoves);
//		System.out.println("movesReceived: " +Arrays.asList(movesReceived));
//		System.out.println("alreadyMadeMoves: " +Arrays.asList(alreadyMadeMoves));
//		System.out.println("newMoves: " +newMoves);
//		System.out.println("The story until now is: " +Main.gameHistory);
		for(int i = 0; i < 2; i++) {
			String cell = newMoves.get(i);
			String[] cellData = cell.replace("(", "").replace(")", "").replace(" ", "").split(",");
			Main.game.setCellToPlayer(Integer.valueOf(cellData[0]), Integer.valueOf(cellData[1]), Integer.valueOf(cellData[2]));
			Main.gameHistory += cell + ".";
			System.out.println("The next move for player " +cellData[0]+ ", is: (" +cellData[1]+ ", " +cellData[2]+ ")");
		}
//		Main.game.emptyCells.stream().forEach(cell -> System.out.println("0000: " +cell.print()));
//		Main.game.player1.stream().forEach(cell -> System.out.println("1111: " +cell.print()));
//		Main.game.player2.stream().forEach(cell -> System.out.println("2222: " +cell.print()));
		Moves.deleteMovesNotDone(newMoves);
	}
	
	/**
	 * Parses the move send, tries to set the cell to the player and if there is an error it informs about it	 * 
	 * @param player
	 * @param s
	 * @return True if there is an error so that the player introduces again the data, false otherwise
	 */
	public static boolean parseMove(int player, String s) {
		String[] xy = s.replace(" ", "").replace("(", "").replace(")", "").split(",");
//		System.out.println("The cell to change is: (" +Integer.valueOf(xy[0])+ ", " +Integer.valueOf(xy[1])+ ")");
		try{
			Main.game.setCellToPlayer(player, Integer.valueOf(xy[0]), Integer.valueOf(xy[1]));
		} catch (Error e) {
			System.out.println(e+ ", cell already in use");
			return true;
		}
		setGameHistoryProvisional(getGameHistoryProvisional() + "("+player+", " +xy[0]+ ", " +xy[1]+ ").");
		return false;
	}
	
	/**
	 * This method is only for debug it should never be used during the actual game
	 * @param gameHistory
	 */
	public static void parseGameDebug(String gameHistory) {
		String[] list = gameHistory.split("\\.");
		Game newGame = new Game(Main.numberOfHexagonsCenterRow, Main.game.playerToPlay);
		for(String cell: list) {
			String[] cellData = cell.replace("(", "").replace(")", "").replace(" ", "").split(",");
			newGame.setCellToPlayer(Integer.valueOf(cellData[0]), Integer.valueOf(cellData[1]), Integer.valueOf(cellData[2]));
		}
		UserInterface ui = new UserInterface(Main.boardSize, Main.numberOfHexagonsCenterRow, newGame);
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
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @return the gameHistoryProvisional
	 */
	public static String getGameHistoryProvisional() {
		return gameHistoryProvisional;
	}

	/**
	 * @param gameHistoryProvisional the gameHistoryProvisional to set
	 */
	public static void setGameHistoryProvisional(String gameHistoryProvisional) {
		Parsers.gameHistoryProvisional = gameHistoryProvisional;
	}

}
