package package_2048_test;

public class Field {
	/*
	 * Hauptklasse des Spiels / Controller des Projektes beinhaltet alle f�r das
	 * Spiel wichtigen Methoden und Schnittstellen
	 */
	private boolean moved = false; // wird zu �berbr�fung, ob Rekursion notwendig benutzt
	public int tileCount = 1; // Kontrollvariable, dass nicht versucht wird Tile zu generieren, wenn kein
								// Platz mehr ist
	public final int X = 4; // vertikale Gr��e des Spielfeldes
	public final int Y = 4; // horizontale Gr��e des Spielfeldes
	private short numOfRec = 0;
	private boolean lost = false;
	public int greatestTile = 2;
	public int score = 0;
	public GameGUI GUI;
	Tile[][] field = new Tile[Y][X]; // Spielfeld an sich besteht aus einem Feld von Tiles
	EasyKI KI = new EasyKI(); // KI zur Erzeugung neuer Tiles

	Field(GameGUI startGUI) {
		// Konstruktor f�r das gezeigte Feld
		GUI = startGUI;
		generateTile();
		generateTile();
	}

	Field(int anything) {
		//dieser Konstruktur wird f�r Felder ben�tigt, die nicht angezeigt werden sollen und keine Felder erzeugt bekommen soll
	}

	public void generateTile() {
		// generiert neues Tile auf einem leeren Eintrag des Feldes, erster gameOver
		// Check
		if (tileCount < X * Y) {// wenn tileCount >=X*Y -> gameOver, da kein neues Tile erzeugt werden k�nnte
			int[] creation = KI.createTile(this);
			field[creation[1]][creation[0]] = new Tile(creation[2]);
			tileCount++;// Inkrementierung um neuesten Stand zu haben
		} else
			gameOver();
	}

	private void fusibleReset() {
		//Felder, die in der letzten Bewegung verschmolzen waren, werden wieder erm�glicht sich neu zu verschmelzen
		for (int h = 0; h < 4; h++) {
			for (int b = 0; b < 4; b++) {
				if (field[h][b] != null)
					field[h][b].fusible = true;
			}
		}
	}
	
	public String fieldAsString() {
		//Stringdarstellung des Feldes, dies ist nicht als Ausgabe gedacht und wird f�r die KIs ben�tigt
		//dabei ist zwischen jeder Zahl ein Leerzeichen, um eine Ausgabe zu erm�glichen
		String output = "";
		for (int h = 0; h < Y; h++) {
			for (int b = 0; b < X; b++) {
				if (field[h][b] != null) {
					output += Integer.toString(field[h][b].potenz) + " ";
				}else {
					output += "0 ";
				}
			}
		}
		return output;
	}
	
	public void ausgeben() {
		// Kontrollfunktion f�r Developers
		System.out.println();
		for (int h = 0; h < 4; h++) {
			for (int b = 0; b < 4; b++) {
				if (field[h][b] != null) {
					if (b == X - 1)
						System.out.println(" " + Integer.toString(field[h][b].get()) + " ");
					else
						System.out.print(" " + Integer.toString(field[h][b].get()) + " ");
				} else if (b == X - 1)
					System.out.println(0);
				else
					System.out.print(0);
			}
		}
		System.out.println();
	}

	public Field cloneField() {
		//Erstellung eines Klones, bei dem neue Tiles erstelllt werden, es werden auch weitere Werte �bertragen, das alles stimmt
		Field output = new Field(1);
		for (int i = 0; i < X; i++) {
			for (int j = 0; j < Y; j++) {
				if (field[i][j] != null) {
					output.field[i][j] = new Tile(this.field[i][j].potenz);
				}
			}
		}
		output.score = this.score;
		output.greatestTile = this.greatestTile;
		output.tileCount = this.tileCount;
		output.numOfRec = this.numOfRec;
		output.KI = this.KI;
		return output;
	}

	public void Move(Direction direction) {
		//wird vom KeyListener aufgerufen, um nicht 4 Funktionen erstellen zu m�ssen
		//bewegen und erstellen neuer Tiles, sowie eine m�gliche Aktualisierung der GUI, falls eine existiert
		//im Moment gibt es keine M�glichkeit, dass diese Funktion aufgerufen wird, wenn keine GUI existiert
		boolean fieldChanged = false;
		switch (direction) {
		case DOWN:
			if (canMoveDown()) {
				moveDown();
				fieldChanged = true;
			}
			break;
		case LEFT:
			if (canMoveLeft()) {
				moveLeft();
				fieldChanged = true;
			}
			break;
		case RIGHT:
			if (canMoveRight()) {
				moveRight();
				fieldChanged = true;
			}
			break;
		case UP:
			if (canMoveUp()) {
				moveUp();
				fieldChanged = true;
			}
			break;
		default:
			break;

		}
		
		if (fieldChanged) {
			if (GUI != null) {
				GUI.updateField();
			}
			generateTile();
			
			if (GUI != null) {
				GUI.updateField();
			}
		}
	}

