package Helpers;

import java.util.ArrayList;

import com.um.omega.game.Game;

public class GameController {
	
	/**
	 * Since each player moves a tile for each player the differentiation is needed. 
	 * playerToMove indicate the owner of tile is going to be played by the playerToPlay
	 */
	private int playerToMove = 1;
	private int playerToPlay;
	
	private final int numberOfPlayers;
	private final int firstPlayer;
	private Game game;
	private Integer[][] moves;
	private ArrayList<String> gameHistory = new ArrayList<>();
	private int movesByAI = 0;
	
	public GameController(Game game, int numberOfPlayers, int firstPlayer) {
		this.game = game;
		this.numberOfPlayers = numberOfPlayers;
		this.firstPlayer = firstPlayer;
		this.playerToPlay = firstPlayer;
		this.moves = new Integer[numberOfPlayers][2];
	}
	
	/**
	 * The AI will always be considered the player 1
	 * @return
	 */
	public boolean isAIturn() {
		return playerToPlay == 1;
	}
	
	public void movesForAI(String s) {
		Parsers.parseNextMove(s, this);
	}

	public void moveAI(int player, int x, int y) {
		try{
			game.setCellToPlayer(player, x, y);
		} catch (Error e) {
			System.err.println(e+ ", ERROR IN THE AI");
			return;
		}
		game.uniteMoveConfirmed(player, x, y);
		gameHistory.add("(" +player+ "," +x+ "," +y+ ")");
		movesByAI++;
		if(movesByAI == numberOfPlayers)
			playerFinish();
	}
		
	public void moveMade(int x, int y) {
		try{
			game.setCellToPlayer(playerToMove, x, y);
		} catch (Error e) {
			System.err.println(e+ ", cell already in use");
			return;
		}
		moves[playerToMove - 1] = new Integer[]{x, y};
		playerToMove++;
		if(playerToMove > numberOfPlayers)
			playerToMove = 1;
	}
	
	public boolean confirmMoves() {
		for(int player = 0; player < moves.length; player++) {
			// The moves are placed in player - 1 because arrays position start in 0
			game.uniteMoveConfirmed(player + 1, moves[player][0], moves[player][1]);
			gameHistory.add("(" +(player + 1)+ "," +moves[player][0]+ "," +moves[player][1]+ ")");
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
		if(playerToPlay > numberOfPlayers)
			playerToPlay = 1;		
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

	/**
	 * @return the playerToMove
	 */
	public int getPlayerToMove() {
		return playerToMove;
	}

	public void clearMoves() {
		moves = new Integer[numberOfPlayers][2];
	}

	public int getFirstPlayer() {
		return firstPlayer;
	}

	public ArrayList<String> getGameHistory() {
		return gameHistory;
	}

}
