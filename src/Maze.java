import java.security.SecureRandom;

public class Maze
{
    private static final int RIGHT = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int UP = 3;

    private final int size;

    // Stores disjoint sets
    private int[] sets;

    // Heights of each set
    private int[] height;

    // A graph is simply a set of edges: graph[i][d] is the edge
    // where i is the index for a Point and d is the direction
    private Edge[][] graph;
    private final int numberOfGraphPoints;

    public Maze(int size)
    {
        if(size <= 0) {
            throw new NumberFormatException("Size of the board must be positive");
        }
        this.size = size;

        // Create one dummy edge for all boundary edges.
        Edge dummy = new Edge(new Point(0, 0), 0);
        dummy.setUsed(true);

        numberOfGraphPoints = size*size;
        graph = new Edge[numberOfGraphPoints][4];

        for (int i = 0; i < size; ++i)
        {
            for (int j = 0; j < size; ++j)
            {
                Point p = new Point(i, j);
                int pIndex = i*size+j; // Point(i, j)'s index is i*size + j

                graph[pIndex][RIGHT] = (j < size-1) ? new Edge(p, RIGHT): dummy;
                graph[pIndex][DOWN] = (i < size-1) ? new Edge(p, DOWN) : dummy;
                graph[pIndex][LEFT] = (j > 0) ? graph[pIndex-1][RIGHT] : dummy;
                graph[pIndex][UP] = (i > 0) ? graph[pIndex-size][DOWN] : dummy;
            }
        }

        // Initialize sets and height array.
        sets = new int[numberOfGraphPoints];
        height = new int[numberOfGraphPoints];
        for(int i=0; i<numberOfGraphPoints; ++i)
        {
            sets[i] = -1;
            height[i] = 0;
        }
    }

    public void generateRandomPermutation()
    {
        int i, j;
        int k = numberOfGraphPoints - 1;
        SecureRandom random = new SecureRandom();

        while(k > 0)
        {
            // Find a random edge that hasn't been considered yet
            do {
                i = random.nextInt(numberOfGraphPoints);
                j = random.nextInt(4);
            } while(graph[i][j].isDeleted() || graph[i][j].isUsed());

            int u = find(i);
            // Find other point that the edge connects to.
            int i2 = (j == RIGHT) ? (i+1) : (j == LEFT ? (i-1) : (j == UP ? (i-size) : (i+size)));
            int v = find(i2);

            if(u != v) { // If from two different sets.
                union(u, v);
                k--;
                // Use (j+2)%4 to find the opposite direction.
                graph[i][j].setDeleted(true);
                graph[i2][(j+2)%4].setDeleted(true);
            }
            else { // Already in same set.
                graph[i][j].setUsed(true);
                graph[i2][(j+2)%4].setUsed(true);
            }
        }
    }

    private int find(int i)
    {
        int r = i;
        while(sets[r] != -1) { // Keep iterating until the root has been found
            r = sets[r];
        }
        // Path compression.
        if(i != r) {
            int k = sets[i];
            while(k != r)
            {
                sets[i] = r;
                i = k;
                k = sets[k];
            }
        }
        return r;
    }

    private void union(int i, int j)
    {
        int ri = height[i];
        int rj = height[j];
        if(ri < rj) {
            sets[i] = j;
        }
        else if(ri > rj) {
            sets[j] = i;
        }
        else {
            height[j]++;
            sets[j] = i;
        }
    }

    public void displayInitialMaze()
    {
        System.out.println("\nInitial Configuration:");

        for (int i = 0; i < size; ++i)
        {
            System.out.print("    -");
            for (int j = 0; j < size; ++j) System.out.print("----");
            System.out.println();
            if (i == 0) {
                System.out.print("Start");
            }
            else {
                System.out.print("    |");
            }

            for (int j = 0; j < size; ++j)
            {
                if (i == size-1 && j == size-1)
                    System.out.print("    End");
                else System.out.print("   |");
            }
            System.out.println();
        }
        System.out.print("    -");
        for (int j = 0; j < size; ++j) System.out.print("----");
        System.out.println();
    }

    public void displayFinalMaze()
    {
        System.out.println("\nEnding Configuration:");

        int x = 0;
        for (int i = 0; i < size; i++)
        {
            System.out.print("    -");
            for (int j = 0; j < size; j++)
            {
                if(!graph[x][UP].isDeleted()) {
                    System.out.print("----");
                }
                else if(j == size-1) { // At right border of maze
                    System.out.print("   -");
                }
                else {
                    System.out.print("    ");
                }
                x++;
            }

            x -= size;
            System.out.println();
            if (i == 0) System.out.print("Start");
            else System.out.print("    |");

            for (int j = 0; j < size; j++)
            {
                if (i == size-1 && j == size-1) { // Check if at end of maze
                    System.out.print("    End");
                }
                else if(!graph[x][RIGHT].isDeleted() ) {
                    System.out.print("   |");
                }
                else {
                    System.out.print("    ");
                }
                x++;
            }
            System.out.println();
        }

        // Print the bottom border of the maze
        System.out.print("    -");
        for (int b = 0; b < size; ++b) System.out.print("----");
        System.out.println();
    }
}