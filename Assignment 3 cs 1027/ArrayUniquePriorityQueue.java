/**
 * @author Eliandro Pizzonia
 * 
 * This class implements the UniquePriorityQueueADT interface and includes all the methods defined in the interface.
 * Arrays are the underlying data structure used to implement this ADT
 */

public class ArrayUniquePriorityQueue<T> {
    private T[] queue;
    private double[] priority;
    private int count;
 
    /**
     * 
     * Constructor that initializes both arrays with capacities of 10 and sets the count to 0
     */
    public ArrayUniquePriorityQueue (){
        queue = (T[]) new Object[10];
        priority = new double[10];
        count = 0;
    }

    /**
     * adds both the data and priority of a cell to the queue
     * @param data
     * @param prio
     */
   public void add (T data, double prio){
        
        // check if the given data item is already contained anywhere in the queue and if it is, the method ends without doing anything
        if (this.contains(data)) {
            return;
        }

        // if the arrays are full, both arrays expand capacity by 5 spaces
        if(getLength() == size()){
            T[] queue_copy = (T[]) new Object[queue.length + 5];
            System.arraycopy(queue, 0, queue_copy, 0, count);
            
            double[] priority_copy = new double[priority.length + 5];
            System.arraycopy(priority,0, priority_copy, 0, count);

            queue = queue_copy;
            priority = priority_copy;

        }
        
        // adding the given data item into the queue so that the priorities are ordered from lowest to highest
        queue[count] = data;
        priority[count] = prio;
        count++;

        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (priority[j] > priority[j + 1]) {
                    double temp_prio = priority[j];
                    priority[j] = priority[j + 1];
                    priority[j + 1] = temp_prio;

                    T temp_data = queue[j];
                    queue[j] = queue[j + 1];
                    queue[j + 1] = temp_data;

                        }
                    }
                }
            }    

    /**
     * 
     * @param data
     * @return returns true if given data item in contained somewhere in queue, or false otherwise
     */
    public boolean contains (T data){
            
        // looping through the queue and checking if each element is equal to data
        for(int i = 0; i < count; i++){
            if(queue[i].equals(data)){
                return true;
                }
            }
        return false;
        }

    /**
     * 
     * @return returns the item with the smallest priority without removing it or changing it 
     * @throws CollectionException
     */
    public T peek () throws CollectionException{
        
        // if the priority queue is empty, a CollectionException is thrown
        if (isEmpty()) {
            throw new CollectionException("PQ is empty");
        }

        // else the smallest priority is found and returned without removing or changing it
        int prio_smallest_index = 0;

        for(int i = 1; i < count; i++){
            if (priority[i] < priority[prio_smallest_index]) {
                prio_smallest_index = i;

            }
        }
        return queue[prio_smallest_index];
    }

    /**
     * 
     * @return removes and returns the item with the smallest priority and shifts shifts any 
        subsequent values from both arrays over to the left to fill the gap made by the 
        removed item
     * @throws CollectionException
     */
    public T removeMin () throws CollectionException{
        
        // if the priority queue is empty, a CollectionException is thrown
        if(isEmpty()){
            throw new CollectionException("PQ is empty");
        }

        // finding the item with the lowest priority and shifting the values in the arrays
        int priority_smallest_index = 0;

        for(int i = 1; i < count; i++){
            if (priority[i] < priority[priority_smallest_index]) {
                priority_smallest_index = i;

            }
        }
        
        T smallest_prio_value = queue[priority_smallest_index];

        for(int i = priority_smallest_index; i < count; i++){
            queue[i] = queue[i+1];
        }

        return smallest_prio_value;

    }

    /**
     * this method allows an update of an items priority and makes sure the queue keeps the correct order even after updating the priority of an item
     * @param data
     * @param newPrio
     * @throws CollectionException
     */
    public void updatePriority (T data, double newPrio) throws CollectionException{
        
        // if the given data item is not contained in the queue, a CollectionException is thrown
        if(!this.contains(data)){
            throw new CollectionException("Item not found in PQ");
        }

        // if the given data item is found in the queue, find the index of the item in the queue
        int index_prio = 0;
         if (this.contains(data)) {
            
            for(int i = 0; i < count; i++){
                if (queue[i] == data) {
                    index_prio = i;
                    
                }
            }    
        
            // creating a copy of the queue without the data item
            T[] queue_copy = (T[]) new Object[queue.length - 1];
            System.arraycopy(queue, 0, queue_copy, 0, index_prio);
            System.arraycopy(queue, index_prio + 1, queue_copy, index_prio, count - index_prio - 1);

            // creating a copy of the priority array without the priority of the item
            double[] priority_copy = new double[priority.length - 1];
            System.arraycopy(priority,0, priority_copy, 0, index_prio);
            System.arraycopy(priority, index_prio + 1, priority_copy, index_prio, count - index_prio - 1);

            // updating the queue and priority array with their copies
            queue = queue_copy;
            priority = priority_copy;
            
            // decrese the count of items in the queue
            count--;

            this.add(data, newPrio);
        }
    } 
/**
 * 
 * @return returns true if the priority queue is empty or false otherwise
 */
    public boolean isEmpty (){
        
        // if count = 0, the priority queue is empty
        if(count == 0){
            return true;
        }
    return false;
    }

    /**
     * 
     * @return returns the number of items stored in the priority queue
     */
    public int size (){
        return count;
    }

    /**
     * 
     * @return returns the capacity of the arrays
     */
    public int getLength(){
        return queue.length;
    }

   
    /**
     * @return returns a string that contains each data item from the queue followed by its corresponding priority in square brackets
     * 
     */
    public String toString (){

        if(isEmpty()){
            return "The PQ is empty";
        }
     
        // creating the string containing each data item from the queue
        StringBuilder str = new StringBuilder();

        for(int i = 0; i < count; i++){
            str.append(queue[i]);
            str.append(" [" + priority[i] + "]");

            if (i < count - 1) {
                str.append(", ");
            }
        }

        return str.toString();
    }

}

