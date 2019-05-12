package com.pirogue.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.pirogue.game.Constants;
import com.pirogue.game.util.Animation;

public abstract class Entity {

	protected float velocity = 0.5f; // TODO: Trouver une solution propre au probl�me de vitesse
	public int x,y, width,height;
	protected int facing; // Direction de l'entit�
	protected int moving = -1; // Direction du d�placement de l'entit� (-1 si on ne se d�place pas)
	protected Animation[] restAnims; // Animations quand on ne bouge pas
	protected Animation[] movingAnims; // Animations de d�placement
	protected boolean isColliding = false; // True si l'entit� est en collision avec un mur (TODO: Ajouter les collisions avec les autres entit�s)
	private boolean momentum = false; // True apr�s un d�placement quand l'entit� glisse un peu � vitesse r�duite

	public Entity(int x, int y) {
		this.x = x*Constants.blockSize;
		this.y = y*Constants.blockSize;
		this.width = Constants.blockSize-2;  // On enl�ve 2 pour pouvoir passer tranquillement dans les couloirs de 1 bloc de largeur
		this.height = Constants.blockSize-2; //
		this.restAnims = Constants.animations.get("debug default");
		this.movingAnims = Constants.animations.get("debug default");
	}

	public void render(Graphics g, int offsetX, int offsetY) {
		float X = this.x-offsetX + (Constants.SCREEN_WIDTH-Constants.blockSize)/2;  // Coordonn�es du coin sup�rieur gauche (en consid�rant que l'entit� a une longueur 
		float Y = this.y-offsetY + (Constants.SCREEN_HEIGHT-Constants.blockSize)/2; // et largeur d'une case enti�re et pas -2, ce qui permet de ne pas d�caler l'image)
		if (Constants.debug) { // Si on est en debug view, on affiche la hitbox
			g.setColor(new Color(1f, 1f, 1f));
			g.drawRect(this.x-offsetX +(Constants.SCREEN_WIDTH-this.width)/2 , this.y-offsetY +(Constants.SCREEN_HEIGHT-this.height)/2, this.width, this.height);
		}
		try {
			if (moving==-1)
				g.drawAnimation(restAnims[facing], X, Y);
			else
				g.drawAnimation(movingAnims[facing], X, Y);
		}
		catch (java.lang.NullPointerException | java.lang.IndexOutOfBoundsException e) { // Permet d'�viter de crash en cas d'erreur
			g.drawAnimation(Constants.animations.get("debug missing")[0], X, Y);
		}
	}

