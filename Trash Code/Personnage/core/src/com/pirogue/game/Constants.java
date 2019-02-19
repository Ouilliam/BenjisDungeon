package com.pirogue.game;

public class Constants {
	public static int toPX = 32;     // 1 M�tre dans le jeu �quivaut � 32px,
	public static float toM = 1f/toPX;  // pour convertir on multiplie ou on divise 
	public static float RESOLUTION; // On ne peut pas la d�finir ici parce qu'elle d�pend de l'OS
	
	public static int MAP_WIDTH = 20;     // Largeur de la map
	public static int MAP_HEIGHT = 15;    // Hauteur de la map
	
	public static int MAP_WIDTHpx = MAP_WIDTH * toPX;
	public static int MAP_HEIGHTpx = MAP_HEIGHT * toPX;

	public static int CAMERA_WIDTH = toPX * 15; // Largeur de la cam�ra. On veut afficher 15 blocs en largeur.
	public static int CAMERA_HEIGHT; // Hauteur de la cam�ra, s'adapte en fonction de la largeur pour avoir une r�solution coh�rente
	public static int HERO_VELOCITY = 300;  // Vitesse du personnage en px/s (je crois)
}
