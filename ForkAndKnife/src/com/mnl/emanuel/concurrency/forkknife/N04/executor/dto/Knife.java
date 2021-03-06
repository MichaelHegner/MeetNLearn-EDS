package com.mnl.emanuel.concurrency.forkknife.N04.executor.dto;

public class Knife {
	private volatile boolean inUse;

	public synchronized void use() {
		if (inUse) throw new IllegalStateException("Knife is already in use.");
		this.inUse = true;
	}
	
	public synchronized void release() {
		this.inUse = false;
	}
}
