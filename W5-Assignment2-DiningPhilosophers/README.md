# W5 - Dining Philosophers

ATTN: As with all peer-assessments, you will receive the average of your 4 scores.  For the programming assignments, you will receive the maximum score out of the ones you submit (so if you get 20/20 for Java and 15/20 for C++, you will get 20/20 for Programming Assignment 1).

The Dining Philosophers problem is as follows:  A group of philosophers are sitting down at a circular table with food in the middle of the table, and a chopstick on each side of each philosopher.  At any time, they are either thinking or eating.  In order to eat, they need to have two chopsticks.  If the chopstick to their left or right is currently being used, they must wait for the other philosopher to put it down.  You may notice that if each philosopher decides to eat at the same time, and each picks up the chopstick to his or her right, he or she will not be able to eat, because everyone is waiting for the chopstick on their left.  This situation is called “deadlock”.  In this assignment, you will use the Monitor Object pattern in an algorithm to prevent deadlock.

The Java language implements the Monitor Object pattern in its implementation of object-level locking and the synchronized/wait()/notify()/notifyAll() constructs.  You might want to look at these resources to see how the pattern is realized by using those Java statements. There should be 5 philosophers and 5 chopsticks, and each philosopher should eat exactly five times, and be represented by a Thread.  The program should create output that looks something like this:

Dinner is starting!

Philosopher 1 picks up left chopstick.
Philosopher 1 picks up right chopstick.
Philosopher 1 eats.
Philosopher 3 picks up left chopstick
Philosopher 1 puts down right chopstick.
Philosopher 3 picks up right chopstick.
Philosopher 2 picks up left chopstick.
Philosopher 1 puts down left chopstick.
Philosopher 3 eats.
Philosopher 2 picks up right chopstick.
Philosopher 2 eats.
Philosopher 2 puts down right chopstick.
Philosopher 2 puts down left chopstick.
Philosopher 3 puts down right chopstick.
Philosopher 3 puts down left chopstick.
…
Dinner is over!

Use Java's synchronization and object locking to design a simple model of the Dining
Philosophers problem in Java  and an algorithm to prevent deadlock.

Please design this program in Java without using extra frameworks or libraries (you may use java.util.concurrent) and contain code in a single file.  Someone should be able to run something like “javac Program.java” and “java Program” and your program should successfully run!
