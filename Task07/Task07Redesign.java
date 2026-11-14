
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Task07Redesign {

    private static final int[] SIZES = {1000, 10000, 100000};
    private static final int WARMUP_RUNS = 2;
    private static final int MEASURED_RUNS = 7;

    // Redesigned circular array queue
    static class CircularQueue {

        private final Integer[] elements;
        private int head;
        private int size;

        CircularQueue(int capacity) {
            elements = new Integer[capacity];
            head = 0;
            size = 0;
        }

        void offer(Integer value) {
            if (size == elements.length) {
                throw new IllegalStateException("Queue is full");
            }

            int tail = (head + size) % elements.length;
            elements[tail] = value;
            size++;
        }

        Integer poll() {
            if (size == 0) {
                return null;
            }

            Integer value = elements[head];
            elements[head] = null;

            head = (head + 1) % elements.length;
            size--;

            return value;
        }

        int size() {
            return size;
        }
    }

    // Original implementation: ArrayList front removal
    static long testArrayList(Integer[] input) {

        List<Integer> list = new ArrayList<>(input.length);

        for (Integer value : input) {
            list.add(value);
        }

        long checksum = 0;
        long start = System.nanoTime();

        while (!list.isEmpty()) {
            checksum += list.remove(0);
        }

        long end = System.nanoTime();

        if (list.size() != 0) {
            throw new IllegalStateException("ArrayList not empty");
        }

        lastChecksum = checksum;
        return end - start;
    }

    // Redesigned implementation: circular queue
    static long testCircularQueue(Integer[] input) {

        CircularQueue queue = new CircularQueue(input.length);

        for (Integer value : input) {
            queue.offer(value);
        }

        long checksum = 0;
        long start = System.nanoTime();

        while (queue.size() > 0) {
            checksum += queue.poll();
        }

        long end = System.nanoTime();

        if (queue.size() != 0) {
            throw new IllegalStateException("Queue not empty");
        }

        lastChecksum = checksum;
        return end - start;
    }

    static long lastChecksum;

    public static void main(String[] args) throws IOException {

        StringBuilder csv = new StringBuilder();

        csv.append("Size,Implementation,Run,Time_ns,Checksum\n");

        System.out.println("===== TASK 07: REDESIGN EXPERIMENT =====");
        System.out.println("Java Version: " +
                System.getProperty("java.version"));
        System.out.println("Warm-up runs: " + WARMUP_RUNS);
        System.out.println("Measured runs: " + MEASURED_RUNS);

        for (int n : SIZES) {

            Integer[] input = new Integer[n];

            for (int i = 0; i < n; i++) {
                input[i] = i + 1;
            }

            System.out.println("\nDATA SIZE: " + n);

            // Warm-up both implementations
            for (int i = 0; i < WARMUP_RUNS; i++) {
                testArrayList(input);
                testCircularQueue(input);
            }

            long arrayTotal = 0;
            long queueTotal = 0;

            long expectedChecksum = (long) n * (n + 1) / 2;

            for (int run = 1; run <= MEASURED_RUNS; run++) {

                long arrayTime = testArrayList(input);
                long arrayChecksum = lastChecksum;

                long queueTime = testCircularQueue(input);
                long queueChecksum = lastChecksum;

                if (arrayChecksum != expectedChecksum ||
                        queueChecksum != expectedChecksum) {
                    throw new IllegalStateException(
                            "Incorrect result at size " + n);
                }

                arrayTotal += arrayTime;
                queueTotal += queueTime;

                csv.append(n).append(",ArrayList,")
                        .append(run).append(",")
                        .append(arrayTime).append(",")
                        .append(arrayChecksum).append("\n");

                csv.append(n).append(",CircularQueue,")
                        .append(run).append(",")
                        .append(queueTime).append(",")
                        .append(queueChecksum).append("\n");
            }

            double arrayAverage =
                    arrayTotal / (double) MEASURED_RUNS / 1_000_000.0;

            double queueAverage =
                    queueTotal / (double) MEASURED_RUNS / 1_000_000.0;

            double speedup = arrayAverage / queueAverage;

            System.out.printf("ArrayList average: %.3f ms%n",
                    arrayAverage);

            System.out.printf("CircularQueue average: %.3f ms%n",
                    queueAverage);

            System.out.printf("Speedup: %.2f times%n", speedup);

            System.out.println("Correct checksum: " + expectedChecksum);
        }

        Path outputFolder = Path.of("Output");
        Files.createDirectories(outputFolder);

        Path csvFile = outputFolder.resolve("raw-data.csv");
        Files.writeString(csvFile, csv.toString());

        System.out.println("\nRaw measurements saved to: " + csvFile);
        System.out.println("All correctness checks passed.");
    }
}
