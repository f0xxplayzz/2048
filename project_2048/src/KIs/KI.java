package KIs;

import pack1.Field;

public interface KI {
	//Interface für alle KIs
	public int[] createTile(Field f,int depth) throws Exception;//Methode wird von jeder KI benötigt, da jede KI Felder erzeugen muss
}
