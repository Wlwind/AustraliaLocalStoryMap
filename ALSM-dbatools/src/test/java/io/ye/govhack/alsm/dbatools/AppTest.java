package io.ye.govhack.alsm.dbatools;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.List;
import java.util.LinkedList;

import org.mockito.Mockito;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		List mockList = mock(List.class);
		
		mockList.add("one");
		mockList.clear();
		
		verify(mockList).add("one");
		verify(mockList).clear();
		
		LinkedList<String> mockString = mock(LinkedList.class);
		
		Mockito.when(mockString.get(0)).thenReturn("first");
		Mockito.when(mockString.get(1)).thenThrow(new RuntimeException());
		
		System.out.println(mockString.get(0));
		
		
		System.out.println(mockString.get(99));
		
		Mockito.verify(mockString).get(0);
	}
}
