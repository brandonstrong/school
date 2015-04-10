/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ascii.trazmaze;

/**
 *
 * @author Brandon
 * @date   3/16/14
 * @lab    Ascii-Traz lab
 */
import java.util.Random;
import java.util.Scanner;
import java.util.Deque;
import java.util.Stack;
import java.util.ArrayDeque;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

/**
 *
 * @author hunterl
 */
public class AsciiTrazMaze extends JFrame {

    //Create boolean for checking if path worked or not
    private static boolean overtime = false;
    private static boolean backtrack = false;
    private static boolean b = true;
    private static boolean t = true;
    private static String timesThrough = "first";

    //Create Stack for tracking
    private static Deque<String> stack = new ArrayDeque<String>();
    private static Deque<String> OTStack = new ArrayDeque<String>();

    //Arrays for direction & opposite directions
    String dir[] = new String[]{"north", "east", "south", "west"};
    String oppDir[] = new String[]{"south", "west", "north", "east"};

    private static final int MAX_WIDTH = 255;
    private static final int MAX_HEIGHT = 255;

    private char[][] maze = new char[MAX_HEIGHT][MAX_WIDTH];

    private Random random = new Random();
    private JPanel mazePanel = new JPanel();
    private int width = 0;
    private int height = 0;
    private boolean step = false;

    private boolean timerFired = false;
    private Timer timer;
    private final int TIMER_DELAY = 200;

    private final int SPRITE_WIDTH = 25;
    private final int SPRITE_HEIGHT = 25;

    private BufferedImage mazeImage;
    private ImageIcon ground = new ImageIcon("sprites/ground.png");
    private ImageIcon wall1 = new ImageIcon("sprites/cactus.png");
    private ImageIcon wall2 = new ImageIcon("sprites/rock.png");
    private ImageIcon finish = new ImageIcon("sprites/well.png");
    private ImageIcon south1 = new ImageIcon("sprites/cowboy-forward-1.png");
    private ImageIcon south2 = new ImageIcon("sprites/cowboy-forward-2.png");
    private ImageIcon north1 = new ImageIcon("sprites/cowboy-back-1.png");
    private ImageIcon north2 = new ImageIcon("sprites/cowboy-back-2.png");
    private ImageIcon west1 = new ImageIcon("sprites/cowboy-left-1.png");
    private ImageIcon west2 = new ImageIcon("sprites/cowboy-left-2.png");
    private ImageIcon east1 = new ImageIcon("sprites/cowboy-right-1.png");
    private ImageIcon east2 = new ImageIcon("sprites/cowboy-right-2.png");

    private long startTime;
    private long currentTime;

    /**
     * Constructor for class Maze. Opens a text file containing the maze, then
     * attempts to solve it.
     *
     * @param fname String value containing the filename of the maze to open.
     */
    public AsciiTrazMaze(String fname) {
        openMaze(fname);
        mazeImage = printMaze();

        timer = new Timer(TIMER_DELAY, new TimerHandler());     // setup a Timer to slow the animation down.
        timer.start();

        addWindowListener(new WindowHandler());     // listen for window event windowClosing

        setTitle("Cowboy Maze");
        setSize(width * SPRITE_WIDTH + 10, height * SPRITE_HEIGHT + 30);
        setVisible(true);

        add(mazePanel);
        setContentPane(mazePanel);

        solveMaze();
    }

    /**
     * Called from the operating system. If no command line arguments are
     * supplied, the method displays an error message and exits. Otherwise, a
     * new instace of AsciiTrazMaze() is created with the supplied filename from
     * the command line.
     *
     * @param args[] Command line arguments, the first of which should be the
     * filename to open.
     */
    public static void main(String[] args) {
        int runny = 1;
        if (args.length > 0) {
            new AsciiTrazMaze(args[0]);
        } else {
            System.out.println();
            System.out.println("Usage: java Maze <filename>.");
            System.out.println("Maximum Maze size:" + MAX_WIDTH + " x " + MAX_HEIGHT + ".");
            System.out.println();
            System.exit(1);
        }
    }

