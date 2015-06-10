public class PingPongFriends
{
	
	public static final int MAX_PRINTS = 10;

	/**
	 * Runnable class that prints either "ping" or "pong"
	 * @author Meggie
	 *
	 */
	private static class PingPongThread implements Runnable
	{
		// What to print
		String phrase;
		// Only print a certain number of times
		int numPrints = 0;
		// Do I have the ball?
		boolean hasBall = false;
		// My myFriend (can't print without him)
		PingPongThread myFriend = null;
		// My MonitorObject
		Object ball;
		
		/**
		 * Constructor
		 * @param p Phrase to print
		 * @param b Object to synchronize/wait/notify on
		 */
		public PingPongThread(String p, Object b)
		{
			phrase = p;
			ball = b;
		}
		
		/**
		 * Setter for myFriend
		 */
		public void setFriend(PingPongThread f)
		{
			myFriend = f;
		}
		
		/**
		 * Will print MAX_PRINTS times, unless myFriend isn't set
		 */
		public void run()
		{
			if (myFriend != null)
			{
				while (numPrints < MAX_PRINTS)
				{
					print();
				}
			}
			else
			{
				System.out.println("I don't have a friend :(");
			}
		}
		
		/**
		 * Setter for hasBall
		 */
		public void getBall()
		{
			hasBall = true;
		}
		
		/**
		 * Unset hasBall, and set the friend's hasBall variable.
		 */
		public void throwBall()
		{
			hasBall = false;
			myFriend.getBall();
		}
		
		/**
		 * This function handles the synchronization and printing.
		 */
		private void print()
		{
			// First part of print() is to wait until we have the ball.
			while (! hasBall)
			{
				// ball is our MonitorObject, so we synchronize on it (only one thread
				// can be in this code at a time). We also use ball to wait and notify so
				// that all threads wait on the same object.
				synchronized (ball)
				{
					try
					{
						ball.wait();
					}
					catch (InterruptedException e)
					{
						System.out.println("InterruptedException!");
					}
				}
			}
			
			// Here we print our phrase, increment the number of times we've printed,
			// and throw the ball to our friend.
			System.out.println(phrase);
			numPrints++;
			throwBall();
			
			// Again we acquire the lock on ball, and notify all threads waiting on ball
			// that it's time to wake up and see if we have the ball.
			synchronized (ball)
			{
				ball.notifyAll();
			}
		}
	}
	/**
	 * @param args - we have none of these
	 */
	public static void main(String[] args) throws InterruptedException
	{
		Object ball = new Object();
		PingPongThread ping = new PingPongThread("ping", ball);
		PingPongThread pong = new PingPongThread("pong", ball);
		
		ping.setFriend(pong);
		pong.setFriend(ping);
		
		ping.getBall();
		
		Thread pingThread = new Thread(ping);
		Thread pongThread = new Thread(pong);
		
		pingThread.start();
		pongThread.start();
		
		pingThread.join();
		pongThread.join();
		
		System.out.println("Done!");
	}
}