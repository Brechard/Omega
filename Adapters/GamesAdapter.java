package Adapters;

import java.util.ArrayList;

import com.um.omega.game.Cell;
import com.um.omega.game.Game;

public class GamesAdapter {

	public static String[] cells;
	
	public void createCells(ArrayList<Cell> listCells) {
		cells = new String[listCells.size()];
		for(Cell cell: listCells) {
			cells[cell.id] = cell.printXY();
		}
	}
	
	public static String translateSimpleGameToGame(String simpleGame) {
		String[] moves = simpleGame.split("\\.");
		String playHistory = "";
		for(String move: moves) {
			playHistory += "(" +move.split(",")[0]+ "," +cells[Integer.valueOf(move.split(",")[1])]+").";
		}
		return playHistory;
	}
	
	public static Game getGame(int[] simpleGame, int numberOfHexagonsCenterRow, int playerToPlay) {
		Game game = new Game(numberOfHexagonsCenterRow, playerToPlay);
		for(int i = 0; i < simpleGame.length; i++)
			if(simpleGame[i] != 0)
				game.setCellToPlayer(simpleGame[i], i);
		
		return game;
	}
	
	public static Game getGame(String moves, int numberOfHexagonsCenterRow, int playerToPlay) {
		String[] m = moves.split("\\.");
		Game game = new Game(numberOfHexagonsCenterRow, playerToPlay);
		for(String move: m) {
			game.setCellToPlayer(Integer.valueOf(move.split(",")[0]), Integer.valueOf(move.split(",")[1]));
		}
		return game;
	}
	
	
}
