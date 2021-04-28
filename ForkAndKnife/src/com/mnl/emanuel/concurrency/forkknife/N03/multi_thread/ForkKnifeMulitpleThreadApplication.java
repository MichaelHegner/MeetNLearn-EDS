package com.mnl.emanuel.concurrency.forkknife.N03.multi_thread;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.mnl.emanuel.concurrency.forkknife.N03.multi_thread.dto.Fork;
import com.mnl.emanuel.concurrency.forkknife.N03.multi_thread.dto.Knife;
import com.mnl.emanuel.concurrency.forkknife.N03.multi_thread.dto.Person;
import com.mnl.emanuel.concurrency.forkknife.N03.multi_thread.dto.Table;
import com.mnl.emanuel.concurrency.utils.ThreadUtils;

/**
 * Beispiel mit je einem Thread je Person.
 */
public class ForkKnifeMulitpleThreadApplication {
	Fork  fork  = new Fork();
	Knife knife = new Knife();
	Table table = new Table(fork, knife);
	
	public void execute() {
		
		// Generate any persons
		List<Person> persons = PersonGenerator.generatePersons();
		
		List<Thread> threadList = new ArrayList<>();
		persons.forEach(person -> {
			// Prepare Meal Consumer
			Runnable mealConsumer = new MealConsumer(table, person);
			
			// Create new thread and add to list.
			Thread thread = new Thread(mealConsumer);
			threadList.add(thread);
		});
		
		// Start all threads.
		threadList.forEach(thread -> thread.start());
		
		// Await all threads.
		threadList.forEach(thread -> ThreadUtils.safeJoin(thread));
	}
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("START APPLICATION");
		
		new ForkKnifeMulitpleThreadApplication().execute();
		
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
	private Table  table;
	private Person person;
	

	public MealConsumer(Table table, Person person) {
		this.table   = table;
		this.person = person;
	}

	@Override
	public void run() {
		person.takeASeat(table);
		person.eat();
		person.leave(table);
	}
}
