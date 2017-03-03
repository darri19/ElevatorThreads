package com.ru.usty.elevator;

public class Person implements Runnable{

	//First things first, I'm the realest
	public ElevatorScene eleScene;
	public int sourceFloor;
	public int destinationFloor;
	
	//Drop this and let the whole world feel it
	public Person(int sourceFloor, int destinationFloor) {
		this.sourceFloor = sourceFloor;
		this.destinationFloor = destinationFloor;
	}

	//I'M SO FANCY
	@Override
	public void run() {
		try {
			System.out.println(sourceFloor);
			eleScene.inSemaphore.get(sourceFloor).acquire(1);
			
			eleScene.outSemaphore.get(destinationFloor).acquire(1);
			eleScene.personExitsAtFloor(destinationFloor);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
