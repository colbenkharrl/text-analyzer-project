import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MenuButton extends JButton {
	private ButtonListener listener;
	private ButtonTransition transition;
	
	public MenuButton(String label, ButtonTransition t) {
		transition = t;
		setText(label);
		setFont(new Font("Verdana", Font.BOLD, 20));
		listener = new ButtonListener();
		addActionListener(listener);
	}
	
	//	Listener for grid buttons
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.out.println("Button transition to: " + transition);
			switch(transition) {
			case ABOUT:
				break;
			case ANALYZE:
				break;
			case ANALYZE_HELP:
				break;
			case HELP_MENU:
				break;
			case HISTORY_HELP:
				break;
			case HISTORY_MENU:
				break;
			case MAIN_MENU:
				break;
			case RECORD:
				break;
			default:
				break;
			}
		}
	}
}
