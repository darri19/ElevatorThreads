package com.ru.usty.elevator;

public class Elevator implements Runnable{
	
	private ElevatorScene eleScene;
	private int currentFloor;
	private int numOfPeople;
	private final int  MAX = 6;
	private int numOfFloors;
	
	public Elevator(ElevatorScene es){
		eleScene = es;
		currentFloor = 0;
		numOfPeople = 0;
	}

	@Override
	public void run() {
		numOfFloors = eleScene.getNumberOfFloors();
		while(true){
//			for(int i = currentFloor; i < numOfFloors; i++){
//				currentFloor = i;
//				doTheStuff();
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			for(int i = currentFloor; i >= 0; i--){
//				currentFloor = i;
//				doTheStuff();
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
			
			doTheStuff();
			try {
				Thread.sleep(eleScene.VISUALIZATION_WAIT_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			currentFloor++;
			doTheStuff();
			try {
				Thread.sleep(eleScene.VISUALIZATION_WAIT_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			currentFloor--;
			
		}
	}
	
	private void doTheStuff() {
//		for(int i = numOfPeople; i >= 0; i--){
//			System.out.println(currentFloor);
//			eleScene.outSemaphore.get(currentFloor).release();
//		}
//		for(int i = numOfPeople; i >= 0; i--){
//			try {
//				eleScene.outSemaphore.get(currentFloor).acquire();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		int number = eleScene.getNumberOfPeopleWaitingAtFloor(currentFloor);
//		if(number >= MAX){
//			number = MAX;
//		}
//		if(6 - numOfPeople < number){
//			number = MAX - numOfPeople;
//		}
//		for (int i = 0; i < number; i++){
//			eleScene.inSemaphore.get(currentFloor).release();
//			numOfPeople++;
//		}
		try {
			Thread.sleep(eleScene.VISUALIZATION_WAIT_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		eleScene.outSemaphore.get(currentFloor).release(numOfPeople);
		int number = eleScene.getNumberOfPeopleWaitingAtFloor(currentFloor);
		
		try {
			Thread.sleep(eleScene.VISUALIZATION_WAIT_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(number >= MAX){
			number = MAX;
		}
		if(MAX - numOfPeople < number){
			number = MAX - numOfPeople;
		}
		eleScene.inSemaphore.get(currentFloor).release(number);	
		eleScene.personCount.set(currentFloor, eleScene.getNumberOfPeopleWaitingAtFloor(currentFloor)-number);
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
