package com.mnl.emanuel.concurrency.forkknife.N01.simple.dto;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private Fork  fork;
	private Knife knife;
	private List<Person> persons = new ArrayList<>();
	
	
	public Table (Fork fork, Knife knife) {
		this.fork = fork;
		this.knife = knife;
	}

	public Fork takeFork() {
		if (fork == null) throw new IllegalStateException("Fork currently now available.");		
		
		Fork fork = this.fork;
		this.fork = null;
		return fork;
	}
	
	public Knife takeKnife() {
		if (knife == null) throw new IllegalStateException("Knife currently now available.");		

		Knife knife = this.knife;
		this.knife = null;
		return knife;
	}
	
	public void retourFork(Fork fork) {
		if (fork == null) throw new IllegalArgumentException("Fork must not be null;");
		
		this.fork = fork;
	}
	
	public void retourKnife(Knife knife) {
		if (knife == null) throw new IllegalArgumentException("Knife must not be null;");
		
		this.knife = knife;
	}
	
	public void addPerson(Person person) {
		persons.add(person);
	}

	public void removePerson(Person person) {
		persons.remove(person);
	}
	
}
