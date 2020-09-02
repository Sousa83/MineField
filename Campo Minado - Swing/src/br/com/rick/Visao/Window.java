package br.com.rick.Visao;

import javax.swing.JFrame;

import br.com.rick.Modelo.Board;

@SuppressWarnings("serial")
public class Window extends JFrame{
	
	//Here's where i create the board, amount of rows, columns and mines 
	public Window () {
		add(new Container(new Board (16, 30, 30)));

		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(690, 438);
		setTitle("MineField");
	}
}
