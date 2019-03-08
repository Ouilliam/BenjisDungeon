package com.pirogue.game;

import org.newdawn.slick.Image;

public class Tile {
	private Image texture; // Je pense qu'en faisant comme �a il peut y avoir des probl�mes de perf sur
	private Image collide; // des grandes maps, si c'est le cas on peut le r�gler easy j'ai d�j� une id�e

	public Tile() {
		this.texture = null;
		this.collide = null;
	}
	
	public Tile(Image texture, Image collide) {
		this.texture = texture;
		this.collide = collide;
	}
	
	public Image getTexture() {
		return texture;
	}
	
	public Image getCollide() {
		return collide;
	}
}
