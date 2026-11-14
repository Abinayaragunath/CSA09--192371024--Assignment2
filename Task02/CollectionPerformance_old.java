
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;

public class CollectionPerformance {

    static volatile long result;

    // Test 1: Add elements at the end
    static long testArrayListAdd(int size) {
        ArrayList<Integer> list = new ArrayList<>();

        long start = System.nanoTime();

        for (int i = 0; i < size; i++) {
            list.add(i);
        }

        long end = System.nanoTime();
        result = list.size();

        return end - start;
    }

    static long testLinkedListAdd(int size) {
        LinkedList<Integer> list = new LinkedList<>();

        long start = System.nanoTime();

        for (int i = 0; i < size; i++) {
            list.add(i);
        }

        long end = System.nanoTime();
        result = list.size();

        return end - start;
    }

    static long testArrayDequeAdd(int size) {
        ArrayDeque<Integer> deque = new ArrayDeque<>();

        long start = System.nanoTime();

        for (int i = 0; i < size; i++) {
            deque.addLast(i);
        }

        long end = System.nanoTime();
        result = deque.size();

        return end - start;
    }

    // Test 2: Traverse all elements
    static long testArrayListTraversal(int size) {
        ArrayList<Integer> list = createArrayList(size);

        long sum = 0;
        long start = System.nanoTime();

        for (int value : list) {
            sum += value;
        }

        long end = System.nanoTime();
        result = sum;

        return end - start;
    }

    static long testLinkedListTraversal(int size) {
        LinkedList<Integer> list = createLinkedList(size);

        long sum = 0;
        long start = System.nanoTime();

        for (int value : list) {
            sum += value;
        }

        long end = System.nanoTime();
        result = sum;

        return end - start;
    }

    static long testArrayDequeTraversal(int size) {
        ArrayDeque<Integer> deque = createArrayDeque(size);

        long sum = 0;
        long start = System.nanoTime();

        for (int value : deque) {
            sum += value;
        }

        long end = System.nanoTime();
        result = sum;

        return end - start;
    }

    // Test 3: Remove elements from the beginning
    static long testArrayListRemove(int size) {
        ArrayList<Integer> list = createArrayList(size);

        long start = System.nanoTime();

        while (!list.isEmpty()) {
            list.remove(0);
        }

        long end = System.nanoTime();
        result = list.size();

        return end - start;
    }

    static long testLinkedListRemove(int size) {
        LinkedList<Integer> list = createLinkedList(size);

        long start = System.nanoTime();

        while (!list.isEmpty()) {
            list.removeFirst();
        }

        long end = System.nanoTime();
        result = list.size();

        return end - start;
    }

    static long testArrayDequeRemove(int size) {
        ArrayDeque<Integer> deque = createArrayDeque(size);

        long start = System.nanoTime();

        while (!deque.isEmpty()) {
            deque.removeFirst();
        }

        long end = System.nanoTime();
        result = deque.size();

        return end - start;
    }

    // Collection creation methods
    static ArrayList<Integer> createArrayList(int size) {
        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            list.add(i);
        }

        return list;
    }

    static LinkedList<Integer> createLinkedList(int size) {
        LinkedList<Integer> list = new LinkedList<>();

        for (int i = 0; i < size; i++) {
            list.add(i);
        }

        return list;
    }

    static ArrayDeque<Integer> createArrayDeque(int size) {
        ArrayDeque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < size; i++) {
            deque.addLast(i);
        }

        return deque;
    }

    // Display results
    static void runExperiment(int size) {

        int repetitions = 5;

        System.out.println("\n======================================");
        System.out.println("COLLECTION SIZE: " + size);
        System.out.println("Repetitions: " + repetitions);
        System.out.println("======================================");

        long arrayAdd = 0;
        long linkedAdd = 0;
        long dequeAdd = 0;

        long arrayTraversal = 0;
        long linkedTraversal = 0;
        long dequeTraversal = 0;

        long arrayRemove = 0;
        long linkedRemove = 0;
        long dequeRemove = 0;

        // Warm-up
        testArrayListAdd(100);
        testLinkedListAdd(100);
        testArrayDequeAdd(100);

        for (int i = 0; i < repetitions; i++) {

            arrayAdd += testArrayListAdd(size);
            linkedAdd += testLinkedListAdd(size);
            dequeAdd += testArrayDequeAdd(size);

            arrayTraversal += testArrayListTraversal(size);
            linkedTraversal += testLinkedListTraversal(size);
            dequeTraversal += testArrayDequeTraversal(size);

            arrayRemove += testArrayListRemove(size);
            linkedRemove += testLinkedListRemove(size);
            dequeRemove += testArrayDequeRemove(size);
        }

        System.out.println("\nADD AT END (Average ms)");
        printResults(arrayAdd, linkedAdd, dequeAdd, repetitions);

        System.out.println("\nTRAVERSAL (Average ms)");
        printResults(arrayTraversal, linkedTraversal,
                dequeTraversal, repetitions);

        System.out.println("\nREMOVE FROM BEGINNING (Average ms)");
        printResults(arrayRemove, linkedRemove,
                dequeRemove, repetitions);
    }

    static void printResults(long a, long l, long d, int repetitions) {

        System.out.printf("ArrayList   : %.3f ms%n",
                a / (double) repetitions / 1_000_000);

        System.out.printf("LinkedList  : %.3f ms%n",
                l / (double) repetitions / 1_000_000);

        System.out.printf("ArrayDeque  : %.3f ms%n",
                d / (double) repetitions / 1_000_000);
    }

    public static void main(String[] args) {

        System.out.println("JAVA COLLECTION PERFORMANCE TEST");
        System.out.println("Java Version: "
                + System.getProperty("java.version"));

        runExperiment(1000);
        runExperiment(10000);
        runExperiment(100000);
    }
}
