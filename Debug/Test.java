package Debug;

import java.util.Arrays;

import javax.swing.JFrame;

import com.um.omega.game.Game;
import com.um.omega.game.Main;
import com.um.omega.game.SearchAlgorithms;
import com.um.omega.game.SimpleGame;
import com.um.omega.game.UserInterface;

import Controllers.GameController;

public class Test {

	private static int numberOfHexagonsSide = 3;
	private static SimpleGame simpleGame;
	private static Game game;
	private static GameController gameController;
	private static int i = 0;
	public static void restartTests() {
		simpleGame = new SimpleGame(numberOfHexagonsSide, 1);		
		game = new Game(numberOfHexagonsSide * 2 - 1, 1);
		gameController = new GameController(game, simpleGame, 2, simpleGame.getPlayerAI());
	}
	
	public static void windowsTest() {
		restartTests();
		long alpha = -9999999;
		long beta = 999999;
		
		// Moves so that the AI already has some score

		printGame();
		
		searchAlphaBeta(alpha, beta, simpleGame, gameController, false);
	}
	
	public static void searchAlphaBeta(long alpha, long beta, SimpleGame simpleGame, GameController gameController, boolean usingWindows) {
		int depthToSearch = 5;
		System.out.println("Start search with alpha: " +alpha+ " and beta: " +beta);
		long startTime = System.currentTimeMillis();
		String[] result = SearchAlgorithms2.basicAlphaBetaWithTT(simpleGame, depthToSearch, alpha, beta, gameController);
		long score = Long.valueOf(result[0]);
		if( score >= beta ) {
			alpha = score; 
			beta = (long) Long.MAX_VALUE;
			System.out.println("Failed high, the new values are: alpha = " +alpha+ " beta = " +beta);
			result = SearchAlgorithms2.basicAlphaBetaWithTT(simpleGame, depthToSearch, alpha, beta, gameController);
		} else if( score <= alpha ) {
			alpha  = (long) Long.MIN_VALUE;
			beta = score;
			System.out.println("Failed low, the new values are: alpha = " +alpha+ " beta = " +beta);
			result = SearchAlgorithms2.basicAlphaBetaWithTT(simpleGame, depthToSearch, alpha, beta, gameController);
		}

		long  endTime = System.currentTimeMillis();
		double duration = (endTime - startTime) * 0.001;
		int minutes = (int) duration/60;
		System.out.println("It took " +minutes+ " minutes " +(duration - minutes * 60) +" s to calculate " +(usingWindows ? "" : "NOT ")+ "using windows");
		System.out.println("Number of searches: " +SearchAlgorithms2.numberOfSearches+ " and prunes: " +SearchAlgorithms2.prunings);
		System.out.println();
		SearchAlgorithms2.resetSearch();
	}
	
	public static void printGame() {
		JFrame frame = new JFrame("Board " +i++);
		UserInterface ui = new UserInterface(Main.boardSize, numberOfHexagonsSide * 2 - 1, game, gameController);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(ui);
		frame.setSize(Main.boardSize, Main.boardSize);
		frame.setVisible(true);
	}

}
