package org.infloop.main.objects;

import java.awt.Color;

public class S_Tetromino extends Tetromino {

	public S_Tetromino() {
		this.name = "S tetro";
		this.color = new Color(0x00ab00); //green
		
		resetFigure();
	}

	@Override
	public void resetFigure() {
		this.figure = new int[3][3];
		this.figure[1][1] = 1;
		this.figure[1][2] = 1;
		this.figure[2][0] = 1;
		this.figure[2][1] = 1;
	}
}
