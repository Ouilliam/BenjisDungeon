package com.pirogue.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.pirogue.game.Constants;
import com.pirogue.game.Inventory;
import com.pirogue.game.util.Animation;

public abstract class Hero extends Entity {
	
	public Inventory inventory;
	private String _class; // Classe du h�ros
	private Animation[][] equipmentAnims; // Animations de tous les �quipements (contient tous les 'calques')
	private Animation[][] inventoryAnims; // Animations � afficher dans l'inventaire (en gros c'est juste une copie de equipmentAnims en plus grand)
	private Animation[] attackAnims; // Animations de l'attaque (TODO: Quand on aura plusieurs attaques il faudra ajouter une dimention au tableau ou faire plusieurs variables genre 'autoAnims', 'ultiAnims', etc)
	private boolean isAttacking = false;
	
	public Hero(int x, int y, String _class) throws SlickException {
		super(x, y);
		this._class = _class;
		this.inventory = new Inventory();
		refreshAnimations();
	}
	
	public void render(Graphics g) {
		super.render(g, x+1, y+1);
		for (int n=0; n<6; n++) {
			if (isAttacking && inventory.objects[n].getType().equals("weapon")) { // Quand on attaque on affiche l'animation de chaque �quipement qui est une arme (TODO: G�rer les deux mains (en alternent entre chaque arme ?)
				g.drawAnimation(attackAnims[facing], (Constants.SCREEN_WIDTH-Constants.blockSize)/2 - (facing==1 ? Constants.blockSize:0), (Constants.SCREEN_HEIGHT-Constants.blockSize)/2); // On ajoute une condition sur le facing car il faut d�caler l'animation ou non selon la direction
				if (attackAnims[facing].isStopped()) { // Quand l'animation est finie, on peut � nouveau attaquer, il faut alors reset les animations
					isAttacking = false;
					for (int i=0; i<attackAnims.length; i++) attackAnims[i].restart(); 
				}
			}
			else
				g.drawAnimation(equipmentAnims[n][facing], (Constants.SCREEN_WIDTH-Constants.blockSize)/2, (Constants.SCREEN_HEIGHT-Constants.blockSize)/2);
		}
		if (inventory.isVisible()) {
			inventory.render(g, inventoryAnims, facing);
		}
	}

	protected void refreshAnimations() {
		/* 
		 * Cette fonction remplit les variables suivantes avec les animations ad�quates en fonction de
		 * l'�quipement du h�ros.
		 * 
		 * TODO: Ajouter des fonctions dans Animation.java pour tout simplifier
		 */
		inventoryAnims = new Animation[7][]; // Animations du h�ros + �quipements (pour l'inventaire)
		equipmentAnims = new Animation[6][]; // Animations des �quipements
		restAnims = Constants.animations.get("heroes " + _class); // Animations quand le h�ros ne se d�place pas
		movingAnims = Constants.animations.get("heroes " + _class); // Animations quand le h�ros se d�place
		attackAnims = Constants.animations.get("heroes attack " + inventory.objects[5].getAcces()); // inventory.equipment[5] correspond � l'arme dans la main droite (voir Inventory.java lignes 93 - 98)
		for (int n=0; n<attackAnims.length; n++) attackAnims[n].stopAt(attackAnims[n].getFrameCount()-1); // Les animations des attaques ne doivent pas tourner en boucle
		isAttacking = false; // Si jamais on change d'arme pendant une attaque
		
		// Pour l'inventaire on a besoin d'agrandir les animations du h�ros
		Animation[] tmp = new Animation[restAnims.length];
		for (int n=0; n<restAnims.length; n++) {
			tmp[n] = restAnims[n].getScaledCopy(inventory.getHeroCell().getWidth(), inventory.getHeroCell().getHeight());
		}
		inventoryAnims[0] = tmp; 
		
		for (int i=0; i<6; i++) {
			Animation[] originalAnims = Constants.animations.get("heroes equipment " + inventory.objects[i].getAcces());
			tmp = new Animation[originalAnims.length];
			for (int n=0; n<originalAnims.length; n++) {
				tmp[n] = originalAnims[n].getScaledCopy(inventory.getHeroCell().getWidth(), inventory.getHeroCell().getHeight());
			}
			equipmentAnims[i] = originalAnims;
			inventoryAnims[i+1] = tmp; 
		}
	}
	
	public void update(GameContainer container, int delta) {
		super.update(container, delta);
		if (!isAttacking) updateFacing(); // Quand on attaque on ne peut pas changer de direction
		if (this.inInventory()) {
			if (inventory.update())	refreshAnimations(); // inventory.update() renvoie true si jamais les �quipements ont �t� modifi�s
		}
	}
	
	protected void updateFacing() {		
		if (Constants.mouseX > Constants.SCREEN_WIDTH/2) // Facing d�pend de la souris
			facing=0;
		else 
			facing=1;
		
		/* Ancien code pour update facing avec les d�placements
		switch (moving) {
		case 0: break;
		case 1: facing=0; break;
		case 2: facing=0; break;
		case 3: facing=0; break;
		case 4: break;
		case 5: facing=1; break;
		case 6: facing=1; break;
		case 7: facing=1; break;
		}
		*/
	}
	
	public void toggleInventory() {
		inventory.setVisible(!inventory.isVisible());
		refreshAnimations(); // Permet de pr�venir un bug d'affichage si on quitte l'inventaire en tenant un objet avec la souris
	}
	
	public boolean inInventory() {
		return inventory.isVisible();
	}

	public void attack() {
		if (!inInventory())	this.isAttacking = true;		
	}
}
