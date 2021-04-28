package com.mnl.emanuel.concurrency.forkknife.N01.simple;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.mnl.emanuel.concurrency.forkknife.N01.simple.dto.Fork;
import com.mnl.emanuel.concurrency.forkknife.N01.simple.dto.Knife;
import com.mnl.emanuel.concurrency.forkknife.N01.simple.dto.Person;
import com.mnl.emanuel.concurrency.forkknife.N01.simple.dto.Table;

/**
 * Iteratives Beispiel im Main Thread.
 */
public class ForkKnifeSingleThreadApplication {
	Fork fork = new Fork();
	Knife knife = new Knife();
	Table table = new Table(fork, knife);
	
	
	public void execute() {
		
		// Generate any persons
		List<Person> persons = PersonGenerator.generatePersons();
		
		
		for (Person person : persons) {
			person.takeASeat(table);
			person.eat();
			person.leave(table);
		}
	}

	
	public static void main(String[] args) {
		System.out.println("START APPLICATION");
		
		new ForkKnifeSingleThreadApplication().execute();
		
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
