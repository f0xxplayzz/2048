package KIs;

import pack1.Field;

public interface KI {
	//Interface f�r alle KIs
	public int[] createTile(Field f,int depth) throws Exception;//Methode wird von jeder KI ben�tigt, da jede KI Felder erzeugen muss
}
