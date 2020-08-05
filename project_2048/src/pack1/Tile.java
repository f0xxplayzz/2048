package pack1;

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
		if(this.potenz == a.potenz&& fusible &&a.fusible && potenz>0) {
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
	public String get() {
		//getter-Methode die den Anzeigewert der jeweiligen Kachel berechnet
		if (potenz>0) 
			return Integer.toString((int) Math.pow(basis,this.potenz));
		else
			return "";
	}
	public int getPotency() {
		return potenz;
	}
	public void setPotency(int n) {
		potenz=n;
	}
	public int getValue() {
		return (int) Math.pow(basis,this.potenz);
	}
}
