package com.pirogue.game.util;

import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.pirogue.game.Constants;

public class AnimationsContainer extends HashMap<String, Animations> {
/*
 * Cette classe est un conteneur qui fonctionne sur le principe de key/value (un peu comme les dictionnaires).
 * 
 * Si on utilise la m�thode loadAnimations(), les sprites seront charg�es comme des objets Animations (le 's'
 * est important cf. com.pirogue.game.util.Animations.java). La cl� permettant d'identifier un objet Animations
 * est le chemin vers l'image de sa spritesheet, en partant du dossier "src/assets/sprites", en rempla�ant les '/'
 * par des espaces et en enlevant l'extension.
 * Exemple - Pour obtenir les animations de l'image 'src/assets/sprites/heroes/rogue.png', il faut faire :
 * Animation[] anims = Constants.animations.get("heroes rogue");
 * 
 * Si on n'utilise pas cette m�thode, on peut utiliser un AnimationContainer simplement afin de mieux ranger des 
 * animations (voir exemple dans com.pirogue.entity.Hero.java)
 */
		
	public void loadAnimations(File currentDirectory) throws SlickException {
	/* Fonction r�currente qui permet de parcourir toute une arborescence de fichiers. */
		for (File file : currentDirectory.listFiles()) { // Pour chaque fichier du dossier que l'on regarde
			if (file.isDirectory()) // Si c'est un dossier, alors on regarde dans ce dossier (r�currence)
				loadAnimations(file);
			else { // Sinon, on charge l'image et on lui attribue une cl� unique  
				if (!(file.getPath().split("\\.")[file.getPath().split("\\.").length-1].equalsIgnoreCase("png"))) { // On teste l'extention au cas o� quelqu'un laisserait tra�ner un fichier .aesprite
					System.out.println("[ INFO ] Ignoring spritesheet '" + file.getPath() + "', only PNG images are allowed.");
					continue; // On skip ce tour de boucle
				}
				
				String key = String.join(" ", file.getPath().substring(19, file.getPath().indexOf(".")).split("\\\\")); // Format de la key
				int cellWidth = Constants.blockSize * (key.matches("heroes attack .*") ? 2:1); // Permet de g�rer des spritesheets qui ne sont pas 64x64
				int cellHeight = Constants.blockSize; // Si on a plus tard des spritesheets avec une hauteur plus grande (pour l'attaque vers le haut par exemple ?)
				SpriteSheet spritesheet = new SpriteSheet(file.getPath(), cellWidth, cellHeight);
				
				this.put(key, new Animations(spritesheet, getDuration(key)));
			}
		}
	}

	public Animations get(String key) {
	/* On remplace la m�thode get de HashMap pour �viter un crash si on demande une image inexistante */  
		if (key.matches(".*empty.*")) // TODO: Optimisation possible: retourner un null et v�rifier si c'est null avant d'afficher pour �viter d'afficher des images vides
			return super.get("debug empty");
		
		Animations anims = super.get(key);
		if (anims == null)
			return super.get("debug missing"); 
		return anims;
	}
	
	public int getDuration(String key) {
		/* Selon l'animation on veut des dur�es diff�rentes */
		if (key.matches(".*slime.*")) return 150;
		if (key.matches("heroes attack .*")) return 65;
		return 100;
	}
}
