package com.mnl.emanuel.concurrency.forkknife.N02.two_thread;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.mnl.emanuel.concurrency.forkknife.N02.two_thread.dto.Fork;
import com.mnl.emanuel.concurrency.forkknife.N02.two_thread.dto.Knife;
import com.mnl.emanuel.concurrency.forkknife.N02.two_thread.dto.Person;
import com.mnl.emanuel.concurrency.forkknife.N02.two_thread.dto.Table;

/**
 * Beispiel mit zwei Threads (Main thread for application and one thread for consuming).
 */
public class ForkKnifeTwoThreadApplication {
	Fork  fork  = new Fork();
	Knife knife = new Knife();
	Table table = new Table(fork, knife);
	
	public void execute() {

		// Generate any persons
		List<Person> persons = PersonGenerator.generatePersons();

		// Prepare Meal Consumer
		Runnable mealConsumer = new MealConsumer(table, persons);
		
		// ATTENTION: runnable.run works, BUT it is a synchronized call and not executed in a separate thread
		// mealConsumer.run();
		
		// Starting new thread
		Thread thread = new Thread(mealConsumer);
		thread.start();
		
		// ATTENTION: Activate next line to wait for second thread to be finished.
		// thread.join();
		
	}

	public static void main(String[] args) throws InterruptedException {
		System.out.println("START APPLICATION");
		
		new ForkKnifeTwoThreadApplication().execute();
		
		System.out.println("END APPLICATION");
	}
	
	static class PersonGenerator {
		private static final int NUMBER_OF_PERSONS = 10;

		public static List<Person> generatePersons() {
			return IntStream.range(1, NUMBER_OF_PERSONS + 1)
				.mapToObj(i -> new Person("Name_" + i))
				.collect(Collectors.toList());
		}
	}

}

/**
 * Consumer useable in a separate thread.
 */
class MealConsumer implements Runnable {
	private Table        table;
	private List<Person> persons;

	public MealConsumer(Table table, List<Person> persons) {
		this.table   = table;
		this.persons = persons;
	}

	@Override
	public void run() {
		for (Person person : persons) {
			person.takeASeat(table);
			person.eat();
			person.leave(table);
		}
	}
	
}
