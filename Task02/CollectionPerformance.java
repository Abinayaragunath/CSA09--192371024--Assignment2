
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;

public class CollectionPerformance {

    static volatile long result;

    static final int REPETITIONS = 5;
    static final int OPERATIONS = 100;
    static final int ACCESS_OPERATIONS = 100;

    // Create collections before starting the timer.
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

    // Add elements at the end.
    static long arrayAdd(int size) {
        ArrayList<Integer> list = new ArrayList<>();
        long start = System.nanoTime();

        for (int i = 0; i < size; i++) {
            list.add(i);
        }

        long time = System.nanoTime() - start;
        result = list.size();
        return time;
    }

    static long linkedAdd(int size) {
        LinkedList<Integer> list = new LinkedList<>();
        long start = System.nanoTime();

        for (int i = 0; i < size; i++) {
            list.add(i);
        }

        long time = System.nanoTime() - start;
        result = list.size();
        return time;
    }

    // Traverse all elements.
    static long arrayTraversal(int size) {
        ArrayList<Integer> list = createArrayList(size);
        long sum = 0;

        long start = System.nanoTime();
        for (int value : list) {
            sum += value;
        }

        long time = System.nanoTime() - start;
        result = sum;
        return time;
    }

    static long linkedTraversal(int size) {
        LinkedList<Integer> list = createLinkedList(size);
        long sum = 0;

        long start = System.nanoTime();
        for (int value : list) {
            sum += value;
        }

        long time = System.nanoTime() - start;
        result = sum;
        return time;
    }

    // Remove elements from the beginning.
    static long arrayRemoveFirst(int size) {
        ArrayList<Integer> list = createArrayList(size);

        long start = System.nanoTime();
        while (!list.isEmpty()) {
            list.remove(0);
        }

        long time = System.nanoTime() - start;
        result = list.size();
        return time;
    }

    static long linkedRemoveFirst(int size) {
        LinkedList<Integer> list = createLinkedList(size);

        long start = System.nanoTime();
        while (!list.isEmpty()) {
            list.removeFirst();
        }

        long time = System.nanoTime() - start;
        result = list.size();
        return time;
    }

    // Insert repeatedly at the beginning.
    static long arrayInsertFirst(int size) {
        ArrayList<Integer> list = createArrayList(size);

        long start = System.nanoTime();
        for (int i = 0; i < OPERATIONS; i++) {
            list.add(0, -i);
        }

        long time = System.nanoTime() - start;
        result = list.size();
        return time;
    }

    static long linkedInsertFirst(int size) {
        LinkedList<Integer> list = createLinkedList(size);

        long start = System.nanoTime();
        for (int i = 0; i < OPERATIONS; i++) {
            list.add(0, -i);
        }

        long time = System.nanoTime() - start;
        result = list.size();
        return time;
    }

    // Insert repeatedly at the middle.
    static long arrayInsertMiddle(int size) {
        ArrayList<Integer> list = createArrayList(size);

        long start = System.nanoTime();
        for (int i = 0; i < OPERATIONS; i++) {
            list.add(list.size() / 2, -i);
        }

        long time = System.nanoTime() - start;
        result = list.size();
        return time;
    }

    static long linkedInsertMiddle(int size) {
        LinkedList<Integer> list = createLinkedList(size);

        long start = System.nanoTime();
        for (int i = 0; i < OPERATIONS; i++) {
            list.add(list.size() / 2, -i);
        }

        long time = System.nanoTime() - start;
        result = list.size();
        return time;
    }

    // Remove repeatedly from the middle.
    static long arrayRemoveMiddle(int size) {
        ArrayList<Integer> list = createArrayList(size);

        long start = System.nanoTime();
        for (int i = 0; i < OPERATIONS; i++) {
            list.remove(list.size() / 2);
        }

        long time = System.nanoTime() - start;
        result = list.size();
        return time;
    }

    static long linkedRemoveMiddle(int size) {
        LinkedList<Integer> list = createLinkedList(size);

        long start = System.nanoTime();
        for (int i = 0; i < OPERATIONS; i++) {
            list.remove(list.size() / 2);
        }

        long time = System.nanoTime() - start;
        result = list.size();
        return time;
    }

    // Access the middle element repeatedly.
    static long arrayKthAccess(int size) {
        ArrayList<Integer> list = createArrayList(size);
        long sum = 0;
        int k = size / 2;

        long start = System.nanoTime();
        for (int i = 0; i < ACCESS_OPERATIONS; i++) {
            sum += list.get(k);
        }

        long time = System.nanoTime() - start;
        result = sum;
        return time;
    }

