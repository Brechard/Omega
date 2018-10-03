package Helpers;

import java.util.ArrayList;
import java.util.Arrays;

import com.um.omega.game.Main;
import Helpers.Parsers;;

public class Moves {
	
	public static void moveConfirmed() {
		Main.gameHistory += Parsers.getGameHistoryProvisional();
		Main.game.setPlayHistory(Main.gameHistory);
		Parsers.setGameHistoryProvisional("");
		Main.playerToPlay = Main.playerToPlay == 1 ? 2 : 1;
	}
	
	public static void undoMove(int player, String s) {
		Parsers.setGameHistoryProvisional("");
		String[] xy = s.replace(" ", "").replace("(", "").replace(")", "").split(",");
		Main.game.deleteMove(player, Integer.valueOf(xy[0]), Integer.valueOf(xy[1]));
	}
	
	public static void played() {
		Main.playerToPlay++;
		if (Main.playerToPlay == 3) 
			Main.playerToPlay = 0;
	}
	
	public static void setPlayerMove(String s) {
		if(Main.playerToPlay == 1)
			Main.player1Move = s;
		else
			Main.player2Move = s;
	}

	public static void deleteMovesNotDone(ArrayList<String> moves){
		moves.remove(1);
		moves.remove(0);
		for(String move: moves) {
			String[] cellData = move.replace("(", "").replace(")", "").replace(" ", "").split(",");
			Main.game.deleteMove(Integer.valueOf(cellData[0]), Integer.valueOf(cellData[1]), Integer.valueOf(cellData[2]));
		}
	}

	public static ArrayList<String> getMoveToDo(String[] newMoves, String[] alreadyMade) {
		ArrayList<String> newToDo = new ArrayList<String>(Arrays.asList(newMoves));
        ArrayList<String> done = new ArrayList<String>(Arrays.asList(alreadyMade));
        
        done.stream().forEach(s -> newToDo.remove(s));
        return newToDo;
    }
}
