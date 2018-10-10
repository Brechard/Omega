package Helpers;

import java.security.SecureRandom;
import java.util.ArrayList;

import com.um.omega.game.SimpleGame;

public class SimpleGameController {

	/**
	 * Since each player moves a tile for each player the differentiation is needed. 
	 * playerToMove indicate the owner of tile is going to be played by the playerToPlay
	 */
	private int playerToMove = 1;
	private int playerToPlay;
	
	private final int numberOfPlayers;
	private final int firstPlayer;
	private SimpleGame simpleGame;
	private int[] game;
	private Integer[][] moves;
	private ArrayList<String> gameHistory = new ArrayList<>();
	private int movesByAI = 0;
	private long[] hashes;
	
	public SimpleGameController(SimpleGame game, int numberOfPlayers, int firstPlayer) {
		this.simpleGame = game;
		this.game = game.getGame();
		this.numberOfPlayers = numberOfPlayers;
		this.firstPlayer = firstPlayer;
		this.playerToPlay = firstPlayer;
		this.moves = new Integer[numberOfPlayers][2];
		calculateHashes(game.getGame().length);
	}
	
//	public void moveAI(int player, int cellId) {
//		simpleGame.makeMove(player, cellId);
//		simpleGame.moveConfirmed(player, cellId);
//		gameHistory.add(player+ "," +cellId);
//		movesByAI++;
//		if(movesByAI == numberOfPlayers)
//			playerFinish();
//	}
	
	public void makeMove(int player, int cellId) {
		simpleGame.makeMove(player, cellId);
		gameHistory.add(player+ "," +cellId);
	}
	
	public void calculateHashes(int size) {
		hashes = new long[size];
		for(int i = 0; i < size; i++)
			hashes[i] = new SecureRandom().nextLong();		
	}
	
	public long getHash() {
		long hash = 0;
		for(int i = 0; i < game.length; i++)
			hash ^= hashes[i] * game[i];
		return hash;
	}
	
	public long getHash(SimpleGame simpleGame) {
		int[] game = simpleGame.getGame();
		long hash = 0;
		for(int i = 0; i < game.length; i++)
			hash ^= hashes[i] * game[i];
		return hash;
	}

}
