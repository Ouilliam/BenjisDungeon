package com.pirogue.entity.projectiles;

import com.pirogue.entity.Projectile;
import com.pirogue.game.Constants;

public class Fireball extends Projectile {

	public Fireball(int x, int y, float angle, boolean fromHero) {
		super(x, y, angle, fromHero);
		this.velocity = Constants.fireBallSpeed;
		this.damages = Constants.fireBallDamages;
		this.collisionsEnabled=false;
	}
}
