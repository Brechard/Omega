package Helpers;

import java.util.ArrayList;

public class SimpleGameHelp {
	
//	public static void parseNextMove(String s, SimpleGameController gameController) {
//		String[] movesReceived = s.split("\\.");
//		
//		ArrayList<String> newMoves = Moves.getMoveToDo(movesReceived, gameController.getGameHistory());
//
//		if(newMoves.size() >= 2) {
//			for(int i = 0; i < 2; i++) {
//				String cell = newMoves.get(i);
//				String[] cellData = cell.replace("(", "").replace(")", "").replace(" ", "").split(",");
//				gameController.moveAI(Integer.valueOf(cellData[0]), Integer.valueOf(cellData[1]), Integer.valueOf(cellData[2]));
//				System.out.println("The next move for player " +cellData[0]+ ", is: (" +cellData[1]+ ", " +cellData[2]+ ")");
//			}
//			
//		} else {
//			System.out.println();
//			System.out.println("-------------");
//			System.out.println("There are no new moves");
//			System.out.println("-------------");
//			System.out.println();
//		}
//		Moves.deleteMovesNotDone(newMoves);		
//	}

}
