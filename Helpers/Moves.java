package Helpers;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;

import com.um.omega.game.Game;
import com.um.omega.game.Main;
import com.um.omega.game.UserInterface;

public class Moves {

	private static String gameHistoryProvisional = "";

	public static void parseNextMove(String s) {
		String[] movesReceived = s.split("\\.");
		String[] alreadyMadeMoves = Main.gameHistory.split("\\.");
		ArrayList<String> newMoves = getMoveToDo(movesReceived, alreadyMadeMoves);
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
		deleteMovesNotDone(newMoves);
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
			System.out.println("Cell already in use");
			return true;
		}
		gameHistoryProvisional += "("+player+", " +xy[0]+ ", " +xy[1]+ ").";
		return false;
	}
	
	/**
	 * This method is only for debug it should never be used during the actual game
	 * @param gameHistory
	 */
	public static void parseGame(String gameHistory) {
		String[] list = gameHistory.split("\\.");
		Game newGame = new Game(Main.numberOfHexagonsCenterRow, Main.game.playerToPlay);
		for(String cell: list) {
			String[] cellData = cell.replace("(", "").replace(")", "").replace(" ", "").split(",");
			newGame.setCellToPlayer(Integer.valueOf(cellData[0]), Integer.valueOf(cellData[1]), Integer.valueOf(cellData[2]));
		}
		JFrame frame = new JFrame("DEBUG BOARD");
		UserInterface ui = new UserInterface(Main.boardSize, Main.numberOfHexagonsCenterRow, newGame);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(ui);
		frame.setSize(Main.boardSize, Main.boardSize);
		frame.setVisible(true);
		long p1 = newGame.getPunctuation(1);
		long p2 = newGame.getPunctuation(2);
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
	
	public static void moveConfirmed() {
		Main.gameHistory += gameHistoryProvisional;
		Main.game.playHistory += gameHistoryProvisional;
		gameHistoryProvisional = "";
	}
	
	public static void undoMove(int player, String s) {
		gameHistoryProvisional = "";
		String[] xy = s.replace(" ", "").replace("(", "").replace(")", "").split(",");
		Main.game.deleteMove(player, Integer.valueOf(xy[0]), Integer.valueOf(xy[1]));
	}
	
	public static void played() {
		Main.playerToPlay++;
		if (Main.playerToPlay == 3) 
			Main.playerToPlay = 0;
	}
	
	public static void setPlayerMove(String s) {
		if(Main.playerToPlay == 1)
			Main.player1Move = s;
		else
			Main.player2Move = s;
	}

	public static void deleteMovesNotDone(ArrayList<String> moves){
		moves.remove(1);
		moves.remove(0);
		for(String move: moves) {
			String[] cellData = move.replace("(", "").replace(")", "").replace(" ", "").split(",");
			Main.game.deleteMove(Integer.valueOf(cellData[0]), Integer.valueOf(cellData[1]), Integer.valueOf(cellData[2]));
		}
	}

	public static ArrayList<String> getMoveToDo(String[] newMoves, String[] alreadyMade) {
		ArrayList<String> newToDo = new ArrayList<String>(Arrays.asList(newMoves));
        ArrayList<String> done = new ArrayList<String>(Arrays.asList(alreadyMade));
        
        done.stream().forEach(s -> newToDo.remove(s));
        return newToDo;
    }
}
