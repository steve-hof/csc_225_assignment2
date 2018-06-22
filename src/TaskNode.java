public class TaskNode {
    public static int task_id;
    public static int priority;
    public static int steps_remaining;

    public TaskNode(int task_id, int time_requirement) {
        this.task_id = task_id;
        priority = 0;
        steps_remaining = time_requirement;
    }

    public static void setPriority(int new_priority) {
        priority = new_priority;
    }

    public static void decrementTimeStep() {
        --steps_remaining;
    }
}
