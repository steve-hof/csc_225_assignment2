/* NiceSimulator.java
   CSC 225 - Summer 2018

   An empty shell of the operations needed by the NiceSimulator
   data structure.

   B. Bird - 06/18/2018

   Added to by Steve Hof (V00320492) for Assignment 2 of CSC 225
*/


import javafx.concurrent.Task;

import java.io.*;
import java.util.*;


public class NiceSimulator {
    public static final int SIMULATE_IDLE = -2;
    public static final int SIMULATE_NONE_FINISHED = -1;
    public TaskNode[] tasks;
    public int max_tasks;
    public int curr_time_step;
    public int size;
    public int lowest_priority_value;
    public int lowest_priority_task_id;
    public Hashtable<Integer, Integer> task_dict;

    /* Constructor(maxTasks)
       Instantiate the data structure with the provided maximum
       number of tasks. No more than maxTasks different tasks will
       be simultaneously added to the simulator, and additionally
       you may assume that all task IDs will be in the range
         0, 1, ..., maxTasks - 1
    */
    public NiceSimulator(int maxTasks){
        tasks = new TaskNode[maxTasks];
        task_dict = new Hashtable<>();
        curr_time_step = 0;
        max_tasks = maxTasks;
        size = 0;
        lowest_priority_value = 0;
//        id_of_lowest_priority_task;
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
        return task_dict.containsKey(taskID);
    }

    /* getPriority(taskID)
       Return the current priority value for the provided
       task ID. You may assume that the task ID provided
       is valid.

    */
    public int getPriority(int taskID){
        int index = task_dict.get(taskID);
        return tasks[index].getPriority();

    }

    /* getRemaining(taskID)
       Given a task ID, return the number of timesteps
       remaining before the task completes. You may assume
       that the task ID provided is valid.

    */
    public int getRemaining(int taskID){
        int index = task_dict.get(taskID);
        return tasks[index].getStepsRemaining();
    }

    /* heapify(int taskID) *** helper method ***
        perform heapify algorithm
     */

    private void heapify(int idx) {
//        int idx = task_dict.get(taskID);
        TaskNode parent_task;
        // find index of parent
        int parent_idx = idx / 2;

        // if parent less than children or we're at the
        // root of the tree, weez awl dun
        if (idx == 1 || tasks[parent_idx].getTaskId() < tasks[idx].getTaskId()) {
            return;
        }
        // else we need to swap the parent and the child
        else {
            parent_task = tasks[parent_idx];
            tasks[parent_idx] = tasks[idx];
            tasks[idx] = parent_task;
        }

        heapify(parent_task.getTaskId());
    }

    
    /* add(taskID, time_required)
       Add a task with the provided task ID and time requirement
       to the system. You may assume that the provided task ID is in
       the correct range and is not a currently-active task.
       The new task will be assigned nice level 0.
    */
    public void add(int taskID, int time_required){
        TaskNode newNode = new TaskNode(taskID, time_required);
        size++;
        task_dict.put(taskID, size);

        tasks[size] = newNode;

        // Check if current_lowest_priority is less than 0
        int new_priority_value = 0;
        if (new_priority_value < lowest_priority_value) {
            lowest_priority_value = new_priority_value;
            lowest_priority_task_id = taskID;
        } else if (new_priority_value == lowest_priority_value) {
            if (size == 0 || size == 1) {
                lowest_priority_task_id = taskID;
            } else if (taskID < tasks[task_dict.get(lowest_priority_task_id)].getTaskId()) {
                lowest_priority_task_id = taskID;
            }
        }
        heapify(task_dict.get(taskID));
    }



    /*
        noKids(int idx) *** helper method that determines if task
        has no kids
     */

    private boolean noKids(int idx) {
        return (idx * 2) > size;
    }



    /*
        bubbleDown(int idx) *** helper method for kill ***
     */

