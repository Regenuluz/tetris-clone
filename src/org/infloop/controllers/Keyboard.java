package org.infloop.controllers;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.infloop.main.Tetris;



public class Keyboard extends KeyAdapter {

	// Reference to the tetris object
	private final Tetris ts;

	/**
	 * Constructor for the keylistener
	 * 
	 * @param ts
	 *            Reference to the tetris object
	 */
	public Keyboard(Tetris ts) {
		this.ts = ts;
	}

	/**
	 * Handles keypresses
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (this.ts.showNameInput) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				return;
			}

			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				this.ts.addHighscore();
			} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				if (this.ts.hsName.length() > 1) {
					String tmp = "";
					for (int i = 0; i < this.ts.hsName.length() - 1; i++) {
						tmp += this.ts.hsName.charAt(i);
					}
					this.ts.hsName = tmp;
				} else {
					this.ts.hsName = "";
				}
			} else {
				int k = e.getKeyCode();
				
				//Tillader kun A-Z, a-z og 0-9
				if (k >= 65 && k <= 90 || k >= 97 && k <= 122 ||  k >= 48 && k <= 57) {
					if (this.ts.hsName.length() < 3) {
						this.ts.hsName += e.getKeyChar();
					}
				}
			}

		} else if (!this.ts.paused) {
			// The up key is pressed
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				this.ts.moveSideways(1);
			}
			// The left key is pressed
			else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				this.ts.moveSideways(-1);
			}
			// The down key is pressed
			else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				this.ts.moveDown();
			}
			// The up/z key is pressed
			else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_X) {
				this.ts.rotateClockwise();
			}
			// blavla
			else if (e.getKeyCode() == KeyEvent.VK_Z) {
				this.ts.rotateCounterClockwise();
			}
			// Restart game
			else if (e.getKeyCode() == KeyEvent.VK_N) {
				this.ts.restart();
			}
			// Harddrop
			else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				this.ts.hardDrop();
			}
			// hold
			else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				this.ts.holdTetro();
			}
		}
		// Pause game
		if (!this.ts.gameOver) {
			if (e.getKeyCode() == KeyEvent.VK_P) {
				this.ts.togglePause();
			}
		}
	}
}
