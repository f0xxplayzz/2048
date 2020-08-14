package package_2048_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class EasyKI implements KI, Runnable{
	//Alle Kommentare sind in der HardKI
	private int numberOfRecursions = 10;
	private volatile ArrayList<Field> fieldsOfFirstIteration = new ArrayList<Field>();
	private volatile HashMap<Field, int[]> coordinatesToField = new HashMap<Field, int[]>();
	private HashMap<int[], Integer> possibilities = new HashMap<int[], Integer>();
	private volatile HashMap<String, Integer> scoreOfFieldsBeforeTilegeneration;
	private volatile HashMap<String, Integer> scoreOfFieldsBeforeMoving;
	
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
	
	@Override
	public int[] createTile(Field f) {

		int[] output = new int[3];
		int counter = 0;
		
		coordinatesToField = new HashMap<Field, int[]>();
		possibilities = new HashMap<int[], Integer>();
		scoreOfFieldsBeforeTilegeneration = new HashMap<String, Integer>();
		scoreOfFieldsBeforeMoving = new HashMap<String, Integer>();
		
		for (int i = 0; i < f.X; i++) {
			for (int j = 0; j < f.Y; j++) {
				if (f.field[i][j] == null) {
					
					addFieldToList(i, j, f);
					
					counter += 2;

				}
			}
		}
		
		for (int k = 0; k < counter; k++) {
			new Thread(this).start();
		}
		
		while (possibilities.size() != counter) {
			try {
				TimeUnit.SECONDS.sleep(1);
				System.out.print(possibilities.size());
				System.out.print(" out of ");
				System.out.print(counter);
				System.out.print(" have finished!");
				System.out.println();
			} catch (Exception e) {
				System.out.println("Problem with sleeping");
			}
		}
		
		
		
		int currentScore = 0;
		
		
		for (int[] i : possibilities.keySet()) {
			if (currentScore < possibilities.get(i)) {
				output[1] = i[0];
				output[0] = i[1];
				output[2] = i[2];
				currentScore = possibilities.get(i);
			}
		}

		return output;
	}

	private int scoringBeforeTilegeneration(Field f, int counter) {

		if (f.tileCount == f.X * f.Y) {
			return f.score;
		}
		
		String tmpString = Integer.toString(f.score) + " " + f.fieldAsString();
		
		if (scoreOfFieldsBeforeTilegeneration.containsKey(tmpString)) {
			return scoreOfFieldsBeforeTilegenerationMethods(tmpString);
		}
		
		ArrayList<Field> fieldsToProcess = new ArrayList<Field>();
		fieldsToProcess = possibleFieldsAfterGeneration(f, fieldsToProcess);

		int tmpInt = maximumTile(fieldsToProcess, counter);
		
		scoreOfFieldsBeforeTilegenerationMethods(tmpString, tmpInt);
		
		return tmpInt;

	}
	
	private int scoringBeforeMoving(Field f, int counter) {
		
		if (counter == 0) {
			return f.score;
		}
		
		String tmpString = f.fieldAsString();
		
		if (scoreOfFieldsBeforeMoving.containsKey(tmpString)) {
			return scoreOfFieldsBeforeMovingMethods(tmpString);
			
		}
		
		ArrayList<Field> fieldsToProcess = new ArrayList<Field>();
		fieldsToProcess = possibleFieldsAfterMove(f, fieldsToProcess);
		
		if (fieldsToProcess.isEmpty()) {
			return f.score;
		}
		
		int tmpInt = maximumMove(fieldsToProcess, counter);
		
		scoreOfFieldsBeforeMovingMethods(f.fieldAsString(), tmpInt);

		return tmpInt;
	}
	
	private int maximumMove(ArrayList<Field> inputList, int counter) {
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

	private int maximumTile(ArrayList<Field> inputList, int counter) {
		if (inputList.isEmpty()) {
			return -1;
		}

		int output = scoringBeforeMoving(inputList.get(0), counter - 1);
		int tmp = 0;

		for (int i = 1; i < inputList.size(); i++) {
			tmp = scoringBeforeMoving(inputList.get(i), counter -1);
			if (output < tmp) {
				output = tmp;
			}
		}

		return output;

	}

	private ArrayList<Field> possibleFieldsAfterMove(Field f, ArrayList<Field> list) {
		
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
		possibilities.put(a, b);
	}

	private synchronized Field getFromFieldsOfFirstIteration() {
		Field tmp = fieldsOfFirstIteration.get(0);
		fieldsOfFirstIteration.remove(tmp);
		return tmp;
	}

	@Override
	public void run() {
		Field tmp = getFromFieldsOfFirstIteration();
		
		//fieldsOfFirstIteration.notifyAll();
		int score = scoringBeforeMoving(tmp, numberOfRecursions);
		addToPossibilities(coordinatesToField.get(tmp), score);
	}
	
}
