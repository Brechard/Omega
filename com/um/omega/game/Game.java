package com.um.omega.game;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class Game {

	public ArrayList<Cell> game = new ArrayList<>();
	public ArrayList<Cell> alreadyCounted;
	private int group;
	private int rate;
	private int numberOfPlayers;

	public Game(int numberOfPlayers, ArrayList<Cell> game) {
		this.numberOfPlayers = numberOfPlayers;
		this.game = game;
	}
	
	public Game(int size, int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
		int half = size / 2;

		for (int row = 0; row < size; row++) {
            int cols = size - java.lang.Math.abs(row - half);
            for (int col = 0; col < cols; col++) {
            	game.add(new Cell(row < half ? col - row : col - half, row - half, new Random().nextInt(numberOfPlayers + 1)));
            }
        }
//        System.out.println("The possible cells are: ");
//        for(Cell p: game)
//        	System.out.println(p.getPointString());
	}
	
	public ArrayList<Cell> getPlayerCells(int player){
		return (ArrayList<Cell>) game.stream().filter(cell -> cell.occupied == player).collect(Collectors.toList());
	}
	
	public int getRate(int player){

		alreadyCounted = new ArrayList<>();
		group = 0;
		rate = 1;
		ArrayList<Cell> playerCells = getPlayerCells(player);
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
		return game.isEmpty() ? 
				true : 
				numberOfPlayers * numberOfPlayers < getPlayerCells(0).size();
	}
	
	public ArrayList<Game> possibleGames() {
			
		ArrayList<Game> possibleGames = new ArrayList<>();
		if(!isPossibleMoreMoves()) return possibleGames;
		ArrayList<Cell> emptyCells = getPlayerCells(0);
		
		for(Cell cell: emptyCells) {
			ArrayList<Cell> emptyCells2 = new ArrayList<Cell>(emptyCells);
			emptyCells2.remove(cell);
			cell.setOccupied(1);
			for(Cell cell2: emptyCells2) {
				cell2.setOccupied(2);
				possibleGames.add(new Game(numberOfPlayers, game));
				cell2.setOccupied(0);
			}
			cell.setOccupied(0);
		}
		return possibleGames;
	}
}
