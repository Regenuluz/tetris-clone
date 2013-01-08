package org.infloop.main.objects;

import java.awt.Color;

public class T_Tetromino extends Tetromino {

	public T_Tetromino() {
		this.name = "T tetro";
		this.color = new Color(0xb25706); // brown

		resetFigure();
	}

	@Override
	public void resetFigure() {
		this.figure = new int[3][3];
		this.figure[1][0] = 1;
		this.figure[1][1] = 1;
		this.figure[1][2] = 1;
		this.figure[2][1] = 1;
	}
}