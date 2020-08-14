package package_2048_test;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyboardListener implements KeyListener {
	
	Field f;
	
	public KeyboardListener (Field startField) {
		f = startField;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyChar() == 'w') {
			f.Move(Direction.UP);
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyChar() == 's') {
			f.Move(Direction.DOWN);
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyChar() == 'a') {
			f.Move(Direction.LEFT);
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyChar() == 'd') {
			f.Move(Direction.RIGHT);
		}
	}

}
