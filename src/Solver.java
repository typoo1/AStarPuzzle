import java.util.Stack;

public class Solver {
	
	private class Node implements Comparable<Node> {
		final Board board;
		final int moves;
		final Node previous;
		final int priority;
		Node(Board board, int moves, Node previous){
			this.board = board;
			this.moves = moves;
			this.previous = previous;
			this.priority = moves + board.manhattan();
		}
		public int compareTo(Node a) {
			if (this.priority > a.priority)
				return 1;
			if (this.priority == a.priority)
				return 0;
			return -1;
		}
	}
	
	private boolean Solvable;
	private int moves;
	private MinPQ<Node> Q;
	private MinPQ<Node> TQ;
    public Solver(Board initial){           // find a solution to the initial board (using the A* algorithm)
    	Q = new MinPQ<Node>();
    	Q.insert(new Node(initial, 0, null));
    	TQ = new MinPQ<Node>();
    	TQ.insert(new Node(initial.twin(), 0, null));
    	
    	//find the solvable board
    	while(!Q.min().board.isGoal() && !TQ.min().board.isGoal()){
    		Node min = Q.delMin();
    		Node TMin = TQ.delMin();
    		
    		Iterable<Board> neighbors = min.board.neighbors();
    		Iterable<Board> Tneighbors = TMin.board.neighbors();
    		for(Board neighbor : neighbors) {
    			if(min.previous != null && neighbor.equals(min.previous.board)) {
    				continue;
    			}
    			Node next = new Node(neighbor, min.moves + 1, min);
    			Q.insert(next);
    		}
    		for(Board Tneighbor : Tneighbors) {
    			if(TMin.previous != null && Tneighbor.equals(TMin.previous.board)){
    				continue;
    			}
    			Node TNext = new Node(Tneighbor, TMin.moves + 1, TMin);
    			TQ.insert(TNext);
    		}
    	}
    	//Case 1: initial board is solvable
    	if(Q.min().board.isGoal()){
    		moves = Q.min().moves;
    		Solvable = true;
    		}
    	
    	//Case 2: Twin board is solvable
    	if(TQ.min().board.isGoal()){
    		moves = -1;
    		Solvable = false;
    	}
    }
    public boolean isSolvable(){            // is the initial board solvable?
    	return Solvable;
    }
    public int moves(){                     // min number of moves to solve initial board; -1 if unsolvable
    	return moves;
    }
    public Iterable<Board> solution(){      // sequence of boards in a shortest solution; null if unsolvable
    	if(!Solvable){
    		return null;
    	}
    	Stack<Board> solution = new Stack<Board>();
		Node low = Q.min();
		while(low != null) {
			solution.push(low.board);
			low = low.previous;
		}
		return solution;
    	
    	
    }
    
    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        String fileName = args[0];
        In in = new In(fileName);
        int N = in.readInt();
        int[][] blocks = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j=0; j < N; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board board = new Board(blocks);
        Solver solver = new Solver(board);
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board eachBoard : solver.solution())
                StdOut.println(eachBoard);
        }
    }
}