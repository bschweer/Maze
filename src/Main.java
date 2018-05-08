import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        // Read in the Size of maze to create
        System.out.print("Enter the size of maze to create: ");
        int size = (new Scanner(System.in)).nextInt();

        Maze maze = new Maze(size);
        maze.displayInitialMaze();
        maze.generateRandomPermutation();
        maze.displayFinalMaze();
    }
}