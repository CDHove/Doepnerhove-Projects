// Names: Jace Halvorson, Carsten Doepnerhove
// x500s: halvo561, doepn008
import java.util.Random;


public class MyMaze{
    Cell[][] maze;


    //--------------------------------------------Constructors and Getters/Setters--------------------------------------------
    public MyMaze(int rows, int cols) {//makes board of cells
        maze = new Cell[rows][cols];
        for (int i=0;i<maze.length;i++) {
            for (int j = 0; j<maze[0].length; j++) {
                maze[i][j] = new Cell();
            }
        }
    }

    public MyMaze() {} //default constructor


    public Cell[][] getMaze() {
        return maze;
    }
    public void setMaze(Cell[][] mz) {maze = mz;}
   //--------------------------------------------Constructors and Getters/Setters--------------------------------------------




    //--------------------------------------------makeMaze--------------------------------------------
    public static MyMaze makeMaze(int rows, int cols) {
        MyMaze newMaze = new MyMaze(rows, cols);
        Cell[][] m = newMaze.getMaze();

        Stack1Gen<int[]> s = new Stack1Gen<int[]>();//stack
        m[0][0].setVisited(true);
        int[] indexes = {0, 0};
        s.push(indexes);

        int direction = 0;
        int x = 0,y = 0;//integers to keep track of current index [x, y]
        boolean triedRight = false, triedDown = false, triedLeft = true, triedUp = true;

        while (s.isEmpty() == false) {//loop until stack is empty
            int[] top = s.top();
            x = top[0];
            y = top[1];

            Random r = new Random();
            int holdDirection = direction;
            direction = (int)Math.floor(r.nextDouble() * 4);
            while (direction == holdDirection) //makes sure direction doesn't get the same value twice in a row
                direction = (int)Math.floor(r.nextDouble() * 4);

            switch (direction) {
                case 0://right
                    if (x < m[0].length - 1 && m[y][x+1].getVisited() == false) {//if it has been visited, loop will repeat and direction will be re-randomized
                        x++;
                        m[y][x].setVisited(true);//increments x then uses it
                        int[] pushed = {x, y};
                        s.push(pushed);
                        m[y][x-1].setRight(false);//remove wall in between current cell and neighbor
                        triedRight = false;
                        triedDown = false;
                        triedLeft = false;
                        triedUp = false;//successful push means try variables should be reset
                    } else
                        triedRight = true;

                    break;
                case 1://down
                    if (y < m.length - 1 && m[y+1][x].getVisited() == false) {//if it has been visited, loop will repeat and direction will be re-randomized
                        y++;
                        m[y][x].setVisited(true);//increments y then uses it
                        int[] pushed = {x, y};
                        s.push(pushed);
                        m[y-1][x].setBottom(false);//remove wall in between current cell and neighbor
                        triedRight = false;
                        triedDown = false;
                        triedLeft = false;
                        triedUp = false;//successful push means try variables should be reset
                    } else
                        triedDown = true;

                    break;
                case 2://left
                    if (x != 0 && m[y][x-1].getVisited() == false) {//if it has been visited, loop will repeat and direction will be re-randomized
                        x--;
                        m[y][x].setVisited(true);//decrements x then uses it
                        int[] pushed = {x, y};
                        s.push(pushed);
                        m[y][x].setRight(false);//remove wall in between current cell and neighbor
                        triedRight = false;
                        triedDown = false;
                        triedLeft = false;
                        triedUp = false;//successful push means try variables should be reset
                    } else
                        triedLeft = true;

                    break;
                case 3://up
                    if (y != 0 && m[y-1][x].getVisited() == false) {//if it has been visited, loop will repeat and direction will be re-randomized
                        y--;
                        m[y][x].setVisited(true);//decrements y then uses it
                        int[] pushed = {x, y};
                        s.push(pushed);
                        m[y][x].setBottom(false);//remove wall in between current cell and neighbor
                        triedRight = false;
                        triedDown = false;
                        triedLeft = false;
                        triedUp = false;//successful push means try variables should be reset
                    } else
                        triedUp = true;

                    break;
                default:
                    System.out.println("default is reached in makeMaze()");
            }

            if (triedRight && triedDown && triedLeft && triedUp) {//if it has tried every direction, it's at a dead end
                s.pop();
                triedRight = false;
                triedDown = false;
                triedLeft = false;
                triedUp = false;
            }
        }//while loop

        //reset value of visited to false for all cells
        for (int i=0;i<m.length;i++) {
            for (int j = 0; j < m[0].length; j++) {
                m[i][j].setVisited(false);
            }
        }

        return newMaze;
    }
    //--------------------------------------------makeMaze--------------------------------------------




