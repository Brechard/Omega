package Helpers;

import java.util.ArrayList;
import java.util.Arrays;

import com.um.omega.game.Main;
import Helpers.Parsers;;

public class Moves {
			
	public static void deleteMovesNotDone(ArrayList<String> moves){
		moves.remove(1);
		moves.remove(0);
		for(String move: moves) {
			String[] cellData = move.replace("(", "").replace(")", "").replace(" ", "").split(",");
			Main.game.deleteMove(Integer.valueOf(cellData[0]), Integer.valueOf(cellData[1]), Integer.valueOf(cellData[2]));
		}
	}

	public static ArrayList<String> getMoveToDo(String[] newMoves, ArrayList<String> alreadyMade) {
		ArrayList<String> newToDo = new ArrayList<String>(Arrays.asList(newMoves));
        ArrayList<String> done = new ArrayList<String>(alreadyMade);
        
        done.stream().forEach(s -> newToDo.remove(s));
        return newToDo;
    }
}