    /**
     * Finds the starting location and passes it to the recursive alpathrithm
     * solve(x, y, facing). The starting location should be the only '.' on the
     * outer wall of the maze.
     */
    public void solveMaze() {
        boolean startFound = false;
        if (!startFound) {
            for (int i = 0; i < width; i++) {       // look for the starting location on the top and bottom walls of the AsciiTrazMaze.
                if (maze[0][i] == '.') {
                    preSolve(i, 0, "south");
                    startFound = true;
                } else if (maze[height - 1][i] == '.') {
                    preSolve(i, height - 1, "north");
                    startFound = true;
                }
            }
        }
        if (!startFound) {
            for (int i = 0; i < height; i++) {      // look for the starting location on the left and right walls of the AsciiTrazMaze.
                if (maze[i][0] == '.') {
                    preSolve(0, i, "east");
                    startFound = true;
                } else if (maze[i][width - 1] == '.') {
                    preSolve(width - 1, i, "west");
                    startFound = true;
                }
            }
        }
        if (!startFound) {
            System.out.println("Start not found!");
        }
    }

    public void preSolve(int x, int y, String facing) {
        //Graphics2D g2 = (Graphics2D)mazePanel.getGraphics();
        //g2.drawImage(mazeImage, null, 0, 0);
        //g2.drawImage(printGuy(facing), x*SPRITE_WIDTH, y*SPRITE_HEIGHT, null, null);
        Scanner input = new Scanner(System.in);
        System.out.println("Press 1 to start");
        input.nextLine();
        startTime = System.currentTimeMillis();
        solve(x, y, facing);
    }

