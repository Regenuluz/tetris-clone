package org.infloop.main.objects;

import java.awt.Color;

public class J_Tetromino extends Tetromino {

	public J_Tetromino() {
		this.name = "J tetro";
		this.color = new Color(0xc0c0c0); // light grey

		resetFigure();
	}

	@Override
	public void resetFigure() {
		this.figure = new int[3][3];
		this.figure[1][0] = 1;
		this.figure[1][1] = 1;
		this.figure[1][2] = 1;
		this.figure[2][2] = 1;
	}
}
