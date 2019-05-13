package com.pirogue.game;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import com.pirogue.game.util.Animations;
import com.pirogue.items.EmptyItem;
import com.pirogue.items.Item;


public class Inventory {
	/*
	 * Les cases de l'inventaire sont d�tect�es directement sur l'image cellsImg, et un Rectangle est cr�� pour chaque case
	 * dans la variable cells, avec un Item qui lui est associ� dans la variable objects. Les 6 premi�res valeurs des tableaux
	 * cells et objects sont r�serv�es aux 6 pi�ces d'�quipement selon l'ordre suivant :
	 *   0: Head
	 *   1: Chest
	 *   2: Legs
	 *   3: Foots
	 *   4: Left Hand
	 *   5: Right Hand
	 */

	private int backgroundX, backgroundY; // Coordonn�es du coin haut gauche du background
	private Image background, cellsImg, selected;
	private ArrayList<Rectangle> cells = new ArrayList<Rectangle>();
	private Rectangle heroCell;

	public int inventorySize=0; // Nombre de cases disponibles dans l'inventaire, calcul� dans loadCells() en fonction de cellsImg
	private boolean isVisible = false;
	public Item[] objects;
	public Item selectedItem = new EmptyItem();
	private int selectedItemIndex = -1;
	private boolean mouseWasPressed = false;
		
	public Inventory() throws SlickException {
		this.cellsImg = new Image(Constants.inventoryCells); // Image sur laquelle se trouvent les cases � d�tecter
		this.background = new Image(Constants.inventoryBackground); // Image de fond � afficher directement
		this.selected = new Image(Constants.inventorySelected); // Image � afficher quand la souris passe sur une case
		this.backgroundX = (Constants.SCREEN_WIDTH-background.getWidth())/2;
		this.backgroundY = (Constants.SCREEN_HEIGHT-background.getHeight())/2;		
		
		loadCells();
		objects = new Item[inventorySize+6];
		for (int n=0; n<inventorySize+6; n++) {
			objects[n] = new EmptyItem();
		}
	}
	
	public boolean update() {
		/* Cette m�thode retourne true si il faut refresh les animations (i.e. si l'�quipement a �t� modifi�), false sinon */
		Input input = Constants.container.getInput(); // Permet de r�cup�rer les inputs clavier (pour le shift + click)
		boolean refresh = false;
		
		int n=0;
		for (Rectangle rect : cells) { // Pour chaque rectangle on regarde si la souris est dedans
			if (rect.contains(Constants.mouseX, Constants.mouseY)) { // Si la souris est dedans, alors la variable n sera l'indice de l'objet sur lequel on a cliqu�
				if (input.isKeyDown(Input.KEY_LSHIFT) && Constants.mousePressed && !mouseWasPressed) { // ----- Shift + click
					int destination;
					for (destination=6; destination<inventorySize+6; destination++) { // Par d�faut, on choisit la premi�re case vide de l'inventaire
						if (objects[destination] instanceof EmptyItem) break;
					}
					if (n>=6) {
						switch (objects[n].getType()) { // Si c'est un �quipement, on prend la case appropri�e
						case "weapon": destination = (!(objects[5] instanceof EmptyItem) && objects[4] instanceof EmptyItem ? 4 : 5); break; // On choisit dans quelle main mettre l'arme selon les armes d�j� pr�sentes
						case "head":   destination = 0; break;
						case "chest":  destination = 1; break;
						case "legs":   destination = 2; break;
						case "boots":  destination = 3; break;
						}
					}
					
					Item tmp = objects[n];
					objects[n] = objects[destination];
					objects[destination] = tmp;				
					mouseWasPressed = Constants.mousePressed;
					return n<6 || destination<6;
				}
				else if (Constants.mousePressed && !mouseWasPressed && selectedItemIndex==-1) { // ------------ Drag
					if (!(objects[n] instanceof EmptyItem)) { // Si il y a un objet dans la case, on l'attrappe
						selectedItem = objects[n];
						selectedItemIndex = n;
						objects[n] = new EmptyItem();
						mouseWasPressed = Constants.mousePressed;
						return n<6;
					}
				}
				else if (!Constants.mousePressed && mouseWasPressed && selectedItemIndex!=-1) { // ------------ Drop
					if (n==selectedItemIndex) { // Si c'est sur la m�me case alors on remet juste l'item dans sa case
						objects[selectedItemIndex] = selectedItem;
						selectedItem = new EmptyItem();
						refresh = n<6 || selectedItemIndex<6; // On refresh seulement si on a touch� aux �quipements
						selectedItemIndex = -1;
						mouseWasPressed = Constants.mousePressed;
						return refresh;
					}
					else { // Sinon on �change les items
						Item tmp = objects[n];
						objects[n] = selectedItem;
						objects[selectedItemIndex] = tmp;
						selectedItem = new EmptyItem();
						refresh = n<6 || selectedItemIndex<6;
						selectedItemIndex = -1;
						mouseWasPressed = Constants.mousePressed;
						return refresh;
					}
				}
			}
			n++;
		}
		
		if (!Constants.mousePressed && mouseWasPressed && selectedItemIndex!=-1) { // Si on rel�che la souris sur aucune case, on remet l'item dans sa case d'origine
			objects[selectedItemIndex] = selectedItem;
			selectedItem = new EmptyItem();
			refresh = selectedItemIndex<6;
			selectedItemIndex=-1;
		}
		mouseWasPressed = Constants.mousePressed;
		return refresh;
	}
	
