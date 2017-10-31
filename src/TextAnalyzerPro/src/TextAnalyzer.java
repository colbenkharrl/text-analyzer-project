import javax.swing.JFrame;

public class TextAnalyzer {
	
	public static void main(String[] args) {
		
		AppInterface app = new AppInterface();
		app.setTitle("Text Analyzer Pro");
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
	}
}