package org.infloop.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.infloop.controllers.Keyboard;
import org.infloop.main.objects.RandomTetromino;
import org.infloop.main.objects.Shadow;
import org.infloop.main.objects.Tetromino;
import org.infloop.ui.GameWindow;
import org.infloop.ui.Screen;



/**
 * This class is used as a base for Tetris games.
 */
public class Tetris extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	// The block size
	public static final int BLOCK_SIZE = 20;

	// Height and width of the gameboard
	public int w;
	public int h;
	public volatile boolean gameOver = true;

	// Used in hardDrop()
	public boolean dropping = false;

	// Score calculations
	public int linesRemoved = 0;
	public int score = 0;
	public int level = 1;

	public List<Highscore> highscore = new ArrayList<>();
	public String hsName = "";
	public boolean showNameInput = false;

	// Gameboard
	public Color[][] gameBoard;
	public Tetromino curTetro;
	public Tetromino nextTetro;
	public Shadow shadow;
	public boolean shadowEnabled = true;
	public Tetromino heldTetro;
	protected boolean canHold = true;

	protected Screen screen;

	protected Gravity gravity;
	public boolean paused = true;

	private static volatile boolean isRunning = false;

	/**
	 * Initializes game window and starts a new game
	 * 
	 * @param w
	 *            width of the board in blocks
	 * @param h
	 *            height of the board in blocks
	 */
	public void startGame(int w, int h) {
		this.w = w;
		this.h = h;

		int rw = (w + 7 + 3) * BLOCK_SIZE;
		int rh = (h + 1) * BLOCK_SIZE;
		
		try {
			loadHighscore();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Create new game window
		new GameWindow(rw, rh, this);
		this.screen = new Screen(rw, rh, this);

		this.gravity = new Gravity(this, 1000);

		// Start a new game
		restart();

		// Set the game to running
		isRunning = true;
		new Thread(this).start(); // Calls run()

		// Activate controls
		addKeyListener(new Keyboard(this));
	}

	/**
	 * Starts / restarts the game
	 */
	public void restart() {
		// Init board
		initBoard();
		// Start game
		this.gameOver = false;

		// Add a tetromino to the board
		this.nextTetro = RandomTetromino.getRandom();
		this.shadow = new Shadow(this.nextTetro.color);
		this.nextTetro.initPos(getStartPos());
		addNewBlock();

		// Reset stats
		this.score = 0;
		this.linesRemoved = 0;
		this.heldTetro = null;
		this.level = 1;

		this.paused = false;
		this.gravity.reset();
		this.requestFocus();
	}

	/**
	 * Initiates the board
	 */
	public void initBoard() {
		this.gameBoard = new Color[this.h][this.w];
		for (int y = 0; y < this.h; y++) {
			for (int x = 0; x < this.w; x++) {
				this.gameBoard[y][x] = this.screen.bgColor;
			}
		}
	}

	/**
	 * Paints the graphics to the screen
	 */
	@Override
	public void run() {
		while (isRunning) {
			// Render the screen
			this.screen.render();

			// Do some sleeping
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Updates the currentBlock
	 */
	protected void addNewBlock() {
		if (!this.gameOver) {
			if (!sideCollision(this.nextTetro) && !topCollision(this.nextTetro)) {
				this.curTetro = this.nextTetro;
				this.shadow = new Shadow(this.curTetro.color);
				updateShadow();

				this.nextTetro = RandomTetromino.getRandom();
				this.nextTetro.initPos(getStartPos());
			} else {
				this.gameOver = true;
				updateHighscore();
			}
		}
	}

	public Point getStartPos() {
		int x = (this.w - this.nextTetro.figure.length) / 2;
		int y = (this.nextTetro.figure.length == 2) ? 0 : -1;

		return new Point(x, y);
	}

	/**
	 * Adds the tetromino to the grid
	 */
	protected void addToGrid() {
		Point[] points = this.curTetro.getPoints();

		for (int i = 0; i < points.length; i++) {
			int nx = points[i].x;
			int ny = points[i].y;

			if (ny >= 0) {
				this.gameBoard[ny][nx] = this.curTetro.color;
			}
		}
	}

	/**
	 * Checks for and removes full lines from the board
	 */
	protected void removeFullLines() {

		Point[] points = this.curTetro.getPoints();

		int l = 0;
		for (int i = 0; i < points.length; i++) {
			boolean remove = true;
			int y = points[i].y;
			if (y < 0) {
				continue;
			}

			for (int x = 0; x < this.w; x++) {
				if (this.gameBoard[y][x] == this.screen.bgColor) {
					remove = false;
					break;
				}
			}

			if (remove) {
				l++;
				for (int ny = y; ny > 0; ny--) {
					for (int x1 = 0; x1 < this.w; x1++) {
						this.gameBoard[ny][x1] = this.gameBoard[ny - 1][x1];
					}
				}

				for (int x = 0; x < this.w; x++) {
					this.gameBoard[0][x] = this.screen.bgColor;
				}

				if ((this.linesRemoved + l) % 10 == 0 && this.linesRemoved > 0) {
					this.gravity.updateTime();
					this.level++;
				}
			}
		}

		if (l > 0) {
			this.linesRemoved += l;
			this.score += 100 * l + 20 * (l - 1);
		}
	}

	/**
	 * Moves the block on the x-axis
	 */
	public void moveSideways(int dir) {
		this.curTetro.pos.x += dir;

		if (sideCollision(this.curTetro)) {
			this.curTetro.pos.x -= dir;
		} else {
			updateShadow();
		}
	}

	/**
	 * Moves the tetromino 1 block down
	 */
	public void moveDown() {
		this.curTetro.pos.y += 1;

		if (hitBottom(this.curTetro)) {
			this.dropping = false;
			addToGrid();
			removeFullLines();
			addNewBlock();
		} else {
			updateShadow();
		}

		this.gravity.restart();
	}

	public void updateShadow() {
		if (!this.shadowEnabled) {
			return;
		}

		this.shadow.pos.x = this.curTetro.pos.x;
		this.shadow.pos.y = this.curTetro.pos.y;

		boolean collision = false;

		do {
			this.shadow.pos.y++;

			Point[] points = this.curTetro.getInternalPoints();

			for (int i = 0; i < points.length; i++) {
				int x = points[i].x + this.shadow.pos.x;
				int y = points[i].y + this.shadow.pos.y;

				if (y < 0 || y >= this.h || x < 0 || x >= this.w) {
					collision = true;
					break;
				}

				if (this.gameBoard[y][x] != this.screen.bgColor) {
					collision = true;
				}
			}

		} while (!collision);

		this.shadow.pos.y--;
	}

	/**
	 * Rotates figure clockwise
	 * 
	 * @param figure
	 * @return
	 */
	public void rotateClockwise() {
		int N = this.curTetro.figure.length;

		int[][] newFigure = new int[N][N];
		for (int y = 0; y < N; y++) {
			for (int x = 0; x < N; x++) {
				newFigure[y][x] = this.curTetro.figure[N - x - 1][y];
			}
		}

		int[][] oldFigure = this.curTetro.figure;
		this.curTetro.figure = newFigure;

		if (sideCollision(this.curTetro)) {
			this.curTetro.figure = oldFigure;
		}

		updateShadow();
	}

	public void rotateCounterClockwise() {
		int N = this.curTetro.figure.length;

		int[][] newFigure = new int[N][N];
		for (int y = 0; y < N; y++) {
			for (int x = 0; x < N; x++) {
				newFigure[y][x] = this.curTetro.figure[x][N - y - 1];
			}
		}

		int[][] oldFigure = this.curTetro.figure;
		this.curTetro.figure = newFigure;

		if (sideCollision(this.curTetro)) {
			this.curTetro.figure = oldFigure;
		}

		updateShadow();
	}

	/**
	 * Checks for collisions above and below position
	 * 
	 * @return true if there is a collision, false otherwise
	 */
	protected boolean topCollision(Tetromino tetro) {
		Point[] points = tetro.getPoints();

		for (int i = 0; i < points.length; i++) {
			int nx = points[i].x;
			int ny = points[i].y;

			if (ny < 0 || ny >= this.h || nx < 0 || nx >= this.w) {
				return true;
			}

			if (this.gameBoard[ny][nx] != this.screen.bgColor) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks for collisions on left and right
	 * 
	 * @return true if there is a collision, false otherwise
	 */
	protected boolean sideCollision(Tetromino tetro) {
		Point[] points = tetro.getPoints();

		for (int i = 0; i < points.length; i++) {
			int nx = points[i].x;
			int ny = points[i].y;

			if (nx < 0 || nx >= this.w || ny >= this.h) {
				return true;
			}

			if (ny < 0) {
				continue;
			}

			if (this.gameBoard[ny][nx] != this.screen.bgColor) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks if the tetromino hits the bottom
	 * 
	 * @return true if it hits, false otherwise
	 */
	protected boolean hitBottom(Tetromino t) {
		if (topCollision(t)) {
			t.pos.y -= 1;
			this.canHold = true;
			return true;
		}

		return false;
	}

	public void hardDrop() {
		this.dropping = true;
		while (this.dropping) {
			moveDown();
		}
	}

	public void togglePause() {
		this.paused = !this.paused;

		this.gravity.togglePause();

		this.requestFocus();
	}

	public void holdTetro() {
		if (this.gameOver) {
			return;
		}

		if (this.heldTetro == null) {
			this.heldTetro = this.curTetro;
			this.heldTetro.resetFigure();
			this.heldTetro.initPos(getStartPos());
			addNewBlock();
		} else if (this.canHold) {
			Tetromino tmp = this.curTetro;
			this.curTetro = this.heldTetro;
			this.heldTetro = tmp;
			this.heldTetro.resetFigure();
			this.heldTetro.initPos(getStartPos());
		}
		this.canHold = false;
		updateShadow();
	}

	public void toggleShadow() {
		this.shadowEnabled = !this.shadowEnabled;
		
		if(this.shadowEnabled) {
			this.updateShadow();
		}
		this.requestFocus();
	}

	public void loadHighscore() throws IOException {
		String file_name = "highscore.dat";

		File file = new File(file_name);
		if (file.exists()) {
			FileInputStream fstream = new FileInputStream(file_name);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null) {
				String[] vars = strLine.split(":");
				String name = vars[0];

				int points = Integer.parseInt(vars[1]);
				int lines = Integer.parseInt(vars[2]);
				Highscore score = new Highscore(name, points, lines);
				this.highscore.add(score);
			}
		} else {
			createHighscore(file);
		}
	}

	protected void createHighscore(File file) throws IOException {
		file.createNewFile();

		FileWriter fstream = new FileWriter(file.getName());
		BufferedWriter out = new BufferedWriter(fstream);
		out.write("AAA:1000:10\n");
		out.write("AAA:500:5\n");
		out.write("AAA:300:3\n");
		out.write("AAA:200:2\n");
		out.write("AAA:100:1\n");
		out.close();

		loadHighscore();
	}

	protected void saveHighscore() throws IOException {
		File file = new File("highscore.dat");
		file.createNewFile();

		FileWriter fstream = new FileWriter(file.getName());
		BufferedWriter out = new BufferedWriter(fstream);
		for (Highscore hs : this.highscore) {
			out.write(hs.name + ":" + hs.score + ":" + hs.lines + "\n");
		}
		out.close();
	}

	protected void updateHighscore() {
		for (Highscore hs : this.highscore) {
			if (hs.score <= this.score) {
				this.showNameInput = true;
				break;
			}
		}

		if (this.highscore.size() == 0) {
			this.showNameInput = true;
		}
	}

	public void addHighscore() {
		Highscore nh = new Highscore(this.hsName.toUpperCase(), this.score, this.linesRemoved);
		this.highscore.add(nh);
		Collections.sort(this.highscore);
		if (this.highscore.size() > 5) {
			this.highscore.remove(this.highscore.size() - 1);
		}

		try {
			saveHighscore();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.showNameInput = false;
		this.hsName = "";
	}
	
	public static void main(String[] args) {		
		Tetris ts = new Tetris();
		ts.startGame(10, 20);
	}
}
