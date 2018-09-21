package com.um.omega.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFrame;

public class Main{
	
	public final static int boardSize = 800;
	public static Game game;
	public static final int numberOfPlayers = 2;
	public final static int sizeSideHexagon = 4;
	private final static int playerToRate = 1;
	private final static int firstPlayer = 1;
	private final static int depthToSearch = 10;
	private static int numberOfHexagonsCenterRow;
	private static HashMap<Long, TTInfo> hashMap = new HashMap<Long, TTInfo>();
//	private static int numberOfSearches = 0;
	// Check Bitboard
	
	public static void main(String[] args) {

		numberOfHexagonsCenterRow = sizeSideHexagon * 2 - 1;
		game = new Game(numberOfHexagonsCenterRow, playerToRate, firstPlayer);
//		multipleSearch(10);
		oneSearch();
	}
	
	public static void oneSearch() {
//		numberOfSearches = 0;
		long startTime = System.currentTimeMillis();
		System.out.println("The best search is:");
		String[] result = alphaBetaWithTT(game, depthToSearch, -99999999, 99999999);
		long  endTime = System.currentTimeMillis();
		game = parser(result[1]);
		System.out.println("Points for player: " +playerToRate+ " = " +game.getPunctuation(playerToRate)+ ".");
		System.out.println("The history of the game is: "+result[1]);
		double duration = (endTime - startTime) * 0.001;  
		int minutes = (int) duration/60;
//		System.out.println("The algorithm has done: " +numberOfSearches+ " searches");
		System.out.println("It took " +minutes+ " minutes " +(duration - minutes * 60) +" s to calculate it.");
//		System.out.println("Repeteaded hashes: " +hashCount);
		printGame(game, "Board");
	}
	
	public static void multipleSearch(int n) {
		long startTime;
		long endTime;
		double duration;
		int minutes;
		double totalDuration = 0;
		for(int i = 0; i < n; i++) {
//			numberOfSearches = 0;
			startTime = System.currentTimeMillis();
			alphaBetaWithTT(game, depthToSearch, -99999999, 99999999);
			endTime = System.currentTimeMillis();
			duration = (endTime - startTime) * 0.001;  
			minutes = (int) duration/60;
			totalDuration += duration;
//			System.out.println("The algorithm has done: " +numberOfSearches+ " searches");
			System.out.println("Launch " +i+ " of the algorithm took " +minutes+ " minutes " +(duration - minutes * 60) +" s to finish.");
		}
		double avg = totalDuration / n;
		minutes = (int) avg/60;
		System.out.println("Doing " +n+ " searches, the averga time was: " +avg+ ", meaning: " +minutes+ " minutes " +(avg - minutes * 60) +" s");
	}
	
//	public static int hashCount = 0;
//	public static ArrayList<Long> hashes = new ArrayList<>();

	public static String[] alphaBeta(Game game, int depth, int alpha, int beta) {
//		numberOfSearches++;
//		System.out.println("The game observed now is: " +game.playHistory);
		if(!game.isPossibleMoreMoves() || depth == 0) {
//			long h = game.getHash();
//			if(hashes.contains(h))
//				hashCount++;
//			else hashes.add(h);
			return new String[] {String.valueOf(game.getRate()), game.playHistory};
		}
		
		int score = (int) -999999999;

		String[] valueToReturn = new String[] {String.valueOf(score), ""};
		String[] valueHelper = new String[] {String.valueOf(score), ""};
		ArrayList<Game> childGames = game.possibleGames();
		int value;
		for(int child = 0; child < childGames.size(); child++) {
			valueHelper = alphaBeta(childGames.get(child), depth -1, -beta, -alpha);
			value = -Integer.valueOf(valueHelper[0]);
			if(value > score) {
				score = value;
				valueToReturn = new String[]{String.valueOf(value), valueHelper[1]};
			}
			if(score > alpha) alpha = score;
			if(score >= beta) break;
		}
		return valueToReturn;
	}
	
	public static String[] alphaBetaWithTT(Game game, int depth, long alpha, long beta) {
		long hash = game.getHash();
		if(hashMap.containsKey(hash))
			return hashMap.get(hash).getInfo();
//		numberOfSearches++;
//		System.out.println("The game observed now is: " +game.playHistory);
		if(!game.isPossibleMoreMoves() || depth == 0) {
//			long h = game.getHash();
//			if(hashes.contains(h))
//				hashCount++;
//			else hashes.add(h);
			return new String[] {String.valueOf(game.getRate()), game.playHistory};
		}
		
		long score = (long) -999999999;

		String[] valueToReturn = new String[] {String.valueOf(score), ""};
		String[] valueHelper = new String[] {String.valueOf(score), ""};
		ArrayList<Game> childGames = game.possibleGames();
		long value;
		for(int child = 0; child < childGames.size(); child++) {
			valueHelper = alphaBetaWithTT(childGames.get(child), depth -1,(long) -beta,(long) -alpha);
			value = -Long.valueOf(valueHelper[0]);
			if(value > score) {
				score = value;
				valueToReturn = new String[]{String.valueOf(value), valueHelper[1]};
			}
			if(score > alpha) alpha = score;
			if(score >= beta) break;
		}
		hashMap.put(hash, new TTInfo(0, valueHelper));
		return valueToReturn;
	}

	
	public static Game parser(String s) {
		String[] list = s.split("\\.");
		for(String cell: list) {
			String[] cellData = cell.replace("(", "").replace(")", "").replace(" ", "").split(",");
			game.setCellToPlayer(Integer.valueOf(cellData[0]), Integer.valueOf(cellData[1]), Integer.valueOf(cellData[2]));
		}
		return game;
	}
	
	public static void printGame(Game game, String title) {
		JFrame frame = new JFrame(title);
		UserInterface ui = new UserInterface(boardSize, numberOfHexagonsCenterRow, game);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(ui);
		frame.setSize(boardSize, boardSize);
		frame.setVisible(true);
	}
}