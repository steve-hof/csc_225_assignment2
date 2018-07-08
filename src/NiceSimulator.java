/* NiceSimulator.java
   CSC 225 - Summer 2018

   An empty shell of the operations needed by the NiceSimulator
   data structure by B. Bird - 06/18/2018

   Added to by Steve Hof (V00320492) for Assignment 2 of CSC 225

   All methods implemented at the prescribed time complexity
*/

public class NiceSimulator {
    public static final int SIMULATE_IDLE = -2;
    public static final int SIMULATE_NONE_FINISHED = -1;
    private TaskNode[] tasks;
    private int max_tasks;
    private int curr_time_step;
    private int size;
    private int[] lookup_table;

    /* Constructor(maxTasks)
       Instantiate the data structure with the provided maximum
       number of tasks. No more than maxTasks different tasks will
       be simultaneously added to the simulator, and additionally
       you may assume that all task IDs will be in the range
         0, 1, ..., maxTasks - 1
    */
    public NiceSimulator(int maxTasks){
        tasks = new TaskNode[maxTasks];
        curr_time_step = 0;
        max_tasks = maxTasks;
        size = 0;

        // maps taskID : task index
        lookup_table = new int[maxTasks];
    }
    

    /* taskValid(taskID)
       Given a task ID, return true if the ID is currently
       in use by a valid task (i.e. a task with at least 1
       unit of time remaining) and false otherwise.

       Note that you should include logic to check whether
       the ID is outside the valid range 0, 1, ..., maxTasks - 1
       of task indices.

       O(1)
    */
    public boolean taskValid(int taskID){
        if (taskID < 0 || taskID >= max_tasks) return false;

        if (lookup_table[taskID] != 0) return true;

        return false;
    }

    /* getPriority(taskID)
       Return the current priority value for the provided
       task ID. You may assume that the task ID provided
       is valid.

       O(1)
    */
    public int getPriority(int taskID){
        int index = lookup_table[taskID];
        return tasks[index].getPriority();

    }

    /* getRemaining(taskID)
       Given a task ID, return the number of timesteps
       remaining before the task completes. You may assume
       that the task ID provided is valid.

       O(1)
    */
    public int getRemaining(int taskID){
        int index = lookup_table[taskID];
        return tasks[index].getStepsRemaining();
    }


    /* heapify(int taskID) *** helper method ***
        performs heapify algorithm learned in class
        as well as keeping the lookup table up to
        date.

        Algorithm is performed on complete trees, so
        the height is within one of log(n). Since
        none of the operations in the algorithm depend
        on n, heapify runs in

        O(log(n))
     */

    private void heapify(int idx) {
        if (size == 1) {
            return;
        }

        TaskNode parent_task;
        // find index of parent
        int parent_idx = idx / 2;

        if (parent_idx == 0) {
            bubbleDown(idx);
            return;
        }
        if (tasks[parent_idx].isLessThan(tasks[idx])) return;

        // else we need to swap the parent and the child
        else {
            parent_task = tasks[parent_idx];
            int old_parent_task_id = parent_task.getTaskId();
            int old_child_task_id = tasks[idx].getTaskId();

            tasks[parent_idx] = tasks[idx];
            tasks[idx] = parent_task;

            lookup_table[old_child_task_id] = parent_idx;
            lookup_table[old_parent_task_id] = idx;
        }

        heapify(parent_idx);
    }

    
    /* add(taskID, time_required)
       Add a task with the provided task ID and time requirement
       to the system. You may assume that the provided task ID is in
       the correct range and is not a currently-active task.
       The new task will be assigned nice level 0.

       All the operations run in constant time and heapify (mentioned
       above) runs in O(log(n)), therefore, add runs in

       O(log(n))
    */
    public void add(int taskID, int time_required){
        TaskNode newNode = new TaskNode(taskID, time_required);
        size++;

        tasks[size] = newNode;
        lookup_table[taskID] = size;

        heapify(size);
    }


