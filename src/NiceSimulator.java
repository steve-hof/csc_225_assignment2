/* NiceSimulator.java
   CSC 225 - Summer 2018

   An empty shell of the operations needed by the NiceSimulator
   data structure.

   B. Bird - 06/18/2018

   Added to by Steve Hof for Assignment 2 of CSC 225
*/


import javafx.concurrent.Task;

import java.io.*;
import java.util.*;


public class NiceSimulator {
    public static final int SIMULATE_IDLE = -2;
    public static final int SIMULATE_NONE_FINISHED = -1;
    public int tasks_left;
    public int max_tasks;
    private int size;
    public TaskNode head;
    public TaskNode tail;

    /* Constructor(maxTasks)
       Instantiate the data structure with the provided maximum
       number of tasks. No more than maxTasks different tasks will
       be simultaneously added to the simulator, and additionally
       you may assume that all task IDs will be in the range
         0, 1, ..., maxTasks - 1
    */
    public NiceSimulator(int maxTasks){
        this.max_tasks = maxTasks;
        tasks_left = maxTasks;
        size = 0;
    }
    

    /* taskValid(taskID)
       Given a task ID, return true if the ID is currently
       in use by a valid task (i.e. a task with at least 1
       unit of time remaining) and false otherwise.

       Note that you should include logic to check whether
       the ID is outside the valid range 0, 1, ..., maxTasks - 1
       of task indices.

    */
    public boolean taskValid(int taskID){
        if (size == 0) return false;

        TaskNode curr = head;

        if (taskID < curr.getTaskId()) return false;

        if (taskID == curr.getTaskId()) return true;

        while (curr.next != null) {
            if (curr.getTaskId() == taskID) return true;
            curr = curr.next;
        }

        return false;
    }

    /* getPriority(taskID)
       Return the current priority value for the provided
       task ID. You may assume that the task ID provided
       is valid.

    */
    public int getPriority(int taskID){
        TaskNode curr = head;
        if (curr.getTaskId() == taskID) return curr.getPriority();

        while (curr.next != null) {
            if (curr.getTaskId() == taskID) return curr.getPriority();
            curr = curr.next;
        }

        return -1;

    }

    /* getRemaining(taskID)
       Given a task ID, return the number of timesteps
       remaining before the task completes. You may assume
       that the task ID provided is valid.

    */
    public int getRemaining(int taskID){
        TaskNode curr = head;
        while (curr.next != null) {
            if (curr.getTaskId() == taskID) return curr.getStepsRemaining();
            curr = curr.next;
        }

        return -1;
    }

    /*
        getCorrectPreviousNode(task_id)

        Helper method for finding the correct place to insert a new
        TaskNode
     */

    private TaskNode getCorrectPreviousNode(int task_id) {
        if (task_id > tail.getTaskId()) return tail;

        TaskNode curr = head;
        if (task_id < curr.getTaskId()) return null;

        while (curr.next != null) {
            if (task_id < curr.next.getTaskId()) return curr;
            curr = curr.next;
        }
        
        // shouldn't get here
        return null;
    }
    
    /* add(taskID, time_required)
       Add a task with the provided task ID and time requirement
       to the system. You may assume that the provided task ID is in
       the correct range and is not a currently-active task.
       The new task will be assigned nice level 0.
    */
    public void add(int taskID, int time_required){
        TaskNode newNode = new TaskNode(taskID, time_required);
        TaskNode curr = head;

        if (size == 0) {
            head = newNode;
            tail = newNode;
            size++;
            return;
        }

        if (size == 1) {
            if (taskID < curr.getTaskId()) {
                curr.prev = newNode;
                head = newNode;

            } else {
                curr.next = newNode;
                tail = newNode;

            }

            size++;
            return;
        }

        TaskNode nodeBefore = getCorrectPreviousNode(taskID);

        if (nodeBefore == null) {
            head.prev = newNode;
            newNode.next = head;
            head = newNode;


        } else if (nodeBefore.next == null) {
            newNode.prev = nodeBefore;
            nodeBefore.next = newNode;
            tail = newNode;

        } else {
            TaskNode nextNode = nodeBefore.next;
            nodeBefore.next = newNode;
            newNode.prev = nodeBefore;
            nextNode.prev = newNode;
            newNode.next = nextNode;
        }

        size++;
    }

    /* kill(taskID)
       Delete the task with the provided task ID from the system.
       You may assume that the provided task ID is in the correct
       range and is a currently-active task.
    */
    public void kill(int taskID){
        TaskNode curr = head;
        
        if (size == 1) {
            head = null;
            tail = null;
            
        } else if (taskID == head.getTaskId()) {
            head = head.next;
            curr.next = null;
            head.prev = null;
            
        } else if (taskID == tail.getTaskId()) {
            curr = tail.prev;
            tail.prev = null;
            curr.next = null;
            tail = curr;
            
        } else {
            TaskNode prev = getCorrectPreviousNode(taskID);
            curr = prev.next;
            TaskNode next = curr.next;
            curr.next = null;
            curr.prev = null;
            prev.next = next;
            next.prev = prev;
        }

        size = size - 1;
    }


    /* renice(taskID, new_priority)
       Change the priority of the the provided task ID to the new priority
       value provided. The change must take effect at the next simulate() step.
       You may assume that the provided task ID is in the correct
       range and is a currently-active task.

    */
    public void renice(int taskID, int new_priority){
        TaskNode curr = head;
        if (curr.getTaskId() == taskID) curr.setPriority(new_priority);

        else {
            while (curr.next != null) {
                if (curr.getTaskId() == taskID) {
                    curr.setPriority(new_priority);
                    return;
                }
            }
        }
    }

    /*getLowestPriority()
        helper method to get node with lowest priority and give
        it to simulate()
     */

    private TaskNode getLowestPriority() {
        TaskNode lowest = head;
        TaskNode curr = head;

        while (curr.next != null) {
            if (curr.next.getPriority() < lowest.getPriority()) lowest = curr.next;

            curr = curr.next;
        }

        return lowest;
    }

    /* simulate()
       Run one step of the simulation:
         - If no tasks are left in the system, the CPU is idle, so return
           the value SIMULATE_IDLE.
         - Identify the next task to run based on the criteria given in the
           specification (tasks with the lowest priority value are ranked first,
           and if multiple tasks have the lowest priority value, choose the
           task with the lowest task ID).
         - Subtract one from the chosen task's time requirement (since it is
           being run for one step). If the task now requires 0 units of time,
           it has finished, so remove it from the system and return its task ID.
         - If the task did not finish, return SIMULATE_NONE_FINISHED.
    */
    public int simulate(){
        if (size == 0) return SIMULATE_IDLE;

        return SIMULATE_NONE_FINISHED;
    }
}
