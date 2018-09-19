package com.um.omega.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class UserInterface2 extends JPanel implements MouseListener, MouseMotionListener{
	private static int xPos = 100;
	private static int yPos = 200;
	private static int width = 10;
	private static int height = 10;
	private static int boardSize;
	private static int numberHexMiddleRow;
	private static Game2 game;
//	private static int x = 0;
//	private static int y = 0;
	
	public UserInterface2(int boardSize, int size, Game2 game) {
		
		this.boardSize = boardSize;
		this.numberHexMiddleRow = size * 2 - 1;
		this.game = game;
	}
		
	public void paintComponent(Graphics g){
		super.paintComponent(g);
//		this.setBackground(Color.YELLOW);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);		
//		g.drawString("Omega", Main.sizeX / 2, 50);
		drawHexagonGrid2(g, new Cell(boardSize / 2, boardSize/2), numberHexMiddleRow , boardSize / numberHexMiddleRow, 0);

//		drawHexagon2(g, new Cell(boardSize / 2, boardSize/2), numberHexMiddleRow , boardSize / (2 * (numberHexMiddleRow)), 0);
//		g.setColor(Color.RED);
//		g.fillRect(xPos, yPos, width, height);
//		g.setColor(Color.BLUE);
//		g.fillRect(xPos*2, yPos, width, height);
	}
	
	private void drawHexagonGrid(Graphics g, Cell origin, int size, int radius, int padding) {
        double ang30 = Math.toRadians(30);
        double xOff = Math.cos(ang30) * (radius + padding);
        double yOff = Math.sin(ang30) * (radius + padding);
        int half = size / 2;

        for (int row = 0; row < size; row++) {
            int cols = size - java.lang.Math.abs(row - half);
            for (int col = 0; col < cols; col++) {
                int xLbl = row < half ? col - row : col - half;
                int yLbl = row - half;
                int x = (int) (origin.x + xOff * (col * 2 + 1 - cols));
                int y = (int) (origin.y + yOff * (row - half) * 3);
                
                int x2 = (int) (origin.x + xOff * (xLbl*2 + yLbl));
                
                System.out.println("ROW?: " +(row < half)+ ",col: " +col+ ", row: " +row+ ", xLbl: " +xLbl+ ", yLbl :" +yLbl+ ", x: " +x+ 
                		", multX: " +(col * 2 + 1 - cols)+ ", y: " +y+ ", multY:" + (row - half) * 3+ ", x2: " +x2);
                drawHexagon(g, xLbl, yLbl, x, y, radius);
            }
        }
    }

//	@SuppressWarnings("unused")
	private void drawHexagonGrid2(Graphics g, Cell origin, int size, int radius, int padding) {

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
		Hexagon hex = new Hexagon(posX, posY, boardSize / numberHexMiddleRow, cell.x, cell.y, - cell.x - cell.y);
		hex.draw((Graphics2D) g, posX, posY, 4, 0xCCCCCC, false);
		hex.draw((Graphics2D) g, posX, posY, 4, getColor(player), true);
//		System.out.println("Draw: " +cell.getPointString()+ ", player: " +player);

		g.setFont(new Font("TimesRoman", Font.BOLD, 15));
		if(player == 1) // The player 1 is white so the letters have to be black
        	g.setColor(Color.BLACK);
        else 
        	g.setColor(Color.WHITE);
        g.drawString(Integer.toString(cell.x) , posX - radius/2, posY - radius/4);
        g.drawString(Integer.toString(cell.y), posX + radius/2, posY - radius/4);
        g.drawString(Integer.toString(- cell.x - cell.y), posX, posY + radius/4);
	}
	
	private int getColor(int player) {
		if(player == 0) return 0x66CDAA;
		else if(player == 1) return 0xFFFFFF;
		else return 0x000000; // Player 2 is black
	}
	
	private void drawHexagon(Graphics g, int xPosition, int yPosition, int x, int y, int r) {
        Hexagon hex = new Hexagon(x, y, r, xPosition, yPosition, - xPosition - yPosition);
//        String text = String.format("%s : %s", posX, posY);
//        int w = metrics.stringWidth(text);
//        int h = metrics.getHeight();

//        g.setColor(Color.BLACK);
//        g.fillPolygon(hex);
        int color = GameSituation.isOccupiedColor(new Cell(xPosition, yPosition));
        // Draw the border of the hexagon
        hex.draw((Graphics2D) g, x, y, 4, 0xCCCCCC, false);
        // Draw the inside of the hexagon
        hex.draw((Graphics2D) g, x, y, 4, color, true);
//      
//        hex.draw((Graphics2D) g, x, y, 4, 0xFFDD88, true);

//        g.setColor(Color.RED);
//        g.drawPolygon(hex);
        g.setFont(new Font("TimesRoman", Font.BOLD, 15));
        if(color == 0xFFFFFF)
        	g.setColor(Color.BLACK);
        else 
        	g.setColor(Color.WHITE);
        g.drawString(Integer.toString(xPosition) , x - r/2, y - r/4);
        g.drawString(Integer.toString(yPosition), x + r/2, y - r/4);
        g.drawString(Integer.toString(- xPosition - yPosition), x, y + r/4);
    }
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		xPos = e.getX();
		yPos = e.getY();
		repaint();
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