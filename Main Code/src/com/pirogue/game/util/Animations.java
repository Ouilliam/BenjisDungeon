package com.pirogue.game.util;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

public class Animations {
/* 
 * Dans notre jeu on g�re les animations d'une mani�re group�e, parce que pour chaque chose que l'on veut animer
 * il faut pr�voir une anipation par direction. Afin de simplifier de le code, la classe Animations (le s � la
 * fin est important) regroupe donc toutes les animations pour chaque direction. Une spritesheet est charg�e selon
 * le sch�ma suivant:
 * Soit un sprite avec n directions de personnages (on esp�re en avoir plus de 2 un jour...) et p frames.
 * 
 *         C1   C2   ..   Cp
 *       ---------------------
 *       |    |    |    |    |
 *  L1   |    Animation 1    |
 *       |    |    |    |    |
 *       ---------------------
 *   .   |    |    |    |    |
 *   .   |    |  .....  |    |
 *   .   |    |    |    |    |
 *       ---------------------
 *       |    |    |    |    |
 *  Ln   |    Animation n    |
 *       |    |    |    |    |
 *       ---------------------
 * 
 * Le tableau d'animations correspondant aura donc n animations � p frames.
 * Si p=1 cela ne pose pas de probl�mes, une seule image sera alors affich�e, c'est pour cette raison
 * que l'on peut charger un sprite comme 'assets/sprites/heroes/equipment/bloody_daggers.png' de la m�me fa�on
 * que 'assets/sprites/heroes/equipment/basic_daggers.png' et surtout l'afficher de la m�me mani�re.
 */
	
	
	/*			Animation anim = new Animation();
	for (int i=0; i<spritesheet.getHorizontalCount(); i++) {
		anim.addFrame(spritesheet.getSprite(i, j), duration);
	}
	anims[j] = anim;*/

	private Animation[] anims;

	public Animations(Animation[] anims) {
		this.anims = anims;
	}

	public Animations(SpriteSheet spritesheet, int duration) {
		anims = new Animation[spritesheet.getVerticalCount()]; // Une animation par ligne de spritesheet
		for (int y=0; y<spritesheet.getVerticalCount(); y++) {
			anims[y] = new Animation(spritesheet, 0, y, spritesheet.getHorizontalCount()-1, y, true, duration, true);
		}
	}
	
	public Animations getScaledCopy(float width, float height) {
		/* Retourne un nouvel objet Animations o� chaque animations est redimentionn�e � la bonne taille. */
		Animation[] newAnims = new Animation[anims.length];

		for (int j=0; j<anims.length; j++) {
			newAnims[j] = new Animation();
			for (int i=0; i<anims[j].getFrameCount(); i++) {
				newAnims[j].addFrame(anims[j].getImage(i).getScaledCopy((int)width, (int)height), anims[j].getDuration(i));
			}
		}
		return new Animations(newAnims);
	}
	
	public Animation get(int index) {
		return anims[index];
	}
	
	public void setPlayOnce() {
		for (int n=0; n<anims.length; n++)
			anims[n].stopAt(anims[n].getFrameCount()-1);
	}
	
	public void setPingPong() {
		for (int n=0; n<anims.length; n++)
			anims[n].setPingPong(true);
	}
	
	public void restartAll() {
		for (int i=0; i<anims.length; i++)
			anims[i].restart();
	}
}
