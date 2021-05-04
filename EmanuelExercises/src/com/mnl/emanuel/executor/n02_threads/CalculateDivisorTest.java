package com.mnl.emanuel.executor.n02_threads;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

/**
 * @author ble
 * 
 */

public class CalculateDivisorTest {

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();


	@Test
	public void testCalculate() throws InterruptedException, ExecutionException {
		CalculateDivisor.main(new String[] { "10", "10000", "4" });
		assertEquals(
				"Ergebnis: Zahl mit maximaler Anzahl Divisoren: 7560 (64 Divisoren)",
				systemOutRule.getLog());
	}

}
