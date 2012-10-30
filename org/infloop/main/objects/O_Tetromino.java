package org.infloop.main.objects;

import java.awt.Color;

public class O_Tetromino extends Tetromino {

	public O_Tetromino() {
		this.name = "O tetro";
		this.color = new Color(0x2223cf); //dark blue
		
		resetFigure();
	}

	@Override
	public void resetFigure() {
		this.figure = new int[2][2];
		this.figure[0][0] = 1;
		this.figure[1][0] = 1;
		this.figure[0][1] = 1;
		this.figure[1][1] = 1;
	}
}
