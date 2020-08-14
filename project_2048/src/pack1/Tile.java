package package_2048_test;

public class Tile {
	//Klasse für jede einzelne Kachel des Spielfeldes
	int potenz;
	public boolean fusible= true;
	final int basis =2;//Basis, Standard=2
	Tile(){//Standardkonstruktor
		this.potenz = 1;
	}
	Tile(int level){//Konstruktor, mit möglicher Angabe der Potenz
		this.potenz=level;
	}
	public boolean isFusible(Tile a) {
		//Methode, die prüft ob zwei Kacheln verschmelzen können
		if(this.potenz == a.potenz&& fusible &&a.fusible) {
			return true;
		}
		return false;
	}
	public Tile fuse(Tile a) {
		//Methode die zwei Kacheln verschmilzt, Eine Kachel muss danach in Field noch gelöscht werden
			this.potenz = this.potenz+1;
			fusible=false;
			return this;
	}
	public int get() {
		//getter-Methode die den Anzeigewert der jeweiligen Kachel berechnet
		return (int) Math.pow(basis,this.potenz);
	}
}
