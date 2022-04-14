import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class Maze extends JFrame implements ActionListener {
    // main JFrame components
    Container c;
    JPanel settingsPanel;
    //JPanel maze;
    BufferedImage mazeDisplay;

    // components for the settings panel
    JButton generateButton;
    JButton solveButton;
    JCheckBox showGeneration;
    JCheckBox showSolver;
    JLabel showGenerationLabel;
    JLabel showSolverLabel;
    JLabel speedLabel;
    JSlider mazeSpeedSlider;
    JLabel rowsLabel;
    JSlider mazeRowsSlider;
    JLabel columnsLabel;
    JSlider mazeColumnsSlider;

    // components for the maze panel
    JLabel visitedLabel;
    mazeCell testCell;
    MazePanel mazeGrid;
    mazeCell[][] mazeCells;



    public Maze(){
        super("Maze Solver");
        this.setSize(1100,1000);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));

        // initialize the slider panel and maze panel
        settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setBounds(825, 40, 300, 1000);



        //set up the maze display panel
        mazeGrid = new MazePanel(50, 50);
        mazeGrid.setBounds(10, 40, 750, 750);
        mazeGrid.setBackground(Color.BLACK);

        setupSettingsComp();
        // helper function to arrange the components in a GridBagLayout
        arrangeSettingsPanel();



        mazeGrid.add(Box.createRigidArea(new Dimension(0, 1550)));
        mazeGrid.add(Box.createRigidArea(new Dimension(650, 0)));
        mazeGrid.add(mazeGrid.percentVisited);
        mazeGrid.setSpeed(mazeSpeedSlider.getValue());

        c = getContentPane();
        c.add(mazeGrid);
        //c.add(Box.createRigidArea(new Dimension(600, 0)));
        c.add(settingsPanel);

        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);


    }

    private void setupSettingsComp() {
        // set up the sliders and there labels
        mazeSpeedSlider = new JSlider(JSlider.HORIZONTAL, 5, 100,50);
        mazeSpeedSlider.setPaintTicks(true);
        mazeSpeedSlider.setPaintLabels(true);
        mazeSpeedSlider.setInverted(true);
        mazeSpeedSlider.setMinorTickSpacing(10);
        mazeSpeedSlider.setMajorTickSpacing(30);
        mazeSpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                speedLabel.setText("Delay Speed: " + mazeSpeedSlider.getValue());
                mazeGrid.setSpeed(mazeSpeedSlider.getValue());
                repaint();
            }
        });

        mazeRowsSlider = new JSlider(JSlider.HORIZONTAL, 10, 100,50);
        mazeRowsSlider.setPaintTicks(true);
        mazeRowsSlider.setPaintLabels(true);
        mazeRowsSlider.setMinorTickSpacing(10);
        mazeRowsSlider.setMajorTickSpacing(30);
        mazeRowsSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                rowsLabel.setText("Rows: " + mazeRowsSlider.getValue());
                mazeGrid.changeMazeSize(mazeRowsSlider.getValue(), mazeColumnsSlider.getValue());
                c.repaint();
                repaint();
            }
        });

        mazeColumnsSlider = new JSlider(JSlider.HORIZONTAL, 10, 100,50);
        mazeColumnsSlider.setPaintTicks(true);
        mazeColumnsSlider.setPaintLabels(true);
        mazeColumnsSlider.setMinorTickSpacing(10);
        mazeColumnsSlider.setMajorTickSpacing(30);
        mazeColumnsSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                columnsLabel.setText("Columns: " + mazeColumnsSlider.getValue());
                mazeGrid.changeMazeSize(mazeRowsSlider.getValue(), mazeColumnsSlider.getValue());
                c.repaint();
                repaint();
            }
        });

        speedLabel = new JLabel("Delay Speed:" + mazeSpeedSlider.getValue());
        speedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rowsLabel = new JLabel("Rows:" + mazeSpeedSlider.getValue());
        rowsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        columnsLabel = new JLabel("Columns:" + mazeColumnsSlider.getValue());
        columnsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        showGenerationLabel = new JLabel("Show Generation");
        showSolverLabel = new JLabel("Show Solver");
        showGeneration = new JCheckBox();
        showGeneration.setSelected(true);
        showGeneration.addActionListener(this);
        showSolver = new JCheckBox();
        showSolver.setSelected(true);
        generateButton = new JButton("Generate");
        generateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateButton.addActionListener(this);
        solveButton = new JButton("Solve");
        solveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        solveButton.addActionListener(this);
    }

    private void arrangeSettingsPanel() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 0, 10, 0);
        //set up the settingsPanel
        g.gridwidth = 1;
        g.gridx = 0;
        g.gridy = 0;
        settingsPanel.add(generateButton, g);
        g.gridx = 1;
        g.anchor = GridBagConstraints.EAST;
        settingsPanel.add(showGeneration, g);
        g.gridx = 2;
        g.anchor = GridBagConstraints.WEST;
        settingsPanel.add(showGenerationLabel, g);
        g.anchor = GridBagConstraints.CENTER;
        g.gridx = 1;
        g.gridy = 2;
        settingsPanel.add(speedLabel, g);
        g.gridx = 0;
        g.gridy = 3;
        g.gridwidth = 3;
        settingsPanel.add(mazeSpeedSlider, g);
        g.gridwidth = 1;
        g.gridx = 1;
        g.gridy = 5;
        settingsPanel.add(rowsLabel, g);
        g.gridx = 0;
        g.gridy = 6;
        g.gridwidth = 3;
        settingsPanel.add(mazeRowsSlider, g);
        g.gridwidth = 1;
        g.gridx = 1;
        g.gridy = 8;
        settingsPanel.add(columnsLabel, g);
        g.gridx = 0;
        g.gridy = 9;
        g.gridwidth = 3;
        settingsPanel.add(mazeColumnsSlider, g);
        g.gridwidth = 1;
        g.gridx = 0;
        g.gridy = 11;
        settingsPanel.add(solveButton, g);
        g.gridx = 1;
        g.anchor = GridBagConstraints.EAST;
        settingsPanel.add(showSolver, g);
        g.gridx = 2;
        g.anchor = GridBagConstraints.WEST;
        settingsPanel.add(showSolverLabel, g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == generateButton){
            if(showGeneration.isSelected())
                mazeGrid.generateMazeAnimated();
            else
                mazeGrid.generateMaze();
        }
        else if(e.getSource() == showGeneration){
            mazeGrid.setShowGeneration(showGeneration.isSelected());
        }
        else if(e.getSource() == showSolver){
            mazeGrid.setShowSolving(showSolver.isSelected());
        }
        else if(e.getSource() == solveButton){
            if(showSolver.isSelected())
                mazeGrid.solveMazeAnimated();
            else
                mazeGrid.solveMaze();
        }
    }

    public static void main(String[] args) {
        Maze app = new Maze();
        app.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                }
        );
    }
}