	public void render(Graphics g, int facing, Animations body, Animations head, Animations chestplate, Animations legs, Animations foots, Animations leftHand, Animations rightHand) {
		// Affichage du background //
		g.drawImage(this.background, backgroundX, backgroundY);
		
		// Affichage de la case sur laquelle se trouve la souris //
		int n=0;
		for (Rectangle rect : cells) {
			g.drawImage(objects[n].getTexture(), rect.getMinX(), rect.getMinY());
			if (rect.contains(Constants.mouseX, Constants.mouseY)) {
				g.drawImage(selected, rect.getMinX(), rect.getMinY());
			}
			n++;
		}
		
		// Affichage du h�ros avec ses �quipements //
		g.drawAnimation(body.get(facing), heroCell.getMinX(), heroCell.getMinY());
		g.drawAnimation(head.get(facing), heroCell.getMinX(), heroCell.getMinY());
		g.drawAnimation(chestplate.get(facing), heroCell.getMinX(), heroCell.getMinY());
		g.drawAnimation(legs.get(facing), heroCell.getMinX(), heroCell.getMinY());
		g.drawAnimation(foots.get(facing), heroCell.getMinX(), heroCell.getMinY());
		g.drawAnimation(leftHand.get(facing), heroCell.getMinX(), heroCell.getMinY());
		g.drawAnimation(rightHand.get(facing), heroCell.getMinX(), heroCell.getMinY());
		
		// Affichage de l'item selectionn�
		if (!(selectedItem instanceof EmptyItem)) {
			Image texture = selectedItem.getTexture();
			g.drawImage(texture, Constants.mouseX-texture.getWidth()/2, Constants.mouseY-texture.getHeight()/2);
		}
	}
	
	private void loadCells() {
		for (int i=0; i<6; i++) cells.add(new Rectangle(0, 0, 0, 0)); // On met 6 cases vides au d�but de la liste pour les 6 �quipements
		
		for (int j=0; j<cellsImg.getHeight(); j++) { 
		for (int i=0; i<cellsImg.getWidth(); i++) {
			Color pixel = cellsImg.getColor(i, j); // On r�cup�re la couleur de chaque pixel
			if (!pixel.equals(Color.white)) { // On ne s'int�resse qu'aux pixels color�s (non blancs)
				int cellWidth=0, cellHeight=0;
				for (int x=i; x<cellsImg.getWidth(); x++) {
					if (cellsImg.getColor(x, j).equals(pixel)) cellWidth++;
					else break;
				}
				for (int y=j; y<cellsImg.getHeight(); y++) {
					if (cellsImg.getColor(i, y).equals(pixel)) cellHeight++;
					else break;
				}
				if (cellWidth==Constants.cellSize && cellHeight==Constants.cellSize) { // Si la case d�tect�e est valide
					Rectangle rect = new Rectangle(i+backgroundX , j+backgroundY, cellWidth, cellHeight); // On cr�� un rectangle correspondant � cette case
					// Selon la couleur de la case, on lui donne un r�le diff�rent
					if (pixel.equals(Constants.invCell)) {
						cells.add(rect);
						this.inventorySize++;
					}
					else if (pixel.equals(Constants.head)) cells.set(0, rect);
					else if (pixel.equals(Constants.chest)) cells.set(1, rect);
					else if (pixel.equals(Constants.legs)) cells.set(2, rect);
					else if (pixel.equals(Constants.foots)) cells.set(3, rect);
					else if (pixel.equals(Constants.leftHand)) cells.set(4, rect);
					else if (pixel.equals(Constants.rightHand)) cells.set(5, rect);
				}
				else if (cellWidth==cellHeight && pixel.equals(Constants.heroCell) && this.heroCell == null) { // V�rification de l'emplacement pour afficher le h�ros
					this.heroCell = new Rectangle(i+backgroundX , j+backgroundY, cellWidth, cellHeight);
				}
			}
		}}
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		if (selectedItemIndex!=-1) { // Si on a s�lectionn� un item, on le remet � sa place
			objects[selectedItemIndex] = selectedItem;
			selectedItem = new EmptyItem();
			selectedItemIndex = -1;
		}
	}
	
	public int getSize() {
		return this.inventorySize;
	}
	
	public Rectangle getHeroCell() {
		return heroCell;
	}
}
