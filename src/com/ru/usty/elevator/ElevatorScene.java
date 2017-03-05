package com.ru.usty.elevator;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * The base function definitions of this class must stay the same
 * for the test suite and graphics to use.
 * You can add functions and/or change the functionality
 * of the operations at will.
 *
 */

//Hello there
public class ElevatorScene {

	//TO SPEED THINGS UP WHEN TESTING,
	//feel free to change this.  It will be changed during grading
	public static final int VISUALIZATION_WAIT_TIME = 500;  //milliseconds

	private int numberOfFloors;
	private int numberOfElevators;

	ArrayList<Integer> personCount; //use if you want but
									//throw away and
									//implement differently
									//if it suits you
	ArrayList<Integer> exitedCount = null;
	public static Semaphore exitedCountMutex;

	public ArrayList<Semaphore> inSemaphore;
	public ArrayList<Semaphore> doStuffAtFloorMutex;
	public ArrayList<Elevator> elevatorAtFloor;

	public ArrayList<Elevator> elevators;
	//public Elevator elevator = new Elevator(this);
	

	//Base function: definition must not change
	//Necessary to add your code in this one
	public void restartScene(int numberOfFloors, int numberOfElevators) {

		/**
		 * Important to add code here to make new
		 * threads that run your elevator-runnables
		 * 
		 * Also add any other code that initializes
		 * your system for a new run
		 * 
		 * If you can, tell any currently running
		 * elevator threads to stop
		 */
		personCount = new ArrayList<Integer>();
		elevators = new ArrayList<Elevator>();
		doStuffAtFloorMutex = new ArrayList<Semaphore>();
		this.numberOfFloors = numberOfFloors;
		this.numberOfElevators = numberOfElevators;
		elevatorAtFloor = new ArrayList<Elevator>();
		for(int i = 0; i< numberOfElevators; i++){
			elevators.add(new Elevator(this,i));
		}

		

		if(exitedCount == null) {
			exitedCount = new ArrayList<Integer>();
		}
		else {
			exitedCount.clear();
		}

		inSemaphore = new ArrayList<Semaphore>();
		for(int i = 0; i < getNumberOfFloors(); i++) {
			this.exitedCount.add(0);
			this.personCount.add(0);
			inSemaphore.add(new Semaphore(0));
			doStuffAtFloorMutex.add(new Semaphore(1));
			elevatorAtFloor.add(null);
		}
		exitedCountMutex = new Semaphore(1);

		

		for(int i = 0; i< numberOfElevators; i++){
			new Thread(elevators.get(i)).start();
		}
		
		
//		for(int i = 0; i < numberOfElevators; i++){
//			Elevator ele = new Elevator(this);
//			ele.run();
//			elevators.add(ele);
//		}
		//elevator = new Elevator(this);
	}

	//Base function: definition must not change
	//Necessary to add your code in this one
	public Thread addPerson(int sourceFloor, int destinationFloor) {

		/**
		 * Important to add code here to make a
		 * new thread that runs your person-runnable
		 * 
		 * Also return the Thread object for your person
		 * so that it can be reaped in the testSuite
		 * (you don't have to join() yourself)
		 */
		Thread person = new Thread(new Person(sourceFloor, destinationFloor, this));	
		person.start();

		//dumb code, replace it!
		personCount.set(sourceFloor, personCount.get(sourceFloor) + 1);
		return null;//person;  //this means that the testSuite will not wait for the threads to finish
	}
	
	public Elevator getElevatorAtFloor(int floor){
		return elevatorAtFloor.get(floor);
	}

	//Base function: definition must not change, but add your code
	public int getCurrentFloorForElevator(int elev) {

		//dumb code, replace it!
		//return elevators.get(elevator).getFloor();
		return elevators.get(elev).getFloor();
	}

	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleInElevator(int elev) {
		
		//return elevators.get(elevator).getNumOfPeople();
		return elevators.get(elev).getNumOfPeople();
	}

	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleWaitingAtFloor(int floor) {
		return personCount.get(floor);
	}

	//Base function: definition must not change, but add your code if needed
	public int getNumberOfFloors() {
		return numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfFloors(int numberOfFloors) {
		this.numberOfFloors = numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public int getNumberOfElevators() {
		return numberOfElevators;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfElevators(int numberOfElevators) {
		this.numberOfElevators = numberOfElevators;
	}

	//Base function: no need to change unless you choose
	//				 not to "open the doors" sometimes
	//				 even though there are people there
	public boolean isElevatorOpen(int elevator) {

		return isButtonPushedAtFloor(getCurrentFloorForElevator(elevator));
	}
	//Base function: no need to change, just for visualization
	//Feel free to use it though, if it helps
	public boolean isButtonPushedAtFloor(int floor) {

		return (getNumberOfPeopleWaitingAtFloor(floor) > 0);
	}

	//Person threads must call this function to
	//let the system know that they have exited.
	//Person calls it after being let off elevator
	//but before it finishes its run.
	public void personExitsAtFloor(int floor, Elevator elev) {
		try {
			
			exitedCountMutex.acquire();
			exitedCount.set(floor, (exitedCount.get(floor) + 1));
			exitedCountMutex.release();
			elev.removePerson();
			

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Base function: no need to change, just for visualization
	//Feel free to use it though, if it helps
	public int getExitedCountAtFloor(int floor) {
		if(floor < getNumberOfFloors()) {
			return exitedCount.get(floor);
		}
		else {
			return 0;
		}
	}

	public void addInEle(Elevator elev) {
		elev.addPerson();
		
	}


}
