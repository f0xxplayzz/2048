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
import javax.swing.JLabel;
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
	JLabel score= new JLabel();
	Field f = new Field();


	public GameGUI(){
		panel.setLayout(new GridLayout(f.Y,f.X,3,3));
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
		panel.add(score);
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
		
			btn1.setText((f.field[0][0].get()));
		
			btn2.setText((f.field[0][1].get()));
		
			btn3.setText((f.field[0][2].get()));
		
			btn4.setText((f.field[0][3].get()));
		
			btn5.setText((f.field[1][0].get()));
		
			btn6.setText((f.field[1][1].get()));
		
			btn7.setText((f.field[1][2].get()));
		
			btn8.setText((f.field[1][3].get()));
		
			btn9.setText((f.field[2][0].get()));
		
			btn10.setText((f.field[2][1].get()));
		
			btn11.setText((f.field[2][2].get()));
		
			btn12.setText((f.field[2][3].get()));
		
			btn13.setText((f.field[3][0].get()));
		
			btn14.setText((f.field[3][1].get()));
		
			btn15.setText((f.field[3][2].get()));
		
			btn16.setText((f.field[3][3].get()));
			
			score.setText(Integer.toString(f.score));
	}

	public static void main(String[] args) {
				GameGUI t =new GameGUI();
			}

		
}
