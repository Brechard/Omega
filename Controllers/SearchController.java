package Controllers;

import java.util.Arrays;

import com.um.omega.game.SearchAlgorithms;
import com.um.omega.game.SimpleGame;

public class SearchController {

	public boolean keepSearching = true;
	public SimpleGame simpleGame;
	public GameController gameController;
	public int depthToSearch;
	// The game can last only 15 minutes
	public final int timeOfGame = 15 * 60;
	public double timeLeftAI = timeOfGame;
	public double timeLeftOpponent = timeOfGame;
	private int roundSearching = 0;
	public SearchController(SimpleGame  simpleGame, GameController gameController, int initialDepthToSearch) {
		this.simpleGame = simpleGame;
		this.gameController = gameController;  
		depthToSearch = initialDepthToSearch;
		SearchAlgorithms.initiateMovesMade(simpleGame.getGame().length);
	}
	
	public String[] startAlphaBetaSearch() {
		SearchAlgorithms.initiateMovesMade(simpleGame.getGame().length);
		return SearchAlgorithms.alphaBetaWithTT(simpleGame, depthToSearch, -999999, 999999, gameController, SearchAlgorithms.getOrderedMovesMade());
	}
	
	public String[] startAspirationSearch(int window) {
		int maximumTime = 120;
		return SearchAlgorithms.aspirationSearch(simpleGame, window, depthToSearch, gameController, maximumTime);
	}
	
	public void calculateWhileOtherPlays() {
		finishPreviousCalculating();
		Thread searchThread = new Thread() {
			public void run() {
				long startTime = System.currentTimeMillis();
				SearchAlgorithms.aspirationSearch(simpleGame, 1000, 10, gameController, timeOfGame);				
				long  endTime = System.currentTimeMillis();
				double duration = (endTime - startTime) * 0.001;  
				timeLeftOpponent -= duration;
				int minutes = (int) timeLeftOpponent/60;
				System.out.println("The opponent has " +minutes+ " minutes " +(timeLeftOpponent - minutes * 60) +" s left of game time.");
			}
		};
		searchThread.start();
	}
	
	public String startSearching() {
		finishPreviousCalculating();
		System.out.println("Calculating ...");
		long startTime = System.currentTimeMillis();
		if((roundSearching & 1) == 0 && roundSearching != 0) {
			depthToSearch++;
			System.out.println("DEPTH AUGMENTED TO "+depthToSearch);
		}

		String[] result = startAspirationSearch(1000);
		roundSearching++;
		
		long  endTime = System.currentTimeMillis();
		double duration = (endTime - startTime) * 0.001;
		timeLeftAI -= duration;
		int minutes = (int) duration/60;
		int minutesAI = (int) timeLeftAI/60;
		System.out.println("It took " +minutes+ " minutes " +(duration - minutes * 60) +" s to calculate it.");
		System.out.println("The AI has " +minutesAI+ " minutes " +(timeLeftAI - minutesAI * 60) +" s left of game time.");
		System.out.println("Number of searches: " +SearchAlgorithms.numberOfSearches+ " and prunes: " +SearchAlgorithms.prunings);
		System.out.println("Response: " +Arrays.asList(result));
		calculateWhileOtherPlays();
		return result[1];
	}
	
	private void finishPreviousCalculating() {
		SearchAlgorithms.finishCalculating(false);
		try {
			while(SearchAlgorithms.searching)
				Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
