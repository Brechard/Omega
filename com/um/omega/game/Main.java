package com.um.omega.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFrame;

import Helpers.Flag;
import Helpers.Moves;
import Helpers.Parsers;

public class Main{
	
	public final static int boardSize = 800;
	public static Game game;
	public static String gameHistory;
	public static final int numberOfPlayers = 2;
	public final static int sizeSideHexagon = 3;
	
	private final static int depthToSearch = 6;
	public static int numberOfHexagonsCenterRow;
	private static JFrame frame = new JFrame("Board");
	public static int playerToPlay;
	public static String player1Move;
	public static String player2Move;
	
	// private static int numberOfSearches = 0;
	// Check Bitboard
	
	public static void main(String[] args) {
		gameHistory = "";
		numberOfHexagonsCenterRow = sizeSideHexagon * 2 - 1;
		
		Scanner sc = new Scanner(System.in);
//		System.out.println("What player plays first? 1 or 2?");
		
		System.out.println("What player plays first? The opponent (2) or me (1)?");
		int whoPlaysFirst = Integer.valueOf(sc.nextLine());
		game = new Game(numberOfHexagonsCenterRow, whoPlaysFirst);
		if(whoPlaysFirst == 1) {
			playerToPlay = 0;
			oneSearch();
		}
		else
			printGame();
		playerToPlay = 1;

//		int i = 0;

		player1Move = "";
		player2Move = "";
		while(game.isPossibleMoreMoves()) {
//			i++;
			System.out.println();
			System.out.println("The movements of the other player are: ");

			System.out.println("Player 1 (White, US): ");
			playerToPlay = 1;
			while(playerToPlay == 1)
				try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace();}
			printGame();

			System.out.println("Player 2 (Black, OPPONENT): ");
			while(playerToPlay == 2)
				try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace();}
			printGame();
			
			System.out.println("Is this correct? Yes, No?");
			String s = sc.nextLine().toUpperCase();
			if(s.equals("N") || s.equals("No")) {
				Moves.undoMove(1, player1Move);
				Moves.undoMove(2, player2Move);
			} else {
				playerToPlay = 0;
				Moves.moveConfirmed();
				oneSearch();
			}
			printGame();
		}
		sc.close();
	}
	
	public static void oneSearch() {
//		numberOfSearches = 0;
		System.out.println();
		System.out.println("Calculating ...");
//		game.playHistory = "";
		long startTime = System.currentTimeMillis();
		String[] result = SearchAlgorithms.aspirationSearch(game, 10, depthToSearch);		
//		String[] result = SearchAlgorithms.alphaBetaWithTT(game, depthToSearch, -99999999, 99999999);
		long  endTime = System.currentTimeMillis();
		System.out.println("Response: " +Arrays.asList(result));
		Parsers.parseGameDebug(result[1]);
		Parsers.parseNextMove(result[1]);
		System.out.println("Player 1 = " +game.getPunctuation(1)+ ".");
		System.out.println("Player 2 = " +game.getPunctuation(2)+ ".");
//		System.out.println("The history of the game is: "+result[1]);
		double duration = (endTime - startTime) * 0.001;  
		int minutes = (int) duration/60;
//		System.out.println("The algorithm has done: " +numberOfSearches+ " searches");

		System.out.println("It took " +minutes+ " minutes " +(duration - minutes * 60) +" s to calculate it.");

//		System.out.println("Repeteaded hashes: " +hashCount);
//		findNextMove();
		printGame();
//		parseGame();
//		game.playHistory = "";		
		
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
			SearchAlgorithms.alphaBetaWithTT(game, depthToSearch, -99999999, 99999999);
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
	

	
	public static void printGame() {
		UserInterface ui = new UserInterface(boardSize, numberOfHexagonsCenterRow, game);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(ui);
		frame.setSize(boardSize, boardSize);
		frame.setVisible(true);
	}
}