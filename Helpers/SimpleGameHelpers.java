package Helpers;

import com.um.omega.game.UnionFind;

public class SimpleGameHelpers {

	public static UnionFind searchNeighbourForUnionFind(int cellId, UnionFind unionFind, int[] game, int numberOfHexagonsSide) {
		int player = game[cellId];
		if(cellId == 0) { // TOP_LEFT_CORNER
			if(player == game[1])
				unionFind.unite(cellId, 1);
			if(player == numberOfHexagonsSide)
				unionFind.unite(cellId, numberOfHexagonsSide);
			if (player == numberOfHexagonsSide + 1)
				unionFind.unite(cellId, numberOfHexagonsSide + 1);
			return unionFind;
		}
		
		if(cellId < numberOfHexagonsSide - 1) { // TOP_SIDE
			if(player == cellId - 1)
				unionFind.unite(cellId, cellId - 1);
			if(player == game[cellId + 1])
				unionFind.unite(cellId, cellId + 1);
			if(player == game[cellId + numberOfHexagonsSide])
				unionFind.unite(cellId, cellId + numberOfHexagonsSide);
			if(player == game[cellId + numberOfHexagonsSide + 1])
				unionFind.unite(cellId, cellId + numberOfHexagonsSide + 1);
			return unionFind;			
		}
		
		if(cellId == numberOfHexagonsSide - 1) { // TOP_RIGHT_CORNER
			if(player == game[cellId - 1])
				unionFind.unite(cellId, cellId - 1);
			if(player == game[cellId + numberOfHexagonsSide])
				unionFind.unite(cellId, cellId + numberOfHexagonsSide);
			if(player == game[cellId + numberOfHexagonsSide + 1])
				unionFind.unite(cellId, cellId + numberOfHexagonsSide + 1);
			return unionFind;
		}

		int cellsBeforeRow = numberOfHexagonsSide;
		int row = 1;
		int cellsInRow = numberOfHexagonsSide;
		for(int i = 1; i < numberOfHexagonsSide - 1; i++) {
			row = i;
			cellsInRow = numberOfHexagonsSide + row;
			if(cellId == cellsBeforeRow) { // TOP_LEFT_SIDE
				if(player == game[cellId + 1])
					unionFind.unite(cellId, cellId + 1);
				if(player == game[cellId - cellsInRow + 1])
					unionFind.unite(cellId, cellId - cellsInRow + 1);
				if(player == game[cellId + cellsInRow])
					unionFind.unite(cellId, cellId + cellsInRow);
				if(player == game[cellId + cellsInRow + 1])
					unionFind.unite(cellId, cellId + cellsInRow + 1);
				return unionFind;
			}
			if(cellId == cellsBeforeRow + cellsInRow - 1) { // TOP_RIGHT_SIDE
				if(player == game[cellId - 1])
					unionFind.unite(cellId, cellId - 1);
				if(player == game[cellId - cellsInRow])
					unionFind.unite(cellId, cellId - cellsInRow);
				if(player == game[cellId + cellsInRow])
					unionFind.unite(cellId, cellId + cellsInRow);
				if(player == game[cellId + cellsInRow + 1])
					unionFind.unite(cellId, cellId + cellsInRow + 1);
				return unionFind;
			}
			if(cellId < cellsBeforeRow + cellsInRow - 1) { // Center part of the top half
				if(player == game[cellId + 1])
					unionFind.unite(cellId, cellId + 1);
				if(player == game[cellId - 1])
					unionFind.unite(cellId, cellId - 1);
				if(player == game[cellId - cellsInRow])
					unionFind.unite(cellId, cellId - cellsInRow);
				if(player == game[cellId - cellsInRow + 1])
					unionFind.unite(cellId, cellId - cellsInRow + 1);
				if(player == game[cellId + cellsInRow])
					unionFind.unite(cellId, cellId + cellsInRow);
				if(player == game[cellId + cellsInRow + 1])
					unionFind.unite(cellId, cellId + cellsInRow + 1);
				return unionFind;
			}
			cellsBeforeRow += cellsInRow ;
		}
		row++;
		cellsInRow++;
		if(cellId == cellsBeforeRow) { // LEFT_CORNER
			if(player == game[cellId  + 1])
				unionFind.unite(cellId, cellId + 1);
			if(player == game[cellId - cellsInRow + 1])
				unionFind.unite(cellId, cellId - cellsInRow + 1);
			if(player == game[cellId + cellsInRow])
				unionFind.unite(cellId, cellId + cellsInRow);
			return unionFind;
		}
			
		if(cellId == cellsBeforeRow + cellsInRow - 1) { // RIGHT_CORNER
			if(player == game[cellId  - 1])
				unionFind.unite(cellId, cellId - 1);
			if(player == game[cellId - cellsInRow])
				unionFind.unite(cellId, cellId - cellsInRow);
			if(player == game[cellId + cellsInRow - 1])
				unionFind.unite(cellId, cellId + cellsInRow - 1);
			return unionFind;			
		}
		
		if(cellId < cellsBeforeRow + cellsInRow - 1) { // Center row
			if(player == game[cellId + 1])
				unionFind.unite(cellId, cellId + 1);
			if(player == game[cellId - 1])
				unionFind.unite(cellId, cellId - 1);
			if(player == game[cellId - cellsInRow])
				unionFind.unite(cellId, cellId - cellsInRow);
			if(player == game[cellId - cellsInRow + 1])
				unionFind.unite(cellId, cellId - cellsInRow + 1);
			if(player == game[cellId + cellsInRow])
				unionFind.unite(cellId, cellId + cellsInRow);
			if(player == game[cellId + cellsInRow - 1])
				unionFind.unite(cellId, cellId + cellsInRow - 1);
			return unionFind;
		}

		cellsBeforeRow += cellsInRow;
		for(int i = row - 1; i > 0; i--) {
			row = i;
			cellsInRow = numberOfHexagonsSide + row;
			if(cellId == cellsBeforeRow) { // BOTTOM_LEFT_SIDE
				if(player == game[cellId + 1])
					unionFind.unite(cellId, cellId + 1);
				if(player == game[cellId - cellsInRow])
					unionFind.unite(cellId, cellId - cellsInRow);
				if(player == game[cellId - cellsInRow - 1])
					unionFind.unite(cellId, cellId - cellsInRow - 1);
				if(player == game[cellId + cellsInRow])
					unionFind.unite(cellId, cellId + cellsInRow);
				return unionFind;
			}
			if(cellId == cellsBeforeRow + cellsInRow - 1) { // BOTTOM_RIGHT_SIDE
				if(player == game[cellId  - 1])
					unionFind.unite(cellId, cellId - 1);
				if(player == game[cellId - cellsInRow])
					unionFind.unite(cellId, cellId - cellsInRow);
				if(player == game[cellId - cellsInRow - 1])
					unionFind.unite(cellId, cellId - cellsInRow - 1);
				if(player == game[cellId + cellsInRow - 1])
					unionFind.unite(cellId, cellId + cellsInRow - 1);
				return unionFind;			
			}
			if(cellId < cellsBeforeRow + cellsInRow - 1) { // Center part of the bottom half
				if(player == game[cellId + 1])
					unionFind.unite(cellId, cellId + 1);
				if(player == game[cellId - 1])
					unionFind.unite(cellId, cellId - 1);
				if(player == game[cellId - cellsInRow])
					unionFind.unite(cellId, cellId - cellsInRow);
				if(player == game[cellId - cellsInRow - 1])
					unionFind.unite(cellId, cellId - cellsInRow - 1);
				if(player == game[cellId + cellsInRow])
					unionFind.unite(cellId, cellId + cellsInRow);
				if(player == game[cellId + cellsInRow - 1])
					unionFind.unite(cellId, cellId + cellsInRow - 1);
				return unionFind;
			}
			cellsBeforeRow += cellsInRow;
		}
		row--;
		cellsInRow--;
		if(cellId == cellsBeforeRow) { // BOTTOM_LEFT_CORNER
			if(player == game[cellId + 1])
				unionFind.unite(cellId, cellId + 1);
			if(player == game[cellId - cellsInRow])
				unionFind.unite(cellId, cellId - cellsInRow);
			if(player == game[cellId - cellsInRow - 1])
				unionFind.unite(cellId, cellId - cellsInRow - 1);
			return unionFind;
		}
		if(cellId > cellsBeforeRow && cellId < cellsBeforeRow + numberOfHexagonsSide + row - 1) { // BOTTOM_SIDE
			if(player == game[cellId - 1])
				unionFind.unite(cellId, cellId - 1);
			if(player == game[cellId + 1])
				unionFind.unite(cellId, cellId + 1);
			if(player == game[cellId - cellsInRow])
				unionFind.unite(cellId, cellId - cellsInRow);
			if(player == game[cellId - cellsInRow - 1])
				unionFind.unite(cellId, cellId - cellsInRow - 1);
			return unionFind;
		}
		if(cellId == cellsBeforeRow + numberOfHexagonsSide + row - 1) { // BOTTOM_RIGHT_CORNER
			if(player == game[cellId - 1])
				unionFind.unite(cellId, cellId - 1);
			if(player == game[cellsBeforeRow - 1])
				unionFind.unite(cellId, cellsBeforeRow - 1);
			if(player == game[cellsBeforeRow - 2])
				unionFind.unite(cellId, cellsBeforeRow - 2);
			return unionFind;
		}
		throw new Error("Cell not found to check if it's neighbour");
	}
	
