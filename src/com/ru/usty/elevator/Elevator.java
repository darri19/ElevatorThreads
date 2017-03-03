package com.ru.usty.elevator;

public class Elevator implements Runnable{
	
	private ElevatorScene eleScene;
	private int currentFloor;
	private int numOfPeople;
	private final int  MAX = 6;
	
	public Elevator(ElevatorScene es){
		eleScene = es;
		currentFloor = 0;
		numOfPeople = 0;
	}

	@Override
	public void run() {
		while(true){
//			for(; currentFloor < eleScene.getNumberOfFloors(); currentFloor++){
//				doTheStuff();
//			}
//			for(; currentFloor >= 0; currentFloor--){
//				doTheStuff();
//			}
		}
	}
	
	private void doTheStuff() {
		for(int i = numOfPeople; i >= 0; i--){
			System.out.println(currentFloor);
			
			eleScene.outSemaphore.get(currentFloor).release();
		}
		for(int i = numOfPeople; i >= 0; i--){
			try {
				eleScene.outSemaphore.get(currentFloor).acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int number = eleScene.getNumberOfPeopleWaitingAtFloor(currentFloor);
		if(number >= MAX){
			number = MAX;
		}
		if(6 - numOfPeople < number){
			number = MAX - numOfPeople;
		}
		for (int i = 0; i < number; i++){
			eleScene.inSemaphore.get(currentFloor).release();
			numOfPeople++;
		}
	}

	public int getFloor(){
		return currentFloor;
	}

	public int getNumOfPeople() {
		return numOfPeople;
	}
	
}
