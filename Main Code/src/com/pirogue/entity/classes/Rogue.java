package com.pirogue.entity.classes;

import org.newdawn.slick.SlickException;

import com.pirogue.entity.Hero;
import com.pirogue.items.Daggers;


public class Rogue extends Hero {

	public Rogue(int x, int y) throws SlickException {
		super(x, y, "rogue");
		this.inventory.objects[5] = new Daggers("Bloody_Daggers_common_",408,"bloody_daggers",8,4.2,0,0,1,0,0); // Commence avec une arme dans sa main droite
		refreshAnimations();
	}
}