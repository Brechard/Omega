package com.um.omega.game;

import java.util.ArrayList;
import java.util.Arrays;

import Helpers.SimpleGameHelpers;
import ObjectsToHelp.CellType;

public class SimpleGame {

	private int numberOfHexagonsSide;
	private int[] game;
	private int playerAI;
	private int playerCalculate;
	private int playerToMove;
	public UnionFind unionAI;
	public UnionFind unionOpponent;
	private String playHistory = "";
	public int[][] lastMoves;

	public SimpleGame(int numberOfHexagonsSide, int playerAI, int[] game, int[][] moves, UnionFind[] unionFinds, String playHistory, int playerCalculate) {
		this.numberOfHexagonsSide = numberOfHexagonsSide;
		this.playerAI = playerAI;
		this.playerCalculate = playerCalculate;
		this.game = game.clone();
		this.unionAI = new UnionFind(unionFinds[0]);
		this.unionOpponent = new UnionFind(unionFinds[1]);
		this.playHistory =  playHistory;
		this.lastMoves = moves;
		for(int[] move: moves) {
//			System.out.println("Make move " +move[1]+ ", player" +move[0]);
			makeMove(move[0], move[1]);
		}
	}

	public SimpleGame(int numberOfHexagonsSide, int playerAI, int[] game, int[] moves, UnionFind[] unionFinds, String playHistory, int playerToMove, int playerCalculate) {
		this.numberOfHexagonsSide = numberOfHexagonsSide;
		this.playerAI = playerAI;
		this.playerCalculate = playerCalculate;
		this.game = game.clone();
		this.unionAI = new UnionFind(unionFinds[0]);
		this.unionOpponent = new UnionFind(unionFinds[1]);
		this.playHistory =  playHistory;
		this.playerToMove = playerToMove;
		makeMove(moves[0], moves[1]);
	}

	/**
	 * Create an empty game
	 * @param numberOfHexagonsCenterRow
	 * @param playerToPlay
	 */
	public SimpleGame(int numberOfHexagonsSide, int playerAI) {
		this.numberOfHexagonsSide = numberOfHexagonsSide;
		int numberOfCells = 1;
		for(int i = 1; i < numberOfHexagonsSide; i++)
			numberOfCells += 6*i;
		game = new int[numberOfCells];
		Arrays.fill(game, 0);
		this.playerAI= playerAI;
		this.playerCalculate = playerAI;
		unionAI = new UnionFind(numberOfCells);
		unionOpponent = new UnionFind(numberOfCells);
		playerToMove = 1;
//		moveConfirmed();
	}
	
	public void makeMove(int player, int cellId){
//		if(game[cellId] != 0)
//			throw new Error("The cellId " +cellId+ " is already occupied " +Arrays.toString(game));
		game[cellId] = player;
		moveConfirmed(player, cellId);
	}
	
	public void moveConfirmed(int player, int cellId) {
		if(player == playerAI)
//			hasNeighbours(player, cellId, union1);
//		else
//			hasNeighbours(player, cellId, union2);
			unionAI = SimpleGameHelpers.searchNeighbourForUnionFind(cellId, unionAI, game, numberOfHexagonsSide);
		else unionOpponent = SimpleGameHelpers.searchNeighbourForUnionFind(cellId, unionOpponent, game, numberOfHexagonsSide);
		playHistory += player+ "," +cellId+ ".";
	}
	
//	public void hasNeighbours(int player, int cellId, UnionFind unionFind) {
//		for(Integer cell2Id: Main.gameController.getNeighbours(cellId)) {
//			if(game[cell2Id] == player)
//				unionFind.unite(cellId, cell2Id);
//		}
//	}

	
	public void deleteMove(int cellId) {
		game[cellId] = 0;
	}
	
	public long getRate() {
		return playerAI == playerCalculate ? unionAI.getCount() - unionOpponent.getRealCount() : unionOpponent.getCount() - unionAI.getRealCount();
	}
	
	public ArrayList<SimpleGame> possibleGames(int[][] moves) {
		ArrayList<SimpleGame> possibleGames = new ArrayList<>();
//		int[][] moves = SearchAlgorithms.getOrderedMovesMade();
		for(int i = 0; i < moves.length; i++) {
//			System.out.print("Move 1 to do now "+moves[i][0]);
//			System.out.println(", " +game[moves[i][0]]);
			if(game[moves[i][0]] == 0) { // The Cell is empty

//				possibleGames.add(new SimpleGame(numberOfHexagonsSide, 
//						playerToMove == 2 ? (playerToPlay == 2 ? 1 : 2) : playerToPlay,
//								game, 
//								new int[] {playerToMove, i},
//								new UnionFind[] {union1, union2},
//								playHistory, 
//								playerToMove == 1 ? 2 : 1));
				
				for(int j = i + 1; j < moves.length; j++) {
//					System.out.print("Move 2 to do now "+moves[j][0]);
//					System.out.println(", " +game[moves[j][0]]);
					if(game[moves[j][0]] == 0) {
						possibleGames.add(new SimpleGame(numberOfHexagonsSide, 
								playerAI,
								game,
								new int[][] {new int[] {1, moves[i][0]}, new int[] {2, moves[j][0]}},
								new UnionFind[] {unionAI, unionOpponent},
								playHistory,
								playerCalculate == 1 ? 2 : 1));
						possibleGames.add(new SimpleGame(numberOfHexagonsSide, 
								playerAI,
								game,
								new int[][] {new int[] {2, moves[i][0]}, new int[] {1, moves[j][0]}},
								new UnionFind[] {unionAI, unionOpponent},
								playHistory,
								playerCalculate == 1 ? 2 : 1));
					}
				}
			}
		}
		return possibleGames;
	}
	
//	public ArrayList<SimpleGame> possibleGames() {
//		ArrayList<SimpleGame> possibleGames = new ArrayList<>();
//		for(int i = 0; i < game.length; i++) {
//			if(game[i] == 0) { // The Cell is empty
//				for(int j = i + 1; j < game.length; j++) {
////					System.out.print("Move 2 to do now "+moves[j][0]);
////					System.out.println(", " +game[moves[j][0]]);
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
//			}
//		}
//		return possibleGames;
//	}

		
	public int[] getGame() {
		return game;
	}
	
	public void printUnions() {
		System.out.println();
		System.out.println("Union AI: ");
		unionAI.printUnionFind();
		System.out.println();
		System.out.println("Union Opponent: ");
		unionOpponent.printUnionFind();
	}
	
	public void print(String s) {
		System.out.println(s);
	}
	public void print(int i) {
		print(String.valueOf(i));
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
	
	public int getPlayerAI() {
		return playerAI;
	}

	public int getPlayerToMove() {
		return playerToMove;
	}
	
	public SimpleGame setAIPlayer(int player) {
		playerAI = player;
		return this;
	}
}















