package com.um.omega.game;

import java.security.SecureRandom;

public class Cell{
    
	public int x;
    public int y;
    public long hash;
    
//    public final int z;
    public static int[][] cube_directions = {
                           new int[] {1, -1}, new int[] {1, 0}, new int[] {0, 1}, 
                           new int[] {-1, 1}, new int[] {-1, 0}, new int[] {0, -1} 
    };
    
    public Cell(int x, int y){
        this.x = x;
        this.y = y;
        hash = new SecureRandom().nextLong(); 
//        this.z = - x - y;
    }
    
    /**
     * Checks if the point passed as parameter is the a neighbor or not
     * @param p point to check if neighbor of
     * @return boolean
     */
    public boolean isNeighbor(Cell p) {
    	
    	for(int[] d: cube_directions)
    		if(equals(p.x + d[0], p.y + d[1])) return true;
    				
    	return false;
    }
    
    public boolean equals(Cell p) {
    	return x == p.x && y == p.y; 
    }
    
    public boolean equals(int x, int y) {
    	return this.x == x && this.y == y; 
    }

    /**
     * 
     * @param p Point to move
     * @param direction where to move
     * @return Point moved
     */
    public void movePoint(Cell p, int[] direction) {
    	p.x += direction[0]; 
    	p.y += direction[1]; 
    }
    
    /**
     * Method mainly for debugging
     * @return Coordinates in string to represent easily
     */
    public String print() {
    	return "(" + x + ", " + y + ")";
    }

}
