import java.util.ArrayList;

/**
 * Implementation of the Dining Philosophers problem for week 5 of the POSA Coursera class.
 * 
 * This implementation follows the Conductor solution - see:
 * http://en.wikipedia.org/wiki/Dining_philosophers_problem#Conductor_solution
 * 
 * The Waiter class acts as the Monitor Object, preventing deadlock.
 * The Philosopher class is the Runnable
 * The Chopstick class prevents two Philosophers from grabbing the same chopstick (which the
 * waiter alone does not do).
 * 
 * @author Meggie
 *
 */
public class Dinner {
	final static String LEFT_CHOPSTICK = "left chopstick";
	final static String RIGHT_CHOPSTICK = "right chopstick";
	final static int NUM_PHILOSOPHERS = 5;

	/**
	 * Get everything created and started.
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Dinner is starting!");
		
		Waiter bob = new Waiter(NUM_PHILOSOPHERS);
		
		// I use ArrayLists of objects for simpler initialization/interaction
		ArrayList<Thread> philosopherThreads = new ArrayList<Thread>(NUM_PHILOSOPHERS);
		ArrayList<Chopstick> chopsticks = new ArrayList<Chopstick>(NUM_PHILOSOPHERS);
		
		// Create Chopsticks
		for (int i = 0; i < NUM_PHILOSOPHERS; i++)
		{
			chopsticks.add(new Chopstick());
		}
		
		// Create Philosophers and assign them chopsticks, with handling for the boundary cases.
		for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
			Chopstick rc, lc;
			// If we're the first philosopher our right chopstick is the last chopstick.
			if (i == 0) {
				lc = chopsticks.get(i);
				rc = chopsticks.get(NUM_PHILOSOPHERS - 1);
			}
			// If we're the last philosopher our left chopstick is the first chopstick.
			else if (i == NUM_PHILOSOPHERS) {
				lc = chopsticks.get(0);
				rc = chopsticks.get(i);
			}
			// Otherwise our right chopstick is simply one less than our left chopstick.
			else {
				lc = chopsticks.get(i);
				rc = chopsticks.get(i - 1);
			}
			Philosopher philo = new Philosopher(NUM_PHILOSOPHERS, i + 1, bob, lc, rc);
			philosopherThreads.add(new Thread(philo));
		}
		
		// Start Philosophers
		for (Thread t: philosopherThreads) {
			t.start();
		}
		// Join Philosophers
		for (Thread t: philosopherThreads) {
			t.join();
		}
		
		System.out.println("Dinner is over!");
	}
	
	/**
	 * The Waiter class is the representative of the Monitor Object pattern in my implementation.
	 * 
	 * Each philosopher waits and notifies on the Waiter, who prevents deadlock by preventing
	 * a philosopher from getting the last chopstick if the philosopher does not already have a
	 * chopstick.
	 * 
	 * @author Meggie
	 *
	 */
	static class Waiter {
		int numActors;
		int usedActors = 0;
		
		Waiter(int num) {
			numActors = num;
		}
		
		public void returnChopstick() {
			usedActors--;
		}
		
		/**
		 * The only complicated method here. This function will allow a caller to increment the
		 * number of used actors if there are sufficient unused actors or if the caller already
		 * has a chopstick.
		 * 
		 * @param hasChopstick
		 * @return whether a chopstick was successfully allocated
		 */
		public boolean requestChopstick(boolean hasChopstick) {
			boolean allowed = false;
			if (usedActors < (numActors - 1) || hasChopstick) {
				usedActors++;
				allowed = true;
			}
			else {
				allowed = false;
			}
			return allowed;
		}
	}
	
	/**
	 * This class represents the thread doing the work.
	 * @author Meggie
	 *
	 */
	static class Philosopher implements Runnable {
		int bitesTaken = 0;
		int maxBites;
		int myId;
		Waiter bob;
		Chopstick leftChopstick;
		Chopstick rightChopstick;
		
		Philosopher(int max, int id, Waiter b, Chopstick lc, Chopstick rc) {
			maxBites = max;
			myId = id;
			bob = b;
			leftChopstick = lc;
			rightChopstick = rc;
		}

		@Override
		public void run() {
			while (bitesTaken < maxBites) {
				eat();
				bitesTaken++;
			}
		}
		
		public void eat() {
			acquireChopstick(leftChopstick, false, LEFT_CHOPSTICK);
			acquireChopstick(rightChopstick, true, RIGHT_CHOPSTICK);
			System.out.println("Philosopher " + myId + " takes bite number " + (bitesTaken + 1));
			releaseChopstick(leftChopstick, LEFT_CHOPSTICK);
			releaseChopstick(rightChopstick, RIGHT_CHOPSTICK);
			// Don't be a greedy philosopher...
			Thread.yield();
		}
		
		/**
		 * Give up a chopstick. Synchronize on the Waiter Monitor Object. Mark the chopstick
		 * as unused, make sure the waiter decrements the used resources, and notify all
		 * threads.
		 * @param stick
		 * @param whichChopstick for printing
		 */
		private void releaseChopstick(Chopstick stick, String whichChopstick) {
			System.out.println("Philosopher " + myId + " puts down " + whichChopstick);
			bob.returnChopstick();
			synchronized (bob) {
				stick.release();
				bob.notifyAll();
			}
		}
		
		/**
		 * Get a chopstick.
		 * @param stick
		 * @param hasChopstick if we already have a chopstick
		 * @param display for printing
		 */
		private void acquireChopstick(Chopstick stick, boolean hasChopstick, String display) {
			// Synchronize on the waiter
			synchronized (bob) {
				// We need to both get an unused chopstick AND check with the waiter before we
				// grab it. Otherwise we wait.
				while (stick.isUsed() || ! bob.requestChopstick(hasChopstick)) {
					try {
						bob.wait();
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// Mark the chopstick as used, and pick it up.
				stick.acquire();
				System.out.println("Philosopher " + myId + " picks up " + display);
			}
		}
	}

	static class Chopstick {
		boolean isUsed = false;
		
		public boolean isUsed() {
			return isUsed;
		}
		
		public void acquire() {
			if (isUsed) {
				// This will tell us if we manage to pick up a chopstick that another 
				// philosopher is using - you shouldn't see this print.
				System.out.println("ERROR: Chopstick being acquired is used");
			}
			isUsed = true;
		}
		
		public void release() {
			isUsed = false;
		}
	}
}
