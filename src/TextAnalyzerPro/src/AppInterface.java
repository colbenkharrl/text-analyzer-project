import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

enum ButtonTransition {
    MAIN_MENU, HELP_MENU, HELP_TEXT_MENU, ANALYZE, RECORD, HISTORY_MENU, ANALYZE_HELP, HISTORY_HELP, ABOUT
}

public class AppInterface extends JFrame {

    public enum State {MAIN_MENU, HELP_MENU, HELP_TEXT_MENU, RECORD, HISTORY, ANALYZING}

    private final int FRAME_WIDTH, FRAME_HEIGHT;
    private final JFileChooser fc;
    private final String SAVE_FILE = "src/Files/Records.txt";
    private File file;
    private JLabel title;
    private MainPanel mainP;
    private State state = State.MAIN_MENU;
    private RecordList records;
    private Record latest;
    int tType;

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
        FRAME_WIDTH = 600;
        FRAME_HEIGHT = 800;
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    private MainPanel updateCenterPanel() {
        MainPanel r;
        switch (state) {
            case ANALYZING:
                //r = new AnalysisPanel();
                r = new LoadingPanel();
                break;
            case MAIN_MENU:
                r = new MenuPanel();
                break;
            case HELP_MENU:
                r = new HelpPanel();
                break;
            case HELP_TEXT_MENU:
                r = new TextPanel(tType);
                break;
            case HISTORY:
                r = new HistoryPanel();
                break;
            case RECORD:
                r = new RecordPanel();
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

    public class MainPanel extends JPanel {

        public MainPanel() {
        }
    }

    private class MenuPanel extends MainPanel {
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
                        AppInterface.this.updateCenterPanel(State.ANALYZING);
                        AppInterface.this.updateCenterPanel(State.RECORD);
                    } else {
                        System.out.println("Open command cancelled by user.");
                    }
                }
            }
        }
    }

    private class HelpPanel extends MainPanel {
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

    private class LoadingPanel extends MainPanel {

        private MenuButton returnBtn;
        private String[] values;
        private JLabel text;
        private JPanel loadingP;

        public LoadingPanel() {
            setLayout(new BorderLayout());
            loadingP = new JPanel();
            text = new JLabel("Analyzing...");
            text.setFont(new Font("Verdana", Font.BOLD, 50));
            text.setHorizontalAlignment(JLabel.CENTER);
            text.setVerticalAlignment(JLabel.CENTER);
            loadingP.setLayout(new GridLayout(1, 1));
            loadingP.setPreferredSize(new Dimension(500, 500));
            loadingP.add(text);
            add(loadingP, BorderLayout.CENTER);
            returnBtn = new MenuButton("Cancel");
            returnBtn.addActionListener(new ButtonListener(ButtonTransition.MAIN_MENU));
            returnBtn.setPreferredSize(new Dimension(500, 100));
            add(returnBtn, BorderLayout.SOUTH);
            latest = Analyzer.Analyze(AppInterface.this.file);
            records.addRecord(latest);
            records.writeFile();
        }
    }

    private class RecordPanel extends MainPanel {
        JLabel one = new JLabel(), two = new JLabel(), three = new JLabel(), four = new JLabel(), five = new JLabel(), six = new JLabel(), seven = new JLabel(), eight = new JLabel();
        private JPanel shell = new JPanel();
        JButton done = new JButton("done");

        public RecordPanel() {
            shell.setLayout(new GridLayout(9, 1));
            one.setText("File: " + latest.getFileName());
            two.setText("Line count: " + latest.getLineCount());
            three.setText("Blank line count: " + latest.getBlankLineCount());
            four.setText("Space count: " + latest.getSpaceCount());
            five.setText("Word count: " + latest.getWordCount());
            six.setText("Average characters per line: " + latest.getAverageCharLine());
            seven.setText("Average characters per word: " + latest.getAverageCharWord());
            eight.setText("Most common words: " + latest.getCommonWordsString());
            done.addActionListener(new ButtonListener(ButtonTransition.MAIN_MENU));
            shell.add(one);
            shell.add(two);
            shell.add(three);
            shell.add(four);
            shell.add(five);
            shell.add(six);
            shell.add(seven);
            shell.add(eight);
            shell.add(done);
            add(new JLabel("File statistics: "), BorderLayout.NORTH);
            add(shell, BorderLayout.CENTER);
        }
    }

    private class HistoryPanel extends MainPanel {

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
            menu = new MenuButton("Return to Main Menu");
            menu.addActionListener(new ButtonListener(ButtonTransition.MAIN_MENU));
            midPanel.add(new RecordSelectionPanel(records.records));
            midPanel.add(new StatAggregationPanel());
            btnPanel.add(deleteHistory);
            btnPanel.add(menu);
            add(midPanel, BorderLayout.CENTER);
            add(btnPanel, BorderLayout.SOUTH);
        }

        private class RecordSelectionPanel extends JPanel {

            public RecordSelectionPanel(Vector<Record> v) {
                setLayout(new BorderLayout());
                recordJList = new JList<Record>(v);
                recordJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                recordJList.setLayoutOrientation(JList.VERTICAL);
                recordJList.setVisibleRowCount(-1);
                JScrollPane scroll = new JScrollPane(recordJList);
                add(new JLabel("Select a record to view:"), BorderLayout.NORTH);
                add(scroll, BorderLayout.CENTER);
            }
        }

        private class StatAggregationPanel extends JPanel {

            private Vector<String> stats;

            public StatAggregationPanel() {
                setLayout(new BorderLayout());
                //	stats placeholder
                stats = new Vector<String>();
                stats.addElement("Files seen: ");
                stats.addElement("Line count: ");
                stats.addElement("Blank line count: ");
                stats.addElement("Space count: ");
                stats.addElement("Word count: ");
                stats.addElement("Average characters per line: ");
                stats.addElement("Average word length: ");
                stats.addElement("Common words: ");
                statsList = new JList<String>(stats);
                statsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                statsList.setLayoutOrientation(JList.VERTICAL);
                statsList.setVisibleRowCount(-1);
                JScrollPane scroll = new JScrollPane(statsList);
                add(new JLabel("Average statistics: "), BorderLayout.NORTH);
                add(scroll, BorderLayout.CENTER);
            }
        }
    }

    public class MenuButton extends JButton {

        public MenuButton(String label) {
            setText(label);
            setFont(new Font("Verdana", Font.BOLD, 20));
        }
    }

    //	Listener for grid buttons
    private class ButtonListener implements ActionListener {

        ButtonTransition transition;

        public ButtonListener(ButtonTransition t) {
            transition = t;
        }

        public void actionPerformed(ActionEvent event) {
            System.out.println("Button transition to: " + transition);
            remove(mainP);
            switch (transition) {
                case ABOUT:
                    tType = 0;
                    state = State.HELP_TEXT_MENU;
                    break;
            
                case ANALYZE:
                    state = State.ANALYZING;
                    //set File in = directory
                    break;
                case ANALYZE_HELP:
                    tType = 1;
                    state = State.HELP_TEXT_MENU;
                    break;
                case HELP_MENU:
                    state = State.HELP_MENU;
                    break;
                case HISTORY_HELP:
                    tType = 2;
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


    private class TextPanel extends MainPanel {
        MenuButton done;
        JLabel data;

        public TextPanel(  int type) {
            //	set up grid layout
            String file = "";
            setLayout(new BorderLayout());
            switch (type) {
                case 1://analyzing
                    file = "analyzing.txt";
                    break;
                case 0://about
                    file = "about.txt";
                    break;
                case 2://hh
                    file = "history_help.txt";
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
            data = new JLabel(sb.toString());
            System.out.println(sb.toString());
            done = new MenuButton("Return to menu");
            done.addActionListener(new ButtonListener(ButtonTransition.MAIN_MENU));

            done.setFont(new Font("Verdana", Font.BOLD, 20));
            add(data, BorderLayout.CENTER);
            add(done, BorderLayout.SOUTH);
        }
    }
}