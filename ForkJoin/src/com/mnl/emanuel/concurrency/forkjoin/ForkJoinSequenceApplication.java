package com.mnl.emanuel.concurrency.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinSequenceApplication {
	private static final ForkJoinPool COMMON_POOL = ForkJoinPool.commonPool();
	private static final int[] arr = {2, 17, 17, 8, 17, 17, 17, 0, 17, 1};
	private static final int toSearch = 17;
	
	

	public static void main(String[] args) throws Exception {
		System.out.println("Start Application 'ForkJoinApplication'");
		
		new ForkJoinSequenceApplication().execute();
		
		System.out.println("End Application 'ForkJoinApplication'");
	}

	private void execute() throws Exception {
		RecursiveTask<Integer> action = new NumberSequenceRecursiveTask(arr, toSearch);
		COMMON_POOL.invoke(action);
		System.out.println(toSearch + ": " + action.get());
	}

}


class NumberSequenceRecursiveTask extends RecursiveTask<Integer> {
	private static final long serialVersionUID = 1L;
	private static final int THRESHOLD = 5;

	private int arr[];
	private int toSearch;
	private int startIndex;
	private int endIndex;
	
    public NumberSequenceRecursiveTask(int[] arr, int toSearch) {
    	this.arr = arr;
    	this.toSearch = toSearch;
    	this.startIndex = 0;
    	this.endIndex = arr.length - 1;
    }
    
    private NumberSequenceRecursiveTask(int[] arr, int toSearch, int startIndex, int endIndex) {
    	this.arr = arr;
    	this.toSearch = toSearch;
    	this.startIndex = startIndex;
    	this.endIndex = endIndex;
    }

	@Override
	protected Integer compute() {
		if (endIndex - startIndex > THRESHOLD) {
			return ForkJoinTask.invokeAll(createSubtasks())
				.stream()
				.mapToInt(ForkJoinTask::join)
				.max().orElseGet(() -> 0);
        } else {
           return processing();
        }
	}

	private List<NumberSequenceRecursiveTask> createSubtasks() {
		List<NumberSequenceRecursiveTask> subtasks = new ArrayList<>();
        subtasks.add(new NumberSequenceRecursiveTask(arr, toSearch, startIndex, endIndex / 2));
        subtasks.add(new NumberSequenceRecursiveTask(arr, toSearch, endIndex / 2, endIndex));
        return subtasks;
	}
	
	private int processing() {
		int longestSequence = 0;
		int dynStartIndex = startIndex;
		int dynEndIndex = endIndex;
		
		while (arr[dynStartIndex] == toSearch) {
			if (dynStartIndex > 0)
				dynStartIndex--;
			else
				break;
		}
		
		while (arr[dynEndIndex] == toSearch) {
			if (dynEndIndex < arr.length - 1)
				dynEndIndex++;
			else
				break;
		}
		
		int temp = 0;
		for (int i = dynStartIndex; i <= dynEndIndex; i++) {
			if (arr[i] == toSearch) {
				temp++;
			} else {
				temp = 0;
			}
			
			if (temp > longestSequence) {
				longestSequence = temp;
			}
		}
		
		return longestSequence;
	}
}

