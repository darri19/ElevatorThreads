package com.ru.usty.elevator;

public class Elevator implements Runnable{
	
	private ElevatorScene eleScene;
	private int currentFloor;
	
	public Elevator(ElevatorScene es){
		eleScene = es;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public int getFloor(){
		return currentFloor;
	}
	
}
