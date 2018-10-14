package com.um.omega.game;

import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFrame;

import Helpers.GameController;
import Helpers.Parsers;

public class Main{
	
	public final static int boardSize = 800;
	public static Game game;
	public static SimpleGame simpleGame;
	public static final int numberOfPlayers = 2;
	public final static int sizeSideHexagon = 5;
	
	private static int depthToSearch = 2;
	public static int numberOfHexagonsCenterRow;
	private static JFrame frame = new JFrame("Board");
	public static GameController gameController;
	// private static int numberOfSearches = 0;
	// Check Bitboard
	
	public static void main(String[] args) {

		numberOfHexagonsCenterRow = sizeSideHexagon * 2 - 1;

		play();
//		playAIvsAI();
//		debug();
//		simpleGame = new SimpleGame(sizeSideHexagon, 1);
//		int[][] bestPlays = new int[simpleGame.getGame().length][2];
//		for(int i = 0; i < simpleGame.getGame().length; i++) {
//			bestPlays[i] = new int[]{i, 1 + new Random().nextInt(20)};
//		}
//		
//		int[][] ordered = bestPlays.clone();
//
//		Arrays.sort(ordered, Comparator.comparing((int[] arr) -> arr[1])
//                .reversed());
//		for(int[] i : ordered)
//			System.out.println(Arrays.toString(i));
//		System.out.println("--------------");
//		for(int[] i : bestPlays)
//			System.out.println(Arrays.toString(i));
			


	}
	
	
	public static void debug() {
		simpleGame = new SimpleGame(sizeSideHexagon, 1);
//		for(SimpleGame g: simpleGame.possibleGames()) {
//			System.out.println(g.getPlayHistory());
//			for(SimpleGame g1: g.possibleGames())
//				System.out.println(g1.getPlayHistory());
//			
//		}
	}
	
	public static void playAIvsAI() {
		simpleGame = new SimpleGame(sizeSideHexagon, 1);
		game = new Game(numberOfHexagonsCenterRow, 1);
		gameController = new GameController(game, simpleGame, numberOfPlayers, 1);
				
		while(simpleGame.isPossibleMoreRounds()) {

			oneSearch(gameController);
			System.out.println("1 done");
			System.out.println("----");
			printGame();			
			oneSearch(gameController);
			System.out.println("2 done");
			System.out.println("----");
			printGame();
		}
	}

	
	public static void play() {
		numberOfHexagonsCenterRow = sizeSideHexagon * 2 - 1;
		Scanner sc = new Scanner(System.in);
//		System.out.println("What player plays first? 1 or 2?");
		
		System.out.println("What player plays first? The opponent (2) or me (1)?");
		int whoPlaysFirst;
		while(true) {
			try {
				whoPlaysFirst = Integer.valueOf(sc.nextLine());							
				break;
			} catch (NumberFormatException e) {
				System.err.println("Error in the input");
			}
		}
		simpleGame = new SimpleGame(sizeSideHexagon, whoPlaysFirst);
		game = new Game(numberOfHexagonsCenterRow, whoPlaysFirst);
		gameController = new GameController(game, simpleGame, numberOfPlayers, whoPlaysFirst);
		
		if(gameController.isAIturn())
			oneSearch(gameController);
//		else
		printGame();
		System.out.println(Arrays.toString(simpleGame.getGame()));

		boolean lastRound = false;
		
		while(!lastRound) {

			System.out.println();
			System.out.println("PlayerToPlay (gameController):  " +gameController.getPlayerToPlay()+ " simpleGame: " +simpleGame.getPlayerToPlay());
			System.out.println("PlayerToMove: " +gameController.getPlayerToMove()+ " it's AI turn? " +gameController.isAIturn());
			System.out.println("The movements of the other player are: ");
			System.out.println("Player 1 (White, US): ");

			while(!gameController.isAIturn() && gameController.getPlayerToMove() == 1)
				try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace();}
			printGame();

			System.out.println("Player 2 (Black, OPPONENT): ");

			while(!gameController.isAIturn() && gameController.getPlayerToMove() == 2)
				try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace();}
			printGame();
			
