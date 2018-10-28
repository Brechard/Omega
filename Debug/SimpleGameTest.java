package Debug;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.um.omega.game.SimpleGame;

import Helpers.SimpleGameHelpers;
import ObjectsToHelp.CellType;

// This should be in another folder but the git was not created correctly and therefore moved here to be able to save it
class SimpleGameTest {

	@Test
	void testGetTypeCell() {
		int numberOfHexagonsSide = 6;
		SimpleGame  game = new SimpleGame (numberOfHexagonsSide, 1);
		assertEquals(CellType.TOP_LEFT_CORNER, SimpleGameHelpers.getTypeCell(0, numberOfHexagonsSide));
		assertEquals(CellType.TOP_SIDE, SimpleGameHelpers.getTypeCell(1, numberOfHexagonsSide));
		assertEquals(CellType.TOP_RIGHT_CORNER, SimpleGameHelpers.getTypeCell(5, numberOfHexagonsSide));
		assertEquals(CellType.TOP_LEFT_SIDE, SimpleGameHelpers.getTypeCell(6, numberOfHexagonsSide));
		assertEquals(CellType.CENTER, SimpleGameHelpers.getTypeCell(8, numberOfHexagonsSide));
		assertEquals(CellType.TOP_RIGHT_SIDE, SimpleGameHelpers.getTypeCell(12, numberOfHexagonsSide));
		assertEquals(CellType.TOP_LEFT_SIDE, SimpleGameHelpers.getTypeCell(13, numberOfHexagonsSide));
		assertEquals(CellType.TOP_LEFT_SIDE, SimpleGameHelpers.getTypeCell(30, numberOfHexagonsSide));
		assertEquals(CellType.TOP_RIGHT_SIDE, SimpleGameHelpers.getTypeCell(39, numberOfHexagonsSide));
		assertEquals(CellType.LEFT_CORNER, SimpleGameHelpers.getTypeCell(40, numberOfHexagonsSide));
		assertEquals(CellType.CENTER, SimpleGameHelpers.getTypeCell(41, numberOfHexagonsSide));
		assertEquals(CellType.CENTER, SimpleGameHelpers.getTypeCell(45, numberOfHexagonsSide));
		assertEquals(CellType.CENTER, SimpleGameHelpers.getTypeCell(49, numberOfHexagonsSide));
		assertEquals(CellType.RIGHT_CORNER, SimpleGameHelpers.getTypeCell(50, numberOfHexagonsSide));
		assertEquals(CellType.BOTTOM_LEFT_SIDE, SimpleGameHelpers.getTypeCell(51, numberOfHexagonsSide));
		assertEquals(CellType.BOTTOM_RIGHT_SIDE, SimpleGameHelpers.getTypeCell(60, numberOfHexagonsSide));
		assertEquals(CellType.BOTTOM_LEFT_SIDE, SimpleGameHelpers.getTypeCell(78, numberOfHexagonsSide));
		assertEquals(CellType.BOTTOM_RIGHT_SIDE, SimpleGameHelpers.getTypeCell(84, numberOfHexagonsSide));
		assertEquals(CellType.BOTTOM_LEFT_CORNER, SimpleGameHelpers.getTypeCell(85, numberOfHexagonsSide));
		assertEquals(CellType.BOTTOM_SIDE, SimpleGameHelpers.getTypeCell(86, numberOfHexagonsSide));
		assertEquals(CellType.BOTTOM_SIDE, SimpleGameHelpers.getTypeCell(89, numberOfHexagonsSide));
		assertEquals(CellType.BOTTOM_RIGHT_CORNER, SimpleGameHelpers.getTypeCell(90, numberOfHexagonsSide));
	}


