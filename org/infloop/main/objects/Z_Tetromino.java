package org.infloop.main.objects;

import java.awt.Color;

public class Z_Tetromino extends Tetromino {

	public Z_Tetromino() {
		this.name = "Z tetro";
		this.color = new Color(0x00aaa7); //cyan

		resetFigure();
	}

	@Override
	public void resetFigure() {
		this.figure = new int[3][3];
		this.figure[1][0] = 1;
		this.figure[1][1] = 1;
		this.figure[2][1] = 1;
		this.figure[2][2] = 1;
	}
}