	public boolean canMove() {
		// Methode pr�ft ob irgendeine Bewegung m�glich ist
		if (canMoveUp() || canMoveDown() || canMoveRight() || canMoveLeft())// disjunktive Verkettung aller Bewegungen
																			// zur �berpr�fung
			return true;
		return false;
	}

	public void gameOver() {
		// gameOver SchnittStelle, muss noch bearbeitet werden
		if (!canMove()) {

			if (lost == false) {
				System.out.println("Game Over");
			}
			lost = true;
		}
	}

	public boolean canMoveUp() {
		// Hilfsmethode, checkt ab ob Bewegung nach oben m�glich ist
		for (int b = 0; b < X; b++) {
			for (int h = 0; h < Y; h++) { // Betrachten jedes Feldes einer Zeile
				if (h > 0 && field[h][b] != null) {// Bewegung nach oben m�glich?
					if (field[h - 1][b] == null)
						return true;
					else {
						if (field[h - 1][b].isFusible(field[h][b]))// Verschmelzen zweier Tiles durch verschieben nach
																	// oben m�glich?
							return true;
					}
				}
			}
		}
		return false;
	}

	public boolean canMoveDown() {
		// Hilfsmethode, checkt ab ob Bewegung nach unten m�glich ist
		for (int b = 0; b < X; b++) {
			for (int h = Y - 1; h > -1; h--) {// Betrachten jedes einzelnen Feldes einer Spalte
				if (h < Y - 1 && field[h][b] != null) {// Bewegung nach unten m�glich?
					if (field[h + 1][b] == null)
						return true;
					else {
						if (field[h + 1][b].isFusible(field[h][b]))// Verschmelzen zweier Tiles durch verschieben nach
																	// unten m�glich?
							return true;
					}
				}
			}
		}
		return false;
	}

	public boolean canMoveRight() {
		// Hilfsmethode, checkt ab ob Bewegung nach rechts m�glich ist
		for (int h = 0; h < Y; h++) {
			for (int b = X - 1; b > -1; b--) {// Betrachten jedes einzelnen Feldes einer Zeile
				if (b < X - 1 && field[h][b] != null) {// Bewgung nach rechts m�glich?
					if (field[h][b + 1] == null)
						return true;
					else {
						if (field[h][b + 1].isFusible(field[h][b])) // Verschmelzen zweier Tiles durch verschieben nach
																	// rechts m�glich?
							return true;
					}
				}
			}
		}
		return false;
	}

	public boolean canMoveLeft() {
		// Hilfsmethode, checkt ab ob Bewegung nach links m�glich ist
		for (int h = 0; h < Y; h++) {
			for (int b = 0; b < X; b++) {// Betrachten jedes einzelnen Feldes einer Zeile
				if (b > 0 && field[h][b] != null) {// Bewegung nach links m�glich?
					if (field[h][b - 1] == null)
						return true;
					else {
						if (field[h][b - 1].isFusible(field[h][b]))// Verschmelzen zweier Tiles durch verschieben nach
																	// links m�glich?
							return true;
					}
				}
			}
		}
		return false;
	}

	public void moveUp() {
		// Methode f�r die Bewegung nach oben
		if (canMove()) {// Abfrage ob Bewegung m�glich
			for (int b = 0; b < X; b++) {
				for (int h = 0; h < Y; h++) {// Betrachten jedes einzelnen Feldes einer Spalte
					// Schleifendurchlauf in dieser Reihenfolge n�tig, damit Bewegung von unten nach
					// oben geht
					if (h > 0 && field[h][b] != null) {
						if (field[h - 1][b] == null) {// Bewegt Feld nach oben falls m�glich
							field[h - 1][b] = field[h][b];
							field[h][b] = null;
							moved = true;// muss gesetzt werden, falls Feld nochmals bewegt werden kann
						} else {
							if (field[h - 1][b].isFusible(field[h][b])) {// Verschmilzt Feld mit Feld dar�ber falls
																			// m�glich
								field[h - 1][b] = field[h - 1][b].fuse(field[h][b]);
								field[h][b] = null;
								tileCount--;
								moved = true;
								score = score + field[h - 1][b].get();
								if (field[h - 1][b].get() > greatestTile) {
									greatestTile = field[h - 1][b].get();
								}
							}
						}
					}
				}
			}
			if (moved == true && numOfRec < Y - 1) {// Rekursiver Aufruf, falls irgendein Feld bewegt wurde, damit
													// Bewegung komplett durchgef�hrt wird
				numOfRec++;
				moved = false;
				moveUp();
			}
		} else
			gameOver();// Sollte keine Bewegung m�glich sein -> Game Over
		numOfRec = 0;
		fusibleReset();
	}

