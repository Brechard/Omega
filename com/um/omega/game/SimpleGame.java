package com.um.omega.game;

import java.util.ArrayList;
import java.util.Arrays;

import Helpers.CellType;
import Helpers.SimpleGameHelpers;

public class SimpleGame {

	private int numberOfHexagonsSide;
	private int[] game;
	private int playerToPlay;
	private int playerToMove;
	public UnionFind union1;
	public UnionFind union2;
	private String playHistory = "";

	public SimpleGame(int numberOfHexagonsSide, int playerToPlay, int[] game, int[][] moves, UnionFind[] unionFinds, String playHistory) {
		this.numberOfHexagonsSide = numberOfHexagonsSide;
		this.playerToPlay = playerToPlay;
		this.game = game.clone();
		this.union1 = new UnionFind(unionFinds[0]);
		this.union2 = new UnionFind(unionFinds[1]);
		this.playHistory =  playHistory;
		for(int[] move: moves) {
			makeMove(move[0], move[1]);
			moveConfirmed(move[0], move[1]);
		}
	}

	public SimpleGame(int numberOfHexagonsSide, int playerToPlay, int[] game, int[] moves, UnionFind[] unionFinds, String playHistory, int playerToMove) {
		this.numberOfHexagonsSide = numberOfHexagonsSide;
		this.playerToPlay = playerToPlay;
		this.game = game.clone();
		this.union1 = new UnionFind(unionFinds[0]);
		this.union2 = new UnionFind(unionFinds[1]);
		this.playHistory =  playHistory;
		this.playerToMove = playerToMove;
		makeMove(moves[0], moves[1]);
	}

	/**
	 * Create an empty game
	 * @param numberOfHexagonsCenterRow
	 * @param playerToPlay
	 */
	public SimpleGame(int numberOfHexagonsSide, int playerToPlay) {
		this.numberOfHexagonsSide = numberOfHexagonsSide;
		int numberOfCells = 1;
		for(int i = 1; i < numberOfHexagonsSide; i++)
			numberOfCells += 6*i;
		game = new int[numberOfCells];
		Arrays.fill(game, 0);
		this.playerToPlay = playerToPlay;
		union1 = new UnionFind(numberOfCells);
		union2 = new UnionFind(numberOfCells);
		playerToMove = 1;
//		moveConfirmed();
	}
	
	public void makeMove(int player, int cellId){
		game[cellId] = player;
		moveConfirmed(player, cellId);
	}
	
	public void deleteMove(int cellId) {
		game[cellId] = 0;
	}
	
	public long getRate() {
		return playerToPlay == 1 ? union1.getCount() : union2.getCount();
	}
	
	public ArrayList<SimpleGame> possibleGames() {
		ArrayList<SimpleGame> possibleGames = new ArrayList<>();
		for(int i = 0; i < game.length - 1; i++) {
			if(game[i] == 0) { // The Cell is empty
				possibleGames.add(new SimpleGame(numberOfHexagonsSide, 
					playerToMove == 1 ? (playerToPlay == 1? 2 : 1) : playerToMove,
					game, 
					new int[] {playerToMove, i},
					new UnionFind[] {union1, union2},
					playHistory, 
					playerToMove == 1 ? 2 : 1));
				
//				for(int j = i + 1; j < game.length; j++) {
//					if(game[j] == 0) {
//						possibleGames.add(new SimpleGame(numberOfHexagonsSide, 
//								playerToPlay == 1 ? 2 : 1,
//								game, 
//								new int[][] {new int[] {1, i}, new int[] {2, j}},
//								new UnionFind[] {union1, union2},
//								playHistory));
//						possibleGames.add(new SimpleGame(numberOfHexagonsSide, 
//								playerToPlay == 1 ? 2 : 1,
//								game, 
//								new int[][] {new int[] {2, i}, new int[] {1, j}},
//								new UnionFind[] {union1, union2},
//								playHistory));
//					}
//				}
			}
		}
		return possibleGames;
	}
	
	public void moveConfirmed(int player, int cellId) {
		if(player == 1)
			union1 = SimpleGameHelpers.searchNeighbourForUnionFind(cellId, union1, game, numberOfHexagonsSide);
		else union2 = SimpleGameHelpers.searchNeighbourForUnionFind(cellId, union2, game, numberOfHexagonsSide);
		playHistory += player+ "," +cellId+ ".";
	}
	
	public int[] getGame() {
		return game;
	}
	
	public void printUnions() {
		System.out.println();
		System.out.println("Union 1: ");
		union1.printUnionFind();
		System.out.println();
		System.out.println("Union 2: ");
		union2.printUnionFind();
	}
	
	public void print(String s) {
		System.out.println(s);
	}
	public void print(int i) {
		print(String.valueOf(i));
	}
	
	public void getHash() {
		
	}
	
	public boolean isPossibleMoreRounds() {
		int empty = 0;
		for(int i: game)
			if(i == 0) empty++;
		return empty > 4;
	}

	public boolean isPossibleMoreMoves() {
		int empty = 0;
		for(int i: game)
			if(i == 0) empty++;
		return empty > 2;
	}

	public String getPlayHistory() {
		return playHistory;
	}
}















