package org.infloop.main;

public class Highscore implements Comparable<Highscore>{
	public String name;
	public int score;
	public int lines;
	
	public Highscore(String name, int score, int lines) {
		this.name = name;
		this.score = score;
		this.lines = lines;
	}

	@Override
	public int compareTo(Highscore hs) {
		if(this.score > hs.score) {
			return -1;
		} else if(this.score < hs.score) {
			return 1;
		}
		
		if(this.lines > hs.lines) {
			return -1;
		} else if(this.lines < hs.lines) {
			return 1;
		}
		
		return 0;
	}
}
