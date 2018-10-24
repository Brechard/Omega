package Helpers;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.um.omega.game.Cell;
import com.um.omega.game.Game;
import com.um.omega.game.Main;
import com.um.omega.game.SimpleGame;

public class GameController {
	
	/**
	 * Since each player moves a tile for each player the differentiation is needed. 
	 * playerToMove indicate the owner of tile is going to be played by the playerToPlay
	 */
	private int playerToMove = 1;
	private int playerToPlay;
	
	private final int numberOfPlayers;
	private Game game;
	// For each player there is the information of the move, [x, y, id]
	private Integer[][] moves;
	private ArrayList<String> gameHistory = new ArrayList<>();
	private int movesByAI = 0;
	private long[] hashes;
	private SimpleGame simpleGame;
	private String[] cellsIdToXY;
	private HashMap<String, Integer> xyToIdMap = new HashMap<>();
	private int playerAI;
	private TextFileManager fileManager;
	public final int numberRoundsAInotPlay;
	private int round = 0;
	/**
	 * Creation of everything needed to control the game and simpleGame
	 * @param game
	 * @param simpleGame
	 * @param numberOfPlayers
	 * @param firstPlayer
	 */
	public GameController(Game game, SimpleGame simpleGame, int numberOfPlayers, int playerAI, int playerToPlay, String numberFile, int numberRoundsAInotPlay) {
		this.game = game;
		this.numberOfPlayers = numberOfPlayers;
		this.playerToPlay = playerToPlay;
		playerToMove = 1;
		this.moves = new Integer[numberOfPlayers][2];
		this.simpleGame = simpleGame;
		this.playerAI = playerAI;
		calculateHashes(simpleGame.getGame().length);
		ArrayList<Cell> allCells = new ArrayList<Cell>(game.emptyCells);
		allCells.addAll(game.player1);
		allCells.addAll(game.player2);
		startDictionariesGameSimpleGame(allCells);
		if(numberFile == null)
			fileManager = new TextFileManager(playerAI);
		else {
			fileManager = new TextFileManager(numberFile);			
			gameHistory = fileManager.getPlayHistory();
			round = (int) gameHistory.size() / 2;			
		}
		this.numberRoundsAInotPlay = numberRoundsAInotPlay;
	}
	
	/**
	 * Constructor for when a Game is created from scratch
	 * @param game
	 * @param simpleGame
	 * @param numberOfPlayers
	 * @param playerAI
	 */
	public GameController(Game game, SimpleGame simpleGame, int numberOfPlayers, int playerAI, int numberRoundsAInotPlay) {
		this(game, simpleGame, numberOfPlayers, playerAI, 1, null, numberRoundsAInotPlay);
	}

	/**
	 * Create a game from scratch where the AI is allow to play from the beginning
	 * @param game
	 * @param simpleGame
	 * @param numberOfPlayers
	 * @param playerAI
	 */
	public GameController(Game game, SimpleGame simpleGame, int numberOfPlayers, int playerAI) {
		this(game, simpleGame, numberOfPlayers, playerAI, 1, null, 0);
	}

	/**
	 * The AI will always be considered the player 1
	 * @return
	 */
	public boolean isAIturn() {
		return playerToPlay == playerAI;
	}
	
	public boolean isAIallowToPlay() {
		return round >= numberRoundsAInotPlay;
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
		simpleGame.makeMove(player, cellId);
		gameHistory.add("(" +player+ ", " +x+ ", " +y+ ", " +cellId+")");
		fileManager.saveMoveInText(player, x, y, cellId);
		movesByAI++;
		if(movesByAI == numberOfPlayers)
			playerFinish();
	}
	
	public void moveMade(int x, int y) {
		int id;
		try{
			id = game.setCellToPlayer(playerToMove, x, y);
			System.out.println("The cell ID is: " +id);
		} catch (Error e) {
			System.err.println(e+ ", cell already in use");
			return;
		}
		moves[playerToMove - 1] = new Integer[]{x, y, id};
		playerToMove++;
		if(playerToMove > numberOfPlayers)
			playerToMove = 1;
		
	}
	
	public boolean confirmMoves() {
		for(int player = 0; player < moves.length; player++) {
			// The moves are placed in player - 1 because arrays position start in 0
			int x = moves[player][0];
			int y = moves[player][1];
			game.uniteMoveConfirmed(player + 1, x, y);
			simpleGame.makeMove(player + 1, moves[player][2]);
			gameHistory.add("(" +(player + 1)+ ", " 
								+moves[player][0]+ ", " 
								+moves[player][1]+ ", " 
								+moves[player][2]+ ")");
			fileManager.saveMoveInText(player + 1, moves[player][0], moves[player][1], moves[player][2]);
		}
		playerFinish();
		System.out.println();
		System.out.println("Moves confirmed");
		System.out.println("It is the turn of the player: " +playerToPlay);
		System.out.println();
		return !game.isPossibleMoreRounds();
	}
	
	public void playerFinish() {
		movesByAI = 0;
		playerToPlay++;
		if(playerToPlay > numberOfPlayers) {			
			playerToPlay = 1;
			round++;
			System.out.println("Round: " +round);
		}
		clearMoves();
	}
	
	public void undoMoves() {
		playerToMove = 1;
		for(int player = 0; player < moves.length; player++) {
			// The moves are placed in player - 1 because arrays position start in 0
			game.deleteMove(player + 1, moves[player][0], moves[player][1]);
		}
		clearMoves();
	}
	
	public void finishGame() {
		System.out.println();
		System.out.println("-------------------------------");
		System.out.println();
		System.out.println("GAME FINISHED");
		System.out.println();
		long p1 = game.getPunctuation(1);
		long p2 = game.getPunctuation(2);
		System.out.println("Player 1 = " +p1+ ".");
		System.out.println("Player 2 = " +p2+ ".");
		System.out.println();
		System.out.println("-------------------------------");
		System.out.println();
		if(p1 == p2)
			System.out.println("BOTH PLAYER HAVE THE SAME RESULT");
		else 
			System.out.println("THE WINNER IS PLAYER = " +(p1 > p2 ? 1 : 2)+ ".");
		System.out.println();
		System.out.println("-------------------------------");
		System.out.println();
		System.out.println();
		fileManager.finishGame(p1, p2);
	}
	
	public void calculateHashes(int size) {
		hashes = new long[size];
		for(int i = 0; i < size; i++)
			hashes[i] = new SecureRandom().nextLong();		
	}
	
	public long getHash() {
		long hash = 0;
		int[] game = simpleGame.getGame();
		for(int i = 0; i < game.length; i++)
			hash ^= hashes[i] * game[i];
		return hash;
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

	/**
	 * @return the playerToMove
	 */
	public int getPlayerToMove() {
		return playerToMove;
	}
	
	public int getPlayerToPlay() {
		return playerToPlay;
	}

	public void clearMoves() {
		moves = new Integer[numberOfPlayers][2];
	}

	public int getAIPlayer() {
		return playerAI;
	}

	public ArrayList<String> getGameHistory() {
		return gameHistory;
	}

}
