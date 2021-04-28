package com.mnl.emanuel.concurrency.forkknife.N04.executor.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Table {
	private Fork  fork;
	private Knife knife;
	private List<Person> persons = new ArrayList<>();
	
	// Using ReentrantLock to control locking AND unlocking
	ReentrantLock forkLock = new ReentrantLock();
	ReentrantLock knifeLock = new ReentrantLock();
	
	
	public Table (Fork fork, Knife knife) {
		this.fork = fork;
		this.knife = knife;
	}

	public Fork takeFork() {
		forkLock.lock();
		
		Fork fork = this.fork;
		this.fork = null;
		return fork;
	}
	
	public Knife takeKnife() {
		knifeLock.lock();	

		Knife knife = this.knife;
		this.knife = null;
		return knife;
	}
	
	public void retourFork(Fork fork) {
		if (fork == null) throw new IllegalArgumentException("Fork must not be null.");
		
		this.fork = fork;
		forkLock.unlock();
	}
	
	public void retourKnife(Knife knife) {
		if (knife == null) throw new IllegalArgumentException("Knife must not be null.");
		
		this.knife = knife;
		knifeLock.unlock();
	}
	
	public void addPerson(Person person) {
		if (person == null) throw new IllegalArgumentException("Person must not be null.");
		
		synchronized (persons) {
			persons.add(person);
		}
	}

	public void removePerson(Person person) {
		if (person == null) throw new IllegalArgumentException("Person must not be null.");

		synchronized (persons) {
			persons.remove(person);
		}
	}
	
}
