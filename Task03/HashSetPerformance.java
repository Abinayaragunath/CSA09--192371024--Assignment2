
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class HashSetPerformance {

    // Demonstrates an incorrect implementation
    static class BrokenStudent {

        int id;
        String name;

        BrokenStudent(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof BrokenStudent)) {
                return false;
            }

            BrokenStudent other = (BrokenStudent) obj;
            return this.id == other.id;
        }

        // hashCode() is intentionally not overridden
    }

    // Correct implementation
    static class Student {

        int id;
        String name;

        Student(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Student)) {
                return false;
            }

            Student other = (Student) obj;
            return this.id == other.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return "Student{id=" + id + ", name='" + name + "'}";
        }
    }

    // Demonstrate the broken contract
    static void testBrokenImplementation() {

        System.out.println("\nTEST 1 - INCORRECT IMPLEMENTATION");

        BrokenStudent s1 = new BrokenStudent(101, "Abi");
        BrokenStudent s2 = new BrokenStudent(101, "Abi");

        Set<BrokenStudent> set = new HashSet<>();

        set.add(s1);
        set.add(s2);

        System.out.println("equals() result: " + s1.equals(s2));
        System.out.println("HashCode 1: " + s1.hashCode());
        System.out.println("HashCode 2: " + s2.hashCode());
        System.out.println("HashSet size: " + set.size());

        System.out.println(
                "Note: Equal objects may have different hash codes."
        );
    }

    // Test 2: Duplicate detection
    static void testDuplicateDetection() {

        System.out.println("\nTEST 2 - DUPLICATE DETECTION");

        Set<Student> students = new HashSet<>();

        Student s1 = new Student(101, "Abi");
        Student s2 = new Student(101, "Abinaya");
        Student s3 = new Student(102, "Arun");

        System.out.println("Add Student 101: " + students.add(s1));
        System.out.println("Add duplicate ID 101: " + students.add(s2));
        System.out.println("Add Student 102: " + students.add(s3));

        System.out.println("Final HashSet size: " + students.size());
        System.out.println("Students: " + students);
    }

    // Test 3: Search and removal
    static void testSearchAndRemoval() {

        System.out.println("\nTEST 3 - SEARCH AND REMOVAL");

        Set<Student> students = new HashSet<>();

        students.add(new Student(101, "Abi"));
        students.add(new Student(102, "Arun"));
        students.add(new Student(103, "Priya"));

        Student searchStudent = new Student(102, "Unknown");

        System.out.println(
                "Student 102 exists: " + students.contains(searchStudent)
        );

        System.out.println(
                "Remove Student 102: " + students.remove(searchStudent)
        );

        System.out.println("Remaining students: " + students.size());
        System.out.println("Final records: " + students);
    }

    public static void main(String[] args) {

        System.out.println("HASHSET EQUALS AND HASHCODE TEST");

        System.out.println(
                "Java Version: " + System.getProperty("java.version")
        );

        testBrokenImplementation();
        testDuplicateDetection();
        testSearchAndRemoval();
    }
}
