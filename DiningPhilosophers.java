
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {

	/**
	 * Represents the entire situation of the philosophers dining at the table
	 * Before running the Main, make sure that you had entered the correct Path to the 
	 * text file.
	 * Text file should be formatted in this manner : 
	 * FIRST_LINE -> numberOfPhilosophers " " numberOfChopsticks
	 * SECOND_LINE -> numberOfHands1 " " numberOfHands2 " " .... for each philosopher
	 */
	public DiningPhilosophers() {
		String path ="...testFile.txt";
		new Room(new FileHandler(path));
	}
	
	/**
	 * Represents a room which,in itself,contains philosophers, the table 
	 * and chop sticks on the table.
	 */
	class Room {
		private Philosopher[] philosophers;
		private Table table;

		
		public Room(FileHandler handler) {
			//Initialize the structure for storing philosophers and the table
			this.philosophers = new Philosopher[handler.getNumPhilo()];
			table = new Table(handler.getNumPhilo(),handler.getNumChop());

			int[] numHands = handler.getNumHandsArray();
			StringBuilder showHands = new StringBuilder();
			
			/*For clarity of testing when running the program, display number of hands 
			* for each philosopher
			*/
			for(int i=0; i<this.philosophers.length; i++) {
				int numberOfHands = numHands[i];
				philosophers[i] = new Philosopher(i,table,numberOfHands);
				showHands.append(i+ " : "+ numberOfHands +"   /");

			}
			System.out.println(showHands);
			
			//Make new philosophers and let them start eating
			for(int i=0; i<this.philosophers.length; i++) {
				new Thread(this.philosophers[i]).start();
			}
		}
			
	}

	/**
	 * Table class acts as a monitor class since it contains monitor functions
	 * within itself.
	 *
	 */
	class Table {

		private Lock mutex = null;
		private Condition[] condition ;
		private ArrayList<Integer> ID_list;
		private boolean[] currentlyEating;
		private int numberOfChopsticks;

		
		public Table(int numOfPhilosophers,int numberOfChopsticks) {
			this.numberOfChopsticks = numberOfChopsticks;
			this.ID_list = new ArrayList<>(numOfPhilosophers);
			this.condition = new Condition[numOfPhilosophers];
			this.mutex = new ReentrantLock();
			this.currentlyEating = new boolean[numOfPhilosophers];

			for(int i=0; i<numOfPhilosophers;i++) {
				this.ID_list.add(i,i);
				this.currentlyEating[i] = false;
				this.condition[i] = this.mutex.newCondition();
			}
			
		}
		
		/**
		 * Monitor function for grabbing the chop sticks
		 * @param ID is the ID of each philosopher trying to get a hold of chop sticks
		 * @param numberOfWanted is the number of chop sticks that philosopher is trying to take
		 */
		public void grabChopsticks(int ID,int numberOfWanted) {
			//This lock ensures only one active philosopher is in the monitor function
			this.mutex.lock();
			try {
				/*
				 * Block the philosopher from taking the chop sticks if the condition
				 * is not met
				 */
				while(this.numberOfChopsticks < numberOfWanted) {
					this.condition[ID].await();
				}

				/*
				 * Grab the sticks and show the current situation at the table
				 */
				this.currentlyEating[ID] = true;
				this.numberOfChopsticks -= numberOfWanted;
				this.seeWhoIsEating();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}finally {
				this.mutex.unlock();
			}
		}
		
		/**
		 * Monitor function for releasing the chop sticks
		 * @param ID represents an ID of the philosopher entering the monitor function
		 * @param numberOfTaken represents a number of chop sticks being returned by ID
		 */
		public void releaseChopsticks(int ID,int numberOfTaken ) {
			this.mutex.lock();
			try {
				
				this.currentlyEating[ID] = false;
				this.seeWhoIsEating();
				this.numberOfChopsticks += numberOfTaken;
				
				for(int i=0;i<this.ID_list.size();i++) {
					this.condition[i].signalAll();
				}
			}finally {
				this.mutex.unlock();
			}
		}
		
		/**
		 * Displays the current eating situation at the table
		 */
		public void seeWhoIsEating() {
			StringBuilder show = new StringBuilder();
			show.append("Currently EATING:");
			for(int i=0; i<this.currentlyEating.length; i++) {
				if(this.currentlyEating[i]) {
					show.append(" "+i);
				}
			}
			System.out.println(show);
		}

	}

	/**
	 * Represents philosophers dining at the table.
	 * When he is not eating, the philosopher thinks.
	 *
	 */
	class Philosopher implements Runnable {

		private Table table;
		private int ID;
		private int requiredChopsticks;
		
		public Philosopher(int ID,Table table, int chopsticks) {
			this.ID = ID;
			this.table = table;
			this.requiredChopsticks = chopsticks;
		}
		
		private void eat() {
			try {
				System.out.println("Philosopher "+ID +" with "+this.requiredChopsticks+"  arms : I'm eating...");
				Thread.sleep(3000);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		private void think() {
			try {
				System.out.println("Philosopher "+ID +" : I'm thinking...");
				Thread.sleep(5000);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		@Override
		public void run() {
			while(true) {
				this.table.grabChopsticks(this.ID,this.requiredChopsticks);
				this.eat();
				System.out.println("Philosopher "+this.ID+": I think I'm done for now..");
				this.table.releaseChopsticks(this.ID,this.requiredChopsticks);
				this.think();
			}
			
		}

	}

	/**
	 * Handles all the data parsing needed to be done for program to run
	 * with desired specifications of philosophers, chop sticks and hands
	 */
	class FileHandler {

		private String fileLocation;
		private int numOfPhilosophers,numOfChopsticks;
		private int[] numOfHands;
		
		public FileHandler(String location) {
			this.fileLocation = location;
			File dataFile = new File(this.fileLocation);
			
			try(Scanner grabInfo = new Scanner(dataFile)){
				String firstLine = grabInfo.nextLine();
				String secondLine = grabInfo.nextLine();
				this.numOfPhilosophers = Integer.parseInt(firstLine.split(" ")[0]);
				this.numOfChopsticks = Integer.parseInt(firstLine.split(" ")[1]);
				String[] hands = secondLine.split(" ");
				this.numOfHands = new int[hands.length];
				for(int i=0;i< hands.length;i++) {
					this.numOfHands[i] = Integer.parseInt(hands[i]);
				}
				
				
			}catch(FileNotFoundException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		public int[] getNumHandsArray() {
			return this.numOfHands;
		}
		public int getNumPhilo() {
			return this.numOfPhilosophers;
		}
		public int getNumChop() {
			return this.numOfChopsticks;
		}
	}
	
	class Chopstick {

		private boolean isFree;
		
		public Chopstick() {
			this.isFree = true;
		}
		
		public boolean getState() {
			return this.isFree;
		}
		public void setState(boolean state) {
			this.isFree = state;
		}
	}
	
	public static void main(String[] args) {
		new DiningPhilosophers();
	}
}
