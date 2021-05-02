package com.mnl.emanuel.concurrency.forkjoin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 * The fork/join provides tools to help speed up parallel processing by attempting to use all 
 * available processor cores – which is accomplished through a divide and conquer approach.
 * 
 * In practice, this means that the framework first “forks”, recursively breaking the task into 
 * smaller independent subtasks until they are simple enough to be executed asynchronously.
 * 
 * After that, the “join” part begins, in which results of all subtasks are recursively joined 
 * into a single result, or in the case of a task which returns void, the program simply waits 
 * until every subtask is executed.
 * 
 * To provide effective parallel execution, the fork/join framework uses a pool of threads called 
 * the ForkJoinPool, which manages worker threads of type ForkJoinWorkerThread.
 * 
 * see Details: https://www.baeldung.com/java-fork-join
 *
 */
public class ForkJoinApplication {
	
	/*
	 * The ForkJoinPool will provide a reference to the common pool, 
	 * which is a default thread pool for every ForkJoinTask.
	 */
	private static final ForkJoinPool COMMON_POOL = ForkJoinPool.commonPool();
	
	private static final String EXAMPLE_WORKLOAD = "This is an example workload for demonstration purpose.";

	public static void main(String[] args) {
		System.out.println("Start Application 'ForkJoinApplication'");
		
		new ForkJoinApplication().execute();
		
		System.out.println("End Application 'ForkJoinApplication'");
	}

	private void execute() {
		RecursiveAction action = new StringUpperRecursiveAction(EXAMPLE_WORKLOAD);
		COMMON_POOL.invoke(action);
		
	}

}


/**
 * The unit of work to be processed is represented by a String called workload. 
 * For demonstration purposes, the task is a nonsensical one: it simply uppercases its input and logs it.
 * 
 * To demonstrate the forking behavior of the framework, the example splits the task if workload.length() 
 * is larger than a specified threshold using the createSubtask() method.
 * 
 * see Details: https://www.baeldung.com/java-fork-join#1-recursiveaction---an-example
 */
class StringUpperRecursiveAction extends RecursiveAction {
	private static final long serialVersionUID = 1L;

	private String workload = "";
    private static final int THRESHOLD = 4;
    
    public StringUpperRecursiveAction(String workload) {
    	this.workload = workload;
    }

	@Override
	protected void compute() {
		if (workload.length() > THRESHOLD) {
            ForkJoinTask.invokeAll(createSubtasks());
        } else {
           processing(workload);
        }
	}

	private List<StringUpperRecursiveAction> createSubtasks() {
		List<StringUpperRecursiveAction> subtasks = new ArrayList<>();

        String partOne = workload.substring(0, workload.length() / 2);
        String partTwo = workload.substring(workload.length() / 2, workload.length());

        subtasks.add(new StringUpperRecursiveAction(partOne));
        subtasks.add(new StringUpperRecursiveAction(partTwo));

        return subtasks;
	}
	
	private void processing(String work) {
		String result     = work.toUpperCase();
		String threadName = Thread.currentThread().getName();
		System.out.println("This result - (" + result + ") - was processed by " + threadName);
	}
}


/**
 * For tasks that return a value, the logic here is similar, 
 * except that the result for each subtask is united in a single result
 */
class NumberProcessRecursiveTask extends RecursiveTask<Integer> {
	private static final long serialVersionUID = 1L;

	private static final int THRESHOLD = 20;

	private int[] arr;

    public NumberProcessRecursiveTask(int[] arr) {
        this.arr = arr;
    }

    @Override
    protected Integer compute() {
        if (arr.length > THRESHOLD) {
            return ForkJoinTask.invokeAll(createSubtasks())
              .stream()
              .mapToInt(ForkJoinTask::join)
              .sum();
        } else {
            return processing(arr);
        }
    }

    private Collection<NumberProcessRecursiveTask> createSubtasks() {
        List<NumberProcessRecursiveTask> dividedTasks = new ArrayList<>();
        dividedTasks.add(new NumberProcessRecursiveTask(Arrays.copyOfRange(arr, 0, arr.length / 2)));
        dividedTasks.add(new NumberProcessRecursiveTask(Arrays.copyOfRange(arr, arr.length / 2, arr.length)));
        return dividedTasks;
    }

    private Integer processing(int[] arr) {
        return Arrays.stream(arr)
          .filter(a -> a > 10 && a < 27)
          .map(a -> a * 10)
          .sum();
    }
}
