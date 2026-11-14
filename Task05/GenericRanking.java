
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

// Generic ranking class
class Ranking<T> {

    public List<T> rank(List<T> items, Comparator<? super T> comparator) {

        List<T> rankedItems = new ArrayList<>(items);

        rankedItems.sort(comparator);

        return rankedItems;
    }
}

// Student class
class Student {

    private int id;
    private String name;
    private double marks;

    public Student(int id, String name, double marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
    }

    public double getMarks() {
        return marks;
    }

    public String toString() {
        return "ID: " + id +
               ", Name: " + name +
               ", Marks: " + marks;
    }
}

// Employee class
class Employee {

    private int id;
    private String name;
    private double salary;

    public Employee(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public String toString() {
        return "ID: " + id +
               ", Name: " + name +
               ", Salary: " + salary;
    }
}

// Main class
public class GenericRanking {

    public static void main(String[] args) {

        Ranking<Student> studentRanking = new Ranking<>();

        List<Student> students = Arrays.asList(
            new Student(101, "Abi", 85.5),
            new Student(102, "Arun", 92.0),
            new Student(103, "Priya", 78.5),
            new Student(104, "Kumar", 88.0)
        );

        // Rank students by marks in descending order
        Comparator<Student> studentComparator =
            Comparator.comparingDouble(Student::getMarks).reversed();

        List<Student> rankedStudents =
            studentRanking.rank(students, studentComparator);

        System.out.println("===== STUDENT RANKING =====");

        for (int i = 0; i < rankedStudents.size(); i++) {
            System.out.println(
                "Rank " + (i + 1) + ": " + rankedStudents.get(i)
            );
        }

        Ranking<Employee> employeeRanking = new Ranking<>();

        List<Employee> employees = Arrays.asList(
            new Employee(201, "Ravi", 45000),
            new Employee(202, "Meena", 65000),
            new Employee(203, "Karthik", 55000)
        );

        // Rank employees by salary in descending order
        Comparator<Employee> employeeComparator =
            Comparator.comparingDouble(Employee::getSalary).reversed();

        List<Employee> rankedEmployees =
            employeeRanking.rank(employees, employeeComparator);

        System.out.println("\n===== EMPLOYEE RANKING =====");

        for (int i = 0; i < rankedEmployees.size(); i++) {
            System.out.println(
                "Rank " + (i + 1) + ": " + rankedEmployees.get(i)
            );
        }
    }
}
