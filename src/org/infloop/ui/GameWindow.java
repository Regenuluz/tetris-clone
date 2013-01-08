package org.infloop.ui;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.infloop.main.Tetris;



public class GameWindow {

	/**
	 * Creates a new game window
	 * 
	 * @param w
	 *            width in pixels
	 * @param h
	 *            heights in pixels
	 * @param ts
	 *            reference to the Tetris game
	 */
	public GameWindow(int w, int h, Tetris ts) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Tetris");

		// Create and set dimension of the game board
		Dimension d = new Dimension(w, h);
		ts.setPreferredSize(d);
		ts.setMinimumSize(d);
		ts.setMaximumSize(d);
		ts.setIgnoreRepaint(true);

		// Get focus
		ts.setFocusable(true);
		ts.requestFocus();

		/* Mac OS X only */
		if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}
		
		gameMenuBar mb = new gameMenuBar(ts);
		frame.setJMenuBar(mb);

		// Add the tetris game to the frame
		frame.getContentPane().add(ts);
		frame.add(ts);

		frame.pack();

		frame.setResizable(false);
		// Sets the frame location in the center of the screen
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
