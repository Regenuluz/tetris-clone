package org.infloop.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;

import org.infloop.main.Highscore;
import org.infloop.main.Tetris;
import org.infloop.main.objects.Tetromino;




public class Screen {
	private final int WIDTH;
	private final int HEIGHT;
	private final Tetris ts;
	private Graphics g;

	public Color bgColor = new Color(0xdddddd);

	/**
	 * Creates a new screen object
	 * 
	 * @param w
	 *            width in pixels
	 * @param h
	 *            height in pixels
	 * @param ts
	 *            tetris object to paint
	 */
	public Screen(int w, int h, Tetris ts) {
		this.WIDTH = w;
		this.HEIGHT = h;

		this.ts = ts;
	}

	/**
	 * Renders the entire screen
	 */
	public void render() {
		BufferStrategy bs = this.ts.getBufferStrategy();
		if (bs == null) {
			this.ts.createBufferStrategy(2);
			return;
		}

		this.g = bs.getDrawGraphics();

		this.g.setColor(this.bgColor);
		this.g.fillRect(0, 0, this.WIDTH, this.HEIGHT);

		// Renders the frame around the game
		// and the statistics
		renderFrame();
		renderStats(4, 7);
		renderHighscore(3, 11);

		// Renders the gameboard, current tetromino
		// and its shadow
		renderGameboard(1, 0);
		renderCurrentTetro(1, 0);
		renderSmallTetro(this.ts.nextTetro, 2, 1);
		if (this.ts.heldTetro != null) {
			renderSmallTetro(this.ts.heldTetro, 6, 1);
		}

		if (this.ts.gameOver) {
			renderMessage("GAME OVER!");
		} else if (this.ts.paused) {
			renderMessage("PAUSED!");
		}

		if (this.ts.showNameInput) {
			renderNameInput(2, 0);
		}

		this.g.dispose();
		bs.show();
	}

	/**
	 * Renders the frame around the game and stats
	 */
	private void renderFrame() {
		Color col = Color.lightGray.darker();
		// Vertical lines
		for (int y = 0; y < this.ts.h + 1; y++) {
			renderBlock(0, y, col);
			renderBlock(this.ts.w + 1, y, col);
			renderBlock(this.WIDTH / Tetris.BLOCK_SIZE - 1, y, col);

			if (y == 1 || y == 2) {
				renderBlock(this.ts.w + 5, y, col);
			}
		}
		// bottom
		for (int x = 0; x < this.WIDTH / Tetris.BLOCK_SIZE - 1; x++) {
			renderBlock(x, this.ts.h, col);

			if (x >= this.ts.w + 1) {
				renderBlock(x, 0, col);
				renderBlock(x, 3, col);
				renderBlock(x, 6, col);
				renderBlock(x, 10, col);
			}
		}

		this.g.setColor(new Color(0xc9decb));
		this.g.fillRect((this.ts.w + 2) * Tetris.BLOCK_SIZE + 2, 2, Tetris.BLOCK_SIZE * 3 - 4, Tetris.BLOCK_SIZE - 4);
		this.g.fillRect((this.ts.w + 6) * Tetris.BLOCK_SIZE + 2, 2, Tetris.BLOCK_SIZE * 3 - 4, Tetris.BLOCK_SIZE - 4);

		this.g.setColor(Color.black);
		this.g.drawRect((this.ts.w + 2) * Tetris.BLOCK_SIZE + 2, 2, Tetris.BLOCK_SIZE * 3 - 4, Tetris.BLOCK_SIZE - 4);
		this.g.drawRect((this.ts.w + 6) * Tetris.BLOCK_SIZE + 2, 2, Tetris.BLOCK_SIZE * 3 - 4, Tetris.BLOCK_SIZE - 4);

		this.g.setFont(new Font("Serif", Font.PLAIN, 3 * Tetris.BLOCK_SIZE / 4));
		this.g.drawString("Next:", (this.ts.w + 2) * Tetris.BLOCK_SIZE + Tetris.BLOCK_SIZE / 2, 3 * Tetris.BLOCK_SIZE / 4);
		this.g.drawString("Hold:", (this.ts.w + 6) * Tetris.BLOCK_SIZE + Tetris.BLOCK_SIZE / 2, 3 * Tetris.BLOCK_SIZE / 4);
	}

