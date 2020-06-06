import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Boggle {
	
	class Cell {
		private int r;
		private int c;
		
		public Cell(int r, int c) {
			this.r = r;
			this.c = c;
		}
		
		public int getR() {
			return this.r;
		}
		
		public int getC() {
			return this.c;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + c;
			result = prime * result + r;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Cell other = (Cell) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (c != other.c)
				return false;
			if (r != other.r)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Cell [r=" + r + ", c=" + c + "]";
		}

		private Boggle getEnclosingInstance() {
			return Boggle.this;
		}
	}
	
	Map<Cell, Character> board;
	private String word;
	
	private Map<Integer, List<Cell>> visited;
	private Deque<Cell> path;
	private int boardHeight;
	private int boardWidth;
    
    public Boggle(final char[][] board, final String word) {
    	this.board = new HashMap<>();
    	
    	this.boardHeight = board.length;
    	this.boardWidth = board[0].length;
    	
    	for (int i = 0; i < board.length; i++) {
    		for (int j = 0; j < board[i].length; j++) {
    			if (word.indexOf(board[i][j]) > -1) {
    				this.board.put(new Cell(i, j), Character.valueOf(board[i][j]));
    			}
    		}
    	}
    	
        this.word = word;
        this.visited = new HashMap<>();
        this.path = new ArrayDeque<>();
    }
    
    private boolean isOutOfBounds(Cell cell) {
    	return (cell.getR() < 0 || 
    		cell.getC() < 0 ||
    		cell.getR() >= this.boardHeight ||
    		cell.getC() >= this.boardWidth);
    }
    
    private boolean isVisited(int letterIndex, Cell cell) {
    	if (this.path.contains(cell)) {
    		return true;
    	}
    	
    	List<Cell> cells = this.visited.get(letterIndex);
    	if (cells != null && cells.contains(cell)) {
    		return true;	
    	}
    	
    	return false;
    }
    
    private void setVisited(int letterIndex, Cell cell) {
    	List<Cell> cells = visited.get(letterIndex);
    	if (cells == null) {
    		cells = new ArrayList<>();
    	}
    	cells.add(cell);
    	visited.put(letterIndex, cells);
    }
    
    private Cell findCell(int letterIndex) {
    	Character letter = Character.valueOf(this.word.charAt(letterIndex));
    	Cell cell = null;
    	
    	if (this.path.isEmpty()) {
    		for (Entry<Cell, Character> entry: this.board.entrySet()) {
        		if (entry.getValue().equals(letter) && !(isVisited(letterIndex, entry.getKey()))) {
        			cell = entry.getKey();
        			break;
        		}
        	}
    		
    	} else {
    		cell = findNeighbour(letterIndex, this.path.peekLast());
    	}
    	
    	return cell;
    }
    
    private Cell findNeighbour(int letterIndex, Cell cell) {
    	int r = cell.getR();
    	int c = cell.getC();
    	Character letter = Character.valueOf(this.word.charAt(letterIndex));
    	Cell nbCell = null;
    	
    	Cell[] neighbours = {
    			new Cell(r - 1, c - 1),
    			new Cell(r - 1, c),
    			new Cell(r - 1, c + 1),
    			new Cell(r, c - 1),
    			new Cell(r, c + 1),
    			new Cell(r + 1, c - 1),
    			new Cell(r + 1, c),
    			new Cell(r + 1, c + 1)
    	};
    	
    	for (Cell tempCell: neighbours) {
    		if ((!isOutOfBounds(tempCell)) && 
    			(!isVisited(letterIndex, tempCell))) {
    			
    			Character optLetter = this.board.get(tempCell);
    			if (optLetter != null && optLetter.equals(letter)) {
    				nbCell = tempCell;
    				break;
    			}
    		}
    	}
    	
    	return nbCell;
    }
    
    public boolean check() {
    	int letterIndex = 0;
    	
    	while (true) {
    		Cell tempCell = findCell(letterIndex);
    		
    		if (tempCell != null) {
    			setVisited(letterIndex, tempCell);
    			path.add(tempCell);
    			
    			if (path.size() == this.word.length()) {
        			return true;
        		}
    			
    			letterIndex++;
    			
    		} else if (letterIndex == 0) {
    			return false;
    		} else if (letterIndex > 0) {
        		visited.remove(letterIndex);
        		path.removeLast();
        		letterIndex--;
    		}
    	}
    }
}