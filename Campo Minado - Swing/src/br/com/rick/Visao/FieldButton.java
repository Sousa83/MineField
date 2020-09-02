package br.com.rick.Visao;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import br.com.rick.Modelo.EventField;
import br.com.rick.Modelo.Field;
import br.com.rick.Modelo.ObserverField;

@SuppressWarnings("serial")
public class FieldButton extends JButton implements ObserverField, MouseListener{
	//How the event is going to be trough of mouse, i do this implements the Interface "MouseListener"
	//The button is the Class that will has many settings
	
	
	//I'll need of colors to represent the status of Field
	private final Color BG_DEFAULT = new Color(184, 184, 184);
	private final Color BG_BLUE = new Color(8, 179, 247);
	private final Color BG_RED = new Color(189, 66, 68);
	private final Color BG_GREEN = new Color(0, 100, 0);

	private Field field;
	
	public FieldButton(Field field) {
		this.field = field;
		
		//For each field i'll set the color, border and register the observer to field the itself
		//Also i need add the instance (current button) in MouseListener
		setBackground(BG_DEFAULT);
		setBorder(BorderFactory.createBevelBorder(0));
		
		addMouseListener(this);
		field.registerObserver(this);
	}
	
	//When the event i call the corresponding operation
	@Override
	public void arrivedEvent(Field field, EventField event) {
		switch (event) {
		case OPEN: 
			open();
			break;
		case EXPLODE: 
			explose();
			break;
		case MARK:
			mark();
			break;
		case MARKOFF:
			markOff();
			break;	
		default:
			defaultButton();			
		}
	}
	
	//For each event i'll set a new color and text
	public void explose() {
		setBackground(BG_RED);
		setForeground(Color.WHITE);
		setText("*");
		
	}

	public void open() {
		setBorder(BorderFactory.createLineBorder(Color.GRAY));

		if (field.isMined()) {
			setBackground(BG_RED);	
			return;
		}
		
		setBackground(BG_DEFAULT);	
		
		//For each number of mines i'll set a different color
		switch (field.minesInNeighborhood()) {
		case 1:
			setForeground(BG_GREEN);
			break;
		case 2:
			setForeground(Color.BLUE);
			break;
		case 3:
			setForeground(Color.YELLOW);
			break;
		case 4:
		case 5:
		case 6:
			setForeground(Color.RED);
			break;
		default:
			setForeground(Color.PINK);
			break;			
		}
		
		String value = !field.safeNeighbor()? field.minesInNeighborhood() + "": "";
		
		setText(value);
	}
	
	public void mark() {
		setBackground(BG_BLUE);
		setText("X");
	}
	
	public void markOff() {
		if (!field.isMarked()) {
			defaultButton();
		}
	}
	
	public void defaultButton () {
		setBackground(BG_DEFAULT);	
		setBorder(BorderFactory.createBevelBorder(0));
		setText("");					
	}
	
	//Here i program the event for each button of mouse
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) {
			field.openField();
		} else {
			field.markField();
		}
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