			System.out.println("Is this correct? Yes, No?");
			String s = sc.nextLine().toUpperCase();
			if(s.equals("N") || s.equals("No")) {
				gameController.undoMoves();
			} else {
				lastRound |= gameController.confirmMoves();
				if(gameController.getFirstPlayer() == 1 && lastRound)
					break;
				System.out.println(Arrays.toString(simpleGame.getGame()));
				System.out.println("---- PlayerToPlay (gameController):  " +gameController.getPlayerToPlay()+ " simpleGame: " +simpleGame.getPlayerToPlay());
				oneSearch(gameController);
				if(depthToSearch < 14) depthToSearch += 2;
				SearchAlgorithms.numberOfSearches = 0;
				System.out.println("--------- PlayerToPlay (gameController):  " +gameController.getPlayerToPlay()+ " simpleGame: " +simpleGame.getPlayerToPlay());
				lastRound |= !game.isPossibleMoreRounds();
				if(gameController.getFirstPlayer() == 1 && lastRound)
					lastRound = false;
			}
			printGame();			
		}
		System.out.println();
		System.out.println();
		System.out.println("-------------------------------");
		System.out.println("GAME FINISHED");
		long p1 = game.getPunctuation(1);
		long p2 = game.getPunctuation(2);
		System.out.println("Player 1 = " +p1+ ".");
		System.out.println("Player 2 = " +p2+ ".");
		System.out.println("-------------------------------");
		System.out.println();
		System.out.println("THE WINNER IS PLAYER = " +(p1 > p2 ? 1 : 2)+ ".");
		System.out.println();
		System.out.println("-------------------------------");
		System.out.println();
		System.out.println();
		sc.close();
	}
	public static int i = -2;
	public static void oneSearch(GameController gameController) {
		System.out.println();
		System.out.println("Calculating ...");
		long startTime = System.currentTimeMillis();
		i++;
		if(i % 4 == 0)
			depthToSearch++;
		
		SearchAlgorithms.initiateMovesMade(simpleGame.getGame().length);
		String[] result = SearchAlgorithms.aspirationSearch(simpleGame, 10, depthToSearch, gameController, 150);

//		for(int[] i: SearchAlgorithms.movesMade) {
//			System.out.println(Arrays.toString(i));
//		}
//		System.out.println("-------");
//		for(int[] i: SearchAlgorithms.getOrderedMovesMade()) {
//			System.out.println(Arrays.toString(i));
//		}
			
//		String[] result = SearchAlgorithms.alphaBetaWithTT(simpleGame, depthToSearch, -99999999, 99999999, gameController, -1);
//		String[] result = SearchAlgorithms.aspirationSearch(simpleGame, 10, depthToSearch, gameController);		
//		String[] result = SearchAlgorithms.alphaBetaWithTT(game, depthToSearch, -99999999, 99999999);
		long  endTime = System.currentTimeMillis();
		double duration = (endTime - startTime) * 0.001;  
		int minutes = (int) duration/60;
		System.out.println("It took " +minutes+ " minutes " +(duration - minutes * 60) +" s to calculate it.");
		System.out.println("Number of searches: " +SearchAlgorithms.numberOfSearches+ " and prunes: " +SearchAlgorithms.prunings);

		System.out.println("Response: " +Arrays.asList(result));

		gameController.movesForAI(result[1]);
//		debugPrintSimpleGame(result[1]);
		System.out.println("Player 1 = " +game.getPunctuation(1)+ ".");
		System.out.println("Player 2 = " +game.getPunctuation(2)+ ".");

	}
	
//	public static void multipleSearch(int n) {
//		long startTime;
//		long endTime;
//		double duration;
//		int minutes;
//		double totalDuration = 0;
//		for(int i = 0; i < n; i++) {
////			numberOfSearches = 0;
//			startTime = System.currentTimeMillis();
//			SearchAlgorithms.aspirationSearch(game, 10, depthToSearch);		
//			SearchAlgorithms.cleanHashMap();
//			endTime = System.currentTimeMillis();
//			duration = (endTime - startTime) * 0.001;  
//			minutes = (int) duration/60;
//			totalDuration += duration;
////			System.out.println("The algorithm has done: " +numberOfSearches+ " searches");
//			System.out.println("Launch " +i+ " of the algorithm took " +minutes+ " minutes " +(duration - minutes * 60) +" s to finish.");
//		}
//		double avg = totalDuration / n;
//		minutes = (int) avg/60;
//		System.out.println("Doing " +n+ " searches, the average time was: " +avg+ ", meaning: " +minutes+ " minutes " +(avg - minutes * 60) +" s");
//	}
//	
	static int boardN = 1;
	public static void debugPrintSimpleGame(String moves) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("Parse game with moves: " +moves);
		Game debugGame = Parsers.parseGameFromSimpleGameMoves(moves, numberOfHexagonsCenterRow, gameController.getPlayerToPlay());
//		System.out.println("Parse game with moves: " +debugGame.getPlayHistory());
		long p1 = debugGame.getPunctuation(1);
		long p2 = debugGame.getPunctuation(2);
		System.out.println();
		System.out.println("-------------------------------");
		System.out.println("DEBUG Player 1 = " +p1+ ".");
		System.out.println("DEBUG Player 2 = " +p2+ ".");
		System.out.println("THE WINNER IS PLAYER = " +(p1 > p2 ? 1 : 2)+ ".");
		System.out.println("-------------------------------");
		System.out.println();
		JFrame frame = new JFrame("Debug board " +boardN++);
		UserInterface ui = new UserInterface(boardSize, numberOfHexagonsCenterRow, debugGame, gameController);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(ui);
		frame.setSize(boardSize, boardSize);
		frame.setVisible(true);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	public static void printGame() {
		UserInterface ui = new UserInterface(boardSize, numberOfHexagonsCenterRow, game, gameController);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(ui);
		frame.setSize(boardSize, boardSize);
		frame.setVisible(true);
	}
}