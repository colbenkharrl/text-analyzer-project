import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class AppInterface extends JFrame {

    private enum State {MAIN_MENU, HELP_MENU, HELP_TEXT_MENU, RECORD, HISTORY, ANALYZING}
    private enum HelpType {ABOUT, ANALYZE, HISTORY}
    private enum ButtonTransition {
        MAIN_MENU, HELP_MENU, HELP_TEXT_MENU, ANALYZE, RECORD, HISTORY_MENU, ANALYZE_HELP, HISTORY_HELP, ABOUT
    }

    private final int FRAME_WIDTH, FRAME_HEIGHT;
    private final JFileChooser fc;
    private final String SAVE_FILE = "src/Files/Records.txt", CLEAR_LINE = "\n\n";
    private File file;
    private JLabel title;
    private JPanel mainP;
    private State state = State.MAIN_MENU;
    private HelpType help;
    private RecordList records;

    public AppInterface() {
        //	initialize file chooser
        fc = new JFileChooser();
        records = new RecordList(SAVE_FILE);
        //	set title
        title = new JLabel("Text Analyzer Pro");
        title.setFont(new Font("Verdana", Font.BOLD, 50));
        title.setHorizontalAlignment(JLabel.CENTER);
        add(title, BorderLayout.NORTH);
        //	create menu
        mainP = updateCenterPanel();
        add(mainP, BorderLayout.CENTER);
        //	app frame dimensions
        FRAME_WIDTH = 550;
        FRAME_HEIGHT = 600;
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    private JPanel updateCenterPanel() {
        JPanel r;
        switch (state) {
            case MAIN_MENU:
                r = new MenuPanel();
                break;
            case HELP_MENU:
                r = new HelpPanel();
                break;
            case HELP_TEXT_MENU:
                r = new TextPanel();
                break;
            case HISTORY:
                r = new HistoryPanel();
                break;
            case RECORD:
                r = new RecordPanel(records.records.lastElement());
                break;
            default:
                r = new MenuPanel();
                break;
        }
        return r;
    }

    private void updateCenterPanel(State s) {
        remove(mainP);
        state = s;
        mainP = updateCenterPanel();
        add(mainP, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    private void updateCenterPanel(Record r) {
    	remove(mainP);
        mainP = new RecordPanel(r);
        add(mainP, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private class MenuPanel extends JPanel {
        private MenuButton analyzeBtn, historyBtn, helpBtn;

        public MenuPanel() {
            //	set up grid layout
            setLayout(new GridLayout(3, 1));
            //	analyze button
            analyzeBtn = new MenuButton("Analyze");
            analyzeBtn.addActionListener(new AnalyzeListener());
            add(analyzeBtn);
            //	history button
            historyBtn = new MenuButton("View History");
            historyBtn.addActionListener(new ButtonListener(ButtonTransition.HISTORY_MENU));
            add(historyBtn);
            //	help button
            helpBtn = new MenuButton("Get Help");
            helpBtn.addActionListener(new ButtonListener(ButtonTransition.HELP_MENU));
            add(helpBtn);
        }

        private class AnalyzeListener implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                System.out.println("Choosing file");
                if (event.getSource() == analyzeBtn) {
                    int returnVal = fc.showOpenDialog(MenuPanel.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        AppInterface.this.file = fc.getSelectedFile();
                        //This is where a real application would open the file.
                        System.out.println("File chosen: " + AppInterface.this.file.getPath());
                        //	transition to loading panel
                        try {
                        	records.addRecord(Analyzer.Analyze(AppInterface.this.file));
                            records.writeFile();
                            AppInterface.this.updateCenterPanel(State.RECORD);
                        } catch (Exception e) {
                        	System.out.println("Exception caught in app interface");      
                        	JOptionPane.showMessageDialog(AppInterface.this, "An error occured, aborting analysis.");
                        }
                    } else {
                        System.out.println("Open command cancelled by user.");
                    }
                }
            }
        }
    }

    private class HelpPanel extends JPanel {
        private MenuButton aboutBtn, analyzeHelpBtn, historyHelpBtn, returnBtn;

        public HelpPanel() {
            //	set up grid layout
            setLayout(new GridLayout(4, 1));
            //	about button
            aboutBtn = new MenuButton("About");
            aboutBtn.addActionListener(new ButtonListener(ButtonTransition.ABOUT));
            add(aboutBtn);
            //	analyze help button
            analyzeHelpBtn = new MenuButton("Analyze Help");
            analyzeHelpBtn.addActionListener(new ButtonListener(ButtonTransition.ANALYZE_HELP));
            add(analyzeHelpBtn);
            //	history help button
            historyHelpBtn = new MenuButton("History Help");
            historyHelpBtn.addActionListener(new ButtonListener(ButtonTransition.HISTORY_HELP));
            add(historyHelpBtn);
            //	main menu button
            returnBtn = new MenuButton("Return to Menu");
            returnBtn.addActionListener(new ButtonListener(ButtonTransition.MAIN_MENU));
            add(returnBtn);
        }
    }

    private class RecordPanel extends JPanel {
        private MenuButton done;
        private Record record;
        private String stats;
        private JScrollPane scroll;
        private JTextArea text;

        public RecordPanel(Record r) {
        	record = r;
        	stats = "Analysis results:\n\n";
        	createRecord();
        	if (state == State.HISTORY) {
            	done.addActionListener(new ButtonListener(ButtonTransition.HISTORY_MENU));
        	} else {
            	done.addActionListener(new ButtonListener(ButtonTransition.MAIN_MENU));
        	}
        }
        
        private void createRecord() {
        	
        	setLayout(new BorderLayout());
        	
        	text = new JTextArea(stats + buildStats());
        	text.setEditable(false);
        	scroll = new JScrollPane(text);
        	
        	done = new MenuButton("Done");
        	done.setPreferredSize(new Dimension(500, 100));
            
        	add(scroll, BorderLayout.CENTER);
            add(done, BorderLayout.SOUTH);
        }
        
        private String buildStats() {
        	return 	"File:\t\t " + record.getFileName() + CLEAR_LINE +
        			"Line count:\t\t " + record.getLineCount() + CLEAR_LINE + 
        			"Blank line count:\t " + record.getBlankLineCount() + CLEAR_LINE + 
        			"Space count:\t\t " + record.getSpaceCount() + CLEAR_LINE + 
        			"Word count:\t\t " + record.getWordCount() + CLEAR_LINE +
        			"Average characters per line:\t " + record.getAverageCharLine() + CLEAR_LINE +
        			"Average characters per word:\t " + record.getAverageCharWord() + CLEAR_LINE + 
        			"Most common words:\t " + record.getCommonWordsString();
        }
    }

    private class HistoryPanel extends JPanel {

        private JList<Record> recordJList;
        private JList<String> statsList;
        private JPanel midPanel, btnPanel;
        private MenuButton deleteHistory, menu;

        public HistoryPanel() {
            setLayout(new BorderLayout());
            midPanel = new JPanel(new GridLayout(2, 1));
            btnPanel = new JPanel(new GridLayout(1, 2));
            btnPanel.setPreferredSize(new Dimension(600, 100));
            deleteHistory = new MenuButton("Delete History");
            deleteHistory.addActionListener(new Deleter());
            menu = new MenuButton("Return to Main Menu");
            menu.addActionListener(new ButtonListener(ButtonTransition.MAIN_MENU));
            midPanel.add(new RecordSelectionPanel(records.records));
            midPanel.add(new StatAggregationPanel());
            btnPanel.add(deleteHistory);
            btnPanel.add(menu);
            add(midPanel, BorderLayout.CENTER);
            add(btnPanel, BorderLayout.SOUTH);
        }
        
        private class Deleter implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				records.empty();
				AppInterface.this.updateCenterPanel(State.HISTORY);
				JOptionPane.showMessageDialog(AppInterface.this, "History deleted.");
			}
        }

        private class RecordSelectionPanel extends JPanel {

            public RecordSelectionPanel(Vector<Record> v) {
                setLayout(new BorderLayout());
                recordJList = new JList<Record>(v);
                recordJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                recordJList.setLayoutOrientation(JList.VERTICAL);
                recordJList.setVisibleRowCount(-1);
                
                recordJList.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent evt) {
                        JList<Record> list = (JList<Record>)evt.getSource();
                        if (evt.getClickCount() == 2) {
                            // Double-click detected
                            int index = list.locationToIndex(evt.getPoint());
                            updateCenterPanel(records.records.get(index));
                        }
                    }
                });
                
                JScrollPane scroll = new JScrollPane(recordJList);
                add(new JLabel("Double-click a record to view:"), BorderLayout.NORTH);
                add(scroll, BorderLayout.CENTER);
            }
        }

        private class StatAggregationPanel extends JPanel {

            public StatAggregationPanel() {
            	try {
	                setLayout(new BorderLayout());
	                statsList = new JList<String>(recordToStats(Analyzer.getAverageStats(records.records)));
	                statsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	                statsList.setLayoutOrientation(JList.VERTICAL);
	                statsList.setVisibleRowCount(-1);
	                JScrollPane scroll = new JScrollPane(statsList);
	                add(new JLabel("Average statistics: "), BorderLayout.NORTH);
	                add(scroll, BorderLayout.CENTER);
            	} catch (Exception e) {
            		e.printStackTrace();
            		System.out.println("Exception caught in app interface");      
                	JOptionPane.showMessageDialog(AppInterface.this, "An error occured loading your history. Your history will be cleared.");
                	records.empty();
                	AppInterface.this.updateCenterPanel(State.HISTORY);
	            }
            }
            
            private Vector<String> recordToStats(Record r) {
            	Vector<String> stats = new Vector<String>();
            	
            	stats.add("Line Count:\t\t " + r.getLineCount());
            	stats.add("Blank Line Count:\t " + r.getBlankLineCount());
            	stats.add("Space Count:\t\t " + r.getSpaceCount());
            	stats.add("Word Count:\t\t " + r.getWordCount());
            	stats.add("Average characters per word:\t " + r.getAverageCharWord());
            	stats.add("Average characters per line:\t " + r.getAverageCharLine());
            	stats.add("Common words:\t " + r.getCommonWordsString());
            	
            	return stats;
            }
        }
    }

    private class MenuButton extends JButton {

        public MenuButton(String label) {
            setText(label);
            setFont(new Font("Verdana", Font.BOLD, 20));
        }
    }

    //	Listener for grid buttons
    private class ButtonListener implements ActionListener {

    	private ButtonTransition transition;

        public ButtonListener(ButtonTransition t) {
            transition = t;
        }

        public void actionPerformed(ActionEvent event) {
            System.out.println("Button transition to: " + transition);
            remove(mainP);
            switch (transition) {
                case ABOUT:
                    help = HelpType.ABOUT;
                    state = State.HELP_TEXT_MENU;
                    break;
            
                case ANALYZE:
                    state = State.ANALYZING;
                    //set File in = directory
                    break;
                case ANALYZE_HELP:
                    help = HelpType.ANALYZE;
                    state = State.HELP_TEXT_MENU;
                    break;
                case HELP_MENU:
                    state = State.HELP_MENU;
                    break;
                case HISTORY_HELP:
                    help = HelpType.HISTORY;
                    state = State.HELP_TEXT_MENU;
                    break;
                case HISTORY_MENU:
                    state = State.HISTORY;
                    break;
                case MAIN_MENU:
                    state = State.MAIN_MENU;
                    break;
                case RECORD:
                    break;
                default:
                    break;
            }
            mainP = updateCenterPanel();
            add(mainP, BorderLayout.CENTER);
            revalidate();
            repaint();
        }
    }


    private class TextPanel extends JPanel {
    	private MenuButton done;
    	private JTextArea data;
    	private JScrollPane scroll;

        public TextPanel() {
            //	set up grid layout
            String file = "";
            setLayout(new BorderLayout());
            switch (help) {
                case ANALYZE://analyzing
                    file = "src/Files/analyzing.txt";
                    break;
                case ABOUT://about
                    file = "src/Files/about.txt";
                    break;
                case HISTORY://hh
                    file = "src/Files/history_help.txt";
                    break;
            }
            BufferedReader br = null;
            FileReader fr = null;
            StringBuilder sb = new StringBuilder();
            try {
                fr = new FileReader(file);
                br = new BufferedReader(fr);
                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) {
                    sb.append(sCurrentLine);
                    sb.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null)
                        br.close();
                    if (fr != null)
                        fr.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            data = new JTextArea(sb.toString());
            data.setEditable(false);
            
            scroll = new JScrollPane(data);
            
            done = new MenuButton("Return to menu");
            done.addActionListener(new ButtonListener(ButtonTransition.HELP_MENU));
            done.setPreferredSize(new Dimension(500, 100));

            done.setFont(new Font("Verdana", Font.BOLD, 20));
            add(scroll, BorderLayout.CENTER);
            add(done, BorderLayout.SOUTH);
        }
    }
}