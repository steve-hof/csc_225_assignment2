/* NiceSimulator.java
   CSC 225 - Summer 2018

   An empty shell of the operations needed by the NiceSimulator
   data structure.

   B. Bird - 06/18/2018
*/


import java.io.*;
import java.util.*;

public class bNiceSimulator{

    public static final int SIMULATE_IDLE = -2;
    public static final int SIMULATE_NONE_FINISHED = -1;
    public TaskNode[] tasks;
    public int max_tasks;
    public int curr_time_step;
    public Hashtable<Integer, TaskNode> task_dict;



    /* Constructor(maxTasks)
       Instantiate the data structure with the provided maximum
       number of tasks. No more than maxTasks different tasks will
       be simultaneously added to the simulator, and additionally
       you may assume that all task IDs will be in the range
         0, 1, ..., maxTasks - 1
    */
    public bNiceSimulator(int maxTasks){
        tasks = new TaskNode[maxTasks];
        task_dict = new Hashtable<>();
        curr_time_step = 0;
        max_tasks = maxTasks;
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
        return false;
    }

    /* getPriority(taskID)
       Return the current priority value for the provided
       task ID. You may assume that the task ID provided
       is valid.

    */
    public int getPriority(int taskID){
        return -1;
    }

    /* getRemaining(taskID)
       Given a task ID, return the number of timesteps
       remaining before the task completes. You may assume
       that the task ID provided is valid.

    */
    public int getRemaining(int taskID){
        return -1;
    }


    /* add(taskID, time_required)
       Add a task with the provided task ID and time requirement
       to the system. You may assume that the provided task ID is in
       the correct range and is not a currently-active task.
       The new task will be assigned nice level 0.
    */
    public void add(int taskID, int time_required){

    }


    /* kill(taskID)
       Delete the task with the provided task ID from the system.
       You may assume that the provided task ID is in the correct
       range and is a currently-active task.
    */
    public void kill(int taskID){

    }


    /* renice(taskID, new_priority)
       Change the priority of the the provided task ID to the new priority
       value provided. The change must take effect at the next simulate() step.
       You may assume that the provided task ID is in the correct
       range and is a currently-active task.

    */
    public void renice(int taskID, int new_priority){

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
        return SIMULATE_IDLE;
    }


}