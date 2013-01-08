package org.infloop.ui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.infloop.controllers.MenuEvents;
import org.infloop.main.Tetris;





public class gameMenuBar extends JMenuBar {

	private static final long serialVersionUID = 1L;

	private Tetris ts;
	public gameMenuBar(Tetris ts) {
		this.ts = ts;
		this.add(createGameMenu());
		this.add(createSettingsMenu());
	}
	
	private JMenu createGameMenu() {
		
		//Create menu item
		JMenu menu = new JMenu("Game");
		menu.setMnemonic(KeyEvent.VK_G); //G
		
		//Create sub menu item
		JMenuItem submenu = new JMenuItem("New Game");
		submenu.setActionCommand("NG");
		submenu.addActionListener(new MenuEvents(this.ts));
		submenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		menu.add(submenu);
		
		submenu = new JMenuItem("Pause");
		submenu.setActionCommand("P");
		submenu.addActionListener(new MenuEvents(this.ts));
		submenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
		menu.add(submenu);
		
		menu.addSeparator();
		
		submenu = new JMenuItem("Quit");
		submenu.setActionCommand("Q");
		submenu.addActionListener(new MenuEvents(this.ts));
		submenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
		
		menu.add(submenu);
		return menu;
	}
	
	private JMenu createSettingsMenu() {
		JMenu menu = new JMenu("Settings");
		
		JMenuItem submenu = new JCheckBoxMenuItem("Enable shadow");
		submenu.setSelected(true);
		submenu.setActionCommand("ES");
		submenu.addActionListener(new MenuEvents(this.ts));
		submenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		
		menu.add(submenu);
		return menu;
	}
}