	@Test
	void testIsNeighbour() {
		int numberOfHexagonsSide = 6;
		SimpleGame game = new SimpleGame (numberOfHexagonsSide, 1);
		// TOP_LEFT_CORNER
		assertEquals(true, SimpleGameHelpers.isNeighbour(0, 1, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(0, 6, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(0, 7, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(0, 0, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(0, 2, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(0, 8, numberOfHexagonsSide));

		// TOP_SIDE
		assertEquals(true, SimpleGameHelpers.isNeighbour(1, 0, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(1, 2, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(1, 7, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(1, 8, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(1, 6, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(1, 9, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(1, 16, numberOfHexagonsSide));
		
		// TOP_RIGHT_CORNER
		assertEquals(true, SimpleGameHelpers.isNeighbour(5, 4, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(5, 11, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(5, 12, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(5, 6, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(5, 10, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(5, 13, numberOfHexagonsSide));
		
		// TOP_LEFT_SIDE
		assertEquals(true, SimpleGameHelpers.isNeighbour(6, 0, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(6, 7, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(6, 13, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(6, 14, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(6, 15, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(6, 1, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(6, 5, numberOfHexagonsSide));

		assertEquals(true, SimpleGameHelpers.isNeighbour(30, 31, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(30, 21, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(30, 40, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(30, 41, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(30, 42, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(30, 22, numberOfHexagonsSide));

		// CENTER in top half
		assertEquals(true, SimpleGameHelpers.isNeighbour(8, 7, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(8, 9, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(8, 1, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(8, 2, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(8, 15, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(8, 16, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(8, 3, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(8, 14, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(8, 17, numberOfHexagonsSide));
		
		// TOP_RIGHT_SIDE		
		assertEquals(true, SimpleGameHelpers.isNeighbour(12, 5, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(12, 11, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(12, 19, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(12, 20, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(12, 21, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(12, 18, numberOfHexagonsSide));

		assertEquals(true, SimpleGameHelpers.isNeighbour(39, 29, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(39, 38, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(39, 49, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(39, 50, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(39, 51, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(39, 40, numberOfHexagonsSide));
		
		// LEFT_CORNER
		assertEquals(true, SimpleGameHelpers.isNeighbour(40, 30, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(40, 41, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(40, 51, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(40, 52, numberOfHexagonsSide));
		
		// Center in center row
		assertEquals(true, SimpleGameHelpers.isNeighbour(45, 44, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(45, 46, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(45, 34, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(45, 35, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(45, 55, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(45, 56, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(45, 57, numberOfHexagonsSide));
		
		// RIGHT_CORNER
		assertEquals(true, SimpleGameHelpers.isNeighbour(50, 39, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(50, 49, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(50, 60, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(50, 51, numberOfHexagonsSide));

		// BOTTOM_LEFT_SIDE
		assertEquals(true, SimpleGameHelpers.isNeighbour(51, 40, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(51, 41, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(51, 52, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(51, 61, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(51, 62, numberOfHexagonsSide));

		assertEquals(true, SimpleGameHelpers.isNeighbour(78, 79, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(78, 70, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(78, 71, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(78, 85, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(78, 86, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(78, 77, numberOfHexagonsSide));
		
		// BOTTOM_RIGHT_SIDE
		assertEquals(true, SimpleGameHelpers.isNeighbour(60, 59, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(60, 49, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(60, 50, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(60, 69, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(60, 68, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(60, 61, numberOfHexagonsSide));

		assertEquals(true, SimpleGameHelpers.isNeighbour(84, 77, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(84, 76, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(84, 83, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(84, 90, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(84, 85, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(84, 75, numberOfHexagonsSide));
		
		// BOTTOM_LEFT_CORNER
		assertEquals(true, SimpleGameHelpers.isNeighbour(85, 86, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(85, 78, numberOfHexagonsSide));
		assertEquals(true, SimpleGameHelpers.isNeighbour(85, 79, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(85, 87, numberOfHexagonsSide));
		assertEquals(false, SimpleGameHelpers.isNeighbour(85, 85, numberOfHexagonsSide));

		// Center in bottom row
		assertEquals(true, SimpleGameHelpers.isNeighbour(88, 87, numberOfHexagonsSide));	
		assertEquals(true, SimpleGameHelpers.isNeighbour(88, 89, numberOfHexagonsSide));	
		assertEquals(true, SimpleGameHelpers.isNeighbour(88, 81, numberOfHexagonsSide));	
		assertEquals(true, SimpleGameHelpers.isNeighbour(88, 82, numberOfHexagonsSide));	
		assertEquals(false, SimpleGameHelpers.isNeighbour(88, 90, numberOfHexagonsSide));	
		assertEquals(false, SimpleGameHelpers.isNeighbour(88, 83, numberOfHexagonsSide));	
		
		// BOTTOM_RIGHT_CORNER
		assertEquals(true, SimpleGameHelpers.isNeighbour(90, 89, numberOfHexagonsSide));	
		assertEquals(true, SimpleGameHelpers.isNeighbour(90, 83, numberOfHexagonsSide));	
		assertEquals(true, SimpleGameHelpers.isNeighbour(90, 84, numberOfHexagonsSide));	
		assertEquals(false, SimpleGameHelpers.isNeighbour(90, 82, numberOfHexagonsSide));			
	
	
	}
}
