import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import javax.swing.*;

enum ButtonTransition {
	MAIN_MENU, HELP_MENU, ANALYZE, RECORD, HISTORY_MENU, ANALYZE_HELP, HISTORY_HELP, ABOUT
}

public class AppInterface extends JFrame {
	
	public enum State { MAIN_MENU, HELP_MENU, RECORD, HISTORY }
		
	private final int FRAME_WIDTH, FRAME_HEIGHT;
	private JLabel title;
	private MainPanel mainP;
	private State state = State.MAIN_MENU;
	
	public AppInterface() {
		
		//	set title
		title = new JLabel("Text Analyzer Pro");
		title.setFont(new Font("Verdana", Font.BOLD, 50));
		title.setHorizontalAlignment(JLabel.CENTER);
		add(title, BorderLayout.NORTH);
		
		//	create menu
		mainP = updateCenterPanel();
		add(mainP, BorderLayout.CENTER);
		
		//	app frame dimensions
		FRAME_WIDTH = 600;
		FRAME_HEIGHT = 800;
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
	}
	
	private MainPanel updateCenterPanel() {
		
		MainPanel r;
		switch(state) {
		case MAIN_MENU:
			r = new MenuPanel();
			break;
		case HELP_MENU:
			r = new HelpPanel();
			break;
		case RECORD:
			r = new RecordPanel();
			break;
		case HISTORY:
			r = new HistoryPanel();
			break;
		default:
			r = new MenuPanel();
			break;
		}
		return r;
	}
}