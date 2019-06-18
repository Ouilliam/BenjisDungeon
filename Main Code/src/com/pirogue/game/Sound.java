package com.pirogue.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


public class Sound {
	
	private Slider[] volume;
	
	public Sound() throws SlickException{
		volume = new Slider[3];
		volume[0] = new Slider(Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT/2-150,Constants.SCREEN_WIDTH/2-80, Constants.SCREEN_HEIGHT/2-150,"Master");
		volume[1] = new Slider(Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT/2,Constants.SCREEN_WIDTH/2-80, Constants.SCREEN_HEIGHT/2,"Music");
		volume[2] = new Slider(Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT/2+150,Constants.SCREEN_WIDTH/2-80, Constants.SCREEN_HEIGHT/2+150,"Effects");
	}


	public void render(Graphics g) {
		g.setColor(new Color(0f,0f,0f,0.45f));
		g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		for (Slider slider : volume) {
			slider.render(g);
		}
	}
	
	public void update() {
		for (Slider slider : volume) {
			slider.update();
		}}
}
