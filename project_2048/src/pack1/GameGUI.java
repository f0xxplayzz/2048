package pack1;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;

public class GameGUI extends JFrame {


	private static final long serialVersionUID = 1L;

	int wert = 0;
	JFrame frame = new JFrame();
	JPanel panel = new JPanel();
	JButton btn1 = new JButton();
	JButton btn2 = new JButton();
	JButton btn3 = new JButton();
	JButton btn4 = new JButton();
	JButton btn5 = new JButton();
	JButton btn6 = new JButton();
	JButton btn7 = new JButton();
	JButton btn8 = new JButton();
	JButton btn9 = new JButton();
	JButton btn10 = new JButton();
	JButton btn11 = new JButton();
	JButton btn12 = new JButton();
	JButton btn13 = new JButton();
	JButton btn14 = new JButton();
	JButton btn15 = new JButton();
	JButton btn16 = new JButton();
	Field f = new Field();


	public GameGUI(){
		panel.setLayout(new GridLayout(4,4,3,3));
		panel.add(btn1);
		panel.add(btn2);
		panel.add(btn3);
		panel.add(btn4);
		panel.add(btn5);
		panel.add(btn6);
		panel.add(btn7);
		panel.add(btn8);
		panel.add(btn9);
		panel.add(btn10);
		panel.add(btn11);
		panel.add(btn12);
		panel.add(btn13);
		panel.add(btn14);
		panel.add(btn15);
		panel.add(btn16);
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(600, 600);
		frame.setVisible(true);
		this.updateField();
		panel.setFocusable(true);
		panel.requestFocus();
		panel.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_UP){
					f.moveUp();
					updateField();
				}

				if(e.getKeyCode() == KeyEvent.VK_DOWN){
					f.moveDown();
					updateField();
				}

				if(e.getKeyCode() == KeyEvent.VK_LEFT){
					f.moveLeft();
					updateField();
				}

				if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					f.moveRight();
					updateField();
				}
			}
			});
		}
	public void updateField() {
		if(!(f.field[0][0]==null)) {
			btn1.setText(Integer.toString(f.field[0][0].get()));
		}else btn1.setText(null);
		if(!(f.field[0][1]==null)) {
			btn2.setText(Integer.toString(f.field[0][1].get()));
		}else btn2.setText(null);
		if(!(f.field[0][2]==null)) {
			btn3.setText(Integer.toString(f.field[0][2].get()));
		}else btn3.setText(null);
		if(!(f.field[0][3]==null)) {
			btn4.setText(Integer.toString(f.field[0][3].get()));
		}else btn4.setText(null);
		if(!(f.field[1][0]==null)) {
			btn5.setText(Integer.toString(f.field[1][0].get()));
		}else btn5.setText(null);
		if(!(f.field[1][1]==null)) {
			btn6.setText(Integer.toString(f.field[1][1].get()));
		}else btn6.setText(null);
		if(!(f.field[1][2]==null)) {
			btn7.setText(Integer.toString(f.field[1][2].get()));
		}else btn7.setText(null);
		if(!(f.field[1][3]==null)) {
			btn8.setText(Integer.toString(f.field[1][3].get()));
		}else btn8.setText(null);
		if(!(f.field[2][0]==null)) {
			btn9.setText(Integer.toString(f.field[2][0].get()));
		}else btn9.setText(null);
		if(!(f.field[2][1]==null)) {
			btn10.setText(Integer.toString(f.field[2][1].get()));
		}else btn10.setText(null);
		if(!(f.field[2][2]==null)) {
			btn11.setText(Integer.toString(f.field[2][2].get()));
		}else btn11.setText(null);
		if(!(f.field[2][3]==null)) {
			btn12.setText(Integer.toString(f.field[2][3].get()));
		}else btn12.setText(null);
		if(!(f.field[3][0]==null)) {
			btn13.setText(Integer.toString(f.field[3][0].get()));
		}else btn13.setText(null);
		if(!(f.field[3][1]==null)) {
			btn14.setText(Integer.toString(f.field[3][1].get()));
		}else btn14.setText(null);
		if(!(f.field[3][2]==null)) {
			btn15.setText(Integer.toString(f.field[3][2].get()));
		}else btn15.setText(null);
		if(!(f.field[3][3]==null)) {
			btn16.setText(Integer.toString(f.field[3][3].get()));
		}else btn16.setText(null);
	}

	public static void main(String[] args) {
				GameGUI t =new GameGUI();
			}

		
}
