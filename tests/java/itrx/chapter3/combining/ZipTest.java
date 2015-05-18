package itrx.chapter3.combining;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

public class ZipTest {

	public static void main(String[] args) throws IOException {
		new ZipTest().exampleZipWithIterable();
		System.in.read();
	}
	
	public void example() {
		Observable.zip(
		        Observable.interval(100, TimeUnit.MILLISECONDS)
		            .doOnNext(i -> System.out.println("Left emits " + i)),
		        Observable.interval(150, TimeUnit.MILLISECONDS)
		            .doOnNext(i -> System.out.println("Right emits " + i)),
		        (i1,i2) -> i1 + " - " + i2
		    )
		    .take(6)
		    .subscribe(System.out::println);
		
		// Left emits
		// Right emits
		// 0 - 0
		// Left emits
		// Right emits
		// Left emits
		// 1 - 1
		// Left emits
		// Right emits
		// 2 - 2
		// Left emits
		// Left emits
		// Right emits
		// 3 - 3
		// Left emits
		// Right emits
		// 4 - 4
		// Left emits
		// Right emits
		// Left emits
		// 5 - 5
	}
	
	public void exampleZipWith() {
		Observable.interval(100, TimeUnit.MILLISECONDS)
			.zipWith(
				Observable.interval(150, TimeUnit.MILLISECONDS), 
				(i1,i2) -> i1 + " - " + i2)
			.take(6)
			.subscribe(System.out::println);
		
		// 0 - 0
		// 1 - 1
		// 2 - 2
		// 3 - 3
		// 4 - 4
		// 5 - 5
	}
	
	public void exampleZipWithIterable() {
		Observable.range(0, 5)
			.zipWith(
				Arrays.asList(0,2,4,6,8),
				(i1,i2) -> i1 + " - " + i2)
			.subscribe(System.out::println);
		
		// 0 - 0
		// 1 - 2
		// 2 - 4
		// 3 - 6
		// 4 - 8
	}
	
	
	//
	// Test
	//
	
	@Test
	public void test() {
		TestScheduler scheduler = Schedulers.test();
		TestSubscriber<String> tester = new TestSubscriber<>();
		
		Observable.zip(
		        Observable.interval(100, TimeUnit.MILLISECONDS, scheduler),
		        Observable.interval(150, TimeUnit.MILLISECONDS, scheduler),
		        (i1,i2) -> i1 + " - " + i2
		    )
		    .subscribe(tester);
		
		scheduler.advanceTimeBy(600, TimeUnit.MILLISECONDS);
		tester.assertReceivedOnNext(Arrays.asList(
			"0 - 0",
			"1 - 1",
			"2 - 2",
			"3 - 3"
		));
		tester.assertNoErrors();
	}
	
	@Test
	public void testZipWith() {
		TestSubscriber<String> tester = new TestSubscriber<>();
		TestScheduler scheduler = Schedulers.test();
		
		Observable.interval(100, TimeUnit.MILLISECONDS, scheduler)
			.zipWith(
				Observable.interval(150, TimeUnit.MILLISECONDS, scheduler), 
				(i1,i2) -> i1 + " - " + i2)
			.subscribe(tester);
		
		scheduler.advanceTimeBy(600, TimeUnit.MILLISECONDS);
		tester.assertReceivedOnNext(Arrays.asList(
			"0 - 0",
			"1 - 1",
			"2 - 2",
			"3 - 3"
		));
		tester.assertNoErrors();
	}
	
	@Test
	public void testZipWithIterable() {
		TestSubscriber<String> tester = new TestSubscriber<>();
		
		Observable.range(0, 5)
			.zipWith(
				Arrays.asList(0,2,4,6,8),
				(i1,i2) -> i1 + " - " + i2)
			.subscribe(tester);
		
		tester.assertReceivedOnNext(Arrays.asList(
			"0 - 0",
			"1 - 2",
			"2 - 4",
			"3 - 6",
			"4 - 8"
		));
		tester.assertTerminalEvent();
		tester.assertNoErrors();
	}

}
