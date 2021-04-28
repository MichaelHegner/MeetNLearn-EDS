package com.mnl.emanuel.concurrency.forkknife.N04.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.mnl.emanuel.concurrency.forkknife.N04.executor.dto.Fork;
import com.mnl.emanuel.concurrency.forkknife.N04.executor.dto.Knife;
import com.mnl.emanuel.concurrency.forkknife.N04.executor.dto.Person;
import com.mnl.emanuel.concurrency.forkknife.N04.executor.dto.Table;

/**
 * Beispiel mit je einem Thread je Person.
 */
public class ForkKnifeExecutorServiceThreadApplication {
	Fork  fork  = new Fork();
	Knife knife = new Knife();
	Table table = new Table(fork, knife);
	
	public void execute() {
		// ExecutorService service = Executors.newSingleThreadExecutor();    // Running all threads in a single thread
		// ExecutorService service = Executors.newFixedThreadPool(2);        // Running all threads in n threads, e.g. 2
		ExecutorService service = Executors.newCachedThreadPool();           // Create a Thread Pool which increase or decrease
		
		// Generate any persons
		List<Person> persons = PersonGenerator.generatePersons();
		
		List<Runnable> consumerList = new ArrayList<>();
		persons.forEach(person -> {
			// Prepare Meal Consumer
			Runnable mealConsumer = new MealConsumer(table, person);
			
			// Add consumer to consumer list.
			consumerList.add(mealConsumer);
		});
		
		// Start all threads.
		consumerList.forEach(service::execute);
	}
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("START APPLICATION");
		new ForkKnifeExecutorServiceThreadApplication().execute();
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
