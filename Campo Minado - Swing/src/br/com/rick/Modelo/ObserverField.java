package br.com.rick.Modelo;

public interface ObserverField {
	//As all observer implements a Interface i did this 
	//I need know on which field arrived and which event
	public void arrivedEvent (Field field, EventField event);
}