	public static boolean isNeighbour(int cellId, int checkCellId, int numberOfHexagonsSide) {
		if(cellId == 0) { // TOP_LEFT_CORNER
			if(checkCellId == 1 
					|| checkCellId == numberOfHexagonsSide 
					|| checkCellId == numberOfHexagonsSide + 1)
				return true;
			else return false;
		}
		
		if(cellId < numberOfHexagonsSide - 1) { // TOP_SIDE
			if(checkCellId == cellId - 1
				|| checkCellId == cellId + 1 
				|| checkCellId == cellId + numberOfHexagonsSide
				|| checkCellId == cellId + numberOfHexagonsSide + 1)
				return true;
			else return false;			
		}
		
		if(cellId == numberOfHexagonsSide - 1) { // TOP_RIGHT_CORNER
			if(checkCellId == cellId - 1 
				|| checkCellId == cellId + numberOfHexagonsSide 
				|| checkCellId == cellId + numberOfHexagonsSide + 1)
				return true;
			else return false;
		}

		int cellsBeforeRow = numberOfHexagonsSide;
		int row = 1;
		int cellsInRow = numberOfHexagonsSide;
		for(int i = 1; i < numberOfHexagonsSide - 1; i++) {
			row = i;
			cellsInRow = numberOfHexagonsSide + row;
			if(cellId == cellsBeforeRow) { // TOP_LEFT_SIDE
				if(checkCellId == cellId + 1
						|| checkCellId == cellId - cellsInRow + 1
						|| checkCellId == cellId + cellsInRow
						|| checkCellId == cellId + cellsInRow + 1)
					return true;
				else return false;
			}
			if(cellId == cellsBeforeRow + cellsInRow - 1) { // TOP_RIGHT_SIDE
				if(checkCellId == cellId - 1
						|| checkCellId == cellId - cellsInRow
						|| checkCellId == cellId + cellsInRow
						|| checkCellId == cellId + cellsInRow + 1)
					return true;
				else return false;
			}
			if(cellId < cellsBeforeRow + cellsInRow - 1) { // Center part of the top half
				if(checkCellId == cellId + 1
						|| checkCellId == cellId - 1
						|| checkCellId == cellId - cellsInRow
						|| checkCellId == cellId - cellsInRow + 1
						|| checkCellId == cellId + cellsInRow
						|| checkCellId == cellId + cellsInRow + 1)
					return true;
				else return false;
			}
			cellsBeforeRow += cellsInRow ;
		}
		row++;
		cellsInRow++;
		if(cellId == cellsBeforeRow) { // LEFT_CORNER
			if(checkCellId == cellId  + 1
					|| checkCellId == cellId - cellsInRow + 1
					|| checkCellId == cellId + cellsInRow)
				return true;
			else return false;
		}
			
		if(cellId == cellsBeforeRow + cellsInRow - 1) { // RIGHT_CORNER
			if(checkCellId == cellId  - 1
					|| checkCellId == cellId - cellsInRow
					|| checkCellId == cellId + cellsInRow - 1)
				return true;
			else return false;			
		}
		
		if(cellId < cellsBeforeRow + cellsInRow - 1) { // Center row
			if(checkCellId == cellId + 1
					|| checkCellId == cellId - 1
					|| checkCellId == cellId - cellsInRow
					|| checkCellId == cellId - cellsInRow + 1
					|| checkCellId == cellId + cellsInRow
					|| checkCellId == cellId + cellsInRow - 1)
				return true;
			else return false;
		}

		cellsBeforeRow += cellsInRow;
		for(int i = row - 1; i > 0; i--) {
			row = i;
			cellsInRow = numberOfHexagonsSide + row;
			if(cellId == cellsBeforeRow) { // BOTTOM_LEFT_SIDE
				if(checkCellId == cellId + 1
						|| checkCellId == cellId - cellsInRow
						|| checkCellId == cellId - cellsInRow - 1
						|| checkCellId == cellId + cellsInRow)
					return true;
				else return false;
			}
			if(cellId == cellsBeforeRow + cellsInRow - 1) { // BOTTOM_RIGHT_SIDE
				if(checkCellId == cellId  - 1
						|| checkCellId == cellId - cellsInRow
						|| checkCellId == cellId - cellsInRow - 1
						|| checkCellId == cellId + cellsInRow - 1)
					return true;
				else return false;			
			}
			if(cellId < cellsBeforeRow + cellsInRow - 1) { // Center part of the bottom half
				if(checkCellId == cellId + 1
						|| checkCellId == cellId - 1
						|| checkCellId == cellId - cellsInRow
						|| checkCellId == cellId - cellsInRow - 1
						|| checkCellId == cellId + cellsInRow
						|| checkCellId == cellId + cellsInRow - 1)
					return true;
				else return false;
			}
			cellsBeforeRow += cellsInRow;
		}
		row--;
		cellsInRow--;
		if(cellId == cellsBeforeRow) { // BOTTOM_LEFT_CORNER
			if(checkCellId == cellId + 1
					||  checkCellId == cellId - cellsInRow
					||  checkCellId == cellId - cellsInRow - 1)
				return true;
			else return false;
		}
		if(cellId > cellsBeforeRow && cellId < cellsBeforeRow + numberOfHexagonsSide + row - 1) { // BOTTOM_SIDE
			if(checkCellId == cellId - 1
					|| checkCellId == cellId + 1
					|| checkCellId == cellId - cellsInRow
					|| checkCellId == cellId - cellsInRow - 1)
				return true;
			else return false;
		}
		if(cellId == cellsBeforeRow + numberOfHexagonsSide + row - 1) { // BOTTOM_RIGHT_CORNER
			if(checkCellId == cellId - 1
					|| checkCellId == cellsBeforeRow - 1 
					|| checkCellId == cellsBeforeRow - 2)
				return true;
			else return false;
		}
		throw new Error("Cell not found to check if neighbour");
	}
	
