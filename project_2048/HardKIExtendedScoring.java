package package_2048_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class HardKIExtendedScoring implements KI, Runnable {

	private int numberOfRecursions = 4;
	private volatile ArrayList<Field> fieldsOfFirstIteration = new ArrayList<Field>();
	private volatile HashMap<Field, int[]> coordinatesToField;
	private volatile HashMap<int[], Integer> possibilities;
	private volatile HashMap<String, Integer> scoreOfFieldsBeforeTilegeneration;
	private volatile HashMap<String, Integer> scoreOfFieldsBeforeMoving;
	
	
	//Die anschlie�enden Methoden m�ssen synchronized sein, 
	//da es Fehler gibt, wenn man einen Value bekommen will, wenn zu viele Threads daran arbeiten
	private synchronized int scoreOfFieldsBeforeTilegenerationMethods(String input) {
		return scoreOfFieldsBeforeTilegeneration.get(input);
	}
	
	private synchronized void scoreOfFieldsBeforeTilegenerationMethods(String inputString, int inputInt) {
		scoreOfFieldsBeforeTilegeneration.put(inputString, inputInt);
	}
	
	private synchronized int scoreOfFieldsBeforeMovingMethods(String input) {
		return scoreOfFieldsBeforeMoving.get(input);
	}
	
	private synchronized void scoreOfFieldsBeforeMovingMethods(String inputString, int inputInt) {
		scoreOfFieldsBeforeMoving.put(inputString, inputInt);
	}
	
	public HardKIExtendedScoring() {
		//m�glichkeit nicht die Anzahl der Rekursionen von Anfang an festzulegen
	}
	
	private synchronized int calcExtendedScore(Field f) {
		int score = 0;
		int[] biggestTile = new int[3];
		int powerOfBiggestTile = 0;
		int posYOfBiggestTile = 0;
		int posXOfBiggestTile = 0;
		int distanceToZeroZeroCorner;
		int distanceToZeroYCorner;
		int distanceToXZeroCorner;
		int distanceToXYCorner;
		int minimumDistanceToACorner;
		for(int y = 0; y < f.Y; y++) {
			for(int x = 0; x < f.X; x++) {
				if(f.field[y][x] == null)
				{
					score += 2; //Inkrementiert Score mit 2 für jedes leere Feld im Field f.
				}
				else if(powerOfBiggestTile <= f.field[y][x].potenz) 
				{
					powerOfBiggestTile = f.field[y][x].potenz;
					posYOfBiggestTile = y;
					posXOfBiggestTile = x;
				} // Sucht das größte Tile in Field f und gibt dieses dann in den 3 Variablen an
			}
		}
		score += Math.pow(f.field[posYOfBiggestTile][posXOfBiggestTile].basis, powerOfBiggestTile);
		//Erhöht Score um den höchste Wert eines Tiles im Field f
		
		//Berechnung von Distanzen mithilfe von Vektorbeträgen.
		distanceToZeroZeroCorner = (int) Math.sqrt((Math.pow(posYOfBiggestTile, 2) + Math.pow(posXOfBiggestTile, 2)));
		distanceToZeroYCorner = (int) Math.sqrt((Math.pow((f.Y - 1) - posYOfBiggestTile, 2) + Math.pow(posXOfBiggestTile, 2)));
		distanceToXZeroCorner = (int) Math.sqrt((Math.pow(posYOfBiggestTile, 2) + Math.pow((f.X-1) - posXOfBiggestTile, 2)));
		distanceToXYCorner = (int) Math.sqrt((Math.pow((f.Y - 1) - posYOfBiggestTile, 2) + Math.pow((f.X-1) - posXOfBiggestTile, 2)));
		minimumDistanceToACorner = (int) Math.min(Math.min(distanceToZeroZeroCorner, distanceToZeroYCorner), Math.min(distanceToXZeroCorner, distanceToXYCorner));
		
		//Erhöhung des Scores um die (maximal mögliche Distanz innerhalb des Fields MINUS die minimale Distanz von einem BiggestTile)*5, da es wichtig ist dass das größte Tile so nah wie möglich an einer Ecke sein soll
		score +=
				5 * Math.sqrt(Math.pow(f.Y-1, 2) + Math.pow(f.X-1, 2)) - minimumDistanceToACorner;
		
		return score;
	}
	public HardKIExtendedScoring(int startRecu) {
		numberOfRecursions = startRecu;
	}

	@Override
	public int[] createTile(Field f) {
		//Herzst�ck der Klasse
		int[] output = new int[3];											//gebrauchte Variablen
		int counter = 0;
		
		coordinatesToField = new HashMap<Field, int[]>();					//Zur�cksetzen der HashMaps von den vorherigen Durchg�ngen
		possibilities = new HashMap<int[], Integer>();
		scoreOfFieldsBeforeTilegeneration = new HashMap<String, Integer>();
		scoreOfFieldsBeforeMoving = new HashMap<String, Integer>();

		for (int i = 0; i < f.X; i++) {
			for (int j = 0; j < f.Y; j++) {
				if (f.field[i][j] == null) {
					
					addFieldToList(i, j, f);								//Zusammensuchen aller m�glichen Felder nach der Erstellung, sowie mitz�hlen der Anzahl der Felder
																			//Alle Informationen werden dabei auf die HashMaps aufgeteilt
					counter += 2;											//Die 2 liegt daran, dass entweder eine 2 oder eine 4 entstehen kann

				}
			}
		}

		for (int k = 0; k < counter; k++) {									//F�r jedes m�gliche Feld wird ein Thread erzeugt, damit nicht mehrere Threads das gleiche Feld untersuchen wird jedes Mal gewartet
			new Thread(this).start();
		}

		while (possibilities.size() != counter) {							//Warten, bis alle Threads fertig sind
			try {
				TimeUnit.SECONDS.sleep(1);
				System.out.print(possibilities.size());						//Ausgabe der aktuell fertigen Threads, sowie die Anzahl aller Threads
				System.out.print(" out of ");
				System.out.print(counter);
				System.out.print(" have finished!");
				System.out.println();
			} catch (Exception e) {
				System.out.println("Problem with sleeping");
			}
		}

		int currentScore = 100000000;

		for (int[] i : possibilities.keySet()) {							//Auswahl und Ausgabe der Koordinaten, sowie der Potenz, mit dem geringsten Score
			if (currentScore > possibilities.get(i)) {
				output[1] = i[0];
				output[0] = i[1];
				output[2] = i[2];
				currentScore = possibilities.get(i);
			}
		}

		return output;
	}

	private int scoringBeforeTilegeneration(Field f, int counter) {
		//es wird eine Liste der m�glichen Felder nach der Erstellung eines Tiles erstellt und die minimum-Funktion damit aufgerufen, 
		//die den minimalen Score der Felder aus der Liste zur�ckgibt, der dann eingespeichert und zur�ckgegeben wird
		//davor wird kontrolliert, ob Tiles erstellt werden k�nn, sowie ob die aktuelle Situation schonmal berechnet wurde
		//in den F�llen wird der aktuelle Score oder der zuvor ausgerechnete Wert zur�ckgegeben
		if (f.tileCount == f.X * f.Y) {
			return calcExtendedScore(f);
		}
		
		String tmpString = Integer.toString(counter) + " " + f.fieldAsString();	//der Counter wird ben�tigt, um auseinander halten zu k�nnen, ob zwei 2er oder eine 4 entstanden ist
																				//falls auch der Score identisch ist, aber ein anderer Weg genutzt wurde, macht es am Endergebnis keinen Unterschied
		if (scoreOfFieldsBeforeTilegeneration.containsKey(tmpString)) {
			return scoreOfFieldsBeforeTilegenerationMethods(tmpString);
			
		}

		ArrayList<Field> fieldsToProcess = new ArrayList<Field>();
		fieldsToProcess = possibleFieldsAfterGeneration(f, fieldsToProcess);

		int tmpInt = minimum(fieldsToProcess, counter);
		
		scoreOfFieldsBeforeTilegenerationMethods(tmpString, tmpInt);
		
		return tmpInt;

	}

	private int scoringBeforeMoving(Field f, int counter) {
		//fast identisch wie scoringBeforeTilegeneration, nur in diesem Fall vor einer Bewegung
		//es ist auch die Rekursionsbremse enthalten und es wird der maximale Score anstattdessen genommmen
		if (counter == 0) {
			return calcExtendedScore(f);
		}
		
		String tmpString = Integer.toString(counter) + f.fieldAsString();
		
		if (scoreOfFieldsBeforeMoving.containsKey(tmpString)) {
			return scoreOfFieldsBeforeMovingMethods(tmpString);
			
		}

		ArrayList<Field> fieldsToProcess = new ArrayList<Field>();
		fieldsToProcess = possibleFieldsAfterMove(f, fieldsToProcess);

		if (fieldsToProcess.isEmpty()) {
			return calcExtendedScore(f);
		}
		
		int tmpInt = maximum(fieldsToProcess, counter);
		
		scoreOfFieldsBeforeMovingMethods(f.fieldAsString(), tmpInt);

		return tmpInt;
	}

	private int maximum(ArrayList<Field> inputList, int counter) {
		//gib den gr��ten Score zur�ck, den ein Feld aus der inputList hat
		if (inputList.isEmpty()) {
			return -1;
		}

		int output = scoringBeforeTilegeneration(inputList.get(0), counter);
		int tmp = 0;

		for (int i = 1; i < inputList.size(); i++) {
			tmp = scoringBeforeTilegeneration(inputList.get(i), counter);
			if (output < tmp) {
				output = tmp;
			}
		}

		return output;
	}

	private int minimum(ArrayList<Field> inputList, int counter) {
		//gib den geringsten Score zur�ck, den ein Feld aus der inputList hat
		if (inputList.isEmpty()) {
			return 1000000000;
		}

		int output = scoringBeforeMoving(inputList.get(0), counter - 1);
		int tmp = 0;

		for (int i = 1; i < inputList.size(); i++) {
			tmp = scoringBeforeMoving(inputList.get(i), counter - 1);
			if (output > tmp) {
				output = tmp;
			}
		}

		return output;

	}

	private ArrayList<Field> possibleFieldsAfterMove(Field f, ArrayList<Field> list) {
		//die m�glichen Felder nach einer Bewegung werden der ArrayList hinzugef�gt 
		if (f.canMoveUp()) {
			Field fieldUp = new Field(1);
			fieldUp = f.cloneField();
			fieldUp.moveUp();
			list.add(fieldUp);
		}
		if (f.canMoveDown()) {
			Field fieldDown = new Field(1);
			fieldDown = f.cloneField();
			fieldDown.moveDown();
			list.add(fieldDown);
		}
		if (f.canMoveLeft()) {
			Field fieldLeft = new Field(1);
			fieldLeft = f.cloneField();
			fieldLeft.moveLeft();
			list.add(fieldLeft);
		}
		if (f.canMoveRight()) {
			Field fieldRight = new Field(1);
			fieldRight = f.cloneField();
			fieldRight.moveRight();
			list.add(fieldRight);
		}

		return list;
	}

	private ArrayList<Field> possibleFieldsAfterGeneration(Field f, ArrayList<Field> list) {
		//die m�glichen Felder nach einer Erstellung werden der ArrayList hinzugef�gt
		for (int i = 0; i < f.X; i++) {
			for (int j = 0; j < f.Y; j++) {
				if (f.field[i][j] == null) {
					list = addFieldToList(i, j, f, list);
				}
			}
		}

		return list;
	}

	private ArrayList<Field> addFieldToList(int x, int y, Field f, ArrayList<Field> list) {
		//F�gt die 2 Felder der ArrayList hinzu, die entstehen, wenn an den Koordinaten x und y eine Feld der Potenz 1 und 2 hinzugef�gt wird
		Field field1 = new Field(1);
		Field field2 = new Field(1);

		field1 = f.cloneField();
		field2 = f.cloneField();

		field1.field[x][y] = new Tile(1);
		field2.field[x][y] = new Tile(2);

		list.add(field1);
		list.add(field2);

		return list;
	}

	private void addFieldToList(int x, int y, Field f) {
		//F�gt die 2 Felder und die Koordinaten den globalen ArrayLists und HashMaps hinzu, die entstehen, wenn an den Koordinaten x und y eine Feld der Potenz 1 und 2 hinzugef�gt wird
		Field field1 = new Field(1);
		Field field2 = new Field(2);

		field1 = f.cloneField();
		field2 = f.cloneField();

		field1.field[x][y] = new Tile(1);
		field2.field[x][y] = new Tile(2);

		fieldsOfFirstIteration.add(field1);
		fieldsOfFirstIteration.add(field2);

		int[] coordinatesWithLevel1 = new int[3];
		int[] coordinatesWithLevel2 = new int[3];

		coordinatesWithLevel1[0] = x;
		coordinatesWithLevel2[0] = x;
		coordinatesWithLevel1[1] = y;
		coordinatesWithLevel2[1] = y;
		coordinatesWithLevel1[2] = 1;
		coordinatesWithLevel2[2] = 2;
		
		coordinatesToField.put(field1, coordinatesWithLevel1);
		coordinatesToField.put(field2, coordinatesWithLevel2);

	}

	private synchronized void addToPossibilities(int[] a, int b) {
		//dies muss synchronized sein, damit es kein verlorenes Update gibt
		possibilities.put(a, b);
	}

	private synchronized Field getFromFieldsOfFirstIteration() {
		//dies muss synchronized sein, damit nicht 2 Threads das gleiche Feld untersuchen
		Field tmp = fieldsOfFirstIteration.get(0);
		fieldsOfFirstIteration.remove(tmp);
		return tmp;
	}

	@Override
	public void run() {
		Field tmp = getFromFieldsOfFirstIteration();
		int score = scoringBeforeMoving(tmp, numberOfRecursions);
		addToPossibilities(coordinatesToField.get(tmp), score);

	}

	public void setNumberOfRecursions(int newNumberOfRecursions) {
		this.numberOfRecursions = newNumberOfRecursions;
	}

}
