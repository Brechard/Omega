package com.um.omega.game;

public class Cell{
    
	public final int x;
    public final int y;
    /*
     * Possible states of the variable are:
     * 0: It is not occupied
     * 1: Occupied by the player 1
     * 2: Occupied by the player 2
     * and so on
     */
    public int occupied;
    
//    public final int z;
    public static Cell[] cube_directions = {
                           new Cell(+1, -1), new Cell(+1, 0), new Cell(0, +1), 
                           new Cell(-1, +1), new Cell(-1, 0), new Cell(0, -1), 
    };
    
    public Cell(int x, int y){
        this.x = x;
        this.y = y;
//        this.z = - x - y;
    }
    
    public Cell(int x, int y, int occupied){
        this.x = x;
        this.y = y;
        this.occupied = occupied;
//        this.z = - x - y;
    }


    /**
     * Checks if the point passed as parameter is the a neighbor or not
     * @param p point to check if neighbor of
     * @return boolean
     */
    public boolean isNeighbor(Cell p) {
    	
    	for(Cell d: cube_directions)
    		if(equals(movePoint(p, d))) return true;
    				
    	return false;
    }
    
    public boolean equals(Cell p) {
    	return x == p.x && y == p.y; 
    }
    
    /**
     * 
     * @param p Point to move
     * @param direction where to move
     * @return Point moved
     */
    public Cell movePoint(Cell p, Cell direction) {
    	return new Cell(p.x + direction.x, p.y + direction.y);
    }

	public int getOccupied() {
		return occupied;
	}

	public void setOccupied(int occupied) {
		this.occupied = occupied;
	}
    
    /**
     * Method mainly for debugging
     * @return Coordinates in string to represent easily
     */
    public String getPointString() {
    	return "(" + x + ", " + y + ")";
    }

    /**
     * Method mainly for debugging
     * @return Coordinates in string to represent easily
     */
    public String getPointStringPlayer() {
    	return "(" + x + ", " + y + ") occupied by player: " +occupied+ ".";
    }
}
