package com.pirogue.game;

import org.newdawn.slick.Image;

public class Tile {
	/*
	 * Un Tile repr�sente un bloc sur une Map. Il contient une texture et 
	 * une image de collision, ce qui simplifie grandement le code.
	 */
	
	private Image texture;
	private Image collide;

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
	
	public boolean equals(Tile tile) {
		/* V�rifie si deux Tiles sont identiques, c'est � dire si elles ont la m�me texture et la m�me image de collision */
		if (this.texture==null && tile.getTexture()==null && this.collide==null && tile.getCollide()==null) return true;
		if (this.texture==null && tile.getTexture()==null) return this.collide.equals(tile.getCollide());
		if (this.collide==null && tile.getCollide()==null) return this.texture.equals(tile.getTexture());
		if (this.texture==null || tile.getTexture()==null || this.collide==null || tile.getCollide()==null) return false; // On veut �viter les NullPointerException
		return this.texture.equals(tile.getTexture()) && this.collide.equals(tile.getCollide());
	}
}