    /**
     * Recursive alpathrithm to solve a AsciiTrazMaze. Places a X at locations
     * already visited. This alpathrithm is very inefficient, it follows the
     * right hand wall and will never find the end if the path leads it in a
     * circle.
     *
     * @param x int value of the current X location in the AsciiTrazMaze.
     * @param y int value of the current Y location in the AsciiTrazMaze.
     * @param facing String value holding one of four cardinal directions
     * determined by the current direction facing.
     */
    private void solve(int x, int y, String facing) {
        if ((timesThrough.equals("first") || timesThrough.equals("secondovertime")) && (stack.size() > 2) && (y == 0 || x == 0)) {
            stack.push(facing);
            stack.push(facing);
            stack.push(facing);
            solve(x, y + 1, "east");
        }

        //Define North, South, East, West, #, and . as short, usable strings
        String n = "north";
        String s = "south";
        String w = "west";
        String e = "east";

        Graphics2D g2 = (Graphics2D) mazePanel.getGraphics(); //don't mess with the next

        while (!timerFired) {   // wait for the timer.
            try {
                //original is 10
                Thread.sleep(10);
            } catch (Exception ex) {
            }
        }
        timerFired = false;
        currentTime = System.currentTimeMillis();

        //original is 50000
        if ((currentTime - startTime) > 50000) {
            System.out.println("\nYou went overtime! Run again to finish within time limit.");
            overtime = true;
            if (timesThrough.equals("first")) {
                writeFile();
            }
            closingMethod();
        }

        //Do not mess with the above part of this method
        //Below is where you put your solution to solving the maze.  
        if (maze[y][x] != 'F') {  //this is if it doesn't find the finish on a turn.........
            g2.drawImage(mazeImage, null, 0, 0);
            g2.drawImage(printGuy(facing), x * SPRITE_WIDTH, y * SPRITE_HEIGHT, null, null);
            mazePanel.setSize(width * SPRITE_WIDTH + 10, height * SPRITE_HEIGHT + 30);
            if (maze[y][x] == 'X') {
                b = false;
            } else {
                b = true;
            }
            maze[y][x] = 'X';   // mark this spot as visited. This is how you can keep track of where you've been. 

            /**
             * This is the code for sure-fire way of solving maze using right
             * hand method
             *
             */
            if (timesThrough.equals("first")) {
                if (b == true) {
                    stack.push(facing);
                } else if (b == false && t == false) {
                    backtrack = true;
                    stack.pop();
                }
                t = false;
                //FACING NORTH
                if (facing.equals(n)) {
                    System.out.println("Right hand is: " + maze[y][x + 1] + " and forward is: " + maze[y - 1][x] + " ,and facing: " + facing);
                    //Check for hashtag to east and period to north
                    if (wall(maze[y][x + 1]) && path(maze[y - 1][x])) {
                        //if cowboy can move straight, he does
                        solve(x, y - 1, n);
                    } else if (path(maze[y][x + 1])) {
                        //If right hand is on period, path there
                        solve(x + 1, y, e);
                    } else {
                        //Gets here if in corner
                        //else turn west (left) to keep right hand on wall
                        t = true;
                        solve(x, y, w);
                    }
                    //FACING EAST
                } else if (facing.equals(e)) {
                    System.out.println("Right hand is: " + maze[y + 1][x] + " and forward is: " + maze[y][x + 1] + " ,and facing: " + facing);
                    //check for hashtag to south and period to east
                    if (wall(maze[y + 1][x]) && path(maze[y][x + 1])) {
                        //if cowboy can move straight, he does
                        solve(x + 1, y, e);
                    } else if (path(maze[y + 1][x])) {
                        //If right hand is on period, path there
                        solve(x, y + 1, s);
                    } else {
                        //Gets here if in corner
                        //else turn north (left) to keep right hand on wall
                        t = true;
                        solve(x, y, n);
                    }
                    //FACING SOUTH
                } else if (facing.equals(s)) {
                    System.out.println("Right hand is: " + maze[y][x - 1] + " and forward is: " + maze[y + 1][x] + " ,and facing: " + facing);
                    //check for hashtag to west adn period to south
                    if (wall(maze[y][x - 1]) && path(maze[y + 1][x])) {
                        //if cowboy can move straight, he does
                        solve(x, y + 1, s);
                    } else if (path(maze[y][x - 1])) {
                        //If right hand is on period, path there
                        solve(x - 1, y, w);
                    } else {
                        //Gets here if in corner
                        //else turn east(left) to keep right hand on wall
                        t = true;
                        solve(x, y, e);
                    }
                    //FACING WEST
                } else {
                    System.out.println("Right hand is: " + maze[y - 1][x] + " and forward is: " + maze[y][x - 1] + " ,and facing: " + facing);
                    if (wall(maze[y - 1][x]) && path(maze[y][x - 1])) {
                        //if cowboy can move straight, he does
                        solve(x - 1, y, w);
                    } else if (path(maze[y - 1][x])) {
                        //If right hand is on period, path there
                        solve(x, y - 1, n);
                    } else {
                        //Gets here if in corner
                        //else turn south (left) to keep right hand on wall
                        t = true;
                        solve(x, y, s);
                    }
                }
                /**
                 * This method uses the data found from the first method to
                 * cycle through the positions.
                 */
            } else if (timesThrough.equals("second") || timesThrough.equals("third")) {
                if (stack.peek().equals(n)) {
                    //if north is popped off stack, move that way
                    System.out.println("Moving: " + stack.pop());
                    solve(x, y - 1, n);
                } else if (stack.peek().equals(e)) {
                    //if east is popped off stack, move that way
                    System.out.println("Moving: " + stack.pop());
                    solve(x + 1, y, e);
                } else if (stack.peek().equals(s)) {
                    //if south is popped off stack, move that way
                    System.out.println("Moving: " + stack.pop());
                    solve(x, y + 1, s);
                } else {
                    //if west is popped off stack, move that way
                    System.out.println("Moving: " + stack.pop());
                    solve(x - 1, y, w);
                }

                /**
                 * If we went overtime on the first try, the go through maze
                 * using left hand rule.
                 */
            } else if (timesThrough.equals("secondovertime")) {
                if (b == true) {
                    stack.push(facing);
                } else if (b == false && t == false) {
                    backtrack = true;
                    stack.pop();
                }

                t = false;
                //FACING NORTH
                if (facing.equals(n)) {
                    System.out.println("Left hand is: " + maze[y][x - 1] + " and forward is: " + maze[y - 1][x] + " ,and facing: " + facing);
                    //Check for hashtag to west and period to north
                    if (wall(maze[y][x - 1]) && path(maze[y - 1][x])) {
                        //if cowboy can move straight, he does
                        solve(x, y - 1, n);
                    } else if (path(maze[y][x - 1])) {
                        //If right hand is on period, path there
                        solve(x - 1, y, w);
                    } else {
                        //Gets here if in corner
                        //else turn east (right) to keep left hand on wall
                        t = true;
                        solve(x, y, e);
                    }
                    //FACING EAST
                } else if (facing.equals(e)) {
                    System.out.println("Left hand is: " + maze[y - 1][x] + " and forward is: " + maze[y][x + 1] + " ,and facing: " + facing);
                    //check for hashtag to north and period to east
                    if (wall(maze[y - 1][x]) && path(maze[y][x + 1])) {
                        //if cowboy can move straight, he does
                        solve(x + 1, y, e);
                    } else if (path(maze[y - 1][x])) {
                        //If left hand is on period, path there
                        solve(x, y - 1, n);
                    } else {
                        //Gets here if in corner
                        //else turn south (right) to keep left hand on wall
                        t = true;
                        solve(x, y, s);
                    }
                    //FACING SOUTH
                } else if (facing.equals(s)) {
                    System.out.println("Left hand is: " + maze[y][x + 1] + " and forward is: " + maze[y + 1][x] + " ,and facing: " + facing);
                    //check for hashtag to east and period to south
                    if (wall(maze[y][x + 1]) && path(maze[y + 1][x])) {
                        //if cowboy can move straight, he does
                        solve(x, y + 1, s);
                    } else if (path(maze[y][x - 1])) {
                        //If left hand is on period, path there
                        solve(x + 1, y, e);
                    } else {
                        //Gets here if in corner
                        //else turn west(right) to keep left hand on wall
                        t = true;
                        solve(x, y, w);
                    }
                    //FACING WEST
                } else {
                    System.out.println("Left hand is: " + maze[y + 1][x] + " and forward is: " + maze[y][x - 1] + " ,and facing: " + facing);
                    if (wall(maze[y + 1][x]) && path(maze[y][x - 1])) {
                        //if cowboy can move straight, he does
                        solve(x - 1, y, w);
                    } else if (path(maze[y + 1][x])) {
                        //If left hand is on period, path there
                        solve(x, y + 1, s);
                    } else {
                        //Gets here if in corner
                        //else turn north (right) to keep left hand on wall
                        t = true;
                        solve(x, y, n);
                    }
                }
            }
        } else {
            stack.push(facing);
            System.out.println("\nFound the finish!");
            //don't mess with the following 4 lines, but you can add stuff below that if you need. 
            currentTime = System.currentTimeMillis();
            long endTime = currentTime - startTime;
            long finalTime = endTime / 1000;
            File file = new File("direction.txt");
            
            if ((backtrack == true) && (timesThrough.equals("first") || timesThrough.equals("secondovertime"))) {
                writeFile();
            }else if(backtrack == false){
                if (file.exists()) {
                    System.out.println("\nBest path found!\n");
                    System.out.println("Removing file...");
                    file.delete();
                    System.out.println("File Removed.\n");
                } else {
                    System.out.println("\nMaze solved first time through.\n");
                }
            }else if (timesThrough.equals("second") || timesThrough.equals("third")) {
                System.out.println("\nRemvoing file...");
                if (file.exists()) {
                    file.delete();
                    System.out.println("File Removed.\n");
                } else {
                    System.err.println(
                            "\nFile: '" + file + "' ('" + file.getAbsolutePath() + "') not found!\n");
                }
            }

            //Write out stack of directions to a file
            System.out.println("Final Time = " + finalTime);
            System.exit(0);
        }
    }

