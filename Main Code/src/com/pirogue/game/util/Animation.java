package com.pirogue.game.util;

public class Animation extends org.newdawn.slick.Animation {
/* 
 * On modifie la classe Animation de slick afin d'y ajouter une m�thode getScaledCopy
 * qui simplifie �norm�ment le code de Hero.refreshAnimations
 */

	public Animation getScaledCopy(float width, float height) {
		/* 
		 * Retourne un nouvel objet Animation o� chaque frame est redimentionn�e � la bonne taille. 
		 */
		Animation newAnim = new Animation();
		for (int i=0; i<this.getFrameCount(); i++) {
			newAnim.addFrame(this.getImage(i).getScaledCopy((int)width, (int)height), this.getDuration(i));
		}
		return newAnim;
	}
	
}
