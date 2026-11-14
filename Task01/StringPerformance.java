import java.lang.management.ManagementFactory;

public class StringPerformance {

    static volatile long result;

    // Used to measure memory allocated by the current thread
    static com.sun.management.ThreadMXBean allocationBean;

    static {
        java.lang.management.ThreadMXBean bean =
                ManagementFactory.getThreadMXBean();

        if (bean instanceof com.sun.management.ThreadMXBean) {
            allocationBean = (com.sun.management.ThreadMXBean) bean;

            try {
                if (allocationBean.isThreadAllocatedMemorySupported()
                        && !allocationBean.isThreadAllocatedMemoryEnabled()) {
                    allocationBean.setThreadAllocatedMemoryEnabled(true);
                }
            } catch (SecurityException e) {
                allocationBean = null;
            }
        }
    }

    // Approach 1: String
    static long usingString(String[] fields, int tickets) {
        long totalLength = 0;

        for (int t = 0; t < tickets; t++) {
            String ticket = "";

            for (String field : fields) {
                ticket = ticket + field + " | ";
            }

            totalLength += ticket.length();
        }

        return totalLength;
    }

    // Approach 2: StringBuilder
    static long usingStringBuilder(String[] fields, int tickets) {
        long totalLength = 0;

        for (int t = 0; t < tickets; t++) {
            StringBuilder ticket = new StringBuilder();

            for (String field : fields) {
                ticket.append(field).append(" | ");
            }

            String completedTicket = ticket.toString();
            totalLength += completedTicket.length();
        }

        return totalLength;
    }

    // Approach 3: StringBuffer
    static long usingStringBuffer(String[] fields, int tickets) {
        long totalLength = 0;

        for (int t = 0; t < tickets; t++) {
            StringBuffer ticket = new StringBuffer();

            for (String field : fields) {
                ticket.append(field).append(" | ");
            }

            String completedTicket = ticket.toString();
            totalLength += completedTicket.length();
        }

        return totalLength;
    }

    // Run the selected approach
    static long execute(String method, String[] fields, int tickets) {
        if (method.equals("String")) {
            return usingString(fields, tickets);
        } else if (method.equals("StringBuilder")) {
            return usingStringBuilder(fields, tickets);
        } else {
            return usingStringBuffer(fields, tickets);
        }
    }

    // Measure average execution time
    static double measureTime(String method, String[] fields,
                              int tickets, int repetitions) {

        long totalTime = 0;

        for (int i = 0; i < repetitions; i++) {
            long start = System.nanoTime();

            result = execute(method, fields, tickets);

            long end = System.nanoTime();
            totalTime += end - start;
        }

        return totalTime / (double) repetitions / 1_000_000.0;
    }

    // Measure approximate allocated memory
    static long measureAllocatedMemory(String method,
                                       String[] fields, int tickets) {

        if (allocationBean == null
                || !allocationBean.isThreadAllocatedMemorySupported()
                || !allocationBean.isThreadAllocatedMemoryEnabled()) {
            return -1;
        }

        long threadId = Thread.currentThread().threadId();

        long before = allocationBean.getThreadAllocatedBytes(threadId);

        result = execute(method, fields, tickets);

        long after = allocationBean.getThreadAllocatedBytes(threadId);

        return after - before;
    }

    // Create sample railway ticket fields
    static String[] createFields(int count) {
        String[] fields = new String[count];

        for (int i = 0; i < count; i++) {
            fields[i] = "Field" + (i + 1) + "_Data";
        }

        return fields;
    }

    // Display one workload's results
    static void runExperiment(String title, int tickets, int fieldCount) {

        String[] fields = createFields(fieldCount);
        int repetitions = 10;

        // Warm-up runs
        for (int i = 0; i < 3; i++) {
            result = usingString(fields, Math.min(tickets, 100));
            result = usingStringBuilder(fields, Math.min(tickets, 100));
            result = usingStringBuffer(fields, Math.min(tickets, 100));
        }

        System.out.println("\n======================================");
        System.out.println(title);
        System.out.println("Tickets       : " + tickets);
        System.out.println("Fields/ticket : " + fieldCount);
        System.out.println("Repetitions   : " + repetitions);
        System.out.println("======================================");

        double stringTime = measureTime(
                "String", fields, tickets, repetitions);

        double builderTime = measureTime(
                "StringBuilder", fields, tickets, repetitions);

        double bufferTime = measureTime(
                "StringBuffer", fields, tickets, repetitions);

        long stringMemory = measureAllocatedMemory(
                "String", fields, tickets);

        long builderMemory = measureAllocatedMemory(
                "StringBuilder", fields, tickets);

        long bufferMemory = measureAllocatedMemory(
                "StringBuffer", fields, tickets);

        System.out.printf("String        : %.3f ms%n", stringTime);
        System.out.printf("StringBuilder : %.3f ms%n", builderTime);
        System.out.printf("StringBuffer  : %.3f ms%n", bufferTime);

        System.out.println("\nApproximate allocated memory:");
        printMemory("String", stringMemory);
        printMemory("StringBuilder", builderMemory);
        printMemory("StringBuffer", bufferMemory);

        System.out.println("\nEstimated source-level String + operators: "
                + (long) tickets * fieldCount * 2);

        System.out.println("Result checksum: " + result);
    }

    static void printMemory(String method, long bytes) {
        if (bytes >= 0) {
            System.out.printf("%-14s: %d bytes (%.2f KB)%n",
                    method, bytes, bytes / 1024.0);
        } else {
            System.out.println(method + ": Measurement not supported");
        }
    }

    public static void main(String[] args) {

        System.out.println("RAILWAY E-TICKET PERFORMANCE TEST");
        System.out.println("Java Version: "
                + System.getProperty("java.version"));

        runExperiment(
                "WORKLOAD 1 - SHORT TICKET",
                1, 6);

        runExperiment(
                "WORKLOAD 2 - NORMAL LOAD",
                10000, 50);

        runExperiment(
                "WORKLOAD 3 - HEAVY LOAD",
                5000, 100);
    }
}
