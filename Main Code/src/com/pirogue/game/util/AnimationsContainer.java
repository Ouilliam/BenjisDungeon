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
 * animations (voir exemple dans com.pirogue.entity.Hero)
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
				int[] cellSize = getCellSize(key);
				SpriteSheet spritesheet = new SpriteSheet(file.getPath(), cellSize[0], cellSize[1]);
				
				Animations anims = new Animations(spritesheet, getDuration(key), getDamageFrame(key));
				setModifiers(anims, key);
				this.put(key, anims);
			}
		}
	}

	public Animations get(String key) {
	/* On remplace la m�thode get de HashMap pour �viter un crash si on demande une image inexistante */  
		if (key.matches(".*empty.*")) // TODO: Optimisation possible: retourner un null et v�rifier si c'est null avant d'afficher pour �viter d'afficher des images vides
			return super.get("debug empty");
		
		Animations anims = super.get(key);
		if (anims == null) {
			return Constants.animations.get("debug missing");
		} 
		return anims;
	}
	
	private int[] getCellSize(String key) {
		/* Selon l'image, on veut des tailles diff�rentes */
		int[] cellSize = {Constants.blockSize, Constants.blockSize};
		if (key.matches("heroes attack .*")) cellSize[0] *= 2;
		if (key.matches("debug life_bar")) {
			cellSize[0] = 142;
			cellSize[1] = 25;
		}
		if (key.matches("mobs slime .*attack.*")) {
			cellSize[0] *= 3;
			cellSize[1] *= 3;
		}
		return cellSize;
	}
	
	private int getDuration(String key) {
		/* Selon l'animation, on veut des dur�es diff�rentes */
		if (key.matches(".* death .*")) return 100;
		if (key.matches(".* slime .*")) return 200;
		if (key.matches("heroes attack .*")) return 50;
		if (key.matches("heroes equipment .*")) return 100;
		if (key.matches("heroes equipment .*")) return 100;
		if (key.matches("heroes equipment .*")) return 100;
		if (key.matches("heroes rest .*")) return 500;
		if (key.matches("heroes moving .*")) return 100;
		return 100;
	}
	
	private int getDamageFrame(String key) {
		/* Selon l'animation, la damageFrame change (voir Animations.java) */
		if (!key.matches(".* attack .*")) return 0;
		if (key.matches(".* auto .*daggers.*")) return 3;
		if (key.matches(".* poison .*daggers.*")) return 4;
		if (key.matches(".*slime.*")) return 5;
		return 0;
	}
	
	private void setModifiers(Animations anims, String key) {
		/* Selon l'animation, on veut qu'elle se joue en boucle ou une seule fois etc */
		if (key.matches(".* moving .*")) anims.setPingPong();
		if (key.matches(".* attack .*")) anims.setPlayOnce();
		if (key.matches(".* death.*")) anims.setPlayOnce();
	}
}
