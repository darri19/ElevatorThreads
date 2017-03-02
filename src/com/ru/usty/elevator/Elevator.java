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
			for(; currentFloor < eleScene.getNumberOfFloors(); currentFloor++){
				try {
					doTheStuff(currentFloor);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for(; currentFloor >= 0; currentFloor--){
				try {
					doTheStuff(currentFloor);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void doTheStuff(int currFloor) throws InterruptedException {
		for(int i = numOfPeople; i >= 0; i--){
			eleScene.outSemaphore.get(currFloor).release();
		}
		for(int i = numOfPeople; i >= 0; i--){
			eleScene.outSemaphore.get(currFloor).acquire();
		}
		int number = eleScene.getNumberOfPeopleWaitingAtFloor(currFloor);
		if(number >= MAX){
			number = MAX;
		}
		if(6 - numOfPeople < number){
			number = MAX - numOfPeople;
		}
		for (int i = 0; i < number; i++){
			eleScene.inSemaphore.get(currFloor).release();
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
