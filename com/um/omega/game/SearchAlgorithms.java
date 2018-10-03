package com.um.omega.game;

import java.util.ArrayList;
import java.util.HashMap;

import Helpers.Flag;

public class SearchAlgorithms {

	private static HashMap<Long, TTInfo> hashMap = new HashMap<Long, TTInfo>();

	public static String[] aspirationSearch(Game game, int delta, int maxDepth) {
		String[] result = null;
		for(int depth = 1; depth <= maxDepth; depth++ ) {
			long alpha = - delta; 
			long beta = + delta;
			result = alphaBetaWithTT(game, depth, alpha, beta);
			long score = Integer.valueOf(result[0]);
			if( score >= beta ) {
				alpha = score; 
				beta = (long) Long.MAX_VALUE;
				System.out.println("Failed high " +depth+ " alpha: " +alpha+ " beta: " +beta);
				result = alphaBetaWithTT(game, depth, alpha, beta);
				score = Integer.valueOf(result[0]);
			} else if( score <= alpha ) {
				alpha  = (long) Long.MIN_VALUE;
				beta = score;
				System.out.println("Failed low " +depth+ " alpha: " +alpha+ " beta: " +beta);
				result = alphaBetaWithTT(game, depth, alpha, beta);
				score = Integer.valueOf(result[0]);
			}
			System.out.println("Search in depth " +depth+ " alpha: " +alpha+ " beta: " +beta);
		}
		return result;
	}

	public static String[] alphaBeta(Game game, int depth, int alpha, int beta) {
//		numberOfSearches++;
//		System.out.println("The game observed now is: " +game.playHistory);
		if(!game.isPossibleMoreMoves() || depth == 0) {
//			long h = game.getHash();
//			if(hashes.contains(h))
//				hashCount++;
//			else hashes.add(h);
			return new String[] {String.valueOf(game.getRate()), game.getPlayHistory()};
		}
		
		int score = Integer.MIN_VALUE;

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

		long originalAlpha = alpha;
		long hash = game.getHash();
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

//		numberOfSearches++;
//		System.out.println("The game observed now is: " +game.playHistory);
		if(!game.isPossibleMoreMoves() || depth == 0) {
//			long h = game.getHash();
//			if(hashes.contains(h))
//				hashCount++;
//			else hashes.add(h);
			return new String[] {String.valueOf(game.getRate()), game.getPlayHistory()};
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
