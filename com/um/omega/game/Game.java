package com.um.omega.game;

import java.util.ArrayList;

public class Game {

	public ArrayList<Cell> player1 = new ArrayList<>();
	public ArrayList<Cell> player2 = new ArrayList<>();
	public ArrayList<Cell> emptyCells = new ArrayList<>();
	public ArrayList<Cell> alreadyCounted;
	private int group;
	private long rate;
	private Cell[] lastMove;
	public int playerToPlay;
	public int playerToRate;
	public int depth;
	public String playHistory;
	
	public Game(ArrayList<Cell> player1, ArrayList<Cell> player2, ArrayList<Cell> emptyCells, Cell[] lastMove, int depth, int playerToPlay, String playHistory, int playerToRate) {
//		System.out.println();
//		System.out.println("New game has been created from previous");
		this.player1 = player1;
		this.player2 = player2;
		this.emptyCells = emptyCells;
		this.depth = depth;
		this.playHistory = playHistory;
		rate = 0;
		for(int i= 0; i < lastMove.length; i++) {
			setCellToPlayer(i + 1, lastMove[i]);
		}
		//calculateRate(playerToPlay);
	}
	
	public Game(int numberOfHexagonsCenterRow, int playerToRate, int playerToPlay) {
		depth = 0;
		rate = 0;
		playHistory = "";
		this.playerToRate = playerToRate;
		this.playerToPlay = playerToPlay;
		int half = numberOfHexagonsCenterRow / 2;
//		int empty = 0;
		for (int row = 0; row < numberOfHexagonsCenterRow; row++) {
            int cols = numberOfHexagonsCenterRow - java.lang.Math.abs(row - half);
            for (int col = 0; col < cols; col++) {
//            	int r = empty > 4 ? new Random().nextInt(2) + 1 : new Random().nextInt(3);
//            	if(r == 0) {
            		emptyCells.add(new Cell(row < half ? col - row : col - half, row - half));
//            		empty++;
//            	}
//            	else if(r == 1)
//        			player1.add(new Cell(row < half ? col - row : col - half, row - half));
//            	else
//        			player2.add(new Cell(row < half ? col - row : col - half, row - half));
            }
        }

//		System.out.println("The empty cells are: ");
//		for(Cell c: emptyCells)
//			System.out.print(c.getPointString());
//		System.out.println();
//
//		System.out.println("The player 1 (white) cells are: ");
//		for(Cell c: player1)
//			System.out.print(c.getPointString());
//		System.out.println();
//
//		System.out.println("The player 2 (black) cells are: ");
//		for(Cell c: player2)
//			System.out.print(c.getPointString());
//		System.out.println();
	}
	
	public long getRate() {
		return rate == 0 ? calculateRate(playerToRate) : rate;
	}
	
	/**
	 * Calculates the rating of a player, not the same as its punctuation since we try to give
	 * more points to those who have groups with a number closer to 3 that is the perfect play
	 * @param player
	 * @return
	 */
	public long calculateRate(int player){

		alreadyCounted = new ArrayList<>();
		group = 0;
		rate = 1;
		ArrayList<Cell> playerCells = (player == 1) ? player1 : player2;

		playerCells.stream().filter(cell -> !alreadyCounted.contains(cell)).forEach(
				cell -> {
					group = 1;
					alreadyCounted.add(cell);
					searchNeighbor(cell, playerCells);
					rate = rate * group;
//					System.out.print("Player: " +player+ ", finish with the point: " +cell.getPointString()+ " RN the group count is: " +group);
//					System.out.println(", and the rate count is: " +rate);
				}
			);

		return rate;
	}
	
	/**
	 * Calculates the punctuation of a player
	 * @param player
	 * @return
	 */
	public long getPunctuation(int player) {
		alreadyCounted = new ArrayList<>();
		group = 0;
		rate = 1;
		ArrayList<Cell> playerCells = (player == 1) ? player1 : player2;

		playerCells.stream().filter(cell -> !alreadyCounted.contains(cell)).forEach(
				cell -> {
					group = 1;
					alreadyCounted.add(cell);
					searchNeighbor(cell, playerCells);
					rate = rate * group;
				}
			);

		return rate;
	}

	
	public void searchNeighbor(Cell point, ArrayList<Cell> cells) {
		cells.stream()
				.filter(cell -> !alreadyCounted.contains(cell) && point.isNeighbor(cell))
				.forEach(
					cell -> {
						alreadyCounted.add(cell);
						group++;
						searchNeighbor(cell, cells);
					}
				);		
	}
	
	public boolean isPossibleMoreMoves() {
		return emptyCells.isEmpty() ? 
				true : 
				emptyCells.size() > 4;
	}
	
	public void setCellToPlayer(int player, Cell cell) {
//		System.out.println("The cell to change is: " +cell.getPointString()+ ", to Player: " +player);
		if(!emptyCells.contains(cell))
			throw new Error("The cell " +cell.print()+" is not empty");
		emptyCells.remove(cell);
		if(player == 1) player1.add(cell);
		else player2.add(cell);
		playHistory += "(" +player+ ", " +cell.x+ ", " +cell.y+ ").";
	}
	
	public void setCellToPlayer(int player, int x, int y) {
//		System.out.println("The cell to change is: " +cellToChange.getPointString()+ ", to Player: " +player);
		for(Cell cell: emptyCells) {
			if(cell.equals(x, y)) {
				emptyCells.remove(cell);
				if(player == 1) player1.add(cell);
				else player2.add(cell);
				playHistory += "(" +player+ ", " +cell.x+ ", " +cell.y+ ").";
				return;
			}
		}
		throw new Error("The cell (" +x+", " +y+ ") is not empty");
	}

	
	/** 
	 * First version where we simply remove the move, it could be better if we can reset the object back and retrieve the possibleGames search
	 */
	public void undoMove() {
		player1.remove(lastMove[0]);
		player2.remove(lastMove[1]);
		emptyCells.add(lastMove[0]);
		emptyCells.add(lastMove[1]);
	}
	
	public ArrayList<Game> possibleGames() {
			
		ArrayList<Game> possibleGames = new ArrayList<>();
		ArrayList<Cell> newEmptyCells;
		for(Cell cell: emptyCells) {
			newEmptyCells = new ArrayList<Cell>(emptyCells);
			newEmptyCells.remove(cell);
			for(Cell cell2: newEmptyCells) {
//				System.out.println("We are going to create a new game with: Player 1: " +cell.getPointString()+ ", player 2: " +cell2.getPointString());
				possibleGames.add(new Game(new ArrayList<Cell>(player1), 
											new ArrayList<Cell>(player2), 
											new ArrayList<Cell>(emptyCells), 
											new Cell[]{cell, cell2}, 
											depth + 1,
											playerToPlay == 1 ? 2 : 1,
											playHistory,
											playerToRate));
			}
		}
		return possibleGames;
	}
	
	public long getHash() {
		long hash = 0;
		for(Cell c: player1)
			hash ^= c.hash;
		for(Cell c: player2)
			hash ^= c.hash;
		return hash;
	}
}
