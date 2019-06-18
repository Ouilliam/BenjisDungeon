package com.pirogue.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Settings {
	
	private Button[] settings;
	
	public Settings() throws SlickException{
		
		settings = new Button[3];
		
		settings[0] = new Button(Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT/2-150, "assets/gui/buttons/Sound.png", "sound");
		settings[1] = new Button(Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT/2, "assets/gui/buttons/Commands.png", "commands");
		settings[2] = new Button(Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT/2+150, "assets/gui/buttons/Return.png", "return_settings");
		
}
	
	public void render(Graphics g) {
		g.setColor(new Color(0f,0f,0f,0.45f));
		g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		for (Button button : settings) {
			button.render(g);
		}
	}
	
	public void update() {
		for (Button button : settings) {
			button.update();
		}}}
	
	
	
	// TODO Auto-generated constructor stub

	
	
	
	

	/*
	 * TODO : Faire une page de r�glage un peu dans le m�me style que dans Menu.java et l'impl�menter
	 * Le bouton dans le menu est d�j� fait il faut juste le link (voir Button.java)
	 * 
	 * Pour l'instant les boutons sont faits de telle sorte que ce soit forc�ment une image mais on peut changer �a
	 * 
	 * 
	 * 
	 * Pro tip pour agrandir du texte:
	 * Dans la m�thode render(Graphics g)
	 * 		g.scale(2f, 2f); // On change l'�chelle du graphics
	 * 		g.drawString("le pain", x, y); // Attention aux coordonn�es qui sont modifi�es parce qu'on a chang� l'�chelle
	 * 		g.scale(0.5f, 0.5f); // Et on la remet
	 */
	

