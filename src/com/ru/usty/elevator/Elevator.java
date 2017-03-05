package com.ru.usty.elevator;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Elevator implements Runnable{
	
	private ElevatorScene eleScene;
	private int currentFloor;
	private int numOfPeople;
	private final int  MAX = 6;
	private int numOfFloors;
	private int numOfWaits = 3;
	public ArrayList<Semaphore> outSemaphore;
	
	public Elevator(ElevatorScene es, int eleNumber){
		eleScene = es;
		currentFloor = 0;
		numOfPeople = 0;

		outSemaphore = new ArrayList<Semaphore>();
		for(int i = 0; i < eleScene.getNumberOfFloors(); i++){
			outSemaphore.add(new Semaphore(0));
		}
	}

	@Override
	public void run() {
		numOfFloors = eleScene.getNumberOfFloors();
		while(true){
			if(!eleScene.isAnyButtonPushed() && numOfPeople==0) {
				goToFirstFloor();
			} else {
				int i;
				for(i = currentFloor+1; i < numOfFloors; i++){
					currentFloor = i;
					doTheStuff();
				}
				for(i = currentFloor-1; i >= 0; i--){
					currentFloor = i;
					doTheStuff();
				}				
			}
		}
	}
	
	private void goToFirstFloor() {
		if(currentFloor>0) {
			currentFloor--;
		}
	}
	
	private void doTheStuff() {
		
		try {
			eleScene.doStuffAtFloorMutex.get(currentFloor).acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		eleScene.elevatorAtFloor.set(currentFloor, this);
		waitABit();
		
		//Let people out
		outSemaphore.get(currentFloor).release(numOfPeople);
		waitABit();
		outSemaphore.get(currentFloor).drainPermits();
		
		//Let people in
		if(eleScene.isButtonPushedAtFloor(currentFloor)) {
			int numberOfPeopleToLetIn = eleScene.getNumberOfPeopleWaitingAtFloor(currentFloor);
			if(numberOfPeopleToLetIn >= MAX){
				numberOfPeopleToLetIn = MAX;
			}
			if(MAX - numOfPeople < numberOfPeopleToLetIn){
				numberOfPeopleToLetIn = MAX - numOfPeople;
			}
			eleScene.inSemaphore.get(currentFloor).release(numberOfPeopleToLetIn);	
			eleScene.personCount.set(currentFloor, eleScene.getNumberOfPeopleWaitingAtFloor(currentFloor)-numberOfPeopleToLetIn);			
		}
		waitABit();

		eleScene.doStuffAtFloorMutex.get(currentFloor).release();
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