	/**
	 * Renders the stats on the screen
	 */
	private void renderStats(int offsetX, int offsetY) {
		this.g.setColor(Color.black);

		this.g.setFont(new Font("Serif", Font.BOLD, Tetris.BLOCK_SIZE));
		FontMetrics fm = this.g.getFontMetrics();

		String lvl = "LEVEL: " + this.ts.level;
		int rx = (Tetris.BLOCK_SIZE + fm.stringWidth(lvl)) / 2 + this.ts.w * Tetris.BLOCK_SIZE + Tetris.BLOCK_SIZE / 2;
		this.g.drawString(lvl, rx, 5 * Tetris.BLOCK_SIZE + Tetris.BLOCK_SIZE / 2);

		int posx = (this.ts.w + offsetX) * Tetris.BLOCK_SIZE;
		int posy = offsetY * Tetris.BLOCK_SIZE + Tetris.BLOCK_SIZE * 5 / 4;

		this.g.setFont(new Font("Serif", Font.PLAIN, 3 * Tetris.BLOCK_SIZE / 4));
		this.g.drawString("Score: " + this.ts.score, posx, posy);
		this.g.drawString("Lines: " + this.ts.linesRemoved, posx, posy + Tetris.BLOCK_SIZE);

	}

	/**
	 * Renders a message on the screen
	 * 
	 * @param message
	 *            Message to show on screen
	 */
	private void renderMessage(String message) {

		this.g.setColor(new Color(0xc9decb));
		int x = 2 * Tetris.BLOCK_SIZE + 2;
		int y = ((this.ts.h - 1) / 2) * Tetris.BLOCK_SIZE + 2;
		int w = (this.ts.w - 2) * Tetris.BLOCK_SIZE - 4;
		int h = Tetris.BLOCK_SIZE * 2 - 4;

		this.g.fillRect(x, y, w, h);
		this.g.setColor(Color.black);
		this.g.drawRect(x, y, w, h);

		this.g.setColor(Color.black);
		this.g.setFont(new Font("Serif", Font.PLAIN, Tetris.BLOCK_SIZE * 8 / 7));

		FontMetrics fm = this.g.getFontMetrics();
		int realWidth = (this.ts.w + 2) * Tetris.BLOCK_SIZE;
		int realHeight = this.ts.h * Tetris.BLOCK_SIZE;

		this.g.drawString(message, (realWidth - fm.stringWidth(message)) / 2, realHeight / 2 + fm.getHeight() / 3);
	}

	/**
	 * Renders the next tetromino
	 */
	private void renderSmallTetro(Tetromino t, int offsetX, int offsetY) {
		Point[] points = t.getInternalPoints();
		Color color = t.color;

		for (int i = 0; i < points.length; i++) {
			this.g.setColor(color);
			int x = (this.ts.w + offsetX) * Tetris.BLOCK_SIZE + ((points[i].x + 1) * Tetris.BLOCK_SIZE / 2);
			int y = offsetY * Tetris.BLOCK_SIZE + (points[i].y * Tetris.BLOCK_SIZE / 2);

			if (t.figure.length == 3) {
				x += Tetris.BLOCK_SIZE / 4;
			} else if (t.figure.length == 2) {
				x += Tetris.BLOCK_SIZE / 2;
				y += Tetris.BLOCK_SIZE / 2;
			} else if (t.figure.length == 4) {
				y += Tetris.BLOCK_SIZE / 4;
			}

			this.g.fillRect(x, y, Tetris.BLOCK_SIZE / 2, Tetris.BLOCK_SIZE / 2);

			addBorder(x, y, x + Tetris.BLOCK_SIZE / 2, y + Tetris.BLOCK_SIZE / 2, color);
		}
	}

	/**
	 * Renders a block
	 * 
	 * @param x
	 *            x-coordinate of the top left corner
	 * @param y
	 *            y-coordinate of the top left corner
	 * @param blockColor
	 *            The color of the block
	 */
	private void renderBlock(int x, int y, Color blockColor) {
		renderBlock(x, y, blockColor, blockColor);
	}

	/**
	 * Renders a block
	 * 
	 * @param x
	 *            x-coordinate of the top left corner
	 * @param y
	 *            y-coordinate of the top left corner
	 * @param fillColor
	 *            The solid color of the block
	 * @param borderColor
	 *            The color of the border
	 */
	private void renderBlock(int x, int y, Color fillColor, Color borderColor) {
		int nx = x * Tetris.BLOCK_SIZE;
		int ny = y * Tetris.BLOCK_SIZE;

		this.g.setColor(fillColor);
		this.g.fillRect(nx, ny, Tetris.BLOCK_SIZE, Tetris.BLOCK_SIZE);

		// Add shade
		addBorder(nx, ny, nx + Tetris.BLOCK_SIZE, ny + Tetris.BLOCK_SIZE, borderColor);
	}

