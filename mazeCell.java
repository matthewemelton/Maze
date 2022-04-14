import java.awt.*;
import java.util.ArrayList;

public class mazeCell{
    int x;
    int y;
    int scale;
    int numNotVisited = 0;
    public ArrayList<mazeCell> adjacentNodes;
    public Boolean[] pathsTraveled = {false, false, false, false};
    boolean visited;
    boolean traveled;
    boolean path;
    boolean rightWall;
    boolean leftWall;
    boolean topWall;
    boolean bottomWall;
    boolean end;

    public mazeCell(int x, int y, int scale){
        this.x = x;
        this.y = y;
        this.scale = scale;
        rightWall = true;
        leftWall = true;
        bottomWall = true;
        topWall = true;
        traveled = false;
        path = false;
        adjacentNodes = new ArrayList<mazeCell>();
    }

    // get methods
    public boolean isVisited() {
        return visited;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isPath() {
        return path;
    }

    public boolean isTraveled() {
        return traveled;
    }

    public boolean isBottomWall() {
        return bottomWall;
    }

    public boolean isTopWall() {
        return topWall;
    }

    public boolean isLeftWall() {
        return leftWall;
    }

    public boolean isRightWall() {
        return rightWall;
    }

    // set methods
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setBottomWall(boolean bottomWall) {
        this.bottomWall = bottomWall;
    }

    public void setLeftWall(boolean leftWall) {
        this.leftWall = leftWall;
    }

    public void setRightWall(boolean rightWall) {
        this.rightWall = rightWall;
    }

    public void setTopWall(boolean topWall) {
        this.topWall = topWall;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setTraveled(boolean traveled) {
        traveled = traveled;
    }

    public int getNumNotVisited() {
        return numNotVisited;
    }

    public void increaseNumNotVisited(int n){
        numNotVisited = n;
    }
    public void decreaseNumNotVisited(){
        numNotVisited--;
    }

    public void drawCell(Graphics g, boolean isStart, boolean isFinal, boolean Traveled){
        int xScaled = x*scale;
        int yScaled = y*scale;
        Graphics2D g2 = (Graphics2D) g;

        //draw the walls
        g2.setColor(Color.white);
        if(topWall){
            g2.drawLine(xScaled, yScaled, xScaled+scale, yScaled);
        }
        if(rightWall){
            g2.drawLine(xScaled+scale, yScaled, xScaled+scale, yScaled+scale);
        }
        if(bottomWall){
            g2.drawLine(xScaled, yScaled+scale, xScaled+scale, yScaled+scale);
        }
        if(leftWall){
            g2.drawLine(xScaled, yScaled, xScaled, yScaled+scale);
        }
        if(isStart){
            g2.setColor(Color.green);
            g2.fillRect(xScaled+1, yScaled+1, scale-1, scale-1);
        }
        if(isFinal){
            g2.setColor(Color.red);
            g2.fillRect(xScaled+1, yScaled+1, scale-1, scale-1);
        }
        if(Traveled){
            g2.setColor(Color.blue);
            g2.fillRect(xScaled+1, yScaled+1, scale-1, scale-1);
        }
    }
    public void setFinal(boolean bool){
        end = bool;
    }

    public void setPath(boolean path) {
        this.path = path;
    }

    public boolean isFinal(){return end;}

}


