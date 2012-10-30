package org.infloop.main.objects;

import java.awt.Color;

public class I_Tetromino extends Tetromino {

	public I_Tetromino() {
		this.name = "I tetro";
		
		this.color = new Color(0xaa0100); //maroon
		resetFigure();
	}
	
	@Override
	public void resetFigure() {
		this.figure = new int[4][4];
		this.figure[1][0] = 1;
		this.figure[1][1] = 1;
		this.figure[1][2] = 1;
		this.figure[1][3] = 1;
	}
}
