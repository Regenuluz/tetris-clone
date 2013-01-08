package org.infloop.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.infloop.main.Tetris;



public class MenuEvents implements ActionListener {
	
	private final Tetris ts;
	
	public MenuEvents(Tetris ts) {
		this.ts = ts;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "Q") { //Quit
			System.exit(0);
		}
		else if(e.getActionCommand() == "NG") { //New game
			this.ts.restart();
		}
		else if(e.getActionCommand() == "P") { //Pause game
			this.ts.togglePause();
		} else if(e.getActionCommand() == "ES") { // Toggle shadow
			this.ts.toggleShadow();
		}
	}
}