    static long linkedKthAccess(int size) {
        LinkedList<Integer> list = createLinkedList(size);
        long sum = 0;
        int k = size / 2;

        long start = System.nanoTime();
        for (int i = 0; i < ACCESS_OPERATIONS; i++) {
            sum += list.get(k);
        }

        long time = System.nanoTime() - start;
        result = sum;
        return time;
    }

    static double average(long total) {
        return total / (double) REPETITIONS / 1_000_000.0;
    }

    static void printComparison(String title, long arrayTime,
                                long linkedTime) {
        System.out.println("\n" + title);
        System.out.printf("ArrayList   : %.3f ms%n", average(arrayTime));
        System.out.printf("LinkedList  : %.3f ms%n", average(linkedTime));
    }

    static void runExperiment(int size) {

        long arrayAdd = 0, linkedAdd = 0;
        long arrayTraversal = 0, linkedTraversal = 0;
        long arrayRemoveFirst = 0, linkedRemoveFirst = 0;
        long arrayInsertFirst = 0, linkedInsertFirst = 0;
        long arrayInsertMiddle = 0, linkedInsertMiddle = 0;
        long arrayRemoveMiddle = 0, linkedRemoveMiddle = 0;
        long arrayKth = 0, linkedKth = 0;

        // Warm-up.
        for (int i = 0; i < 2; i++) {
            arrayAdd(100);
            linkedAdd(100);
            arrayTraversal(100);
            linkedTraversal(100);
            arrayRemoveFirst(100);
            linkedRemoveFirst(100);
            arrayInsertFirst(100);
            linkedInsertFirst(100);
            arrayInsertMiddle(100);
            linkedInsertMiddle(100);
            arrayRemoveMiddle(100);
            linkedRemoveMiddle(100);
            arrayKthAccess(100);
            linkedKthAccess(100);
        }

        for (int i = 0; i < REPETITIONS; i++) {
            arrayAdd += arrayAdd(size);
            linkedAdd += linkedAdd(size);

            arrayTraversal += arrayTraversal(size);
            linkedTraversal += linkedTraversal(size);

            arrayRemoveFirst += arrayRemoveFirst(size);
            linkedRemoveFirst += linkedRemoveFirst(size);

            arrayInsertFirst += arrayInsertFirst(size);
            linkedInsertFirst += linkedInsertFirst(size);

            arrayInsertMiddle += arrayInsertMiddle(size);
            linkedInsertMiddle += linkedInsertMiddle(size);

            arrayRemoveMiddle += arrayRemoveMiddle(size);
            linkedRemoveMiddle += linkedRemoveMiddle(size);

            arrayKth += arrayKthAccess(size);
            linkedKth += linkedKthAccess(size);
        }

        System.out.println("\n======================================");
        System.out.println("COLLECTION SIZE: " + size);
        System.out.println("Repetitions: " + REPETITIONS);
        System.out.println("Insert/remove operations per run: " + OPERATIONS);
        System.out.println("Kth access operations per run: " + ACCESS_OPERATIONS);
        System.out.println("======================================");

        printComparison("ADD AT END", arrayAdd, linkedAdd);
        printComparison("TRAVERSAL", arrayTraversal, linkedTraversal);
        printComparison("REMOVE FROM BEGINNING",
                arrayRemoveFirst, linkedRemoveFirst);
        printComparison("INSERT AT BEGINNING",
                arrayInsertFirst, linkedInsertFirst);
        printComparison("INSERT AT MIDDLE",
                arrayInsertMiddle, linkedInsertMiddle);
        printComparison("REMOVE FROM MIDDLE",
                arrayRemoveMiddle, linkedRemoveMiddle);
        printComparison("KTH ELEMENT ACCESS",
                arrayKth, linkedKth);
    }

    // Practical ArrayDeque Up Next queue demonstration.
    static void demonstrateUpNextQueue() {
        System.out.println("\n===== ARRAYDEQUE UP NEXT QUEUE =====");

        ArrayDeque<String> upNext = new ArrayDeque<>();

        upNext.offerLast("Java");
        upNext.offerLast("DBMS");
        upNext.offerLast("DSA");

        System.out.println("Initial queue: " + upNext);

        System.out.println("Now playing: " + upNext.peekFirst());

        System.out.println("Completed: " + upNext.pollFirst());

        upNext.offerLast("System Design");

        System.out.println("Updated Up Next queue: " + upNext);

        System.out.println("Next item: " + upNext.peekFirst());
    }

    public static void main(String[] args) {

        System.out.println("JAVA COLLECTION PERFORMANCE TEST");
        System.out.println("Java Version: "
                + System.getProperty("java.version"));

        runExperiment(1000);
        runExperiment(10000);
        runExperiment(100000);

        demonstrateUpNextQueue();

        System.out.println("\nAll experiments completed.");
    }
}
