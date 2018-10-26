package Debug;

import java.util.Arrays;

import javax.swing.JFrame;

import com.um.omega.game.Game;
import com.um.omega.game.SearchAlgorithms;
import com.um.omega.game.SimpleGame;
import com.um.omega.game.UserInterface;

import Controllers.GameController;
import Helpers.Parsers;

public class PlayGame {

	private static int i = 0;
	private static JFrame frame = new JFrame("Board");
	private static int sizeSideHexagon = 4;
	private static int numberOfHexagonsCenterRow;
	private static Game game;
	private static int depthToSearch = 1;
	private final static int boardSize = 800;
	private static GameController gameController;
	private static int aiSearch = 0;

	public static void debug(int depth, int sizeSideHex) {
		sizeSideHexagon = sizeSideHex;
		int playerAI = 1;
		int numberOfHexagonsCenterRow = sizeSideHexagon * 2 - 1;
		game = new Game(numberOfHexagonsCenterRow, 1);
		SimpleGame simpleGameAIis1 = new SimpleGame(sizeSideHexagon, playerAI);		
		GameController gameControllerAIis1 = new GameController(game, simpleGameAIis1, 2, playerAI);
//		gameControllerAIis1.moveAI(1, 0, -2, 0);
//		gameControllerAIis1.moveAI(2, 0, 0, 9);

		System.out.println("gameControllerAIis1, AI is player: " +simpleGameAIis1.getPlayerAI()+ " to move: " +simpleGameAIis1.getPlayerToMove());
		String[] result = oneSearch(gameControllerAIis1, simpleGameAIis1);

		restart(simpleGameAIis1, depth);
		playerAI = 2;
		SimpleGame simpleGameAIis2 = new SimpleGame(sizeSideHexagon, playerAI);
		game = new Game(numberOfHexagonsCenterRow, 1);
		GameController gameControllerAIis2 = new GameController(game, simpleGameAIis2, 2, playerAI);
		
		gameControllerAIis2.movesForAI(result[1]);
//		gameControllerAIis2.moveAI(1, 0, -2, 0);
//		gameControllerAIis2.moveAI(2, 1, -2, 1);
		printGame(game);
		System.out.println("gameControllerAIis2, AI is player: " +simpleGameAIis2.getPlayerAI()+ " to move: " +simpleGameAIis2.getPlayerToMove());
		oneSearch(gameControllerAIis2, simpleGameAIis2);
		

//		debugPrintSimpleGame(simpleGameAIis1.getPlayHistory(), gameControllerAIis1);		
		
//		for(SimpleGame g: simpleGame.possibleGames()) {
//			System.out.println(g.getPlayHistory());
//			for(SimpleGame g1: g.possibleGames())
//				System.out.println(g1.getPlayHistory());
//			
//		}
	}
	
	public static void restart(SimpleGame simpleGame, int depth) {
		depthToSearch = depth - 1;
		SearchAlgorithms.cleanHashMap();
		SearchAlgorithms.initiateMovesMade(simpleGame.getGame().length);		
	}
	
	public static void playAIvsAI() {
		sizeSideHexagon = 5;
		numberOfHexagonsCenterRow = sizeSideHexagon * 2 - 1;
		SimpleGame simpleGame1 = new SimpleGame(sizeSideHexagon, 1);
		SimpleGame simpleGame2 = new SimpleGame(sizeSideHexagon, 2);
		game = new Game(numberOfHexagonsCenterRow, 1);

		GameController2 gameControllerHelper = new GameController2(game, simpleGame1, simpleGame2, 2, 1);
		while(simpleGame1.isPossibleMoreRounds()) {
			// Game that thinks it is player 1
			// The gameController is only used for the hash table
			searchAIvsAI(gameControllerHelper, simpleGame1);
			System.out.println("1 done");
			System.out.println("----");
			printGame(game);
			
			searchAIvsAI(gameControllerHelper, simpleGame2);
			System.out.println("2 done");
			System.out.println("----");
			printGame(game);
		}
	}
	public static void searchAIvsAI(GameController2 gameController, SimpleGame simpleGame) {
		System.out.println();
		System.out.println("Calculating ...");
		aiSearch++;
		
		if(aiSearch % 4 == 0) {
			depthToSearch++;
			System.out.println("DEPTH AUGMENTED TO "+depthToSearch);
		}
		Debug.SearchAlgorithms2.initiateMovesMade(simpleGame.getGame().length);

		String[] result = Debug.SearchAlgorithms2.aspirationSearch(simpleGame, 600, depthToSearch, gameController, 120);
		System.out.println("Response: " +Arrays.asList(result));
		gameController.movesForAI(result[1]);
		debugPrintSimpleGame(result[1], gameController);
		System.out.println("Player 1 = " +game.getPunctuation(1)+ ".");
		System.out.println("Player 2 = " +game.getPunctuation(2)+ ".");

	}
	
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
		JFrame frame = new JFrame("Debug board " +i++);
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
		JFrame frame = new JFrame("Debug board " +i++);
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

	
	public static void printGame(Game game) {
		
		UserInterface ui = new UserInterface(boardSize, numberOfHexagonsCenterRow, game, gameController);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(ui);
		frame.setSize(boardSize, boardSize);
		frame.setVisible(true);
	}


}
