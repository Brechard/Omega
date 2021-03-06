package Debug;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.um.omega.game.Cell;
import com.um.omega.game.Game;
import com.um.omega.game.Hexagon;

public class UserInterface extends JPanel implements MouseListener{
	private static int xPos = 100;
	private static int yPos = 200;
	private static int width = 10;
	private static int height = 10;
	private static int boardSize;
	private static int numberHexMiddleRow;
	private static Game game;
	private static ArrayList<Hexagon> grid = new ArrayList<>();
	private static GameController2 gameController;
	private static int cellId = 0;
//	private static int x = 0;
//	private static int y = 0;
	
	public UserInterface(int boardSize, int size, Game game, GameController2 gameController) {
		
		UserInterface.boardSize = boardSize;
		UserInterface.numberHexMiddleRow = size * 2 - 1;
		UserInterface.game = game;
		UserInterface.gameController = gameController;
        addMouseListener(this);
	}
		
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
//		this.setBackground(Color.YELLOW);
//		g.drawString("Omega", Main.sizeX / 2, 50);
		drawHexagonGrid(g, new Cell(boardSize / 2, boardSize/2, 0), numberHexMiddleRow , boardSize / numberHexMiddleRow, 0);
	}
	
	private void drawHexagonGrid(Graphics g, Cell origin, int size, int radius, int padding) {

		double ang30 = Math.toRadians(30);
        double xOff = Math.cos(ang30) * (radius + padding);
        double yOff = Math.sin(ang30) * (radius + padding);
        
		game.emptyCells.stream().forEach(
			cell -> drawHexagon(g, origin, size, radius, padding, cell, xOff, yOff, 0)
		);
		game.player1.stream().forEach(
				cell -> drawHexagon(g, origin, size, radius, padding, cell, xOff, yOff, 1)
			);
		game.player2.stream().forEach(
				cell -> drawHexagon(g, origin, size, radius, padding, cell, xOff, yOff, 2)
			);
	}
	
	private void drawHexagon(Graphics g, Cell origin, int size, int radius, int padding, Cell cell, double xOff, double yOff, int player) {

		int posX = (int) (origin.x + xOff * (cell.x*2 + cell.y));
		int posY = (int) (origin.y + yOff * cell.y * 3);
		Hexagon hex = new Hexagon(posX, posY, boardSize / numberHexMiddleRow, cell.x, cell.y, - cell.x - cell.y, cellId++);
		hex.draw((Graphics2D) g, posX, posY, 4, 0xCCCCCC, false);
		hex.draw((Graphics2D) g, posX, posY, 4, getColor(player), true);
//		System.out.println("Draw: " +cell.getPointString()+ ", player: " +player);
		grid.add(hex);
		g.setFont(new Font("TimesRoman", Font.BOLD, 15));
		if(player == 1) // The player 1 is white so the letters have to be black
        	g.setColor(Color.BLACK);
        else 
        	g.setColor(Color.WHITE);
        g.drawString(Integer.toString(cell.x) , posX - radius/2, posY - radius/4);
        g.drawString(Integer.toString(cell.y), posX + radius/2, posY - radius/4);
        g.drawString(Integer.toString(cell.id), posX, posY + radius/4);
	}
	
	private int getColor(int player) {
		if(player == 0) return 0x66CDAA;
		else if(player == 1) return 0xFFFFFF;
		else return 0x000000; // Player 2 is black
	}

	@Override
	public void mouseClicked(MouseEvent e) {


	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}