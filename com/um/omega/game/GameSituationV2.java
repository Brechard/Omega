package com.um.omega.game;

import java.util.ArrayList;

/**
 * In this version we will use only one list for the cells and game situation having a variable in the object Point
 * that will indicates us if the cell is occupied or not
 * @author Rodrigo
 *
 */
public class GameSituationV2 {
	
	public ArrayList<Cell> alreadyCounted;
	public ArrayList<Cell> game;
	private int group;
	private int rate;

	public GameSituationV2(ArrayList<Cell> game) {
		this.game = game;
		
//		System.out.println("The black in the games are in: " + Arrays.toString(black));
	}

	public int getRate(int player){

		alreadyCounted = new ArrayList<>();
		group = 0;
		rate = 1;
		
		game.stream().filter(cell -> cell.getOccupied() == player && !alreadyCounted.contains(cell)).forEach(
				cell -> {
					group = 1;
					alreadyCounted.add(cell);
					searchNeighbor(cell, game);
					rate = rate * group;
				}
				);
		
//		for(Cell p: points) {
////			System.out.println("Should we check the point: " +p.getPointString()+ "?");
//			if(alreadyCounted.contains(p)) continue;
////			System.out.println("It has not been checked yet so let's go!");
//			group = 1;
//			alreadyCounted.add(p);
//			searchNeighbor(p, points);
//			rate = rate * group;
////			System.out.print("Finish with the point: " +p.getPointString()+ " RN the group count is: " +group);
////			System.out.println(", and the rate count is: " +group);
//		}
		
		return rate;
	}
	
	public void searchNeighbor(Cell point, ArrayList<Cell> points) {
		for(Cell p: points) {
			if(alreadyCounted.contains(p)) continue;
//			System.out.println("Check if the point: " +point.getPointString()+ " is neighbor of: " + p.getPointString());
//			System.out.println("RN the group count is: " +group);
//			System.out.println("RN the rate count is: " +group);
			if(point.isNeighbor(p)) {
				alreadyCounted.add(p);
				group++;
//				System.out.println("IT IS, so now the group count is: " +group);
//				System.out.println();
				searchNeighbor(p, points);
			}
		}
		
	}
	
}
