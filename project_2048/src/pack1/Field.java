package pack1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import KIs.MaxMaxKI;
import KIs.RandomKI;

public class Field implements Serializable{
	/* Hauptklasse des Spiels / Controller des Projektes
	 * beinhaltet alle f�r das Spiel wichtigen MEthoden und Schnittstellen
	 * */
	private boolean moved =false; //wird zu �berbr�fung, ob Rekursion notwendig benutzt
	private int tileCount = 1;	//Kontrollvariable, dass nicht versucht wird Tile zu generieren, wenn kein Platz mehr ist
	public final int X=8; //vertikale Gr��e des Spielfeldes
	public final int Y=8;	//horizontale Gr��e des Spielfeldes
	private short numOfRec = 0;
	private boolean lost = false;
	public int greatestTile = 2;
	public int score=0;
	public boolean main=true;
	RandomKI rKI = new RandomKI();
	public Tile[][] field = new Tile[Y][X]; //Spielfeld an sich besteht aus einem Feld von Tiles
		//KI zur Erzeugung neuer Tiles
	
	
	Field() {
		for(int h=0; h<4; h++) {
			for(int b=0;b<4;b++) {
				field[h][b]=new Tile(0);
			}
		}
		//Standardkonstruktor, kein anderer ben�tigt, da Ausgangssituation immer gleich
		field[3][3].setPotency(1);
	}
	public Field(Tile[][] field,boolean moved,int tileCount,short numOfRec,boolean lost,int greatestTile,int score,boolean main) {
		this.field=field;
		this.moved=moved;
		this.tileCount=tileCount;
		this.numOfRec=numOfRec;
		this.lost=lost;
		this.greatestTile=greatestTile;
		this.score=score;
		this.main=main;
	}
	public Field clone() {
		Tile[][] field2= new Tile[Y][X];
		for(int h=0; h<Y; h++) {
			for(int b=0;b<X;b++) {
				field2[h][b]=new Tile(0);
			}
		}
		for(int h=0; h<Y; h++) {
			for(int b=0;b<X;b++) {
				field2[h][b].setPotency(field[h][b].getPotency());
			}
		}
		boolean temp;
		if(moved)
			temp=true;
		else
			temp=false;
		boolean moved2 = temp; 
		int tileCount2 = this.getTileCount();	
		short numOfRec2 =numOfRec;
		if(lost)
			temp=true;
		else
			temp=false;
		boolean lost2 = temp;
		int greatestTile2 = this.getGreatestTile();
		int score2= this.getScore();
		if(main)
			temp=true;
		else
			temp=false;
		boolean main2=temp;
		return new Field(field2,moved2,tileCount2,numOfRec2,lost2,greatestTile2,score2,main2);
	}
	public boolean isMoved() {
		return moved;
	}
	public void setMoved(boolean moved) {
		this.moved = moved;
	}
	public int getTileCount() {
		return tileCount;
	}
	public void setTileCount(int tileCount) {
		this.tileCount = tileCount;
	}
	public short getNumOfRec() {
		return numOfRec;
	}
	public void setNumOfRec(short numOfRec) {
		this.numOfRec = numOfRec;
	}
	public boolean isLost() {
		return lost;
	}
	public void setLost(boolean lost) {
		this.lost = lost;
	}
	public int getGreatestTile() {
		return greatestTile;
	}
	public void setGreatestTile(int greatestTile) {
		this.greatestTile = greatestTile;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public boolean isMain() {
		return main;
	}
	public void setMain(boolean main) {
		this.main = main;
	}
	public int getX() {
		return X;
	}
	public int getY() {
		return Y;
	}
	public void generateTile() {
		//generiert neues Tile auf einem leeren Eintrag des Feldes, erster gameOver Check
		if(tileCount<X*Y) {
		MaxMaxKI KI = new MaxMaxKI();//wenn tileCount >=X*Y -> gameOver, da kein neues Tile erzeugt werden k�nnte
		int[] creation= KI.createTile(this.clone(),3);
		field[creation[0]][creation[1]]=new Tile(creation[2]);
		tileCount++;//Inkrementierung um neuesten Stand zu haben
		}
		else
			gameOver();
		main=true;
	}
	private void fusibleReset() {
		for(int h=0; h<4; h++) {
			for(int b=0;b<4;b++) {
				if(field[h][b].getPotency()!=0)
					field[h][b].fusible=true;
			}
		}
	}
	public void ausgeben() {
		//Kontrollfunktion f�r Developers
		for(int h=0; h<4; h++) {
			for(int b=0;b<4;b++) {
				if(field[h][b].getPotency()!=0) {
					if(b==X-1)
						System.out.println(Integer.toString(field[h][b].getValue()));
					else
					System.out.print(Integer.toString(field[h][b].getValue()));
					}
				else
					if(b==X-1)
						System.out.println(0);
						else
							System.out.print(0);
				}
			}
	}
	public boolean canMove() {
		//FMethode pr�ft ob irgendeine Bewegung m�glich ist
		if(canMoveUp()||canMoveDown()||canMoveRight()||canMoveLeft()) {//disjunktive Verkettung aller Bewegungen zur �berpr�fung
			return true;
			}
		return false;
	}
	public void gameOver() {
		//gameOver SchnittStelle, muss noch bearbeitet werden
		lost=true;
		//System.out.println("Game Over");
	}
	private boolean canMoveUp() {
		//Hilfsmethode, checkt ab ob Bewegung nach oben m�glich ist
		for(int b=0; b<X; b++) { 
			for(int h=0;h<Y;h++) { //Betrachten jedes Feldes einer Zeile
				if(h>0 && field[h][b].getPotency()!=0) {//Bewegung nach oben m�glich?
					if(field[h-1][b].getPotency()==0) {
						return true;}
					else{
						if(field[h-1][b].isFusible(field[h][b])) {//Verschmelzen zweier Tiles durch verschieben nach oben m�glich?
							return true;}
						}
					}
				}
			}
		return false;
	}
	private boolean canMoveDown() {
		//Hilfsmethode, checkt ab ob Bewegung nach unten m�glich ist
		for(int b=0; b<X; b++) {
			for(int h=Y-1;h>-1;h--) {//Betrachten jedes einzelnen Feldes einer Spalte
				if(h<Y-1 && field[h][b].getPotency()!=0) {//Bewegung nach unten m�glich?
					if(field[h+1][b].getPotency()==0)
						return true;
					else{
						if(field[h+1][b].isFusible(field[h][b]))//Verschmelzen zweier Tiles durch verschieben nach unten m�glich?
							return true;
						}
					}
				}
			}
		return false;
	}
	private boolean canMoveRight() {
		//Hilfsmethode, checkt ab ob Bewegung nach rechts m�glich ist
		for(int h=0; h<Y; h++) {
			for(int b=X-1;b>-1;b--) {//Betrachten jedes einzelnen Feldes einer Zeile
				if(b<X-1 && field[h][b].getPotency()!=0) {//Bewgung nach rechts m�glich?
					if(field[h][b+1].getPotency()==0) 
						return true;
					else{
						if(field[h][b+1].isFusible(field[h][b])) //Verschmelzen zweier Tiles durch verschieben nach rechts m�glich?
							return true;
						}
					}
				}
			}
		return false;
	}
	private boolean canMoveLeft() {
		//Hilfsmethode, checkt ab ob Bewegung nach links m�glich ist
		for(int h=0; h<Y; h++) {
			for(int b=0;b<X;b++) {//Betrachten jedes einzelnen Feldes einer Zeile
				if(b>0 && field[h][b].getPotency()!=0) {//Bewegung nach links m�glich?
					if(field[h][b-1].getPotency()==0) 
						return true;
					else{
						if(field[h][b-1].isFusible(field[h][b]))//Verschmelzen zweier Tiles durch verschieben nach links m�glich?
							return true;
						}
					}
				}
			}
		return false;
	}
	public void moveUp(){
		//Methode f�r die Bewegung nach oben
		if(canMove()) {//Abfrage ob Bewegung m�glich
		for(int b=0; b<X; b++) {
			for(int h=0;h<Y;h++) {//Betrachten jedes einzelnen Feldes einer Spalte
				//Schleifendurchlauf in dieser Reihenfolge n�tig, damit Bewegung von unten nach oben geht
				if(h>0 && field[h][b].getPotency()!=0) {
					if(field[h-1][b].getPotency()==0) {//Bewegt Feld nach oben falls m�glich
						field[h-1][b].setPotency(field[h][b].getPotency());
						field[h][b].setPotency(0);
						moved=true;//muss gesetzt werden, falls Feld nochmals bewegt werden kann
					}
					else{
						if(field[h-1][b].isFusible(field[h][b])) {//Verschmilzt Feld mit Feld dar�ber falls m�glich
							field[h-1][b]=field[h-1][b].fuse(field[h][b]);
							field[h][b].setPotency(0);
							tileCount--;
							moved=true;
							score= score + field[h-1][b].getValue();
							if(field[h-1][b].getValue()>greatestTile) {
								greatestTile=field[h-1][b].getValue();
							}
							}
						}
					}
				}
			}
		if(moved==true&& numOfRec<Y-1) {//Rekursiver Aufruf, falls irgendein Feld bewegt wurde, damit Bewegung komplett durchgef�hrt wird
			numOfRec++;
			moved=false;
			moveUp();
		}
		else if(numOfRec>0&&main)
			generateTile();
		}
		else
			gameOver();//Sollte keine Bewegung m�glich sein -> Game Over
	numOfRec=0;
	fusibleReset();
	//System.out.println(score);
	}
	public void moveDown(){
		//Methode f�r die Bewegung nach unten
		//Wie moveUp nur umgekehrter Schleifendurchlauf und Bewegungsrichtung
		//F�r mehr Infos siehe moveUp
		if(canMove()) {
		for(int b=0; b<X; b++) {
			for(int h=Y-1;h>-1;h--) {//Schleife, so dass Bewegung von oben nach unten
				if(h<Y-1 && field[h][b].getPotency()!=0) {
					if(field[h+1][b].getPotency()==0) {
						field[h+1][b].setPotency(field[h][b].getPotency());;
						field[h][b].setPotency(0);
						moved=true;
					}
					else{
						if(field[h+1][b].isFusible(field[h][b])) {
							field[h+1][b]=field[h+1][b].fuse(field[h][b]);
							field[h][b].setPotency(0);
							moved=true;
							tileCount--;
							score= score + field[h+1][b].getValue();
							if(field[h+1][b].getValue()>greatestTile) {
								greatestTile=field[h+1][b].getValue();
							}
							}
						}
					}
				}
			}
		if(moved==true&&numOfRec<Y-1) {//rekursiver Aufruf
			numOfRec++;
			moved=false;
			moveDown();
		}
		else if(numOfRec>0&&main)
			generateTile();
		}
		else
			gameOver();
		numOfRec=0;
		fusibleReset();
		//System.out.println(score);
	}
	public void moveRight(){
		//Methode f�r Bewegung nach rechts
		//Wie moveDown , nur H�he und Breite vertauscht
		//f�r mehr Infos siehe moveUp/moveDown
		if(canMove()) {
		for(int h=0; h<Y; h++) {
			for(int b=X-1;b>-1;b--) {//Schleife so dass Bewegung vonlinks nach rechts
				if(b<X-1 && field[h][b].getPotency()!=0) {
					if(field[h][b+1].getPotency()==0) {
						field[h][b+1].setPotency(field[h][b].getPotency());
						field[h][b].setPotency(0);
						moved=true;
					}
					else{
						if(field[h][b+1].isFusible(field[h][b])) {
							field[h][b+1]=field[h][b+1].fuse(field[h][b]);
							field[h][b].setPotency(0);
							moved=true;
							tileCount--;
							score= score + field[h][b+1].getValue();
							if(field[h][b+1].getValue()>greatestTile) {
								greatestTile=field[h][b+1].getValue();
							}
							}
						}
					}
				}
			}
		if(moved==true&&numOfRec<X-1) {//rekursiver Aufruf
			numOfRec++;
			moved=false;
			moveRight();
		}
		else if(numOfRec>0&&main)
			generateTile();
		else
			gameOver();
		numOfRec=0;
		fusibleReset();
		//System.out.println(score);
	}
	}
	public void moveLeft(){
		//Methode f�r Bewegung nach links
		//Wie moveUp, nur H�he und Breite vertauscht
		if(canMove()) {
		for(int h=0; h<Y; h++) {
			for(int b=0;b<X;b++) {//Schleife, so dass Bewegung von rechts nach links
				if(b>0 && field[h][b].getPotency()!=0) {
					if(field[h][b-1].getPotency()==0) {
						field[h][b-1].setPotency(field[h][b].getPotency());
						field[h][b].setPotency(0);
						moved=true;
					}
					else{
						if(field[h][b-1].isFusible(field[h][b])) {
							field[h][b-1]=field[h][b-1].fuse(field[h][b]);
							field[h][b].setPotency(0);
							moved=true;
							tileCount--;
							score= score + field[h][b-1].getValue();
							if(field[h][b-1].getValue()>greatestTile) {
								greatestTile=field[h][b-1].getValue();
							}
							}
						}
					}
				}
			}
		if(moved==true&&numOfRec<X-1) {
			numOfRec++;
			moved=false;//rekursiver Aufruf
			moveLeft();
		}
		else if(numOfRec>0&&main)
			generateTile();
	}
	else 
		gameOver();
		numOfRec=0;
		fusibleReset();
		//System.out.println(score);
	}
}