	/**
	 * Adds shadows to a block
	 * 
	 * @param x1
	 *            x-coordinate of the top left corner
	 * @param y1
	 *            y-coordinate of the top left corner
	 * @param x2
	 *            x-coordinate of the bottom right corner
	 * @param y2
	 *            y-coordinate of the bottom right corner
	 * @param baseCol
	 *            Base color of the border
	 */
	private void addBorder(int x1, int y1, int x2, int y2, Color baseCol) {
		// Add light shade
		this.g.setColor(baseCol.brighter());
		this.g.drawLine(x1, y1, x2 - 1, y1); // top
		this.g.drawLine(x1 + 1, y1 + 1, x2 - 2, y1 + 1);
		this.g.drawLine(x1, y1, x1, y2 - 2); // left
		this.g.drawLine(x1 + 1, y1, x1 + 1, y2 - 3);

		// Add dark shade
		this.g.setColor(baseCol.darker().darker());
		this.g.drawLine(x1, y2 - 1, x2 - 1, y2 - 1); // bottom
		this.g.drawLine(x1 + 2, y2 - 2, x2 - 2, y2 - 2);
		this.g.drawLine(x2 - 1, y1 + 1, x2 - 1, y2 - 1);// right
		this.g.drawLine(x2 - 2, y1 + 2, x2 - 2, y2 - 2);
	}

	/**
	 * Renders the current tetromino on the screen
	 * 
	 * @param offsetX
	 *            offset on the x-axis in blocks
	 * @param offsetY
	 *            offset on the y-axis in blocks
	 */
	private void renderCurrentTetro(int offsetX, int offsetY) {
		if (this.ts.shadowEnabled) {
			// Render shadow first, so that it doesn't overlap the cur tetro
			renderShadow(offsetX, offsetY);
		}

		Point[] points = this.ts.curTetro.getPoints();
		for (int i = 0; i < points.length; i++) {
			renderBlock(points[i].x + offsetX, points[i].y + offsetY, this.ts.curTetro.color);
		}
	}

	/**
	 * Renders the shadow of the current tetromino
	 * 
	 * @param offsetX
	 *            offset on the x-axis in blocks
	 * @param offsetY
	 *            offset on the y-axis in blocks
	 */
	private void renderShadow(int offsetX, int offsetY) {
		Point[] points = this.ts.curTetro.getInternalPoints();

		for (int i = 0; i < points.length; i++) {
			int x = points[i].x + this.ts.shadow.pos.x + offsetX;
			int y = points[i].y + this.ts.shadow.pos.y + offsetY;
			Color col = Color.lightGray.darker();

			renderBlock(x, y, col, col);
		}
	}

	/**
	 * Renders the game board on the screen
	 * 
	 * @param offsetX
	 *            offset on the x-axis in blocks
	 * @param offsetY
	 *            offset on the y-axis in blocks
	 */
	private void renderGameboard(int offsetX, int offsetY) {
		for (int y = 0; y < this.ts.gameBoard.length; y++) {
			for (int x = 0; x < this.ts.gameBoard[0].length; x++) {
				if (this.ts.gameBoard[y][x] != this.bgColor) {
					renderBlock(x + offsetX, y + offsetY, this.ts.gameBoard[y][x]);
				}
			}
		}
	}

