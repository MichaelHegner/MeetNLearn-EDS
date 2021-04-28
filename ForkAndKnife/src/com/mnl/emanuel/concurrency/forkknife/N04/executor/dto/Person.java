package com.mnl.emanuel.concurrency.forkknife.N04.executor.dto;

import com.mnl.emanuel.concurrency.utils.ThreadUtils;

public class Person {
	
	private String name;
	
	// Person can keep a fork and/or knife
	private Table table;
	
	
	/**
	 * Construtor. 
	 * 
	 * @param name the person's name.
	 */
	public Person(String name) {
		this.name = name;
	}
	
	public void eat() {
		Fork forkFromTable = table.takeFork();
		Knife knifeFromTable = table.takeKnife();
		
		System.out.println(this.toString() + " is currently eating");
		ThreadUtils.safeSleep(1000); // Simulate eating
		System.out.println(this.toString() + " has been finished with eating");
		
		table.retourFork(forkFromTable);
		table.retourKnife(knifeFromTable);
	}
	
	
	@Override
	public String toString() {
		return "Person [name=" + name + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * Get name for person.
	 * 
	 * @return the person's name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Take seat at a given table.
	 * 
	 * @param table the table to get lunch
	 */
	public void takeASeat(Table table) {
		System.out.println(this.toString() + " is taking seat.");
		this.table = table;
	}

	/**
	 * Leave table.
	 * 
	 * @param table the table to get lunch
	 */
	public void leave(Table table) {
		System.out.println(this.toString() + " is leaving table.");
		table.removePerson(this);
		this.table = null;
	}
	
}
