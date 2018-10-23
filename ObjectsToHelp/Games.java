package ObjectsToHelp;

import com.um.omega.game.Game;
import com.um.omega.game.SimpleGame;

public class Games {

	public SimpleGame simpleGame;
	public Game game;
	public int playerTurn;
	public String fileNumber;

	public Games(SimpleGame simpleGame, Game game, int playerTurn, String fileNumber) {
		this.simpleGame = simpleGame;
		this.game = game;
		this.playerTurn = playerTurn;
		this.fileNumber = fileNumber;
	}
}
