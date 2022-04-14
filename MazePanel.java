import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Stack;

// this is where the depth first search algo. should run
public class MazePanel extends JPanel {
    private mazeCell[][] mazeCells;
    private mazeCell startingCell;
    private mazeCell nextCell;
    int width;
    int height;
    int speed;
    float numVisited = 0;
    public JLabel percentVisited;
    private Stack<mazeCell> cellsVisited = new Stack<mazeCell>();
    Boolean isFinished;
    Boolean isSolved;
    Boolean solving;
    Boolean startFlag;
    Boolean showGeneration;
    Boolean showSolving;
    private mazeCell currentCell;

    public MazePanel(int newWidth, int newHeight){
        width = newWidth;
        height = newHeight;
        isFinished = true;
        //create a grid based on the given dimension
        changeMazeSize(height, width);

        // collect the adjacent cells for each node
        getAdjacentNodes();

        currentCell = mazeCells[0][0];
        cellsVisited.push(currentCell);
        startFlag = true;
        showGeneration = true;
        showSolving = true;
        solving = false;
        this.setVisible(true);
        percentVisited = new JLabel("Maze Visited: 0%");
        percentVisited.setForeground(Color.white);
    }


    private void getAdjacentNodes() {
        for(int i=0; i<height; i++){
            for(int j=0; j< width; j++){
                int x = mazeCells[i][j].getX();
                int y = mazeCells[i][j].getY();
                // cell is not on the top or the bottom row and not in a corner
                if(((i != 0 && i != height-1) && (j > 0 && j < width)) && (j != 0 && j != width-1)) {
                    mazeCells[i][j].adjacentNodes.add(mazeCells[i-1][j]); // cell above
                    mazeCells[i][j].adjacentNodes.add(mazeCells[i][j-1]); // cell left
                    mazeCells[i][j].adjacentNodes.add(mazeCells[i][j+1]); // cell right
                    mazeCells[i][j].adjacentNodes.add(mazeCells[i+1][j]); // cell below
                    mazeCells[i][j].increaseNumNotVisited(4);
                }
                //cell is on either the left column or right column
                else if(((j == 0) || (j == width-1)) && (i != 0 && i != height-1)){
                    if(j == 0) { // left column
                        mazeCells[i][j].adjacentNodes.add(mazeCells[i][j+1]); // cell right
                        mazeCells[i][j].adjacentNodes.add(mazeCells[i+1][j]); // cell below
                        mazeCells[i][j].adjacentNodes.add(mazeCells[i-1][j]); // cell above
                        mazeCells[i][j].increaseNumNotVisited(3);
                    }
                    // right column
                    else{
                        mazeCells[i][j].adjacentNodes.add(mazeCells[i+1][j]); // cell below
                        mazeCells[i][j].adjacentNodes.add(mazeCells[i-1][j]); // cell above
                        mazeCells[i][j].adjacentNodes.add(mazeCells[i][j-1]); // cell left
                        mazeCells[i][j].increaseNumNotVisited(3);
                    }
                }
                // cell is on the top row and not in a corner
                else if(i == 0 && (j != 0 && j != width-1)){
                    mazeCells[i][j].adjacentNodes.add(mazeCells[i][j-1]); // cell left
                    mazeCells[i][j].adjacentNodes.add(mazeCells[i][j+1]); // cell right
                    mazeCells[i][j].adjacentNodes.add(mazeCells[i+1][j]); // cell below
                    mazeCells[i][j].increaseNumNotVisited(3);
                }
                // cell is on the bottom row and not in a corner
                else if((j!=0 && j!=width-1) && i==height-1){
                    mazeCells[i][j].adjacentNodes.add(mazeCells[i][j-1]); // cell left
                    mazeCells[i][j].adjacentNodes.add(mazeCells[i][j+1]); // cell right
                    mazeCells[i][j].adjacentNodes.add(mazeCells[i-1][j]); // cell above
                    mazeCells[i][j].increaseNumNotVisited(3);
                }
                // top left corner or bottom left corner
                else if(j == 0 && (i == 0 || i == height-1)){
                    // top left corner
                    if(i == 0){
                        mazeCells[i][j].adjacentNodes.add(mazeCells[i+1][j]); // cell below
                        mazeCells[i][j].adjacentNodes.add(mazeCells[i][j+1]); // cell right
                        mazeCells[i][j].increaseNumNotVisited(2);
                    }
                    // bottom left corner
                    else if(i == height-1){
                        mazeCells[i][j].adjacentNodes.add(mazeCells[i][j+1]); // cell right
                        mazeCells[i][j].adjacentNodes.add(mazeCells[i-1][j]); // cell above
                        mazeCells[i][j].increaseNumNotVisited(2);
                    }
                }
                // top right corner or bottom right corner
                else if(j == width-1 && (i == 0 || i == height-1)){
                    // top right corner
                    if(i == 0){
                        mazeCells[i][j].adjacentNodes.add(mazeCells[i][j-1]); // cell left
                        mazeCells[i][j].adjacentNodes.add(mazeCells[i+1][j]); // cell below
                        mazeCells[i][j].increaseNumNotVisited(2);
                    }
                    // bottom right corner
                    else if(i == height-1){
                        mazeCells[i][j].adjacentNodes.add(mazeCells[i-1][j]); // cell above
                        mazeCells[i][j].adjacentNodes.add(mazeCells[i][j-1]); // cell left
                        mazeCells[i][j].increaseNumNotVisited(2);
                    }
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
       for(int i=0; i<height; i++){
           for(int j=0; j<width; j++){
                   if (i == 0 && j == 0)
                       mazeCells[i][j].drawCell(g, true, false, false);
                   else if (i == height - 1 && j == width - 1)
                       mazeCells[i][j].drawCell(g, false, true, false);
                   else
                       mazeCells[i][j].drawCell(g, false, false, false);

                   if(mazeCells[i][j].isPath())
                       mazeCells[i][j].drawCell(g, false, false, true);
           }
       }
       percentVisited.repaint();
    }


    private void breakWalls(mazeCell currentCell, mazeCell nextCell) {
        if(currentCell.getX() - nextCell.getX() == -1) {
            currentCell.setRightWall(false);
            nextCell.setLeftWall(false);
        }
        else if(currentCell.getX() - nextCell.getX() == 1){
            currentCell.setLeftWall(false);
            nextCell.setRightWall(false);
        }
        else if(currentCell.getY() - nextCell.getY() == -1){
            currentCell.setBottomWall(false);
            nextCell.setTopWall(false);
        }
        else if(currentCell.getY() - nextCell.getY() == 1){
            currentCell.setTopWall(false);
            nextCell.setBottomWall(false);
        }
        repaint();
    }


    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public mazeCell getStartingCell() {
        return startingCell;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void generateMazeAnimated(){
        if(isFinished){
            resetMaze();
        }
        isFinished = false;
        Timer mazeTimer = new Timer(speed, null);
        mazeTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<mazeCell> notVisited = new ArrayList<mazeCell>();
                if(!currentCell.isVisited()) {
                    cellsVisited.push(currentCell);
                    currentCell.setVisited(true);
                }

                for (int i = 0; i < currentCell.adjacentNodes.size(); i++) {
                    if (!currentCell.adjacentNodes.get(i).isVisited()) {
                        notVisited.add(currentCell.adjacentNodes.get(i));
                    }
                }
                if(!notVisited.isEmpty()) {
                    currentCell.decreaseNumNotVisited();
                    int index = (int) Math.floor((Math.random() * notVisited.size()));
                    nextCell = notVisited.get(index);
                    notVisited.remove(index);
                    breakWalls(currentCell, nextCell);
                    currentCell = nextCell;
                }
                // there are no unvisited neighbors
                //if (notVisited.isEmpty()) {
                else
                    currentCell = cellsVisited.pop();
                //currentCell.setVisited(true);
                //cellsVisited.push(currentCell);
                mazeTimer.setDelay(speed);
                if(cellsVisited.isEmpty() || !showGeneration) {
                    mazeTimer.stop();
                    if(!showGeneration && !cellsVisited.isEmpty()){
                        generateMaze();
                    }
                    else {
                        isFinished = true;
                        System.out.println("Done");
                    }
                }

            }
        });
        mazeTimer.start();
    }

    public void generateMaze(){
        if(isFinished){
            resetMaze();
        }
        isFinished = false;
        while(!cellsVisited.isEmpty()){
                ArrayList<mazeCell> notVisited = new ArrayList<mazeCell>();
                if(!currentCell.isVisited()) {
                    cellsVisited.push(currentCell);
                    currentCell.setVisited(true);
                }

                for (int i = 0; i < currentCell.adjacentNodes.size(); i++) {
                    if (!currentCell.adjacentNodes.get(i).isVisited()) {
                        notVisited.add(currentCell.adjacentNodes.get(i));
                    }
                }
                if(!notVisited.isEmpty()) {
                    currentCell.decreaseNumNotVisited();
                    int index = (int) Math.floor((Math.random() * notVisited.size()));
                    nextCell = notVisited.get(index);
                    notVisited.remove(index);
                    breakWalls(currentCell, nextCell);
                    currentCell = nextCell;
                }
                else
                    currentCell = cellsVisited.pop();
                if(cellsVisited.isEmpty()) {
                    isFinished = true;
                    System.out.println("Done");
                }

            }
    }

    public void changeMazeSize(int newHeight, int newWidth){
        if(isFinished) {
            height = newHeight;
            width = newWidth;
            mazeCells = new mazeCell[height][width];

            int scale;
            if(height > 76 || width > 77)
                scale = 7;
            else if((height < 55 && height > 45) && (width < 55 && width > 45))
                scale = 10;
            else if((height < 55 && height > 20) && (width < 45 && height > 20))
                scale = 15;
            else if(width < 20 && height < 20)
                scale = 35;
            else
                scale = 10;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    mazeCells[y][x] = new mazeCell(x, y, scale);
                }
            }
            getAdjacentNodes();

            cellsVisited.clear();
            startingCell = mazeCells[0][0];
            mazeCells[height-1][width-1].setFinal(true);
            currentCell = mazeCells[0][0];
            cellsVisited.push(currentCell);

            repaint();
        }
    }

    public void solveMaze(){
        float totalCells = height * width;
        if(!solving) {
            currentCell = startingCell;
            cellsVisited.clear();
            numVisited = 0;
            cellsVisited.push(currentCell);
            isSolved = false;
            solving = true;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    mazeCells[i][j].setVisited(false);
                }
            }
        }

        int x = currentCell.getX();
        int y = currentCell.getY();

        while(!cellsVisited.isEmpty() && !(x == width-1 && y == height-1)){
            ArrayList<mazeCell> notVisited = new ArrayList<mazeCell>();
            if(!currentCell.isVisited()) {
                cellsVisited.push(currentCell);
                currentCell.setVisited(true);
                numVisited++;
            }
            else{
                currentCell.setPath(false);
            }

            x = currentCell.getX();
            y = currentCell.getY();

            // left wall is gone
            if(!currentCell.isLeftWall()){
                if(!mazeCells[y][x-1].isVisited())
                    notVisited.add(mazeCells[y][x-1]);
            }
            // right wall is gone
            if(!currentCell.isRightWall()){
                if(!mazeCells[y][x+1].isVisited())
                    notVisited.add(mazeCells[y][x+1]);
            }
            // top wall is gone
            if(!currentCell.isTopWall()){
                if(!mazeCells[y-1][x].isVisited())
                    notVisited.add(mazeCells[y-1][x]);
            }
            // bottom wall is gone
            if(!currentCell.isBottomWall()){
                if(!mazeCells[y+1][x].isVisited())
                    notVisited.add(mazeCells[y+1][x]);
            }

            if(!notVisited.isEmpty()) {
                int index = (int) Math.floor((Math.random() * notVisited.size()));
                nextCell = notVisited.get(index);
                currentCell = nextCell;
                currentCell.setPath(true);
            }
            // there are no unvisited neighbors
            //if (notVisited.isEmpty()) {
            else {
                currentCell.setPath(false);
                currentCell = cellsVisited.pop();
                currentCell.setPath(true);
            }
            repaint();
        }
        int visited = (int)((numVisited/totalCells) * 100);
        percentVisited.setText("Maze Visited: " + visited + "%");
        percentVisited.repaint();
        repaint();
        solving = false;
        isSolved = true;

    }

    public void solveMazeAnimated(){
        isSolved = false;
        solving = true;
        float totalCells = height * width;
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                mazeCells[i][j].setVisited(false);
            }
        }
        Timer solveTimer = new Timer(speed, null);
        solveTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<mazeCell> notVisited = new ArrayList<mazeCell>();
                if(!currentCell.isVisited()) {
                    cellsVisited.push(currentCell);
                    currentCell.setVisited(true);
                    currentCell.setPath(true);
                    numVisited++;
                }
                else
                    currentCell.setPath(false);

                int x = currentCell.getX();
                int y = currentCell.getY();

                // left wall is gone
                if(!currentCell.isLeftWall()){
                    if(!mazeCells[y][x-1].isVisited())
                        notVisited.add(mazeCells[y][x-1]);
                }
                // right wall is gone
                if(!currentCell.isRightWall()){
                    if(!mazeCells[y][x+1].isVisited())
                        notVisited.add(mazeCells[y][x+1]);
                }
                // top wall is gone
                if(!currentCell.isTopWall()){
                    if(!mazeCells[y-1][x].isVisited())
                        notVisited.add(mazeCells[y-1][x]);
                }
                // bottom wall is gone
                if(!currentCell.isBottomWall()){
                    if(!mazeCells[y+1][x].isVisited())
                        notVisited.add(mazeCells[y+1][x]);
                }

                if(!notVisited.isEmpty()) {
                    int index = (int) Math.floor((Math.random() * notVisited.size()));
                    nextCell = notVisited.get(index);
                    currentCell = nextCell;
                    currentCell.setPath(true);
                    repaint();
                }
                // there are no unvisited neighbors
                //if (notVisited.isEmpty()) {
                else {
                    currentCell.setPath(false);
                    currentCell = cellsVisited.pop();
                    currentCell.setPath(false);
                    repaint();
                }
                //currentCell.setVisited(true);
                //cellsVisited.push(currentCell);

                if(cellsVisited.isEmpty() || (y == height-1 && x == width-1) || (!showSolving && !cellsVisited.isEmpty())) {
                    solveTimer.stop();
                    if(!showSolving && !cellsVisited.isEmpty())
                        solveMaze();
                    solving = false;
                }
                else{
                    solveTimer.setDelay(speed);
                    int visited = (int)((numVisited/totalCells) * 100);
                    percentVisited.setText("Maze Visited: " + visited + "%");
                    percentVisited.repaint();
                    repaint();
                }
                repaint();
            }
        });
        solveTimer.start();

    }


    public void setShowGeneration(Boolean showGeneration) {
        this.showGeneration = showGeneration;
    }

    private void resetMaze(){
        changeMazeSize(height,width);
    }

    public void setShowSolving(Boolean showSolving) {
        this.showSolving = showSolving;
    }
}
