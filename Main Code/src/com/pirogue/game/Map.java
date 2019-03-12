package com.pirogue.game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Map {
	
	public boolean vision; // Permet d'afficher les collisions pour debug (touche A)
	
	protected int width, height;
	protected float blockSize;
	protected boolean grille[][];
	protected Tile Blocks[][];
	private int salleX=0, salleY=0, tailleSalleX=0, tailleSalleY=0;
	public int spawnX,spawnY;
	 

	public Map(int width, int height) {
		this.width = width;
		this.height = height;
		this.Blocks = new Tile[width][height];
		this.grille = new boolean[width][height];
		this.blockSize = Constants.blockSize;
		
		for(int i=0; i<width; i++) {
			for(int j=0; j<height; j++) {
				this.grille[i][j] = false;		//initialisation des 2 tableaux
				this.Blocks[i][j] = new Tile(Constants.spritesheet.getSprite(3, 1),Constants.collidesheet.getSprite(3, 1)); //"Vide";
			}
		}

		for(int i=0; i<Constants.roomsRatio; i++) { //nombre de salles g�n�r�es
			this.generate();
		}
		this.scanBlock();
	}
	
	public void generate() {										//g�n�ration des salles
		this.salleX = 10 + (int)(Math.random() * (((width-10) - 10) + 1));
		this.salleY = 10 + (int)(Math.random() * (((height-10) - 10) + 1));//nb al�atoire entre 10 et height-10 (140)
		this.tailleSalleX = 3 + (int)(Math.random() * ((8 - 3) + 1));
		this.tailleSalleY = 3 + (int)(Math.random() * ((8 - 3) + 1));//nb al�atoire entre 3 et 8
	
		int test = 0;
		for(int i=this.salleX-this.tailleSalleX; i<=this.salleX+this.tailleSalleX; i++) {
			for(int j=this.salleY-this.tailleSalleY; j<=this.salleY+this.tailleSalleY; j++) {
				if(this.grille[i][j] == true) {
					test++;					//on compte le nombre de blocks deja occup�s
				}
			}
		}
	
		if(test <= 50) {				//tol�rance du nombre de blocks d�ja occup�s
		for(int i=this.salleX-this.tailleSalleX; i<=this.salleX+this.tailleSalleX; i++) {
			for(int j=this.salleY-this.tailleSalleY; j<=this.salleY+this.tailleSalleY; j++) {
					this.grille[i][j] = true;			//g�n�ration de la salle si les conditions sont ok
					this.spawnX = this.salleX;	//Coordonn�es de spawn du h�ros au milieu de la salle
					this.spawnY = this.salleY;	//
				}									//sinon RIEN !
			}
		}
	}

	public void scanBlock() {
		Tile Droite = new Tile(Constants.spritesheet.getSprite(0, 1),Constants.collidesheet.getSprite(0,1)), Gauche = new Tile(Constants.spritesheet.getSprite(1, 1),Constants.collidesheet.getSprite(1,1)), Bas = new Tile(Constants.spritesheet.getSprite(1, 0),Constants.collidesheet.getSprite(1,0)), Haut = new Tile(Constants.spritesheet.getSprite(0, 0),Constants.collidesheet.getSprite(0,0));
		for(int i=1; i<width-1; i++) {
			for(int j=1; j<height-1; j++) {
				
				//test Block g�n�r�: cela permet de savoir quel type de block afficher
				if(this.grille[i][j] == true) {
					if(this.grille[i-1][j-1] == true && this.grille[i-1][j] == true && this.grille[i-1][j+1] == true && this.grille[i][j+1] == true && this.grille[i+1][j+1] == true && this.grille[i+1][j] == true && this.grille[i+1][j-1] == true && this.grille[i][j-1] == true) {
						this.Blocks[i][j] = new Tile(Constants.spritesheet.getSprite(3, 0),Constants.collidesheet.getSprite(3,0));//"Sol"; ok
					}
					if(this.grille[i-1][j] == false && this.grille[i][j+1] == true && this.grille[i+1][j] == true && this.grille[i][j-1] == true) {
						if(this.Blocks[i][j-1].equals(Droite)) {
							this.Blocks[i][j-1] = new Tile(Constants.spritesheet.getSprite(0, 2),Constants.collidesheet.getSprite(0,2));//coin bas droite
							this.Blocks[i][j] = new Tile(Constants.spritesheet.getSprite(1, 3),Constants.collidesheet.getSprite(1,3));//coin haut gauche
						}
						else {
							this.Blocks[i][j] = Gauche;//"Gauche"; ok
						}	 
					}
					if(this.grille[i-1][j] == true && this.grille[i][j+1] == false && this.grille[i+1][j] == true && this.grille[i][j-1] == true) {
						if(this.Blocks[i-1][j].equals(Haut)) {
							this.Blocks[i-1][j] = new Tile(Constants.spritesheet.getSprite(0, 2),Constants.collidesheet.getSprite(0,2));//coin bas droite
							this.Blocks[i][j] = new Tile(Constants.spritesheet.getSprite(1, 3),Constants.collidesheet.getSprite(1,3));//coin haut gauche
						}
						else {
							this.Blocks[i][j] = Bas;//"Bas"; ok 
						}
					}
					if(this.grille[i-1][j] == true && this.grille[i][j+1] == true && this.grille[i+1][j] == false && this.grille[i][j-1] == true) {
						if(this.Blocks[i][j-1].equals(Gauche)) {
							this.Blocks[i][j-1] = new Tile(Constants.spritesheet.getSprite(1, 2),Constants.collidesheet.getSprite(1,2));//coin bas gauche
							this.Blocks[i][j] = new Tile(Constants.spritesheet.getSprite(0, 3),Constants.collidesheet.getSprite(0,3));//coin haut droite
						}
						else {
							this.Blocks[i][j] = Droite;//"Droite"; ok 
						}
					}
					if(this.grille[i-1][j] == true && this.grille[i][j+1] == true && this.grille[i+1][j] == true && this.grille[i][j-1] == false) {
						if(this.Blocks[i-1][j].equals(Bas)) {
							this.Blocks[i-1][j] = new Tile(Constants.spritesheet.getSprite(1, 2),Constants.collidesheet.getSprite(1,2));//coin bas gauche
							this.Blocks[i][j] = new Tile(Constants.spritesheet.getSprite(0, 3),Constants.collidesheet.getSprite(0,3));//coin haut droite
						}
						else {
							this.Blocks[i][j] = Haut;//"Haut"; ok 
						}
					}
					if(this.grille[i-1][j] == false && this.grille[i][j+1] == true && this.grille[i+1][j+1] == true && this.grille[i+1][j] == true && this.grille[i][j-1] == false) {
						this.Blocks[i][j] = new Tile(Constants.spritesheet.getSprite(1, 3),Constants.collidesheet.getSprite(1,3));//"Coin-Haut-Gauche"; ok
					}
					if(this.grille[i-1][j] == false && this.grille[i][j+1] == false && this.grille[i+1][j] == true && this.grille[i+1][j-1] == true && this.grille[i][j-1] == true) {
						this.Blocks[i][j] = new Tile(Constants.spritesheet.getSprite(1, 2),Constants.collidesheet.getSprite(1,2));//"Coin-Bas-Gauche"; ok
					}
					if(this.grille[i-1][j-1] == true && this.grille[i-1][j] == true && this.grille[i][j+1] == false && this.grille[i+1][j] == false && this.grille[i][j-1] == true) {
						this.Blocks[i][j] = new Tile(Constants.spritesheet.getSprite(0, 2),Constants.collidesheet.getSprite(0,2));//"Coin-Bas-Droite"; ok
					}
					if(this.grille[i-1][j] == true && this.grille[i-1][j+1] == true && this.grille[i][j+1] == true && this.grille[i+1][j] == false && this.grille[i][j-1] == false) {
						this.Blocks[i][j] = new Tile(Constants.spritesheet.getSprite(0, 3),Constants.collidesheet.getSprite(0,3));//"Coin-Haut-Droite"; ok
					}
					if(this.grille[i-1][j-1] == false && this.grille[i-1][j] == true && this.grille[i-1][j+1] == true && this.grille[i][j+1] == true && this.grille[i+1][j] == true && this.grille[i+1][j-1] == true && this.grille[i][j-1] == true) {
						this.Blocks[i][j] = new Tile(Constants.spritesheet.getSprite(3, 3),Constants.collidesheet.getSprite(3,3));//"Angle-Haut-Gauche"; ok
					}
					if(this.grille[i-1][j-1] == true && this.grille[i-1][j] == true && this.grille[i-1][j+1] == false && this.grille[i][j+1] == true && this.grille[i+1][j+1] == true && this.grille[i+1][j] == true && this.grille[i][j-1] == true) {
						this.Blocks[i][j] = new Tile(Constants.spritesheet.getSprite(3, 2),Constants.collidesheet.getSprite(3,2));//"Angle-Haut-Droite"; ok
					}
					if(this.grille[i-1][j] == true && this.grille[i-1][j+1] == true && this.grille[i][j+1] == true && this.grille[i+1][j+1] == false && this.grille[i+1][j] == true && this.grille[i+1][j-1] == true && this.grille[i][j-1] == true) {
						this.Blocks[i][j] = new Tile(Constants.spritesheet.getSprite(2, 2),Constants.collidesheet.getSprite(2,2));//"Angle-Bas-Droite"; ok
					}
					if(this.grille[i-1][j-1] == true && this.grille[i-1][j] == true && this.grille[i][j+1] == true && this.grille[i+1][j+1] == true && this.grille[i+1][j] == true && this.grille[i+1][j-1] == false && this.grille[i][j-1] == true) {
						this.Blocks[i][j] = new Tile(Constants.spritesheet.getSprite(2, 3),Constants.collidesheet.getSprite(2,3));//"Angle-Bas-Gauche"; ok
					}	
				}
			}
		}
	}



	public void render(Graphics g) {
		render(g, 0,0);
	}
	
	public Image getTileImage(int x, int y) {
		return Blocks[x][y].getTexture();
	}

	public Image getCollideImage(int x, int y) {
		return Blocks[x][y].getCollide();
	}

	// J'ai pas trop le temps de tester le truc des offset et je me suis peut-�tre fail quelque part
	public void render(Graphics g, float offsetX, float offsetY) {
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				Image texture;
				if (this.vision) texture = Blocks[x][y].getCollide();
				else texture = Blocks[x][y].getTexture();
				
				if (texture != null) {
					g.drawImage(texture, x*blockSize-offsetX, y*blockSize-offsetY);
				}
			}
		}
	}
}