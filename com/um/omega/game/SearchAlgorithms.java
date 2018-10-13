package com.um.omega.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import Helpers.Flag;
import Helpers.GameController;

public class SearchAlgorithms {

	private static HashMap<Long, TTInfo> hashMap = new HashMap<Long, TTInfo>();
	
	public static long numberOfSearches = 0;
	public static long prunings = 0;
	
	public static void cleanHashMap() {
		hashMap = new HashMap<Long, TTInfo>();
	}
	public static String[] aspirationSearch(SimpleGame game, int delta, int maxDepth, GameController gameController) {
//		System.out.println("Start the search, the playerToPlay is: " +game.playerToPlay+ " the startPlayer is: " +Main.gameController.getFirstPlayer());
//		System.out.println("Is it possible to keep playing?" +(game.emptyCells.size() < 4)+ " and " +(game.playerToPlay == Main.gameController.getFirstPlayer()));
		String[] result = null;
		for(int depth = 1; depth <= maxDepth; depth++ ) {
			long alpha = - delta; 
			long beta = + delta;
			numberOfSearches = 0;
			result = alphaBetaWithTT(game, depth, alpha, beta, gameController, -1);
			long score = Long.valueOf(result[0]);
			System.out.println("For depth " +depth+ " the result is: " +Arrays.toString(result));
			if( score >= beta ) {
				alpha = score; 
				beta = (long) Long.MAX_VALUE;
				System.out.println("Failed high " +depth+ " alpha: " +alpha+ " beta: " +beta);
				result = alphaBetaWithTT(game, depth, alpha, beta, gameController, -1);
				score = Long.valueOf(result[0]);
			} else if( score <= alpha ) {
				alpha  = (long) Long.MIN_VALUE;
				beta = score;
				System.out.println("Failed low " +depth+ " alpha: " +alpha+ " beta: " +beta);
				result = alphaBetaWithTT(game, depth, alpha, beta, gameController, -1);
				score = Long.valueOf(result[0]);
			}
			System.out.println("Search in depth " +depth+ " alpha: " +alpha+ " beta: " +beta);
		}
		return result;
	}

	public static String[] alphaBetaWithTT(SimpleGame game, int depth, long alpha, long beta, GameController gameController, int previousMinMax) {
		numberOfSearches++;
		if(numberOfSearches > 10)
			return new String[] {String.valueOf(1), ""};
		long originalAlpha = alpha;
		long hash = gameController.getHash(game.getGame());
		int minMax = -previousMinMax;

		System.out.println("Depth " +depth+ " playerToPlay " +game.getPlayerToPlay()+ " playerToMove " +game.getPlayerToMove()+ " previousMinMax: " +previousMinMax);
		System.out.println("Depth " +depth+ " game: " +Arrays.toString(game.getGame()));
		System.out.println("MinMax: " +(minMax == 1 ? "Min" : "Max"));

		//		System.out.println("I HAVE BEEN CALLED BY: " +Arrays.toString(game.getGame()));
//		System.out.println("hashMapContains " +hashMap.containsKey(hash)+ " the hash " +hash);

		if(hashMap.containsKey(hash) && hashMap.get(hash).depth >= depth) {
			TTInfo gameInfo = hashMap.get(hash);
			
			if (gameInfo.flag == Flag.EXACT) 
				return gameInfo.getInfo();
			else if (gameInfo.flag == Flag.LOWER_BOUND)
				alpha = Math.max(alpha, gameInfo.value);
			else if (gameInfo.flag == Flag.UPPER_BOUND) 
				beta = Math.min(beta, gameInfo.value);
			if (alpha >= beta)
				return gameInfo.getInfo();
		}
//		System.out.println("Hash not found: " +game.getHash());
		
//		System.out.println();
//		System.out.println("In depth " +depth+ " is it possible to keep playing?" +(game.emptyCells.size() < 4)+ " and " 
//				+(game.playerToPlay == Main.gameController.getFirstPlayer()));
//		System.out.println("The playerToPlay is: " +game.playerToPlay+ " the startPlayer is: " +Main.gameController.getFirstPlayer());

//		numberOfSearches++;
//		System.out.println("The game observed now is: " +game.playHistory);
//		if(Main.a == 2) {
//		}
		if(!game.isPossibleMoreMoves() || depth == 0) {
//			System.out.println("The rate is: " +game.getRate());
//			System.out.println();
//			long h = game.getHash();
//			if(hashes.contains(h))
//				hashCount++;
//			else hashes.add(h);
			return new String[] {String.valueOf(game.getRate()), game.getPlayHistory()};
		}
//		System.out.println();
		
		long score = Long.MIN_VALUE;

		String[] valueToReturn = new String[] {String.valueOf(score), ""};
		String[] valueHelper = new String[] {String.valueOf(score), ""};
		ArrayList<SimpleGame> childGames = game.possibleGames();
		long value;
		if(childGames.size() == 0)
			System.out.println("------------------------------------- " +game.getPlayHistory());
		
//		System.out.println("Depth " +depth+ " number of childGames: " +childGames.size()+ " alpha: " +alpha+ " beta: " +beta);
//		
//		System.out.println("Depth " +depth+ " game: " +Arrays.toString(game.getGame()));
		for(int child = 0; child < childGames.size(); child++) {
//			System.out.println("Depth " +depth+ " child: " +child+ " childGame: " +Arrays.toString(childGames.get(child).getGame()));
			if(minMax == 1)
				valueHelper = alphaBetaWithTT(childGames.get(child), depth -1,(long) -beta,(long) -alpha, gameController, minMax);
			else
				valueHelper = alphaBetaWithTT(childGames.get(child), depth -1,(long) alpha,(long) beta, gameController, minMax);
			value = -minMax * Long.valueOf(valueHelper[0]);
//			System.out.println("Depth " +depth+ " child: " +child+ " score: "+score+ " beta: " +beta+" value calculated: " +valueHelper[0]);
//			System.out.println(valueHelper[1]);
			if(value > score) {
				score = value;
				valueToReturn = new String[]{String.valueOf(value), valueHelper[1]};
			}
			if(score > alpha) alpha = score;
			if(score >= beta) {
//				System.out.println("Depth " +depth+ " child: " +child+ " BREAK --------------");
				prunings++;
				break;
			}
		}
	 	Flag flag;
		/* Fail-low result implies an upper bound */ 
		if (score <= originalAlpha)
			flag = Flag.UPPER_BOUND;
		
		/* Fail-high result implies a lower bound */ 
		else if (score >= beta) 
			flag = Flag.LOWER_BOUND;
		else  flag = Flag.EXACT;

		hashMap.put(hash, new TTInfo(flag, valueHelper, depth));
		return valueToReturn;
	}
}