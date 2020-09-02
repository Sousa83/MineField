package br.com.rick.Visao;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.rick.Modelo.Board;

@SuppressWarnings("serial")
public class Container extends JPanel{
	/*
	 * I do this class extends the JPanel to make it easy for me.
	 * 
	 * I do a grid layout with the amount the rows and columns, before i'll add each button for each field of Board
	 * Now, i need register the observers
	 */
	
	public Container(Board board) {
		setLayout(new GridLayout(board.getRows(), board.getColumns()));
		board.getFields(f -> add(new FieldButton(f)));

		board.registerObserver(o -> {
			SwingUtilities.invokeLater(() -> {
				if (o.equals(true)) {
					JOptionPane.showMessageDialog(this, "You win XD");
				} else {
					JOptionPane.showMessageDialog(this, "You lose DX");				
				}
				
				board.resetBoard();
			});
		});
	}
}
