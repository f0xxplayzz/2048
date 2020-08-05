package KIs;

import java.util.Random;

import pack1.Field;

public class RandomKI implements KI {
	//Klasse der randomisierten KI, implementiert Interface KI, erzeugt randomisert neue Felder im Spielfeld
	private boolean finished =false;//Prüfvariable
	Random random=new Random();//Zur Erstellung von Zufallszahlen
	public RandomKI(){
		
	}
	@Override
	public int[] createTile(Field f,int depth) {//Man übergibt nur inneren Teil des Spielfeldes(Feld an sich)
		int x;
		int y;
		int level;
		int[] result = new int[3];
		while(!finished ) {//Schleife wird erst verlassen wenn Tile gesetzt wurde
			x = random.nextInt(f.X);
			y = random.nextInt(f.Y);//Erzeugt zufällige Indizes für Höhe und Breite
			if(f.field[y][x].get().equals("")) {//Wenn an der Stelle x,y kein Tile vorliegt wird eines gesetzt
				int k= random.nextInt(10);
				if (k<8) {
					level=1;
				}
				else
					level=2;
				
				result[0]=y;
				result[1]=x;
				result[2]=level;
				finished=true;//PRüfvariable auf true setzen
			}
		}
		finished= false;//Rücksetzen der Variable
		return result;//ausgeben des neuen Feldes
	}

}