	public void update(GameContainer container, int delta) {
		if (moving!=-1) {
			int futureX = x;
			int futureY = y;
			int movement = Math.round(velocity * delta * (momentum ? 0.5f : 1f)); // Calcul du d�placement, en r�duisant la vitesse si l'entit� est entrain de glisser

			switch (moving) {
			case 0:	futureY -= movement; break;  // N
			case 2:	futureX += movement; break;  // E
			case 4:	futureY += movement; break;  // S
			case 6:	futureX -= movement; break;  // O
			case 1:	futureX += Math.round(movement * 0.707f); futureY -= Math.round(movement * 0.707f); break;  // NE
			case 3:	futureX += Math.round(movement * 0.707f); futureY += Math.round(movement * 0.707f); break;  // SE
			case 5:	futureX -= Math.round(movement * 0.707f); futureY += Math.round(movement * 0.707f); break;  // SO
			case 7:	futureX -= Math.round(movement * 0.707f); futureY -= Math.round(movement * 0.707f); break;  // NO
			}

			// Si on sort de la map ben en fait non (au cas o�)
			if (futureX<0) futureX=0;
			if (futureY<0) futureY=0;
			if (futureX>Constants.mapWidth*Constants.blockSize-width) futureX=Constants.mapWidth*Constants.blockSize-width;
			if (futureY>Constants.mapHeight*Constants.blockSize-height) futureY=Constants.mapHeight*Constants.blockSize-height;

			// V�rification des collisions
			// On num�rote les quatres coins de l'entit�e comme �a:
			// 0 | 1
			// -----
			// 2 | 3
			boolean[] corners = {false, false, false, false}; // Liste des coins � checker

			switch (moving) { // Pour all�ger on ne check que certains coins selon la direction (3 coins pour un d�placement diagonal, 2 sinon)
			case 0: corners[0]=true; corners[1]=true; break;
			case 1: corners[0]=true; corners[1]=true; corners[3]=true; break;
			case 2: corners[1]=true; corners[3]=true; break;
			case 3: corners[1]=true; corners[3]=true; corners[2]=true; break;
			case 4: corners[3]=true; corners[2]=true; break;
			case 5: corners[3]=true; corners[2]=true; corners[0]=true; break;
			case 6: corners[2]=true; corners[0]=true; break;
			case 7: corners[2]=true; corners[0]=true; corners[1]=true; break;
			}

			if (isColliding(corners, futureX, futureY)) {
				switch (moving) { // Quand on se d�place en diagonale on peut quand meme peut-�tre glisser sur un mur
                case 1: // Diagonnale haut droite
                    if (!isColliding(corners, this.x + movement, this.y)) // En d�placant vers 'E' au lieu de 'NE'  
                    	this.x = Math.round(this.x + movement);
                    else if (!isColliding(corners, this.x, this.y - movement))   // En d�placant vers 'N' au lieu de 'NE'
                    	this.y = Math.round(this.y - movement);
                    break;
                case 3: // Diagonnale bas droite
                    if (!isColliding(corners, this.x + movement, this.y))  // En d�placant vers 'E' au lieu de 'SE'
                    	this.x = Math.round(this.x + movement);
                    else if (!isColliding(corners, this.x, this.y + movement))  // En d�placant vers 'S' au lieu de 'SE'
                    	this.y = Math.round(this.y + movement);
                    break;
                case 5: // Diagonnale bas gauche
                    if (!isColliding(corners, this.x - movement, this.y))  // En d�placant vers 'O' au lieu de 'SO'
                    	this.x = Math.round(this.x - movement);
                    else if (!isColliding(corners, this.x, this.y + movement))  // En d�placant vers 'S' au lieu de 'SO'
                    	this.y = Math.round(this.y + movement);
                    break;
                case 7: // Diagonnale haut gauche
                    if (!isColliding(corners, this.x - movement, this.y))  // En d�placant vers 'O' au lieu de 'NO'
                    	this.x = Math.round(this.x - movement);
                    else if (!isColliding(corners, this.x, this.y - movement))  // En d�placant vers 'N' au lieu de 'NO'
                    	this.y = Math.round(this.y - movement);
                    break;
                }
			}
			else { // Si il n'y a pas de collision alors aucun probl�me
				this.x = futureX;
				this.y = futureY;
			}
		}
	}

	private boolean isColliding(boolean[] corners, int futureX, int futureY) {
		Image img;
		int cornerX=0, cornerY=0;

		for (int i=0; i<4; i++) { // Pour chaque coin
			if (corners[i]) { // Si c'est un coin � checker
				// Selon le coin on r�cup�re ses coordonn�es
				switch (i) {
				case 0:
					cornerX = futureX-width/2;
					cornerY = futureY-height/2;
					break;
				case 1:
					cornerX = futureX+width/2;
					cornerY = futureY-height/2;
					break;
				case 2:
					cornerX = futureX-width/2;
					cornerY = futureY+height/2;
					break;
				case 3:
					cornerX = futureX+width/2;
					cornerY = futureY+height/2;
					break;
				}
				
				// On r�cup�re l'image de collision de la Tile sur laquelle se trouve le coin 
				img = Constants.dungeon.getCurrentFloor().getCollideImage((int)(cornerX/Constants.blockSize), (int)(cornerY/Constants.blockSize));
				if (img != null) {
					// On r�cup�re la couleur du pixel sur lequel se trouve le coin
					Color color = img.getColor((int)(cornerX % Constants.blockSize), (int)(cornerY % Constants.blockSize));
					if (color.getRed()==255 && color.getGreen()==0 && color.getBlue()==0) {
						return true; // Si c'est du rouge alors il y a collision
					}
				}
				else return true; // Si l'image est null (pour du vide par exemple), on ne peut pas marcher dessus
			}
		}
		return false; // Si pour tous les coins � v�rifier il n'y a pas de collision alors c'est bon
	}

	protected abstract void refreshAnimations();

	public void setFacing(int facing) {
		this.facing = facing;
	}

	public void setMoving(int moving) {
		this.moving = moving;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
	
	public void setMomentum(boolean momentum) {
		this.momentum = momentum;
	}
}
