package com.ru.usty.elevator;

public class Person implements Runnable{

	//First things first, I'm the realest
	public ElevatorScene eleScene;
	public int sourceFloor;
	public int destinationFloor;
	public Elevator elevator;
	
	//Drop this and let the whole world feel it
	public Person(int sourceFloor, int destinationFloor, ElevatorScene elSce) {
		this.sourceFloor = sourceFloor;
		this.destinationFloor = destinationFloor;
		this.eleScene = elSce;
	}

	//I'M SO FANCY
	@Override
	public void run() {
		try {
			eleScene.inSemaphore.get(sourceFloor).acquire(1);
			elevator = eleScene.getElevatorAtFloor(sourceFloor);
			eleScene.addInEle(elevator);
			elevator.outSemaphore.get(destinationFloor).acquire(1);
			eleScene.personExitsAtFloor(destinationFloor, elevator);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
