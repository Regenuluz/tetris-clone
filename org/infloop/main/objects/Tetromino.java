package org.infloop.main.objects;

import java.awt.Color;
import java.awt.Point;

public abstract class Tetromino {

	public int[][] figure;
	public Color color;
	public Shadow shadow;

	// The position of the top left corner of the figure
	public Point pos = new Point();

	// How many blocks that's used to build the block
	public int nBlocks = 4;
	public String name = "";
	
	public abstract void resetFigure();

	/**
	 * Returns the real position of the blocks the tetromino is made of
	 * 
	 * @return Point[] position of the blocks the tetromino is made of
	 */
	public Point[] getPoints() {
		Point[] points = new Point[this.nBlocks];
		int N = this.figure.length;
		int v = 0;
		for (int y = 0; y < N; y++) {
			for (int x = 0; x < N; x++) {
				if (this.figure[y][x] == 1) {
					points[v] = new Point((this.pos.x + x), (this.pos.y + y));
					v++;
				}
			}
		}

		return points;
	}

	public Point[] getInternalPoints() {
		Point[] points = getPoints();
		
		for (int i = 0; i < points.length; i++) {
			points[i].x -= this.pos.x;
			points[i].y -= this.pos.y;
		}
		
		return points;
	}
	
	public void initPos(Point pos) {
		this.pos.x = pos.x;
		this.pos.y = pos.y;
	}
}
