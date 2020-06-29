import java.util.Stack;
import java.util.Arrays;

public class Board {
    // class used to store coordinates
    private class Coord {
        int i;
        int j;
    }
    private final int[][] board;
    // Dimensions for the board
    private final int N;
    // location of blank tile
    private Coord oCoord;

    // construct a board from an N-by-N array of blocks (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        N = (int)blocks.length;
        board = new int[N][N];
        oCoord = new Coord();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = (int)blocks[i][j];
                // Coordinates for blank tile
                if (blocks[i][j] == 0) {
                    oCoord.i = i;
                    oCoord.j = j;
                }
            }
        }
    }
    public int dimension() {
        return N;
    }
    
    
    public int hamming() {  // number of blocks out of place
        int count = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (board[i][j] != 0 && board[i][j] != (i*dimension() + j + 1)) {
                    count++;
                }
            }
        }
        return count;
    }

    public int manhattan(){                 // sum of Manhattan distances between blocks and goal
    	int man = 0;
    	for(int i = 0; i < dimension(); i++)
    		for(int j = 0; j < dimension(); j++)
    			if(board[j][i] != i*3 + j + 1)
    				man = man + manD(board[i][j], i ,j);
    	return man;
    }
    
    public int manD(int cVal, int cI, int cJ){ //returns the Manhattan distance between a block and goal
    	int i = cVal/dimension();
    	int j = cVal%dimension();
    	return Math.abs(i- cI) + Math.abs(j-cJ);
    }

    
    public boolean isGoal() {	// is this board the goal board?
        return hamming() == 0;
    }

    
    public Board twin() {
        // copy our board
        int[][] blocks = reBoard();
        // if neither of the first two blocks are blank,
        if (blocks[0][0] != 0 && blocks[0][1] != 0) {
            // switch first two blocks
            swap(0,0,0,1, blocks);
        } else {
            // otherwise, switch first two blocks on second row
        	swap(1,0,1,1, blocks);
        }
        return new Board(blocks);
    }
    
    public void swap(int i, int j, int cI, int cJ, int[][] blocks){
    	blocks[i][j] = board[cI][cJ];
        blocks[cI][cJ] = board[i][j];
    }


    public boolean equals(Object y){        // does this board equal y?
    	int boardY[][] = ((Board) y).reBoard();
    	for(int i = 0; i < dimension(); i++)
    		for(int j = 0; j < dimension(); j++)
    			if(board[j][i] != boardY[j][i])
    				return false;
    	return true;
    }

    private int[][] reBoard() {
        int[][] boardCopy = new int[dimension()][dimension()];
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                boardCopy[i][j] = board[i][j];
            }
        }
        return boardCopy;
    }
    public Iterable<Board> neighbors() {
        Stack<Board> stack = new Stack<Board>();
        // Case1: blank square x > 0
        if (oCoord.j > 0) {
        	// make a board, swap blank and tile, and stack
            int[][] blocks = reBoard();
            blocks[oCoord.i][oCoord.j] = board[oCoord.i][oCoord.j-1];
            blocks[oCoord.i][oCoord.j-1] = board[oCoord.i][oCoord.j];
            stack.push(new Board(blocks));
        }
        // Case2: blank square x < dimension
        if (oCoord.j < dimension()-1) {
            int[][] blocks = reBoard();
            blocks[oCoord.i][oCoord.j] = board[oCoord.i][oCoord.j+1];
            blocks[oCoord.i][oCoord.j+1] = board[oCoord.i][oCoord.j];
            stack.push(new Board(blocks));
        }
        // Case3: blank square y < dimension
        if (oCoord.i > 0) {
            int[][] blocks = reBoard();
            blocks[oCoord.i][oCoord.j] = board[oCoord.i-1][oCoord.j];
            blocks[oCoord.i-1][oCoord.j] = board[oCoord.i][oCoord.j];
            stack.push(new Board(blocks));
        }
        // Case4: blank square y > 0
        if (oCoord.i < dimension()-1) {
            int[][] blocks = reBoard();
            blocks[oCoord.i][oCoord.j] = board[oCoord.i+1][oCoord.j];
            blocks[oCoord.i+1][oCoord.j] = board[oCoord.i][oCoord.j];
            stack.push(new Board(blocks));
        }

        return stack;
    }

    // string representation of the board (in the output format specified below)
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((int)dimension() + "\n");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                stringBuilder.append(String.format("%d ", (int)board[i][j]));
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}