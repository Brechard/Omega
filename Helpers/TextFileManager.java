package Helpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import com.um.omega.game.Game;
import com.um.omega.game.Main;
import com.um.omega.game.SimpleGame;

import ObjectsToHelp.Games;

public class TextFileManager {
	
	private String numberFile;
	
	public TextFileManager(int playerAI) {
		numberFile = "-" +Main.sizeSideHexagon+ "-" +new File("history/").listFiles().length/2;
		try {
			PrintWriter outJavaFriendly = new PrintWriter("history/gameHistory_JavaFriendly" +numberFile+ ".txt");
			PrintWriter outHumanFriendly = new PrintWriter("history/gameHistory_HumanFriendly" +numberFile+ ".txt");
			outJavaFriendly.println(Main.sizeSideHexagon+ "," +playerAI);
			outHumanFriendly.println("The AI is the player: " +playerAI+ ", the size of the board is: " +Main.sizeSideHexagon);
			outHumanFriendly.close();
			outJavaFriendly.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public TextFileManager(String numberFile) {
		this.numberFile = numberFile;
	}
	
	public ArrayList<String> getPlayHistory() {
		FileReader fr = null;
		BufferedReader br = null;
		ArrayList<String> gameHistory = new ArrayList<>();
		try {
			fr = new FileReader("history/gameHistory_JavaFriendly" +numberFile+ ".txt");
			br = new BufferedReader(fr);
			String sCurrentLine = br.readLine();
			while ((sCurrentLine = br.readLine()) != null && !sCurrentLine.equals("")) {
				gameHistory.add("(" +sCurrentLine.split(",")[0]+ ", " 
						+sCurrentLine.split(",")[2]+ ", " 
						+sCurrentLine.split(",")[3]+ ", " 
						+sCurrentLine.split(",")[1]+ ")");
			}

		} catch (IOException e) {
			
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return gameHistory;
	}
	public void saveMoveInText(int player, int x, int y, int cellId) {
		try {
			String javaFriendlyText = player+ "," +cellId+ "," +x+ "," +y +System.lineSeparator();
			String humanFriendlyText = "Player: " +player+ " moves in (" +x+ ", " +y+ ") cell with Id: " +cellId +System.lineSeparator();
		    Files.write(Paths.get("history/gameHistory_JavaFriendly" +numberFile+ ".txt"), javaFriendlyText.getBytes(), StandardOpenOption.APPEND);
		    Files.write(Paths.get("history/gameHistory_HumanFriendly" +numberFile+ ".txt"), humanFriendlyText.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Games recoverLastGame() {
		File[] files = new File("history/").listFiles();
		return recoverGame(files[files.length - 1].getName());
	}
	
	public static Games recoverGame(String fileName) {
		System.out.println("Loading game: " +fileName);
		FileReader fr = null;
		BufferedReader br = null;
		Games games = null;
		SimpleGame simpleGame = null;
		Game game = null;
		try {
			fr = new FileReader("history/" +fileName);
			br = new BufferedReader(fr);
			
			String sCurrentLine = br.readLine();

			simpleGame = new SimpleGame(Integer.valueOf(sCurrentLine.split(",")[0]), Integer.valueOf(sCurrentLine.split(",")[1]));
			game = new Game(Integer.valueOf(sCurrentLine.split(",")[0]) * 2 - 1, Integer.valueOf(sCurrentLine.split(",")[1]));
			int movesDone = 0;
			while ((sCurrentLine = br.readLine()) != null && !sCurrentLine.equals("")) {
				movesDone++;
				simpleGame.makeMove(Integer.valueOf(sCurrentLine.split(",")[0]), Integer.valueOf(sCurrentLine.split(",")[1]));
				game.setCellToPlayer(Integer.valueOf(sCurrentLine.split(",")[0]), Integer.valueOf(sCurrentLine.split(",")[2]), Integer.valueOf(sCurrentLine.split(",")[3]));
				game.uniteMoveConfirmed(Integer.valueOf(sCurrentLine.split(",")[0]), Integer.valueOf(sCurrentLine.split(",")[2]), Integer.valueOf(sCurrentLine.split(",")[3]));
			}
			
			String fileNumber = fileName.replace(".txt", "").replace("history/", "").replace("gameHistory_JavaFriendly", "");
			games = new Games(simpleGame, game, movesDone / 2 % 2 == 0 ? 1 : 2, fileNumber);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return games;
	}
	
	public void finishGame(long p1, long p2) {
		try(FileWriter fw = new FileWriter("history/gameHistory_JavaFriendly" +numberFile+ ".txt", true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter outHumanFriendly = new PrintWriter(bw)){
			outHumanFriendly.println();
			outHumanFriendly.println("-------------------------------");
			outHumanFriendly.println();
			outHumanFriendly.println("GAME FINISHED");
			outHumanFriendly.println();
			outHumanFriendly.println("Player 1 = " +p1+ ".");
			outHumanFriendly.println("Player 2 = " +p2+ ".");
			outHumanFriendly.println();
			outHumanFriendly.println("-------------------------------");
			outHumanFriendly.println();
			if(p1 == p2)
				outHumanFriendly.println("BOTH PLAYER HAVE THE SAME RESULT");
			else 
				outHumanFriendly.println("THE WINNER IS PLAYER = " +(p1 > p2 ? 1 : 2)+ ".");
			outHumanFriendly.println();
			outHumanFriendly.println("-------------------------------");
			outHumanFriendly.close();		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
