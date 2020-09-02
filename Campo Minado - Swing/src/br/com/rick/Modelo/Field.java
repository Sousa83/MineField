package br.com.rick.Modelo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Field {
	/*
	 * The field will need attributes of position (row and column), states (open, marked, undermined) and lists.
	 * A list for observers because this class has events and a list for neighboring fields (neighbors)
	 */
	
	//Position of field
	private final int row;
	private final int column;
	
	//States of field
	private boolean open;
	private boolean marked;
	private boolean undermined;
	
	//All field has a neighboring field
	private List<Field> neighbors = new ArrayList<>();
	
	//The Subject is a Field because the event will happens here
	private Set<ObserverField> observers = new HashSet<>();
	
	//I need to register and notify the observers
	public void registerObserver (ObserverField observer) {
		observers.add(observer);
	}
	
	private void notifyObservers(EventField event) {
		observers.parallelStream().forEach(o -> o.arrivedEvent(this, event));
	}
	
	Field(int line, int column) {
		this.row = line;
		this.column = column;
	}
	
	// A neighboring field is a field that is on one side or diagonally.
	boolean addNeighbor(Field neighbor) {
		//First check if it's a diagonal field by the position difference with the current field
		boolean differenceLine = neighbor.row != this.row;
		boolean differenceCol = neighbor.column != this.column;
		
		boolean diagonal = differenceCol && differenceLine;
		
		//Now, i need to know the difference in position (distance)
		int delta = Math.abs(this.row - neighbor.row) + Math.abs(this.column - neighbor.column);
		
		//Is a neighbor field if: is not diagonal and distance is 1; or is diagonal and distance is 2
		if (delta == 1 && !diagonal) {
			neighbors.add(neighbor);
			return true;			
		}
		else if (delta == 2 && diagonal) {			
			neighbors.add(neighbor);
			return true;
		}
		else {		
			return false;
		}
	}
	
	//Here there are points that events can happen. I'll do have to do tests with the states attributes, if the test returns "true" i need notify the observers
	
	//Marking the field
	public  void markField() {
		//I only can to mark a field that is not open
		if (!open) {
			marked = !marked;			
		}

		if (marked) {
			notifyObservers(EventField.MARK);
		}
		else {
			notifyObservers(EventField.MARKOFF);			
		}
	}

	//Opening the field 
	public boolean openField() {
		//I only can to open a field that is not opened and marked
		if (!open && !marked) {			
			if (undermined) {
				notifyObservers(EventField.EXPLODE);	
				return true;
			}
			
			setOpen();
			
			//If the neighboring field is safe, i open it
			if (safeNeighbor()) {
				neighbors.forEach(n -> n.openField());				
			}
			return true;
		}
		else {
			return false;			
		}
	}
	
	public boolean safeNeighbor() {
		//i'm filtering out neighbors that don't match those predicates
		return neighbors.stream().noneMatch(n -> n.undermined);
	}

	//Every field has a objective, i need it to do the objective of game
	boolean objective() {
		if (open && !undermined) {
			return true;			
		}
		else if (undermined && marked) {
			return true;
		}
		else {
			return false;			
		}
	}
	
	//When the neighborhood is not safe i need to know the number of mines	
	public int minesInNeighborhood() {
		return (int) (neighbors.stream().filter(n -> n.undermined).count());
	}
	
	//*******Getters and Setters********
	public boolean isMarked() {
		return marked;
	}
	
	public boolean isMined() {
		return undermined;
	}
	
	public boolean isOpen() {
		return open;
	}

	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}

	void setOpen () {
		open = true;
		
		if(open)
		notifyObservers(EventField.OPEN);			
	}
	
	public void setUndermining() {
		undermined = true;
	}
	
	void reset() {
		open = false;
		undermined = false;
		marked = false;
		notifyObservers(EventField.RESET);
	}
}

