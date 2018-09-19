package com.um.omega.game;

import java.util.ArrayList;

public class GameSituation {

	public static ArrayList<Cell> white;
	public static ArrayList<Cell> black;
	public static int group = 0;
	public ArrayList<Cell> alreadyCounted;

	public GameSituation(ArrayList<Cell> white, ArrayList<Cell> black) {
		this.white = white;
		this.black = black;
		
//		System.out.println("The black in the games are in: " + Arrays.toString(black));
	}
	
	public int getRate(Boolean searchwhite){
		ArrayList<Cell> points;
		alreadyCounted = new ArrayList<>();
		group = 0;
		int rate = 1;

		if(searchwhite) points = new ArrayList<>(white);
		else points = new ArrayList<>(black);
				
		for(Cell p: points) {
//			System.out.println("Should we check the point: " +p.getPointString()+ "?");
			if(alreadyCounted.contains(p)) continue;
//			System.out.println("It has not been checked yet so let's go!");
			group = 1;
			alreadyCounted.add(p);
			searchNeighbor(p, points);
			rate = rate * group;
//			System.out.print("Finish with the point: " +p.getPointString()+ " RN the group count is: " +group);
//			System.out.println(", and the rate count is: " +group);
		}
		
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
	
	/**
	 * 
	 * @return color to draw depending on if is it occupied and who occupies it
	 */
	public static int isOccupiedColor(Cell p){
		for(Cell i: white) 
			if(i.equals(p)) return 0xFFFFFF; // White
		for(Cell i: black)
			if(i.equals(p)) return 0x000000; // Black		
//		for(Point i: red)
//			if(i.equals(p)) return 0xFF6464; // Pale Red
//		for(Point i: blue)
//			if(i.equals(p)) return 0x6464E1; // Pale blue
		return 0x66CDAA; // Pale Green
	}
	/**
	 * 
	 * @return if the cell is occupied
	 */
	public static boolean isOccupied(Cell p){
		for(Cell i: white) 
			if(i.equals(p)) return true;
		for(Cell i: black)
			if(i.equals(p)) return true; 
		return false;
	}
	
	// Possible things to return
	// 1st Array of GameSituations
	// 2nd 
//	public void possibleMovements(){
//		Main.possibleCells;
//	}
	
}