	/**
	 * Renders the highscore
	 * 
	 * @param offsetX
	 *            Offset on the x-axis in blocks
	 * @param offsetY
	 *            Offset on the y-axis in blocks
	 */
	private void renderHighscore(int offsetX, int offsetY) {
		int x = (this.ts.w + offsetX) * Tetris.BLOCK_SIZE;
		int y = offsetY * Tetris.BLOCK_SIZE + Tetris.BLOCK_SIZE / 2;
		int w = 5 * Tetris.BLOCK_SIZE;
		int h = 8 * Tetris.BLOCK_SIZE;

		this.g.setColor(new Color(0xc9decb));
		this.g.fillRoundRect(x - Tetris.BLOCK_SIZE / 2, y, w + Tetris.BLOCK_SIZE, h, Tetris.BLOCK_SIZE, Tetris.BLOCK_SIZE);

		this.g.setColor(Color.black);
		this.g.drawRoundRect(x - Tetris.BLOCK_SIZE / 2, y, w + Tetris.BLOCK_SIZE, h, Tetris.BLOCK_SIZE, Tetris.BLOCK_SIZE);

		this.g.drawString("Highscore:", x + Tetris.BLOCK_SIZE * 3 / 4, y + Tetris.BLOCK_SIZE);
		this.g.drawLine(x, y + Tetris.BLOCK_SIZE + 5, x + w, y + Tetris.BLOCK_SIZE + 5);
		this.g.drawLine(x + w / 2, y + Tetris.BLOCK_SIZE * 3 / 2, x + w / 2, y + h - Tetris.BLOCK_SIZE * 1 / 4);

		int i = 0;
		for (Highscore hs : this.ts.highscore) {
			String name = hs.name;
			String score = hs.score + "";
			// int lines = hs.lines;

			int ny = y + (i + 2) * Tetris.BLOCK_SIZE + Tetris.BLOCK_SIZE / 2;
			this.g.drawString((i + 1) + " " + name, x, ny);

			FontMetrics fm = this.g.getFontMetrics();
			this.g.drawString(score, x + w - fm.stringWidth(score), ny);
			i++;
		}
	}

	private void renderNameInput(int offsetX, int offsetY) {
		int h = 5 * Tetris.BLOCK_SIZE;
		int x = offsetX * Tetris.BLOCK_SIZE;
		int y = (this.ts.h / 2) * Tetris.BLOCK_SIZE + offsetY * Tetris.BLOCK_SIZE - h / 2;
		int w = (this.ts.w - 2) * Tetris.BLOCK_SIZE;

		this.g.setColor(new Color(0xc9decb));
		this.g.fillRoundRect(x, y, w, h, Tetris.BLOCK_SIZE, Tetris.BLOCK_SIZE);
		this.g.setColor(Color.black);
		this.g.drawRoundRect(x, y, w, h, Tetris.BLOCK_SIZE, Tetris.BLOCK_SIZE);

		this.g.setFont(new Font("Serif", Font.PLAIN, Tetris.BLOCK_SIZE * 3 / 4));
		this.g.drawString("Please enter your name:", x + Tetris.BLOCK_SIZE * 3 / 8, y + Tetris.BLOCK_SIZE);
		this.g.drawLine(x, y + Tetris.BLOCK_SIZE * 5 / 4, x + w, y + Tetris.BLOCK_SIZE * 5 / 4);

		this.g.setColor(new Color(0x99aadd));
		int of = Tetris.BLOCK_SIZE * 4 / 3;
		this.g.fillRect(of + x + Tetris.BLOCK_SIZE, y + Tetris.BLOCK_SIZE * 2, Tetris.BLOCK_SIZE, Tetris.BLOCK_SIZE * 3 / 2);
		this.g.fillRect(of + x + Tetris.BLOCK_SIZE * 2 + 5, y + Tetris.BLOCK_SIZE * 2, Tetris.BLOCK_SIZE, Tetris.BLOCK_SIZE * 3 / 2);
		this.g.fillRect(of + x + Tetris.BLOCK_SIZE * 3 + 10, y + Tetris.BLOCK_SIZE * 2, Tetris.BLOCK_SIZE, Tetris.BLOCK_SIZE * 3 / 2);

		this.g.setColor(Color.black);
		this.g.drawRect(of + x + Tetris.BLOCK_SIZE, y + Tetris.BLOCK_SIZE * 2, Tetris.BLOCK_SIZE, Tetris.BLOCK_SIZE * 3 / 2);
		this.g.drawRect(of + x + Tetris.BLOCK_SIZE * 2 + 5, y + Tetris.BLOCK_SIZE * 2, Tetris.BLOCK_SIZE, Tetris.BLOCK_SIZE * 3 / 2);
		this.g.drawRect(of + x + Tetris.BLOCK_SIZE * 3 + 10, y + Tetris.BLOCK_SIZE * 2, Tetris.BLOCK_SIZE, Tetris.BLOCK_SIZE * 3 / 2);

		this.g.setFont(new Font("Serif", Font.PLAIN, Tetris.BLOCK_SIZE));

		for (int i = 0; i < this.ts.hsName.length(); i++) {
			String l = this.ts.hsName.substring(i, i + 1).toUpperCase();
			this.g.drawString(l, of + x + Tetris.BLOCK_SIZE * (12 * (1 + i)) / 10, y + Tetris.BLOCK_SIZE * 3 + Tetris.BLOCK_SIZE / 3);
		}
	}
}