    /**
     * checks to see if current char is an X
     *
     * @param c
     * @return
     */
    public void writeFile() {
        try {
            System.out.println("\nWriting improved path to file...");
            PrintWriter writer = new PrintWriter("direction.txt", "UTF-8");
            if (timesThrough.equals("first")) {
                writer.println("second");
            } else {
                writer.println("third");
            }
            if (overtime == true) {
                writer.println("overtime");
            }
            while (stack.peek() != null) {
                writer.println(stack.pop());
            }
            writer.close();
            System.out.println("Improved path written. Please run again for better results.\n");
        } catch (Exception ex) {
            System.err.println("\nFailed to write file\n");
        }
    }

    public boolean checkX(char c) {
        if (c == 'X') {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns true if a wall and false if not
     *
     * @param c
     * @return
     */
    public boolean wall(char c) {
        if (c == '%' || c == '#') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if a path and false if not
     *
     * @param c
     * @return
     */
    public boolean path(char c) {
        if (c == '.' || c == 'X' || c == 'F') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Opens a text file containing a maze and stores the data in the 2D char
     * array maze[][].
     *
     * @param fname String value containing the file name of the maze to open.
     */
    public void openMaze(String fname) {
        String in = "";
        int i = 0;
        try {
            Scanner sc = new Scanner(new File("direction.txt"));

            if (sc.hasNext()) {
                timesThrough = sc.nextLine();
                if (!timesThrough.equals("second") && !timesThrough.equals("third")) {
                    timesThrough = "first";
                    System.out.println("This is the " + timesThrough + " time through.\n");
                } else {
                    System.out.println("This is the " + timesThrough + " time through.\n");
                    String overtimeCheck = sc.nextLine();

                    if (overtimeCheck.equals("overtime")) {
                        timesThrough = "secondovertime";

                        sc.close();
                    } else {
                        stack.push(overtimeCheck);
                    }
                    while (sc.hasNext()) {
                        stack.push(sc.nextLine());
                    }
                    stack.pop();
                }
            }
            sc.close();
        } catch (Exception exc) {

            System.err.println("No file with best path found");
            if (timesThrough.equals("first")) {
                System.out.println("This is the " + timesThrough + " time through.\n");
            }
        }
        try {
            Scanner sc = new Scanner(new File(fname));
            while (sc.hasNext()) {
                in = sc.nextLine();
                in = trimWhitespace(in);
                if (in.length() <= MAX_WIDTH && i < MAX_HEIGHT) {
                    for (int j = 0; j < in.length(); j++) {
                        if (in.charAt(j) == '#') {      // if this spot is a wall, randomize the wall peice to display
                            if (random.nextInt(2) == 0) {
                                maze[i][j] = '#';
                            } else {
                                maze[i][j] = '#';
                            }
                        } else {
                            maze[i][j] = in.charAt(j);
                        }
                    }
                } else {
                    System.out.println("Maximum maze size exceeded: (" + MAX_WIDTH + " x " + MAX_HEIGHT + ")!");
                    System.exit(1);
                }
                i++;
            }
            width = in.length();
            height = i;
            System.out.println("(" + width + " x " + height + ") maze opened.");
            System.out.println();
            sc.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e);
        }
    }

    /**
     * Removes white space from the supplied string and returns the trimmed
     * String.
     *
     * @param s String value to strip white space from.
     * @return String stripped of white space.
     */
    public String trimWhitespace(String s) {
        String newString = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ') {
                newString += s.charAt(i);
            }
        }
        return newString;
    }

    /**
     * Returns the sprite facing the direction supplied.
     *
     * @param facing String value containing 1 of 4 cardinal directions to make
     * the sprite face.
     * @return Image of the sprite facing the proper direction.
     */
    private Image printGuy(String facing) {
        if (facing.equals("south")) {  // draw sprite facing south
            if (step) {
                step = false;
                return south1.getImage();
            } else {
                step = true;
                return south2.getImage();
            }
        } else if (facing.equals("north")) {  // draw sprite facing north
            if (step) {
                step = false;
                return north1.getImage();
            } else {
                step = true;
                return north2.getImage();
            }
        } else if (facing.equals("east")) {  // draw sprite facing east
            if (step) {
                step = false;
                return east1.getImage();
            } else {
                step = true;
                return east2.getImage();
            }
        } else if (facing.equals("west")) {  // draw sprite facing west
            if (step) {
                step = false;
                return west1.getImage();
            } else {
                step = true;
                return west2.getImage();
            }
        }
        return null;
    }

    /**
     * Prints the AsciiTrazMaze using sprites.
     *
     * @return BufferedImage rendition of the maze.
     */
    public BufferedImage printMaze() {
        BufferedImage mi = new BufferedImage(width * SPRITE_WIDTH, height * SPRITE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics g2 = mi.createGraphics();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (maze[i][j] == '#') {    // draw wall
                    g2.drawImage(wall1.getImage(), j * SPRITE_WIDTH, i * SPRITE_HEIGHT, null, null);
                } else if (maze[i][j] == '%') {   // draw wall
                    g2.drawImage(wall2.getImage(), j * SPRITE_WIDTH, i * SPRITE_HEIGHT, null, null);
                } else if (maze[i][j] == '.' || maze[i][j] == 'X') {  // draw ground
                    g2.drawImage(ground.getImage(), j * SPRITE_WIDTH, i * SPRITE_HEIGHT, null, null);
                } else if (maze[i][j] == 'F') {   // draw finish
                    g2.drawImage(finish.getImage(), j * SPRITE_WIDTH, i * SPRITE_HEIGHT, null, null);
                }
            }
        }
        return mi;
    }

    public void closingMethod() {

        long endTime = currentTime - startTime;
        long finalTime = endTime / 100;
        System.out.println("Final Time = " + ((double) finalTime / (double) 10));
        System.exit(0);
    }

    /**
     * Handles the Timer, updates the boolean timerFired every time the Timer
     * ticks. Used to slow the animation down.
     */
    private class TimerHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            timerFired = true;
        }
    }

    /**
     * Catch the windowClosing event and exit gracefully.
     */
    private class WindowHandler extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            removeAll();
            closingMethod();
            System.exit(0);
        }
    }

}
