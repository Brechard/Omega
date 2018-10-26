package Debug;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import com.um.omega.game.Cell;
import com.um.omega.game.Game;
import com.um.omega.game.SimpleGame;

import Helpers.Parsers;

public class GameController2 {
	
	/**
	 * Since each player moves a tile for each player the differentiation is needed. 
	 * playerToMove indicate the owner of tile is going to be played by the playerToPlay
	 */
	private Game game;
	// For each player there is the information of the move, [x, y, id]
	private ArrayList<String> gameHistory = new ArrayList<>();
	private long[] hashes;
	private SimpleGame simpleGame1;
	private SimpleGame simpleGame2;
	private String[] cellsIdToXY;
	private HashMap<String, Integer> xyToIdMap = new HashMap<>();
	private int firstPlayer;
	/**
	 * Creation of everything needed to control the game and simpleGame
	 * @param game
	 * @param simpleGame
	 * @param numberOfPlayers
	 * @param firstPlayer
	 */
	public GameController2(Game game, SimpleGame simpleGame1, SimpleGame simpleGame2, int numberOfPlayers, int firstPlayer) {
		this.game = game;
		this.simpleGame1 = simpleGame1;
		this.simpleGame2 = simpleGame2;
		this.firstPlayer = firstPlayer;
		calculateHashes(simpleGame1.getGame().length);
		ArrayList<Cell> allCells = new ArrayList<Cell>(game.emptyCells);
		allCells.addAll(game.player1);
		allCells.addAll(game.player2);
		startDictionariesGameSimpleGame(allCells);
	}
	
	public void movesForAI(String simpleGameMoves) {
		Parsers.parseNextMove(Parsers.parseSimpleGameMovesToGame(simpleGameMoves, this), this);
	}

	public void moveAI(int player, int x, int y, int cellId) {
		try{
			game.setCellToPlayer(player, x, y);
		} catch (Error e) {
			System.err.println(e+ ", ERROR IN THE AI");
			return;
		}
		game.uniteMoveConfirmed(player, x, y);
		simpleGame1.makeMove(player, cellId);
		simpleGame2.makeMove(player, cellId);
		gameHistory.add("(" +player+ ", " +x+ ", " +y+ ", " +cellId+")");
	}
	
	public void calculateHashes(int size) {
		hashes = new long[size];
		for(int i = 0; i < size; i++)
			hashes[i] = new SecureRandom().nextLong();		
	}
	
	public long getHash(int[] game) {
		long hash = 0;
		for(int i = 0; i < game.length; i++)
			hash ^= hashes[i] * game[i];
		return hash;
	}
	
	/**
	 * Get the coordinates x and y of a cell given its Id
	 * @param id
	 * @return "x,y"
	 */
	public String getXYFromId(int id) {
		return cellsIdToXY[id];
	}
	
	public int getIdFromXY(int x, int y) {
		return xyToIdMap.get(x+ ", " +y);
	}
	
	public void startDictionariesGameSimpleGame(ArrayList<Cell> listCells) {
		cellsIdToXY = new String[listCells.size()];
		
		for(Cell cell: listCells) {
			cellsIdToXY[cell.id] = cell.printXY();
			xyToIdMap.put(cell.printXY(), cell.id);
		}
	}

	public int getFirstPlayer() {
		return firstPlayer;
	}

	public ArrayList<String> getGameHistory() {
		return gameHistory;
	}

	public Game getGame() {
		return game;
	}
}
