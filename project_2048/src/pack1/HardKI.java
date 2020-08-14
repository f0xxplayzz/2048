package package_2048_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class HardKI implements KI, Runnable {

	private int numberOfRecursions = 10;
	private volatile ArrayList<Field> fieldsOfFirstIteration = new ArrayList<Field>();
	private volatile HashMap<Field, int[]> coordinatesToField;
	private volatile HashMap<int[], Integer> possibilities;
	private volatile HashMap<String, Integer> scoreOfFieldsBeforeTilegeneration;
	private volatile HashMap<String, Integer> scoreOfFieldsBeforeMoving;
	
	
	//Die anschließenden Methoden müssen synchronized sein, 
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
	
	public HardKI() {
		//möglichkeit nicht die Anzahl der Rekursionen von Anfang an festzulegen
	}
	
	public HardKI(int startRecu) {
		numberOfRecursions = startRecu;
	}

	@Override
	public int[] createTile(Field f) {
		//Herzstück der Klasse
		int[] output = new int[3];											//gebrauchte Variablen
		int counter = 0;
		
		coordinatesToField = new HashMap<Field, int[]>();					//Zurücksetzen der HashMaps von den vorherigen Durchgängen
		possibilities = new HashMap<int[], Integer>();
		scoreOfFieldsBeforeTilegeneration = new HashMap<String, Integer>();
		scoreOfFieldsBeforeMoving = new HashMap<String, Integer>();

		for (int i = 0; i < f.X; i++) {
			for (int j = 0; j < f.Y; j++) {
				if (f.field[i][j] == null) {
					
					addFieldToList(i, j, f);								//Zusammensuchen aller möglichen Felder nach der Erstellung, sowie mitzählen der Anzahl der Felder
																			//Alle Informationen werden dabei auf die HashMaps aufgeteilt
					counter += 2;											//Die 2 liegt daran, dass entweder eine 2 oder eine 4 entstehen kann

				}
			}
		}

		for (int k = 0; k < counter; k++) {									//Für jedes mögliche Feld wird ein Thread erzeugt, damit nicht mehrere Threads das gleiche Feld untersuchen wird jedes Mal gewartet
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
		//es wird eine Liste der möglichen Felder nach der Erstellung eines Tiles erstellt und die minimum-Funktion damit aufgerufen, 
		//die den minimalen Score der Felder aus der Liste zurückgibt, der dann eingespeichert und zurückgegeben wird
		//davor wird kontrolliert, ob Tiles erstellt werden könn, sowie ob die aktuelle Situation schonmal berechnet wurde
		//in den Fällen wird der aktuelle Score oder der zuvor ausgerechnete Wert zurückgegeben
		if (f.tileCount == f.X * f.Y) {
			return f.score;
		}
		
		String tmpString = Integer.toString(f.score) + " " + f.fieldAsString();	//der Score wird benötigt, um auseinander halten zu können, ob zwei 2er oder eine 4 entstanden ist
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
			return f.score;
		}
		
		String tmpString = Integer.toString(f.score) + f.fieldAsString();
		
		if (scoreOfFieldsBeforeMoving.containsKey(tmpString)) {
			return scoreOfFieldsBeforeMovingMethods(tmpString);
			
		}

		ArrayList<Field> fieldsToProcess = new ArrayList<Field>();
		fieldsToProcess = possibleFieldsAfterMove(f, fieldsToProcess);

		if (fieldsToProcess.isEmpty()) {
			return f.score;
		}
		
		int tmpInt = maximum(fieldsToProcess, counter);
		
		scoreOfFieldsBeforeMovingMethods(f.fieldAsString(), tmpInt);

		return tmpInt;
	}

	private int maximum(ArrayList<Field> inputList, int counter) {
		//gib den größten Score zurück, den ein Feld aus der inputList hat
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
		//gib den geringsten Score zurück, den ein Feld aus der inputList hat
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
		//die möglichen Felder nach einer Bewegung werden der ArrayList hinzugefügt 
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
		//die möglichen Felder nach einer Erstellung werden der ArrayList hinzugefügt
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
		//Fügt die 2 Felder der ArrayList hinzu, die entstehen, wenn an den Koordinaten x und y eine Feld der Potenz 1 und 2 hinzugefügt wird
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
		//Fügt die 2 Felder und die Koordinaten den globalen ArrayLists und HashMaps hinzu, die entstehen, wenn an den Koordinaten x und y eine Feld der Potenz 1 und 2 hinzugefügt wird
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