    /*
        noKids(int idx) *** helper method that determines if task
        has no kids

        O(1)
     */
    private boolean noKids(int idx) {
        return (idx * 2) > size;
    }


    /*
        bubbleDown(int idx) *** helper method for kill ***

        Running time can be explained as similar to heapify (above).
        None of the individual operations depend on n, and
        the height of the tree is within a constant of log(n).
        Since the algorithm moves down the tree and not side to
        side, the number of operations is the height

        O(log(n))
     */

    private void bubbleDown(int idx) {
        // if task is a leaf, we dun
        if (noKids(idx)) return;

        // compare kids
        int left_child_idx = idx * 2;
        int right_child_idx = (idx * 2) + 1;
        int smallest_child_idx;

        // check if there are two kids, otherwise must be left
        if (right_child_idx > size) {
        smallest_child_idx = left_child_idx;

        } else {
            // determine which child is smaller
            smallest_child_idx = (tasks[left_child_idx].isLessThan(tasks[right_child_idx])) ? left_child_idx : right_child_idx;
        }

        // if parent is already smaller, we dun
        if (tasks[idx].isLessThan(tasks[smallest_child_idx])) return;

        // Swap child with parent
        TaskNode child_task = tasks[smallest_child_idx];
        TaskNode parent_task = tasks[idx];
        int parent_taskId = parent_task.getTaskId();
        int child_taskId = child_task.getTaskId();

        tasks[smallest_child_idx] = tasks[idx];
        tasks[idx] = child_task;

        // Update lookup table
        lookup_table[parent_taskId] = smallest_child_idx;
        lookup_table[child_taskId] = idx;

        bubbleDown(smallest_child_idx);
    }

    /* kill(taskID)
       Delete the task with the provided task ID from the system.
       You may assume that the provided task ID is in the correct
       range and is a currently-active task.


    */
    public void kill(int taskID){
        // get index of task to kill (in the heap array)
        int task_to_remove_idx = lookup_table[taskID];
        if (task_to_remove_idx == 0) return;

        // create a pointer to the last task in the heap array
        TaskNode last_task = tasks[size];

        // if the taskID to kill is the same as the taskID of the
        // last element in the array, simply remove the last element

        if (taskID == last_task.getTaskId()) {
            tasks[size] = null;
            lookup_table[taskID] = 0;
            size = size - 1;
            return;
        }

        // remove the last task
        tasks[size] = null;

        // put the last task at the index of the element to remove
        tasks[task_to_remove_idx] = last_task;
        lookup_table[taskID] = task_to_remove_idx;
        size = size - 1;

        lookup_table[taskID] = 0;
        lookup_table[last_task.getTaskId()] = task_to_remove_idx;
        // Put tasks in correct order
        bubbleDown(task_to_remove_idx);
    }


    /* renice(taskID, new_priority)
       Change the priority of the the provided task ID to the new priority
       value provided. The change must take effect at the next simulate() step.
       You may assume that the provided task ID is in the correct
       range and is a currently-active task.

       Because of the lookup table, finding the task to re-nice takes
       constant time. Heapify and bubbleDown both take O(log(n)) as previously
       described.

       O(log(n))
    */

    public void renice(int taskID, int new_priority) {
        int idx = lookup_table[taskID];
        tasks[idx].setPriority(new_priority);
        heapify(idx);
        bubbleDown(idx);
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

         - None of the methods depend on n but the kill method that is called
           is O(log(n)) (as described above)

           O(log(n))
    */
    public int simulate(){
        if (size == 0) return SIMULATE_IDLE;


        // Lowest priority task is always at index 1
        int curr_task_idx = 1;

        // Use up one time step
        tasks[curr_task_idx].decrementTimeStep();
        curr_time_step++;

        // Check if task has been completed
        if (tasks[curr_task_idx].getStepsRemaining() <= 0) {
            int temp = tasks[curr_task_idx].getTaskId();
            kill(tasks[curr_task_idx].getTaskId());

            return temp;
        }

        return SIMULATE_NONE_FINISHED;
    }
}
