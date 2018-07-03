/*
    TaskNode.java

    A class to contain all aspects of a 'task' as
    detailed in 2018 Summer CSC 225 course by Bill Bird.

    Class written by Steve Hof

    All methods run in constant time
 */


public class TaskNode {
    private int task_id;
    private int priority;
    private int steps_remaining;


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
        steps_remaining = steps_remaining - 1;
    }

    public boolean isLessThan(TaskNode other) {
        if (this.getPriority() < other.getPriority()) return true;

        if (this.getPriority() == other.getPriority()) {
            return this.getTaskId() < other.getTaskId();

        }

        return false;
    }
}
