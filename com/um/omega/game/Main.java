package com.um.omega.game;

import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFrame;

import Debug.GameController2;
import Debug.SearchAlgorithms2;
import Helpers.GameController;
import Helpers.Parsers;
import Helpers.TextFileManager;
import ObjectsToHelp.Games;

public class Main{
	
	public final static int boardSize = 800;
	public static Game game;
	public static SimpleGame simpleGame;
	public static final int numberOfPlayers = 2;
	public final static int sizeSideHexagon = 4;
	
	private static int depthToSearch = 1;
	public static int numberOfHexagonsCenterRow;
	private static JFrame frame = new JFrame("Board");
	public static GameController gameController;
	// private static int numberOfSearches = 0;
	// Check Bitboard
	
	public static void main(String[] args) {

		numberOfHexagonsCenterRow = sizeSideHexagon * 2 - 1;

		play(recoverLastGame(true));
		
//		playAIvsAI();

//		debug(depthToSearch);
		
	}
	
	public static Games recoverLastGame(boolean recover) {
		if(!recover)
			return null;
		return TextFileManager.recoverLastGame();
	}
	
	public static void play(Games gamesRecovered) {
		Scanner sc = new Scanner(System.in);
//		System.out.println("What player plays first? 1 or 2?");
		int playerAI;
		if(gamesRecovered != null) {
			simpleGame = gamesRecovered.simpleGame;
			game = gamesRecovered.game;
			playerAI = simpleGame.getPlayerAI();
		} else {
			System.out.println("What player is the AI?");
			while(true) {
				try {
					playerAI = Integer.valueOf(sc.nextLine());							
					if(playerAI != 1 && playerAI != 2)
						throw new NumberFormatException("It is only possible to select between player 1 or 2");
					break;
				} catch (NumberFormatException e) {
					System.err.println("Error in the input " +e);
				}
			}
			simpleGame = new SimpleGame(sizeSideHexagon, playerAI);
			game = new Game(numberOfHexagonsCenterRow, playerAI);
		}
		if(gamesRecovered != null)
			gameController = new GameController(game, simpleGame, numberOfPlayers, playerAI, gamesRecovered.playerTurn, gamesRecovered.fileNumber);
		else gameController = new GameController(game, simpleGame, numberOfPlayers, playerAI);

		if(gameController.isAIturn())
			oneSearch(gameController, simpleGame);
//		else
		printGame();
		System.out.println(Arrays.toString(simpleGame.getGame()));

		boolean lastRound = false;
		
		while(!lastRound) {

			System.out.println();
			System.out.println("PlayerToPlay (gameController):  " +gameController.getPlayerToPlay()+ " simpleGame AIPlayer: " +simpleGame.getPlayerAI());
			System.out.println("PlayerToMove: " +gameController.getPlayerToMove()+ " it's AI turn? " +gameController.isAIturn());
			System.out.println("The movements of the other player are: ");
			System.out.println("Player 1 (White, " +(playerAI == 1 ? " AI" : " OPPONENT")+"): ");

			while(!gameController.isAIturn() && gameController.getPlayerToMove() == 1)
				try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace();}
			printGame();
			
			System.out.println("Player 2 (Black, " +(playerAI == 2 ? " AI" : " OPPONENT")+"): ");

			while(!gameController.isAIturn() && gameController.getPlayerToMove() == 2)
				try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace();}
			printGame();
			System.out.println("Is this correct? Yes, No?");
			String s = sc.nextLine().toUpperCase();
			if(s.equals("N") || s.equals("No")) {
				gameController.undoMoves();
			} else {
				lastRound |= gameController.confirmMoves();
				if(gameController.getAIPlayer() == 1 && lastRound)
					break;
				System.out.println(Arrays.toString(simpleGame.getGame()));
				System.out.println("---- PlayerToPlay (gameController):  " +gameController.getPlayerToPlay()+ " simpleGame AIPlayer: " +simpleGame.getPlayerAI());
				oneSearch(gameController, simpleGame);
				SearchAlgorithms.numberOfSearches = 0;
				System.out.println("--------- PlayerToPlay (gameController):  " +gameController.getPlayerToPlay()+ " simpleGame AIPlayer: " +simpleGame.getPlayerAI());
				lastRound |= !game.isPossibleMoreRounds();
				if(gameController.getAIPlayer() == 1 && lastRound)
					lastRound = false;
			}
			printGame();			
		}
		gameController.finishGame();
		sc.close();
	}
	
	public static int i = 0;

	public static String[] oneSearch(GameController gameController, SimpleGame simpleGame) {
		System.out.println();
		System.out.println("Calculating ...");
		long startTime = System.currentTimeMillis();

//		System.out.println("Is "+i+" even? " +((i & 1) == 0)+ " depth: " +depthToSearch);
		if((i & 1) == 0 && i != 0) {
			depthToSearch++;
			System.out.println("DEPTH AUGMENTED TO "+depthToSearch);
		}
		i++;
		
		SearchAlgorithms.initiateMovesMade(simpleGame.getGame().length);
		String[] result = SearchAlgorithms.aspirationSearch(simpleGame, 10, depthToSearch, gameController, 12000);
//		String[] result = SearchAlgorithms.alphaBetaWithTT(simpleGame, 2, -999999, 999999, gameController, SearchAlgorithms.getOrderedMovesMade());

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
		debugPrintSimpleGame(result[1], gameController);
		System.out.println("Player 1 = " +game.getPunctuation(1)+ ".");
		System.out.println("Player 2 = " +game.getPunctuation(2)+ ".");
		return result;

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

	public static void debugPrintSimpleGame(String moves, GameController2 gameController) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Parse game with moves: " +moves);
		Game debugGame = Parsers.parseGameFromSimpleGameMoves(moves, numberOfHexagonsCenterRow);
//		System.out.println("Parse game with moves: " +debugGame.getPlayHistory());
		long p1 = debugGame.getPunctuation(1);
		long p2 = debugGame.getPunctuation(2);
		System.out.println();
		System.out.println("-------------------------------");
		System.out.println("DEBUG Player 1 = " +p1+ ".");
		System.out.println("DEBUG Player 2 = " +p2+ ".");
		if(p1 == p2)
			System.out.println("BOTH PLAYER HAVE THE SAME RESULT");
		else 
			System.out.println("THE WINNER IS PLAYER = " +(p1 > p2 ? 1 : 2)+ ".");
		System.out.println("-------------------------------");
		System.out.println();
		JFrame frame = new JFrame("Debug board " +boardN++);
		Debug.UserInterface ui = new Debug.UserInterface(boardSize, numberOfHexagonsCenterRow, debugGame, gameController);
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

	
	public static void debugPrintSimpleGame(String moves, GameController gameController) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Parse game with moves: " +moves);
		Game debugGame = Parsers.parseGameFromSimpleGameMoves(moves, numberOfHexagonsCenterRow);
//		System.out.println("Parse game with moves: " +debugGame.getPlayHistory());
		long p1 = debugGame.getPunctuation(1);
		long p2 = debugGame.getPunctuation(2);
		System.out.println();
		System.out.println("-------------------------------");
		System.out.println("DEBUG Player 1 = " +p1+ ".");
		System.out.println("DEBUG Player 2 = " +p2+ ".");
		if(p1 == p2)
			System.out.println("BOTH PLAYER HAVE THE SAME RESULT");
		else 
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
		printGame(game);
	}
	
	public static void printGame(Game game) {
		UserInterface ui = new UserInterface(boardSize, numberOfHexagonsCenterRow, game, gameController);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(ui);
		frame.setSize(boardSize, boardSize);
		frame.setVisible(true);
	}
}