    private void bubbleDown(int idx) {
        // if task is a leaf, we dun
        if (noKids(idx)) return;

        // compare kids
        int left_child_idx = idx * 2;
        int right_child_idx = (idx * 2) + 1;
        int smallest_child_idx;

        // check if there are two kids, otherwise must be left
        if (right_child_idx > size - 1) {
            smallest_child_idx = left_child_idx;

        } else {
            smallest_child_idx = (tasks[left_child_idx].getTaskId() <
                    tasks[right_child_idx].getTaskId()) ? left_child_idx : right_child_idx;
        }

        // if parent task id is already smaller, we dun
        if (tasks[idx].getTaskId() < tasks[smallest_child_idx].getTaskId()) return;

        // Swap child with parent
        TaskNode child_task = tasks[smallest_child_idx];
        tasks[smallest_child_idx] = tasks[idx];
        tasks[idx] = child_task;

        // Update dictionary O(1)
        task_dict.put(tasks[idx].getTaskId(), idx);
        task_dict.put(tasks[smallest_child_idx].getTaskId(), smallest_child_idx);

        bubbleDown(smallest_child_idx);
    }

    /* kill(taskID)
       Delete the task with the provided task ID from the system.
       You may assume that the provided task ID is in the correct
       range and is a currently-active task.
    */
    public void kill(int taskID){
        int task_to_remove_idx = task_dict.get(taskID);
        TaskNode task_to_remove = tasks[task_to_remove_idx];
        TaskNode last_task = tasks[size];
        tasks[size] = null;
        tasks[task_to_remove_idx] = last_task;
        size = size - 1;

        // if task to kill has lowest priority, must update lowest priority
        if (lowest_priority_task_id == taskID) {
            // find new lowest priority value

            // change to ask for index instead
//            lowest_priority_task_id = findLowestPriorityIdx();
            int lowest_priority_idx = findLowestPriorityIdx();
//            lowest_priority_value = tasks[task_dict.get(lowest_priority_task_id)].getPriority();
        }

        // Remove from dictionary
        task_dict.remove(taskID);

        // Put tasks in correct order
        bubbleDown(task_to_remove_idx);
    }


    /* renice(taskID, new_priority)
       Change the priority of the the provided task ID to the new priority
       value provided. The change must take effect at the next simulate() step.
       You may assume that the provided task ID is in the correct
       range and is a currently-active task.

    */


    public void renice(int taskID, int new_priority) {
        int idx = task_dict.get(taskID);
        tasks[idx].setPriority(new_priority);

        if (new_priority < lowest_priority_value) {
            lowest_priority_value = new_priority;
            lowest_priority_task_id = taskID;
        }
    }

    /*
        findLowestPriority() *** helper method ***
        returns the index of the task with the lowest priority

     */

    private int findLowestPriorityIdx() {
        // If only one task left, return first index
        if (size == 1) return 1;

        int lowest_priority_idx = 1;
        int curr_idx = 2;
        do {
            if (tasks[curr_idx].getPriority() < tasks[lowest_priority_idx].getPriority()) {
                lowest_priority_idx = curr_idx;
            }
            curr_idx++;
        } while (curr_idx <= size);

        int testaroo = tasks[lowest_priority_idx].getTaskId();
        return lowest_priority_idx;
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
//        int temp;
        if (size == 0) return SIMULATE_IDLE;

        // Find lowest priority task
//        int curr_task_idx = findLowestPriorityIdx();
        int curr_task_idx = task_dict.get(lowest_priority_task_id);
//        TaskNode curr_task = tasks[curr_task_idx];

        // Use up one time step
        tasks[curr_task_idx].decrementTimeStep();
        curr_time_step++;

        // Check if task has been completed
        if (tasks[curr_task_idx].getStepsRemaining() == 0) {
            int temp = tasks[curr_task_idx].getTaskId();
            kill(tasks[curr_task_idx].getTaskId());

            return temp;
        }
        
        return SIMULATE_NONE_FINISHED;
    }
}
