package com.um.omega.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFrame;

public class Main{
	
	public final static int boardSize = 800;
	public static ArrayList<Cell> possibleCells = new ArrayList<>();
	public static ArrayList<Cell> testWhite = new ArrayList<>(Arrays.asList(
			new Cell(0 , -2),
			new Cell(1, -2),
			new Cell(0, -1),
//			new Point(-1, 0),
			new Cell(-1, 1),
			new Cell(-2, 2),
			new Cell(2, 0),
			new Cell(1,1)
	));
	public static ArrayList<Cell> testBlack = new ArrayList<>(Arrays.asList(
			new Cell(2, -2),
			new Cell(-2, 0),
//			new Point(0, 0),
			new Cell(1, 0),
			new Cell(-1, 2),
			new Cell(-1, 0),
			new Cell(0, 1),
			new Cell(0, 2)
	));
	
	public static ArrayList<Cell> testGameV2 = new ArrayList<>();
	public static GameSituation game;
	public static GameSituationV2 gameV2;
	public static Game gameV3;
	public static Game2 gameV4;
	public static final int numberOfPlayers = 2;
	public final static int sizeSideHexagon = 3;
	private final static int playerToRate = 1;
	private final static int firstPlayer = 1;
	private final static int depthToSearch = 5;
	private static int numberOfHexagonsCenterRow;
	// Check Bitboard
	
	public static void main(String[] args) {
//		calculatePossibleCells();
//		calculatePossibleCellsForTestV2();
//		game = new GameSituation(testWhite, testBlack);
//		gameV2 = new GameSituationV2(testGameV2);
		
		numberOfHexagonsCenterRow = sizeSideHexagon * 2 - 1;
		
		gameV4 = new Game2(numberOfHexagonsCenterRow, playerToRate, firstPlayer);
		
		long startTime = System.currentTimeMillis();
		System.out.println("The best search is: ");
		String[] result = alphaBeta(gameV4, depthToSearch, -99999999, 99999999);
		long endTime = System.currentTimeMillis();
		
		gameV4 = parser(result[1]);
		System.out.println("Points for player: " +playerToRate+ " = " +gameV4.getRate(playerToRate)+ ".");
		System.out.println("The history of the game is: "+result[1]);
		double duration = (endTime - startTime) * 0.001;  
		int minutes = (int) duration/60;
		System.out.println("It took " +minutes+ " minutes " +(duration - minutes * 60) +" s to calculate it.");
		
		
		JFrame frame = new JFrame("Board 0");
		UserInterface2 ui = new UserInterface2(boardSize, numberOfHexagonsCenterRow, gameV4);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(ui);
		frame.setSize(boardSize, boardSize);
		frame.setVisible(true);
//		long white = gameV4.getRate(1);
//		long black = gameV4.getRate(2);
//		System.out.println("The score is P1 (white): " +white+ ", and P2 (black): " +black);
//		System.out.println("It's possible to keep going: " +gameV4.isPossibleMoreMoves());
//		System.out.println();
//		int possible = 0;
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		for(Game2 g: gameV4.possibleGames()) {
//			frame = new JFrame("Board " + (++possible));
//			ui = new UserInterface2(boardSize, centralSize, g);
//			
//			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			frame.add(ui);
//			frame.setSize(boardSize, boardSize);
//			frame.setVisible(true);
//			white = g.getRate(1);
//			black = g.getRate(2);
//			possible++;
//			System.out.println("The score in game: " +possible+ " is P1: " +white+ ", and P2: " +black);
//			System.out.println("It's possible to keep going: " +gameV4.isPossibleMoreMoves());
//			System.out.println();
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}

	}
	
//	private static Game2 bestGame;
	
//	private static String getRatint() {
//		System.out.println("What is the score?");
//		Scanner sc = new Scanner(System.in);
//		return sc.nextLine();
//	}
	
	public static String[] alphaBeta(Game2 game, int depth, int alpha, int beta) {
//		System.out.println("The game observed now is: " +game.playHistory);
		if(!game.isPossibleMoreMoves() || depth == 0) return new String[] {String.valueOf(game.getRate()), game.playHistory};
		
		int score = (int) -999999999;

		String[] valueToReturn = new String[] {String.valueOf(score), ""};
		String[] valueHelper = new String[] {String.valueOf(score), ""};
		ArrayList<Game2> childGames = game.possibleGames();
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
	
	public static Game2 parser(String s) {
		Game2 game = new Game2(numberOfHexagonsCenterRow, playerToRate, firstPlayer);
		String[] list = s.split("\\.");
		for(String cell: list) {
			String[] cellData = cell.replace("(", "").replace(")", "").replace(" ", "").split(",");
			game.setCellToPlayer(Integer.valueOf(cellData[0]), Integer.valueOf(cellData[1]), Integer.valueOf(cellData[2]));
		}
		return game;
	}
	
//	public static void calculatePossibleCells(){
//
//		int half = sizeSideHexagon / 2;
//        for (int row = 0; row < sizeSideHexagon; row++) {
//            int cols = sizeSideHexagon - java.lang.Math.abs(row - half);
////            System.out.println("Draw the row: " + row + ", with " + cols + " columns");
//            for (int col = 0; col < cols; col++) {
////                System.out.println("Draw the col: " + col);
//                possibleCells.add(new Cell(row < half ? col - row : col - half, row - half));
//            }
//        }
//        System.out.println("The possible cells are:");
//        for(Cell i: possibleCells)
//            System.out.println(i.getPointString());
//
//	}
//	
//	public static void calculatePossibleCellsForTestV2(){
//
//		int half = sizeSideHexagon / 2;
//		int x;
//		int y;
//		int player;
//        for (int row = 0; row < sizeSideHexagon; row++) {
//            int cols = sizeSideHexagon - java.lang.Math.abs(row - half);
////            System.out.println("Draw the row: " + row + ", with " + cols + " columns");
//            for (int col = 0; col < cols; col++) {
//            	x = (int) row < half ? col - row : col - half;
//            	y = (int) row - half;
//
//            	if(x == 0 && y == -2
//    			|| x == 1 && y == -2
//    			|| x == 0 && y == -1
//    			|| x == -1 && y == 1
//    			|| x == -2 && y == 2
//    			|| x == 2 && y == 0
//    			|| x == 1 && y == 1)
//            		player = 1;
//
//            	else if (x == 2 && y == -2
//        			|| x == -2 && y == 0
//        			|| x == 1 && y == 0
//        			|| x == -1 && y == 2
//        			|| x == -1 && y == 0
//        			|| x == 0 && y == 1
//        			|| x == 0 && y == 2) 
//            			player = 2;
//            	else player = 0;
//            	testGameV2.add(new Cell(x, y, player));
////                System.out.println("Add the cell: (" +x+ ", " +y+ ") to the player: " +player);
//            }
//        }
//        
//        System.out.println("The possible cells for V2 are:");
//        for(Cell i: testGameV2)
//            System.out.println(i.getPointStringPlayer());
//        
//	}

}
