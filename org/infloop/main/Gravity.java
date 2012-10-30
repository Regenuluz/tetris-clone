package org.infloop.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;


public class Gravity implements ActionListener {
	private final Tetris ts;
	private int timeStep;
	private Timer timer;
	private final int initTime;

	public Gravity(Tetris ts, int timeStep) {
		this.ts = ts;
		this.timeStep = timeStep;
		this.initTime = timeStep;
		this.timer = new Timer(timeStep, this);
	}

	public void restart() {
		this.timer.stop();
		this.timer = new Timer(this.timeStep, this);
		this.timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!this.ts.paused) {
			this.ts.moveDown();
		}
	}

	public void togglePause() {
		if (this.timer.isRunning()) {
			this.timer.stop();
		} else {
			this.timer.start();
		}
	}

	public void updateTime() {
		if (this.timeStep > 100) {
			this.timeStep -= 50;
		}
		this.timer.setDelay(this.timeStep);
	}

	public void reset() {
		this.timeStep = this.initTime;
		this.timer.stop();
		this.timer = new Timer(this.timeStep, this);
		this.timer.start();
	}

}
