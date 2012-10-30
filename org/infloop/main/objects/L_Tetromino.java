package org.infloop.main.objects;

import java.awt.Color;

public class L_Tetromino extends Tetromino {

	public L_Tetromino() {
		this.name = "L tetro";
		this.color = new Color(0xa204a3); //magenta
		
		resetFigure();
	}

	@Override
	public void resetFigure() {
		this.figure = new int[3][3];
		this.figure[1][0] = 1;
		this.figure[1][1] = 1;
		this.figure[1][2] = 1;
		this.figure[2][0] = 1;
	}
}
