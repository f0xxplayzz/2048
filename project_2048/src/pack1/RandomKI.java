package package_2048_test;

import java.util.Random;

public class RandomKI implements KI {
	//Klasse der randomisierten KI, implementiert Interface KI, erzeugt randomisert neue Felder im Spielfeld
	private boolean finished =false;//Pr�fvariable
	Random random=new Random();//Zur Erstellung von Zufallszahlen
	RandomKI(){
		
	}
	@Override
	public int[] createTile(Field f) {//Man �bergibt nur inneren Teil des Spielfeldes(Feld an sich)
		int x;
		int y;
		int level;
		int[] result = new int[3];
		while(!finished ) {//Schleife wird erst verlassen wenn Tile gesetzt wurde
			x = random.nextInt(f.X);
			y = random.nextInt(f.Y);//Erzeugt zuf�llige Indizes f�r H�he und Breite
			if(f.field[y][x]==null) {//Wenn an der Stelle x,y kein Tile vorliegt wird eines gesetzt
				int k= random.nextInt(10);
				if (k<8) {
					level=1;
				}
				else
					level=2;
				
				result[0]=x;
				result[1]=y;
				result[2]=level;
				finished=true;//PR�fvariable auf true setzen
			}
		}
		finished= false;//R�cksetzen der Variable
		return result;//ausgeben des neuen Feldes
	}

}
