package com.um.omega.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;

public class Hexagon extends Polygon {

    private static final long serialVersionUID = 1L;

    public static final int SIDES = 6;

    private Cell[] points = new Cell[SIDES];
    private Cell center = new Cell(0, 0);
    private int radius;
    private int rotation = 90;
    private int xCubePosition;
    private int yCubePosition;
    private int zCubePosition;
    
    public Hexagon(Cell center, int radius) {
        npoints = SIDES;
        xpoints = new int[SIDES];
        ypoints = new int[SIDES];

        this.center = center;
        this.radius = radius;

        updatePoints();
    }

    public Hexagon(int x, int y, int radius, int xCubePosition, int yCubePositino, int zCubePosition) {
        this(new Cell(x, y), radius);
        this.setxCubePosition(xCubePosition);
        this.setyCubePosition(yCubePositino);
        this.setzCubePosition(zCubePosition);
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;

        updatePoints();
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;

        updatePoints();
    }

    public void setCenter(Cell center) {
        this.center = center;

        updatePoints();
    }

    public void setCenter(int x, int y) {
        setCenter(new Cell(x, y));
    }

    private double findAngle(double fraction) {
        return fraction * Math.PI * 2 + Math.toRadians((rotation + 180) % 360);
    }

    private Cell findPoint(double angle) {
        int x = (int) (center.x + Math.cos(angle) * radius);
        int y = (int) (center.y + Math.sin(angle) * radius);

        return new Cell(x, y);
    }

    protected void updatePoints() {
        for (int p = 0; p < SIDES; p++) {
            double angle = findAngle((double) p / SIDES);
            Cell point = findPoint(angle);
            xpoints[p] = point.x;
            ypoints[p] = point.y;
            points[p] = point;
        }
    }

    public void draw(Graphics2D g, int x, int y, int lineThickness, int colorValue, boolean filled) {
        // Store before changing.
        Stroke tmpS = g.getStroke();
        Color tmpC = g.getColor();

        g.setColor(new Color(colorValue));
        g.setStroke(new BasicStroke(lineThickness, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));

        if (filled)
            g.fillPolygon(xpoints, ypoints, npoints);
        else
            g.drawPolygon(xpoints, ypoints, npoints);

        // Set values to previous when done.
        g.setColor(tmpC);
        g.setStroke(tmpS);
    }

	public int getxCubePosition() {
		return xCubePosition;
	}

	public void setxCubePosition(int xCubePosition) {
		this.xCubePosition = xCubePosition;
	}

	public int getyCubePosition() {
		return yCubePosition;
	}

	public void setyCubePosition(int yCubePosition) {
		this.yCubePosition = yCubePosition;
	}

	public int getzCubePosition() {
		return zCubePosition;
	}

	public void setzCubePosition(int zCubePosition) {
		this.zCubePosition = zCubePosition;
	}
}