	public static CellType getTypeCell(int cellId, int numberOfHexagonsSide) {
		if(cellId == 0)
			return CellType.TOP_LEFT_CORNER;
		if(cellId < numberOfHexagonsSide - 1)
			return CellType.TOP_SIDE;
		if(cellId == numberOfHexagonsSide - 1)
			return CellType.TOP_RIGHT_CORNER;
		int cellsBeforeRow = numberOfHexagonsSide;
		int row = 1;
		for(int i = 1; i < numberOfHexagonsSide - 1; i++) {
			row = i;
			if(cellId == cellsBeforeRow)
				return CellType.TOP_LEFT_SIDE;
			if(cellId == cellsBeforeRow + numberOfHexagonsSide + row - 1)
				return CellType.TOP_RIGHT_SIDE;

			cellsBeforeRow += numberOfHexagonsSide + row ;
		}
		row++;
		if(cellId == cellsBeforeRow)
			return CellType.LEFT_CORNER;

		if(cellId == cellsBeforeRow + numberOfHexagonsSide + row - 1)
			return CellType.RIGHT_CORNER;
		
		for(int i = row - 1; i > 0; i--) {
			row = i;
			cellsBeforeRow += numberOfHexagonsSide + row + 1;
			if(cellId == cellsBeforeRow)
				return CellType.BOTTOM_LEFT_SIDE;
			if(cellId == cellsBeforeRow + numberOfHexagonsSide + row - 1)
				return CellType.BOTTOM_RIGHT_SIDE;

		}
		cellsBeforeRow += numberOfHexagonsSide + row;
		row--;
		if(cellId == cellsBeforeRow)
			return CellType.BOTTOM_LEFT_CORNER;
		if(cellId > cellsBeforeRow && cellId < cellsBeforeRow + numberOfHexagonsSide + row - 1)
			return CellType.BOTTOM_SIDE;
		if(cellId == cellsBeforeRow + numberOfHexagonsSide + row - 1)
			return CellType.BOTTOM_RIGHT_CORNER;
		
		return CellType.CENTER;
	}
	
	
	public static void print(String s) {
		System.out.println(s);
	}
	public static void print(int i) {
		print(String.valueOf(i));
	}

}
