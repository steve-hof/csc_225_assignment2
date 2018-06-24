public class TaskNode {
    public int task_id;
    public int priority;
    public int steps_remaining;

    public TaskNode next;
    public TaskNode prev;

    public TaskNode(int task_id, int time_requirement) {
        this.task_id = task_id;
        priority = 0;
        steps_remaining = time_requirement;
    }

    public int getTaskId() {
        return task_id;
    }

    public int getPriority() {
        return priority;
    }

    public int getStepsRemaining() {
        return steps_remaining;
    }
    public void setPriority(int new_priority) {
        priority = new_priority;
    }

    public void decrementTimeStep() {
        --steps_remaining;
    }

}
