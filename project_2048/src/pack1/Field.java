package pack1;

public class Field {
	/* Hauptklasse des Spiels / Controller des Projektes
	 * beinhaltet alle für das Spiel wichtigen MEthoden und Schnittstellen
	 * */
	private boolean moved =false; //wird zu Überbrüfung, ob Rekursion notwendig benutzt
	private int tileCount = 1;	//Kontrollvariable, dass nicht versucht wird Tile zu generieren, wenn kein Platz mehr ist
	public final int X=4; //vertikale Größe des Spielfeldes
	public final int Y=4;	//horizontale Größe des Spielfeldes
	private short numOfRec = 0;
	private boolean lost = false;
	public int greatestTile = 2;
	public int score=0;
	Tile[][] field = new Tile[Y][X]; //Spielfeld an sich besteht aus einem Feld von Tiles
	RandomKI KI = new RandomKI();	//KI zur Erzeugung neuer Tiles
	
	Field() {
		//Standardkonstruktor, kein anderer benötigt, da Ausgangssituation immer gleich
		field[3][3] = new Tile();
		generateTile();
	}
	public void generateTile() {
		//generiert neues Tile auf einem leeren Eintrag des Feldes, erster gameOver Check
		if(tileCount<X*Y) {//wenn tileCount >=X*Y -> gameOver, da kein neues Tile erzeugt werden könnte
		int[] creation= KI.createTile(this);
		field[creation[1]][creation[0]]=new Tile(creation[2]);
		tileCount++;//Inkrementierung um neuesten Stand zu haben
		}
		else
			gameOver();
	}
	private void fusibleReset() {
		for(int h=0; h<4; h++) {
			for(int b=0;b<4;b++) {
				if(field[h][b]!=null)
					field[h][b].fusible=true;
			}
		}
	}
	private void ausgeben() {
		//Kontrollfunktion für Developers
		for(int h=0; h<4; h++) {
			for(int b=0;b<4;b++) {
				if(field[h][b]!=null) {
					if(b==X-1)
						System.out.println(Integer.toString(field[h][b].get()));
					else
					System.out.print(Integer.toString(field[h][b].get()));
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
		//FMethode prüft ob irgendeine Bewegung möglich ist
		if(canMoveUp()||canMoveDown()||canMoveRight()||canMoveLeft())//disjunktive Verkettung aller Bewegungen zur Überprüfung
			return true;
		return false;
	}
	public void gameOver() {
		//gameOver SchnittStelle, muss noch bearbeitet werden
		lost=true;
		System.out.println("Game Over");
	}
	private boolean canMoveUp() {
		//Hilfsmethode, checkt ab ob Bewegung nach oben möglich ist
		for(int b=0; b<X; b++) { 
			for(int h=0;h<Y;h++) { //Betrachten jedes Feldes einer Zeile
				if(h>0 && field[h][b]!=null) {//Bewegung nach oben möglich?
					if(field[h-1][b]==null)
						return true;
					else{
						if(field[h-1][b].isFusible(field[h][b]))//Verschmelzen zweier Tiles durch verschieben nach oben möglich?
							return true;
						}
					}
				}
			}
		return false;
	}
	private boolean canMoveDown() {
		//Hilfsmethode, checkt ab ob Bewegung nach unten möglich ist
		for(int b=0; b<X; b++) {
			for(int h=Y-1;h>-1;h--) {//Betrachten jedes einzelnen Feldes einer Spalte
				if(h<Y-1 && field[h][b]!=null) {//Bewegung nach unten möglich?
					if(field[h+1][b]==null)
						return true;
					else{
						if(field[h+1][b].isFusible(field[h][b]))//Verschmelzen zweier Tiles durch verschieben nach unten möglich?
							return true;
						}
					}
				}
			}
		return false;
	}
	private boolean canMoveRight() {
		//Hilfsmethode, checkt ab ob Bewegung nach rechts möglich ist
		for(int h=0; h<Y; h++) {
			for(int b=X-1;b>-1;b--) {//Betrachten jedes einzelnen Feldes einer Zeile
				if(b<X-1 && field[h][b]!=null) {//Bewgung nach rechts möglich?
					if(field[h][b+1]==null) 
						return true;
					else{
						if(field[h][b+1].isFusible(field[h][b])) //Verschmelzen zweier Tiles durch verschieben nach rechts möglich?
							return true;
						}
					}
				}
			}
		return false;
	}
	private boolean canMoveLeft() {
		//Hilfsmethode, checkt ab ob Bewegung nach links möglich ist
		for(int h=0; h<Y; h++) {
			for(int b=0;b<X;b++) {//Betrachten jedes einzelnen Feldes einer Zeile
				if(b>0 && field[h][b]!=null) {//Bewegung nach links möglich?
					if(field[h][b-1]==null) 
						return true;
					else{
						if(field[h][b-1].isFusible(field[h][b]))//Verschmelzen zweier Tiles durch verschieben nach links möglich?
							return true;
						}
					}
				}
			}
		return false;
	}
	public void moveUp(){
		//Methode für die Bewegung nach oben
		if(canMove()) {//Abfrage ob Bewegung möglich
		for(int b=0; b<X; b++) {
			for(int h=0;h<Y;h++) {//Betrachten jedes einzelnen Feldes einer Spalte
				//Schleifendurchlauf in dieser Reihenfolge nötig, damit Bewegung von unten nach oben geht
				if(h>0 && field[h][b]!=null) {
					if(field[h-1][b]==null) {//Bewegt Feld nach oben falls möglich
						field[h-1][b]=field[h][b];
						field[h][b]=null;
						moved=true;//muss gesetzt werden, falls Feld nochmals bewegt werden kann
					}
					else{
						if(field[h-1][b].isFusible(field[h][b])) {//Verschmilzt Feld mit Feld darüber falls möglich
							field[h-1][b]=field[h-1][b].fuse(field[h][b]);
							field[h][b]=null;
							tileCount--;
							moved=true;
							score= score + field[h-1][b].get();
							if(field[h-1][b].get()>greatestTile) {
								greatestTile=field[h-1][b].get();
							}
							}
						}
					}
				}
			}
		if(moved==true&& numOfRec<Y-1) {//Rekursiver Aufruf, falls irgendein Feld bewegt wurde, damit Bewegung komplett durchgeführt wird
			numOfRec++;
			moved=false;
			moveUp();
		}
		else if(numOfRec>0)
			generateTile();
		}
		else
			gameOver();//Sollte keine Bewegung möglich sein -> Game Over
	numOfRec=0;
	fusibleReset();
	System.out.println(score);
	}
	public void moveDown(){
		//Methode für die Bewegung nach unten
		//Wie moveUp nur umgekehrter Schleifendurchlauf und Bewegungsrichtung
		//Für mehr Infos siehe moveUp
		if(canMove()) {
		for(int b=0; b<X; b++) {
			for(int h=Y-1;h>-1;h--) {//Schleife, so dass Bewegung von oben nach unten
				if(h<Y-1 && field[h][b]!=null) {
					if(field[h+1][b]==null) {
						field[h+1][b]=field[h][b];
						field[h][b]=null;
						moved=true;
					}
					else{
						if(field[h+1][b].isFusible(field[h][b])) {
							field[h+1][b]=field[h+1][b].fuse(field[h][b]);
							field[h][b]=null;
							moved=true;
							tileCount--;
							score= score + field[h+1][b].get();
							if(field[h+1][b].get()>greatestTile) {
								greatestTile=field[h+1][b].get();
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
		else if(numOfRec>0)
			generateTile();
		}
		else
			gameOver();
		numOfRec=0;
		fusibleReset();
		System.out.println(score);
	}
	public void moveRight(){
		//Methode für Bewegung nach rechts
		//Wie moveDown , nur Höhe und Breite vertauscht
		//für mehr Infos siehe moveUp/moveDown
		if(canMove()) {
		for(int h=0; h<Y; h++) {
			for(int b=X-1;b>-1;b--) {//Schleife so dass Bewegung vonlinks nach rechts
				if(b<X-1 && field[h][b]!=null) {
					if(field[h][b+1]==null) {
						field[h][b+1]=field[h][b];
						field[h][b]=null;
						moved=true;
					}
					else{
						if(field[h][b+1].isFusible(field[h][b])) {
							field[h][b+1]=field[h][b+1].fuse(field[h][b]);
							field[h][b]=null;
							moved=true;
							tileCount--;
							score= score + field[h][b+1].get();
							if(field[h][b+1].get()>greatestTile) {
								greatestTile=field[h][b+1].get();
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
		else if(numOfRec>0)
			generateTile();
		}
		else
			gameOver();
		numOfRec=0;
		fusibleReset();
		System.out.println(score);
	}	
	public void moveLeft(){
		//Methode für Bewegung nach links
		//Wie moveUp, nur Höhe und Breite vertauscht
		if(canMove()) {
		for(int h=0; h<Y; h++) {
			for(int b=0;b<X;b++) {//Schleife, so dass Bewegung von rechts nach links
				if(b>0 && field[h][b]!=null) {
					if(field[h][b-1]==null) {
						field[h][b-1]=field[h][b];
						field[h][b]=null;
						moved=true;
					}
					else{
						if(field[h][b-1].isFusible(field[h][b])) {
							field[h][b-1]=field[h][b-1].fuse(field[h][b]);
							field[h][b]=null;
							moved=true;
							tileCount--;
							score= score + field[h][b-1].get();
							if(field[h][b-1].get()>greatestTile) {
								greatestTile=field[h][b-1].get();
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
		else if(numOfRec>0)
			generateTile();
	}
	else 
		gameOver();
		numOfRec=0;
		fusibleReset();
		System.out.println(score);
	}
}