	public void moveDown() {
		// Methode f�r die Bewegung nach unten
		// Wie moveUp nur umgekehrter Schleifendurchlauf und Bewegungsrichtung
		// F�r mehr Infos siehe moveUp
		if (canMove()) {
			for (int b = 0; b < X; b++) {
				for (int h = Y - 1; h > -1; h--) {// Schleife, so dass Bewegung von oben nach unten
					if (h < Y - 1 && field[h][b] != null) {
						if (field[h + 1][b] == null) {
							field[h + 1][b] = field[h][b];
							field[h][b] = null;
							moved = true;
						} else {
							if (field[h + 1][b].isFusible(field[h][b])) {
								field[h + 1][b] = field[h + 1][b].fuse(field[h][b]);
								field[h][b] = null;
								moved = true;
								tileCount--;
								score = score + field[h + 1][b].get();
								if (field[h + 1][b].get() > greatestTile) {
									greatestTile = field[h + 1][b].get();
								}
							}
						}
					}
				}
			}
			if (moved == true && numOfRec < Y - 1) {// rekursiver Aufruf
				numOfRec++;
				moved = false;
				moveDown();
			}
		} else {
			gameOver();
		}
		numOfRec = 0;
		fusibleReset();
	}

	public void moveRight() {
		// Methode f�r Bewegung nach rechts
		// Wie moveDown , nur H�he und Breite vertauscht
		// f�r mehr Infos siehe moveUp/moveDown
		if (canMove()) {
			for (int h = 0; h < Y; h++) {
				for (int b = X - 1; b > -1; b--) {// Schleife so dass Bewegung vonlinks nach rechts
					if (b < X - 1 && field[h][b] != null) {
						if (field[h][b + 1] == null) {
							field[h][b + 1] = field[h][b];
							field[h][b] = null;
							moved = true;
						} else {
							if (field[h][b + 1].isFusible(field[h][b])) {
								field[h][b + 1] = field[h][b + 1].fuse(field[h][b]);
								field[h][b] = null;
								moved = true;
								tileCount--;
								score = score + field[h][b + 1].get();
								if (field[h][b + 1].get() > greatestTile) {
									greatestTile = field[h][b + 1].get();
								}
							}
						}
					}
				}
			}
			if (moved == true && numOfRec < X - 1) {// rekursiver Aufruf
				numOfRec++;
				moved = false;
				moveRight();
			}
		} else
			gameOver();
		numOfRec = 0;
		fusibleReset();
	}

	public void moveLeft() {
		// Methode f�r Bewegung nach links
		// Wie moveUp, nur H�he und Breite vertauscht
		if (canMove()) {
			for (int h = 0; h < Y; h++) {
				for (int b = 0; b < X; b++) {// Schleife, so dass Bewegung von rechts nach links
					if (b > 0 && field[h][b] != null) {
						if (field[h][b - 1] == null) {
							field[h][b - 1] = field[h][b];
							field[h][b] = null;
							moved = true;
						} else {
							if (field[h][b - 1].isFusible(field[h][b])) {
								field[h][b - 1] = field[h][b - 1].fuse(field[h][b]);
								field[h][b] = null;
								moved = true;
								tileCount--;
								score = score + field[h][b - 1].get();
								if (field[h][b - 1].get() > greatestTile) {
									greatestTile = field[h][b - 1].get();
								}
							}
						}
					}
				}
			}
			if (moved == true && numOfRec < X - 1) {
				numOfRec++;
				moved = false;// rekursiver Aufruf
				moveLeft();
			}
		} else
			gameOver();
		numOfRec = 0;
		fusibleReset();
	}
}

enum Direction {
	UP, DOWN, LEFT, RIGHT;
}
