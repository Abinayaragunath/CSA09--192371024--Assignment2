
import java.util.*;

public class PharmacyInventory {

    static final int REPETITIONS = 5;
    static final int WARMUP_RUNS = 2;

    static volatile long resultChecksum = 0;

    // Generate medicine code
    static String medicineCode(int id) {
        return String.format("MED%06d", id);
    }

    // Create medicines in descending insertion order
    static void insertMedicines(
            Map<String, Integer> map, int size) {

        for (int i = size; i >= 1; i--) {
            map.put(medicineCode(i), 100 + i);
        }
    }

    // Prepare lookup keys before timing
    static String[] createLookupKeys(int size, int count) {
        String[] keys = new String[count];

        Random random = new Random(42);

        for (int i = 0; i < count; i++) {
            int id = random.nextInt(size) + 1;
            keys[i] = medicineCode(id);
        }

        return keys;
    }

    // Perform stock lookups
    static long performLookups(
            Map<String, Integer> map, String[] keys) {

        long checksum = 0;

        for (String key : keys) {
            checksum += map.get(key);
        }

        return checksum;
    }

    // Calculate average lookup time
    static double measureLookup(
            Map<String, Integer> map, String[] keys) {

        for (int i = 0; i < WARMUP_RUNS; i++) {
            resultChecksum = performLookups(map, keys);
        }

        long totalTime = 0;

        for (int i = 0; i < REPETITIONS; i++) {
            long start = System.nanoTime();

            resultChecksum = performLookups(map, keys);

            totalTime += System.nanoTime() - start;
        }

        return totalTime / (double) REPETITIONS / 1_000_000.0;
    }

    // Generate a report from a map
    static String generateReport(Map<String, Integer> map) {
        StringBuilder report = new StringBuilder();

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            report.append(entry.getKey())
                  .append(" = ")
                  .append(entry.getValue())
                  .append('\n');
        }

        return report.toString();
    }

    // Measure report-generation time
    static double measureReport(Map<String, Integer> map) {

        long totalTime = 0;

        for (int i = 0; i < REPETITIONS; i++) {
            long start = System.nanoTime();

            String report = generateReport(map);

            totalTime += System.nanoTime() - start;

            resultChecksum += report.length();
        }

        return totalTime / (double) REPETITIONS / 1_000_000.0;
    }

    static void runExperiment(int size) {

        Map<String, Integer> hashMap = new HashMap<>();
        Map<String, Integer> treeMap = new TreeMap<>();
        Map<String, Integer> linkedHashMap = new LinkedHashMap<>();

        insertMedicines(hashMap, size);
        insertMedicines(treeMap, size);
        insertMedicines(linkedHashMap, size);

        int lookupCount = 100000;
        String[] keys = createLookupKeys(size, lookupCount);

        System.out.println("\n======================================");
        System.out.println("INVENTORY SIZE: " + size);
        System.out.println("======================================");

        System.out.println("\nMAP SIZES");
        System.out.println("HashMap       : " + hashMap.size());
        System.out.println("TreeMap       : " + treeMap.size());
        System.out.println("LinkedHashMap : " + linkedHashMap.size());

        System.out.println("\n100,000 STOCK LOOKUPS");
        System.out.printf("HashMap       : %.3f ms%n",
                measureLookup(hashMap, keys));

        System.out.printf("TreeMap       : %.3f ms%n",
                measureLookup(treeMap, keys));

        System.out.printf("LinkedHashMap : %.3f ms%n",
                measureLookup(linkedHashMap, keys));

        System.out.println("\nCODE-ORDER REPORT");
        System.out.printf("TreeMap report: %.3f ms%n",
                measureReport(treeMap));

        System.out.println("First code: " +
                treeMap.keySet().iterator().next());

        System.out.println("Last code: " +
                ((TreeMap<String, Integer>) treeMap)
                        .lastKey());

        System.out.println("\nINSERTION-ORDER REPORT");
        System.out.printf("LinkedHashMap report: %.3f ms%n",
                measureReport(linkedHashMap));

        System.out.println("First inserted code: " +
                linkedHashMap.keySet().iterator().next());

        System.out.println("Last inserted code: " +
                ((LinkedHashMap<String, Integer>) linkedHashMap)
                        .keySet().stream()
                        .reduce((first, second) -> second)
                        .orElse("None"));

        System.out.println("\nORDER VERIFICATION");
        System.out.println("TreeMap sorted order: " +
                isSorted(treeMap));

        System.out.println("LinkedHashMap preserves descending insertion: " +
                isDescending(linkedHashMap));

        System.out.println("Lookup checksum: " + resultChecksum);
    }

    static boolean isSorted(Map<String, Integer> map) {
        String previous = null;

        for (String key : map.keySet()) {
            if (previous != null && previous.compareTo(key) > 0) {
                return false;
            }
            previous = key;
        }

        return true;
    }

    static boolean isDescending(Map<String, Integer> map) {
        String previous = null;

        for (String key : map.keySet()) {
            if (previous != null && previous.compareTo(key) < 0) {
                return false;
            }
            previous = key;
        }

        return true;
    }

    public static void main(String[] args) {

        System.out.println("PHARMACY INVENTORY MAP EXPERIMENT");
        System.out.println("Java Version: " +
                System.getProperty("java.version"));
        System.out.println("Measured repetitions: " + REPETITIONS);
        System.out.println("Warm-up runs: " + WARMUP_RUNS);

        runExperiment(1000);
        runExperiment(10000);
        runExperiment(100000);

        System.out.println("\n======================================");
        System.out.println("ALL EXPERIMENTS COMPLETED");
        System.out.println("======================================");
    }
}
