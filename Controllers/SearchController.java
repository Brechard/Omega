package Controllers;

import java.util.Arrays;

import com.um.omega.game.SearchAlgorithms;
import com.um.omega.game.SimpleGame;

public class SearchController {

	public boolean keepSearching = true;
	public SimpleGame simpleGame;
	public GameController gameController;
	public int depthToSearch;
	// The game can last only 15 minutes but we have to considering the time used to tell the stones to the opponent
	public final int timeOfGame = 10 * 60;
	public double timeLeftAI = timeOfGame;
	public double timeLeftOpponent = timeOfGame;
	private int roundSearching = 0;
	public SearchController(SimpleGame  simpleGame, GameController gameController, int initialDepthToSearch, int roundSearching) {
		this.simpleGame = simpleGame;
		this.gameController = gameController;  
		depthToSearch = initialDepthToSearch;
		SearchAlgorithms.initiateMovesMade(simpleGame.getGame().length);
		this.roundSearching = roundSearching;
	}
	
	public String[] startAlphaBetaSearch() {
		SearchAlgorithms.initiateMovesMade(simpleGame.getGame().length);
		return SearchAlgorithms.alphaBetaWithTT(simpleGame, depthToSearch, -999999, 999999, gameController, SearchAlgorithms.getOrderedMovesMade());
	}
	
	public String[] startAspirationSearch(int window, int maxTime) {
		return SearchAlgorithms.aspirationSearch(simpleGame, window, depthToSearch, gameController, maxTime);
	}
	
	public void calculateWhileOtherPlays() {
//		finishPreviousCalculating();
//		Thread searchThread = new Thread() {
//			public void run() {
//				long startTime = System.currentTimeMillis();
//				SearchAlgorithms.aspirationSearch(simpleGame, 1000, 10, gameController, timeOfGame);				
//				long  endTime = System.currentTimeMillis();
//				double duration = (endTime - startTime) * 0.001;  
//				if(gameController.isAIturn()) // Because this finishes once it is AI turn again
//					timeLeftOpponent -= duration;
//				int minutes = (int) timeLeftOpponent/60;
//				System.out.println("The opponent has " +minutes+ " minutes " +(timeLeftOpponent - minutes * 60) +" s left of game time.");
//			}
//		};
//		searchThread.start();
	}
	
	public String startSearching() {
		finishPreviousCalculating();
		System.out.println("Calculating ...");
		long startTime = System.currentTimeMillis();
		if((roundSearching & 1) == 0 && roundSearching != 0) {
			depthToSearch++;
			System.out.println("DEPTH AUGMENTED TO "+depthToSearch);
		}
		
		// Use of a variation of the Gaussian function to calculate the maximum time available considering that there are 15 rounds
		// y = exp(-(-14+x)^2/(9)/(3*sqrt(2*pi))
		int maxTime = (int) (timeLeftAI * Math.exp(-Math.pow((-15+roundSearching), 2)/(9)/(3*Math.sqrt(2*Math.PI))));
//		int maxTime = 60;
		if(maxTime < 10)
			maxTime = 20;
		String[] result = startAspirationSearch(1000, maxTime);
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
