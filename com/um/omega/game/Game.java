package com.um.omega.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Game {

	public ArrayList<Cell> player1 = new ArrayList<>();
	public ArrayList<Cell> player2 = new ArrayList<>();
	public ArrayList<Cell> emptyCells = new ArrayList<>();
	public ArrayList<Cell> alreadyCounted;
	private int group;
	private long rate;
	private long punctuation;
	public int playerToPlay;
	public int playerToRate;
	public int depth;
	private String playHistory;
	public UnionFind union1;
	public UnionFind union2;
	
	public Game(ArrayList<Cell> player1, ArrayList<Cell> player2, ArrayList<Cell> emptyCells, Cell[] lastMove, 
			int depth, int playerToPlay, String playHistory, UnionFind union1, UnionFind union2) {
//		System.out.println();
//		System.out.println("New game has been created from previous");
		this.player1 = player1;
		this.playerToPlay = playerToPlay;
		this.player2 = player2;
		this.emptyCells = emptyCells;
		this.depth = depth;
		this.playHistory = playHistory;
		this.union1 = new UnionFind(union1);
		this.union2 = new UnionFind(union2);
		rate = 0;
		for(int i= 0; i < lastMove.length; i++) {
			setCellToPlayer(i + 1, lastMove[i]);
		}
		//calculateRate(playerToPlay);
	}
	
	/**
	 * Create a game from scratch, all cells are created
	 * @param numberOfHexagonsCenterRow
	 * @param playerToPlay
	 */
	public Game(int numberOfHexagonsCenterRow, int playerToPlay) {
		depth = 0;
		rate = 0;
		playHistory = "";
		this.playerToPlay = playerToPlay;
		int half = numberOfHexagonsCenterRow / 2;
//		int empty = 0;
		for (int row = 0; row < numberOfHexagonsCenterRow; row++) {
            int cols = numberOfHexagonsCenterRow - java.lang.Math.abs(row - half);
            for (int col = 0; col < cols; col++) {
//            	int r = empty > 4 ? new Random().nextInt(2) + 1 : new Random().nextInt(3);
//            	if(r == 0) {
            		emptyCells.add(new Cell(row < half ? col - row : col - half, row - half, emptyCells.size()));
            		
//            		empty++;
//            	}
//            	else if(r == 1)
//        			player1.add(new Cell(row < half ? col - row : col - half, row - half));
//            	else
//        			player2.add(new Cell(row < half ? col - row : col - half, row - half));
            }
        }
		createNeighbours();
		union1 = new UnionFind(emptyCells.size());
		union2 = new UnionFind(emptyCells.size());
//		System.out.println("The empty cells are: ");
//		for(Cell c: emptyCells)
//			System.out.print(c.print());
//		System.out.println();
//
//		System.out.println("The player 1 (white) cells are: ");
//		for(Cell c: player1)
//			System.out.print(c.print());
//		System.out.println();
//
//		System.out.println("The player 2 (black) cells are: ");
//		for(Cell c: player2)
//			System.out.print(c.print());
//		System.out.println();
	}
	public void uniteMoveConfirmed(int player, Cell cell) {
		ArrayList<Cell> playerCells = player == 1 ? player1 : player2;
		
		for(Cell neighbour: cell.getNeighbours()) {
			if(playerCells.contains(neighbour)) {
				if(player == 1)
					union1.unite(cell.id, neighbour.id);
				else 
					union2.unite(cell.id, neighbour.id);
			}
		}
	}
	
	public void printUnions() {
		System.out.println();
		System.out.println("Union 1: ");
		union1.printUnionFind();
		System.out.println();
		System.out.println("Union 2: ");
		union2.printUnionFind();
	}
	
	public void uniteMoveConfirmed(int player, int x, int y) {
		ArrayList<Cell> playerCells = (player == 1) ? player1 : player2;
		for(Cell cell: playerCells) {
			if(cell.equals(x, y)) {
				uniteMoveConfirmed(player, cell);
				return;
			}
		}
		throw new Error("The cell (" +x+", " +y+ ") is not empty");
	}
	
	public long getRate() {
		return rate == 0 ? calculateRate(playerToPlay) : rate;
	}
	
	/**
	 * Calculates the rating of a player, not the same as its punctuation since we try to give
	 * more points to those who have groups with a number closer to 3 that is the perfect play
	 * @param player
	 * @return
	 */
	public long calculateRate(int player){
		rate = (player == 1) ? union1.getCount() : union2.getCount();
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
		punctuation = 1;
		ArrayList<Cell> playerCells = (player == 1) ? player1 : player2;

		playerCells.stream().filter(cell -> !alreadyCounted.contains(cell)).forEach(
				cell -> {
					group = 1;
					alreadyCounted.add(cell);
					searchNeighbor(cell, playerCells);
					punctuation = punctuation * group;
				}
			);

		return punctuation;
	}
	
	public void searchNeighbor(Cell point, ArrayList<Cell> cells) {
		cells.stream()
				.filter(cell -> !alreadyCounted.contains(cell) && point.isNeighbour(cell))
				.forEach(
					cell -> {
						alreadyCounted.add(cell);
						group++;
						searchNeighbor(cell, cells);
					}
				);		
	}
	
	public boolean isPossibleMoreRounds() {
		return emptyCells.isEmpty() ? 
 				false : 
				emptyCells.size() > 4;
	}

	public boolean isPossibleMoreMoves() {
		return emptyCells.isEmpty() ? 
 				false : 
				emptyCells.size() > 2;
	}

	public void createNeighbours() {
		ArrayList<Cell> cellsToSearch = new ArrayList<>(emptyCells);

		for(Cell c: emptyCells) {
			cellsToSearch.remove(c);
			for(Cell c1: cellsToSearch) {
				if(c.isNeighbour(c1)) {
					c.addNeighbour(c1);
					c1.addNeighbour(c);
				}
			}
		}
	}
	
	/**
	 * This method is used internally and by for the AI moves, therefore the changes are added to the history
	 * @param player
	 * @param cell
	 */
	private void setCellToPlayer(int player, Cell cell) {
//		System.out.println("The cell to change is: " +cell.print()+ ", to Player: " +player);
		if(!emptyCells.contains(cell))
			throw new Error("The cell " +cell.print()+" is not empty");
		emptyCells.remove(cell);
		if(player == 1) player1.add(cell);
		else player2.add(cell);
		uniteMoveConfirmed(player, cell);
		playHistory += "(" +player+ ", " +cell.x+ ", " +cell.y+ ").";
	}
	
	/**
	 * This method is used outside the class and therefore there history will not be recorded until the moves are confirmed
	 * @param player
	 * @param x
	 * @param y
	 * @throws Error
	 */
	public int setCellToPlayer(int player, int x, int y) throws Error{
		for(Cell cell: emptyCells) {
			if(cell.equals(x, y)) {
				emptyCells.remove(cell);
				if(player == 1) player1.add(cell);
				else player2.add(cell);
				return cell.id;
			}
		}
		throw new Error("The cell (" +x+", " +y+ ") is not empty");
	}

	public void setCellToPlayer(int player, int id) throws Error{
		for(Cell cell: emptyCells) {
			if(cell.id == id) {
				emptyCells.remove(cell);
				if(player == 1) player1.add(cell);
				else player2.add(cell);
//				uniteMoveConfirmed(player, cell);

				playHistory += "(" +player+ ", " +cell.x+ ", " +cell.y+ ").";
				return;
			}
		}
		throw new Error("The cell with id " +id+ " is not empty");
	}

	public void deleteMove(int player, int x, int y) {
//		System.out.println("Delete from the player " +player+ " (" +x+ ", " +y+")");
		ArrayList<Cell> playerList = (player == 1) ? player1 : player2;
		
		for(Cell cell: playerList) {
			if(cell.equals(x, y)) {
				System.out.println("Deleted cell: " +cell.print()+ " of player "+ player);
				playerList.remove(cell);
				emptyCells.add(cell);
				return;
			}
		}
		
	}
	
	/**
	 * @return the playHistory
	 */
	public String getPlayHistory() {
		return playHistory;
	}

	/**
	 * @param playHistory the playHistory to set
	 */
	public void setPlayHistory(String playHistory) {
		this.playHistory = playHistory;
	}
}
