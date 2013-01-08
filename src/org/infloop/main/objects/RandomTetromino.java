package org.infloop.main.objects;

import java.util.Random;

public class RandomTetromino {
	private static Random random = new Random();

	public static Tetromino getRandom() {
		int r = random.nextInt(7);
		Tetromino figure;

		if (r == 0) {
			figure = new I_Tetromino();
		} else if (r == 1) {
			figure = new L_Tetromino();
		} else if (r == 2) {
			figure = new J_Tetromino();
		} else if (r == 3) {
			figure = new O_Tetromino();
		} else if (r == 4) {
			figure = new S_Tetromino();
		} else if (r == 5) {
			figure = new Z_Tetromino();
		} else {
			figure = new T_Tetromino();
		}

		return figure;
	}
}
