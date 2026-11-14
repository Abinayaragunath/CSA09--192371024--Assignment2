
import java.util.Comparator;
import java.util.PriorityQueue;

class Patient {

    private int patientId;
    private String name;
    private int priority;

    public Patient(int patientId, String name, int priority) {
        this.patientId = patientId;
        this.name = name;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "Patient ID: " + patientId
                + ", Name: " + name
                + ", Priority: " + priority;
    }
}

public class PriorityQueueDemo {

    public static void main(String[] args) {

        // Lower priority number means more urgent
        PriorityQueue<Patient> emergencyQueue =
                new PriorityQueue<>(
                        Comparator.comparingInt(Patient::getPriority)
                );

        // Add patients
        emergencyQueue.offer(new Patient(101, "Arun", 3));
        emergencyQueue.offer(new Patient(102, "Priya", 1));
        emergencyQueue.offer(new Patient(103, "Kumar", 2));
        emergencyQueue.offer(new Patient(104, "Meena", 1));
        emergencyQueue.offer(new Patient(105, "Ravi", 4));

        System.out.println("===== HOSPITAL EMERGENCY QUEUE =====");

        System.out.println("Total patients: " + emergencyQueue.size());

        System.out.println("\nPatients served according to priority:");

        int position = 1;

        while (!emergencyQueue.isEmpty()) {

            Patient patient = emergencyQueue.poll();

            System.out.println(
                    position + ". " + patient
            );

            position++;
        }

        System.out.println("\nRemaining patients: " + emergencyQueue.size());
    }
}
