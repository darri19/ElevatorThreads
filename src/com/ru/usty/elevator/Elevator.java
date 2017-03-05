package com.ru.usty.elevator;

public class Elevator implements Runnable{
	
	private ElevatorScene eleScene;
	private int currentFloor;
	private int numOfPeople;
	private final int  MAX = 6;
	private int numOfFloors;
	private int numOfWaits = 3;
	private int elevatorNumber;
	
	public Elevator(ElevatorScene es, int eleNumber){
		eleScene = es;
		currentFloor = 0;
		numOfPeople = 0;
		elevatorNumber = eleNumber;
	}

	@Override
	public void run() {
		numOfFloors = eleScene.getNumberOfFloors();
		while(true){
			for(int i = currentFloor+1; i < numOfFloors; i++){
				currentFloor = i;
				doTheStuff();
			}
			for(int i = currentFloor-1; i >= 0; i--){
				currentFloor = i;
				doTheStuff();
			}
		}
	}
	
	private void doTheStuff() {

		waitABit();
		
		//Let people out
		eleScene.outSemaphore.get(currentFloor).release(numOfPeople);
		waitABit();
		eleScene.outSemaphore.get(currentFloor).drainPermits();
		
		//Let people in
		int numberOfPeopleToLetIn = eleScene.getNumberOfPeopleWaitingAtFloor(currentFloor);
		if(numberOfPeopleToLetIn >= MAX){
			numberOfPeopleToLetIn = MAX;
		}
		if(MAX - numOfPeople < numberOfPeopleToLetIn){
			numberOfPeopleToLetIn = MAX - numOfPeople;
		}
		eleScene.inSemaphore.get(currentFloor).release(numberOfPeopleToLetIn);	
		eleScene.personCount.set(currentFloor, eleScene.getNumberOfPeopleWaitingAtFloor(currentFloor)-numberOfPeopleToLetIn);
		waitABit();
	}

	private void waitABit() {
		try {
			Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME/numOfWaits);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	public int getFloor(){
		return currentFloor;
	}

	public int getNumOfPeople() {
		return numOfPeople;
	}

	public void addPerson() {
		numOfPeople++;
	}

	public void removePerson() {
		numOfPeople--;
	}
	
}
