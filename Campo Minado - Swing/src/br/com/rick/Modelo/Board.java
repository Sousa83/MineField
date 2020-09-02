package br.com.rick.Modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Board implements ObserverField{
	//The Board has rows, columns, mines, a list of Fields and Observers (this class also has events where the player will win or lose)
	private final int rows;
	private final int columns;
	private final int mines;
	
	private final List<Field> fields = new ArrayList<>();
	private final List<Consumer<Boolean>> observers = new ArrayList<>();
	
	public Board(int line, int column, int mines) {
		this.rows = line;
		this.columns = column;
		this.mines = mines;
		
		//When i initialize the board, i need to call these methods 
		generateFields();
		connectNeighbors();
		distributeMines();
	}
	
	//I need to register and notify the observers
	public void registerObserver (Consumer<Boolean> observer) {
		observers.add(observer);
	}
	
	private void notifyObserver (boolean result) {
		observers.parallelStream().forEach(o -> o.accept(result));
	}
	
	//As i'll be interacting across the Board, i need of methods to open and mark the filds.
	
	public void openField(int line, int column) {
		fields.parallelStream()
		  .filter(field -> field.getRow() == line && field.getColumn() == column)
		  .findFirst().ifPresent(field -> field.openField());

	}
	
	public void markField(int line, int column) {
		fields.parallelStream()
			  .filter(field -> field.getRow() == line && field.getColumn() == column)
			  .findFirst()
			  .ifPresent(field -> field.markField());
	}
	
	
	//Fields are generated and added to the Board and as observers 
	private void generateFields() {
		for (int l = 0; l < rows; l++) {
			for (int c = 0; c < columns; c++) {
				//I'll fill by columns
				Field field = new Field (l, c);
				fields.add(field);
				
				//I also have a field as Subject, because when the player lose all fields be will opened
				field.registerObserver(this);
			}
		}
	}

	private void openMineds() {
		fields.parallelStream().filter(f -> f.isMined()).filter(f -> !f.isMarked()).forEach(f -> f.setOpen());		
	}
	
	//I need connect them as neighbors; i have two ties just to make sure
	private void connectNeighbors() {
		for(Field field1: fields) {
			for(Field field2: fields) {
				field1.addNeighbor(field2);
			}
		}
	}
	
	//Mines are randomly generated
	private void distributeMines() {
		long fieldsMined = 0;
		int random = 0;
		
		do {
			random = (int) (Math.random() * fields.size());
			fields.get(random).setUndermining();
			fieldsMined = fields.stream().filter(f -> f.isMined()).count();
		} while (fieldsMined < mines);
	}
	
	//finally the auxiliary methods: 
	public boolean objectiveOfGame() {
		return fields.stream().allMatch(f -> f.objective());
	}
	
	public void resetBoard() {
		for (Field field: fields) {
			field.reset();
		}
		
		distributeMines();
	}
	
	//The board is the Observer because it will return the result of game
	//As the result is a event i need notify 
	@Override
	public void arrivedEvent(Field field, EventField event) {
		if (event == EventField.EXPLODE) {
			openMineds();
			notifyObserver(false);
		}
		else if (objectiveOfGame()) {
			notifyObserver(true);
		}
	}
	
	// ****Getters****
	
	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
	
	public void getFields(Consumer<Field> function) {
		fields.forEach(function);
	}
}