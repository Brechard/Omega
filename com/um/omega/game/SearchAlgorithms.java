package com.um.omega.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.text.html.MinimalHTMLWriter;

import Controllers.GameController;
import Helpers.Parsers;
import ObjectsToHelp.Flag;
import ObjectsToHelp.TTInfo;

public class SearchAlgorithms {

	private static HashMap<Long, TTInfo> hashMap = new HashMap<Long, TTInfo>();
	
	public static long numberOfSearches = 0;
	public static long prunings = 0;
	public static int[][] movesMade;
	private static boolean calculate = true;
	public static boolean searching = false;
		
	public static String[] aspirationSearch(SimpleGame game, int delta, int maxDepth, GameController gameController, final int counter) {
		int minutes = counter/60;
		System.out.println("Start the search, depth: " +maxDepth+ " for AI player: " +game.getPlayerAI()
			+ " with a maximum time to calculate it of " +minutes+ " minutes " +(counter - minutes*60)+ "s");
		System.out.println("GAME USED IN SEARCH: " +game.getPlayHistory());
		searching = true;
//		System.out.println("Is it possible to keep playing?" +(game.emptyCells.size() < 4)+ " and " +(game.playerToPlay == Main.gameController.getFirstPlayer()));
		String[] result = null;
		String[] newResult = null;
		
		startCalculating();
		
		Thread counterThread = new Thread() {
			public void run() {
				try {
					Thread.sleep(counter * 1000);
					finishCalculating(true);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}						
			}
		};
		counterThread.start();
		for(int depth = 2; depth <= maxDepth; depth++ ) {
			long alpha = - delta; 
			long beta = + delta;
			numberOfSearches = 0;
			int[][] moves = getOrderedMovesMade();
			initiateMovesMade(movesMade.length);
			newResult = alphaBetaWithTT(game, depth, alpha, beta, gameController, moves);
			
//			Main.debugPrintSimpleGame(newResult[1], gameController);;
			long score = Long.valueOf(newResult[0]);
			System.out.println("For depth " +depth+ " the result is: " +Arrays.toString(newResult));
			if(newResult[1] == null || newResult[1] == "" || (result != null && newResult[1].length() < result[1].length())) {
				System.out.println("For depth " +depth+ " the result is an error");
				break;
			}
			result = newResult;
			if( score >= beta ) {
				alpha = score; 
				beta = (long) Long.MAX_VALUE;
//				System.out.println("Failed high " +depth+ " alpha: " +alpha+ " beta: " +beta);
				result = alphaBetaWithTT(game, depth, alpha, beta, gameController, moves);
				score = Long.valueOf(result[0]);
			} else if( score <= alpha ) {
				alpha  = (long) Long.MIN_VALUE;
				beta = score;
//				System.out.println("Failed low " +depth+ " alpha: " +alpha+ " beta: " +beta);
				result = alphaBetaWithTT(game, depth, alpha, beta, gameController, moves);
				score = Long.valueOf(result[0]);
			}
//			System.out.println("Search in depth " +depth+ " alpha: " +alpha+ " beta: " +beta);
//			System.out.println("-------");
//			for(int[] i: SearchAlgorithms.getOrderedMovesMade())
//				System.out.println(Arrays.toString(i));
//			System.out.println("-------");
		}
		counterThread.stop();
		searching = false;
		return result;
	}

	public static String[] alphaBetaWithTT(SimpleGame game, int depth, long alpha, long beta, GameController gameController, int[][] moves) {
		numberOfSearches++;
		long originalAlpha = alpha;
		long hash = gameController.getHash(game.getGame());

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
		
		if(!game.isPossibleMoreMoves() || depth == 0 || !calculate) {
			return new String[] {String.valueOf(game.getRate()), game.getPlayHistory()};
		}
		
		long score = Long.MIN_VALUE;

		String[] valueToReturn = new String[] {String.valueOf(score), ""};
		String[] valueHelper = new String[] {String.valueOf(score), ""};
		ArrayList<SimpleGame> childGames = game.possibleGames(moves);
		long value;

		for(int child = 0; child < childGames.size(); child++) {
			valueHelper = alphaBetaWithTT(childGames.get(child), depth -1,(long) -beta,(long) -alpha, gameController, moves);
//			final int child2 = child; 
			value = -Long.valueOf(valueHelper[0]);
			if(value > score) {
//				final int child2 = child;
//				new Thread() {
//					public void run() {
//					System.out.println("Depth: " +depth+ " move: " +i[1]);
//				}
//				moves = getOrderedMovesMade();
//					}
//				}.start();
				score = value;
				valueToReturn = new String[]{String.valueOf(value), valueHelper[1]};
			}
			if(score > alpha) alpha = score;
			if(score >= beta) {
				for(int[] i: childGames.get(child).lastMoves)
					moveMade(i[1]);
//				moves = getOrderedMovesMade();
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
	
	public static void initiateMovesMade(int size) {
		movesMade = new int[size][2];
		for(int i = 0; i < size; i++)
			movesMade[i] = new int[] {i, 0};
	}
	
	public static void moveMade(int id) {
		movesMade[id][1]++;
	}
	
	public static int[][] getOrderedMovesMade(){
		int[][] ordered = movesMade.clone();

		Arrays.sort(ordered, Comparator.comparing((int[] arr) -> arr[1])
                .reversed());
		return ordered;
	}
	
	public static void cleanHashMap() {
		hashMap = new HashMap<Long, TTInfo>();
	}
	
	public static void finishCalculating(boolean timerOut) {
		if(timerOut)
			System.out.println("TAKING TOO LONG, STOP");
		calculate = false;
	}

	public static void startCalculating() {
		calculate = true;
	}

}