    //--------------------------------------------printMaze--------------------------------------------
    public void printMaze(boolean path) {
        String output = "";
        int jStart = 0;//for (int j = jStart...), determines the starting position for the second loop

        for (int i=0;i<maze[0].length;i++) {//top border
            output += " ___";
        }
        output += "\n";

        for (int i=0;i<maze.length;i++) {
            if (i == 0) {//makes starting point in top left
                if (maze[0][0].getVisited() && maze[0][0].getBottom())
                    output += " _*_";
                else if (maze[0][0].getBottom())
                    output += " ___";
                else if (maze[0][0].getVisited())
                    output += "  * ";
                else
                    output += "    ";

                jStart = 1;
            }
            for (int j = jStart; j < maze[0].length; j++) {
                if (j == 0 || (j > 0 && maze[i][j-1].getRight()))
                    output += "|";
                else
                    output += " ";

                if (maze[i][j].getVisited() && maze[i][j].getBottom())
                    output += "_*_";
                else if (maze[i][j].getBottom())
                    output += "___";
                else if (maze[i][j].getVisited())
                    output += " * ";
                else
                    output += "   ";
            }

            if (i != maze.length - 1) {
                output += "|" + "\n";
            }
            jStart = 0;
        }

        System.out.println(output + "\n");
    }
    //--------------------------------------------printMaze--------------------------------------------




    //--------------------------------------------solveMaze--------------------------------------------
    public void solveMaze(MyMaze inMaze) {
        Cell[][] m = inMaze.getMaze();

        Q1Gen<int[]> q = new Q1Gen<int[]>();//queue for legal moves
        int[] indexes = {0,0};
        q.add(indexes);


        while (q.isEmpty() == false) {
            //gets location data from current cell
            int[] currentCell = q.remove();
            int x = currentCell[0];
            int y = currentCell[1];
            m[y][x].setVisited(true);


            //loop to check and add valid cells to queue
            //also checks if the current cell is the end of the maze
            for (int i=0; i<4; i++) {
                switch (i) {
                    case 0://right case
                        if (x<m[0].length-1 && m[y][x+1].getVisited()==false && m[y][x].getRight()==false) {
                            if (y==m.length-1 && x+1==m[0].length-1) {//End of maze
                                m[y][x+1].setVisited(true);
                                printMaze(true);
                                return;
                            } else {
                                int[] holdCell = {x+1, y};
                                q.add(holdCell);
                            }
                        }
                        break;
                    case 1://down case
                        if (y<m.length-1 && m[y+1][x].getVisited()==false && m[y][x].getBottom()==false) {
                            if (y==m.length-1 && x+1==m[0].length-1) {//End of maze
                                m[y+1][x].setVisited(true);
                                printMaze(true);
                                return;
                            } else {
                                int[] holdCell = {x, y+1};
                                q.add(holdCell);
                            }
                        }
                        break;
                    case 2://left case
                        if (x!=0) {
                            if (m[y][x-1].getVisited()==false && m[y][x-1].getRight()==false){
                                if (y == m.length - 1 && x + 1 == m[0].length - 1) {//End of maze
                                    m[y][x + 1].setVisited(true);
                                    printMaze(true);
                                    return;
                                } else {
                                    int[] holdCell = {x-1, y};
                                    q.add(holdCell);
                                }
                            }
                        }
                        break;
                    case 3://up case
                        if (y!=0) {
                            if (m[y-1][x].getVisited() == false && m[y-1][x].getBottom() == false) {
                                if (y == m.length - 1 && x + 1 == m[0].length - 1) {//End of maze
                                    m[y-1][x].setVisited(true);
                                    printMaze(true);
                                    return;
                                } else {
                                    int[] holdCell = {x, y-1};
                                    q.add(holdCell);
                                }
                            }
                        }
                        break;
                }

            }
        }//While loop
    }
    //--------------------------------------------solveMaze--------------------------------------------




    public static void main(String[] args){
        MyMaze t = new MyMaze();
        t = (t.makeMaze(10, 10));
        t.printMaze(true);
        t.solveMaze(t);
    }
}
