/**
 * @author Eliandro Pizzonia
 * 
 * This class determines the path that Freddy follows from the start cell to the end cell
 * (the cell with Franny) based on the movement rules and priorities
 */
public class FrogPath {
    
    private Pond pond;

    /**
     * @param filename
     * constructor to initialize the pond using the given filename
     */
    public FrogPath (String filename){
    
    // try catch structure to be able to handle exceptions in the pond constructor
        try{
            pond = new Pond(filename);
        }
        catch(Exception e){
        }
    }

    /**
     * creates a priorityqueue to store all unmarked cells that can be reached from currCell and 
     * computes the priorities for the cells based on the order in which the cell should be chosen
     * 
     * @param currCell
     * @return returns the best cell based off the lowest priorty or movement rules (lillypad, near alligator, etc..)
     */
    public Hexagon findBest(Hexagon currCell){

        // creating the priority queue
        ArrayUniquePriorityQueue<Hexagon> priorityQueue = new ArrayUniquePriorityQueue<>();
        
        
        for(int i = 0; i < 6; i++){
            
            // try catch structure to catch any exceptions (such as checking a cell that is out of the pond)
            try{

                // marking the current cell in the stack 
                currCell.markInStack();

                // looping through the 6 neighbouring cells around the current cell to check their priorities
                Hexagon neighbouring_cell = currCell.getNeighbour(i);
    
                double priority = 0.0;
                
                // checking if there is an alligator in a nearby cell 
                check_alligator(neighbouring_cell);
                
                // if there is an alligator nearby, the cell is skipped
                if (check_alligator(neighbouring_cell)) {
                    continue;
                }

                // if the cell is already marked out the stack, the cell is skipped
                if (neighbouring_cell.isMarkedOutStack()) {
                    continue;
                }

                // if the cell is already marked in the stack, the cell is skipped
                if (neighbouring_cell.isMarkedInStack()) {
                    continue;
                }
                // giving the cell a certain priority based on the cell type
                else if (neighbouring_cell.isAlligator()) {
                    priority = 10.0;
                }
    
                else if(neighbouring_cell.isLilyPadCell()){
                    priority = 4.0;
                }
                
                else if(neighbouring_cell.isReedsCell()){
                    for(int c = 0; c < 6; c++ ){
                        check_alligator(neighbouring_cell);
                        if (check_alligator(neighbouring_cell)) {
                            priority = 10;
                        }
                        else{
                             priority = 5.0;
                        }
                    }
                }
    
                else if(neighbouring_cell.isWaterCell()){
                    priority = 6.0;
                }
    
                else if(neighbouring_cell.isMudCell()){
                    continue;
                }
                
                // handling the cells with flies (food)
                else if(neighbouring_cell instanceof FoodHexagon){
                    int numFlies = ((FoodHexagon)neighbouring_cell).getNumFlies();
    
                    if(numFlies == 1){
                        priority = 2.0;  
                    }
    
                    else if(numFlies == 2){
                        priority = 1.0;
                    }
    
                    else if (numFlies == 3) {
                        priority = 0.0;
                    }
    
                    else if (numFlies == 0){
                        priority = 6.0;
                    }
                }
                // adding the cells to the priority queue 
                priorityQueue.add(neighbouring_cell, priority);
            }
            catch(Exception e){
                continue;
            }
        }
        // checking if the current cell is a lilly pad to consider the cells that are two cells away
        on_lilypad(currCell, 0, priorityQueue);
        
        if (priorityQueue.isEmpty()) {
            return null;
        }
    
        
        // returning the best cell (the cell with the lowest priority will be at the front)
        Hexagon best_cell = priorityQueue.peek();
        return best_cell;
    }
    
    /**
     * private helper method to check and potentially move Freddy to the cells two cells away if Freddy is curently on a lilly pad cell
     * @param cell
     * @param prio
     * @param prio_Queue
     */
    private void on_lilypad(Hexagon cell, double prio, ArrayUniquePriorityQueue<Hexagon> prio_Queue){
        
        if(cell.isLilyPadCell()){
            prio = 0.0;
        
            for(int i = 0; i < 6; i++){
                    
                    // marking the current cell in the stack
                    cell.markInStack();

                     // looping through the 6 neighbouring cells around the current cell to check their priorities
                    Hexagon neighbouring_cell = cell.getNeighbour(i);

                    for(int j = 0; j < 6; j++){
                        try{ 
                            
                            // looping through the 6 cells 2 cells away from the neighbouring cells to check their priorities
                            Hexagon two_cells_away = neighbouring_cell.getNeighbour(j);
    
                        // checking if there is an alligator in a nearby cell 
                        check_alligator(two_cells_away);

                        // if the cell is already marked in the stack, the cell is skipped
                        if (two_cells_away.isMarkedInStack()) {
                            continue;
                        }

                        // if the cell is already marked out the stack, the cell is skipped
                        if (two_cells_away.isMarkedOutStack()) {
                            continue;
                        }
                        
                        // if there is an alligator nearby, the cell is skipped
                        if(check_alligator(two_cells_away)){
                            continue;
                        }
                        
                        // adding 0.5 to the priority if the cell 2 cells away is in a straight line
                        if (i == j) {
                            prio = 0.5;
                        }

                        // adding 1 to the priority if the cell 2 cells away is not in a straight line
                        else{
                            prio = 1.0;
                        }
    
                        if (two_cells_away.isAlligator()) {
                            prio += 10.0; 
                        }
    
                        else if(two_cells_away.isLilyPadCell()){
                            prio += 4.0;
                        }
    
                        else if(two_cells_away.isReedsCell()){
                            for(int c = 0; c < 6; c++ ){
                                if (two_cells_away.getNeighbour(c).isAlligator()) {
                                    prio += 10.0;
                            }
                            prio = 5.0;
                        }
                    }
    
                        else if(two_cells_away.isWaterCell()){
                            prio += 6.0;
                        }
    
                        else if(two_cells_away.isMudCell()){
                            continue;
                        }
                        
                        // handling the cells with flies (food)
                        else if(two_cells_away instanceof FoodHexagon){
                            int numFlies = ((FoodHexagon)two_cells_away).getNumFlies();
    
                            if(numFlies == 1){
                                prio += 2.0;  
                            }
                        
                            else if(numFlies == 2){
                                prio += 1.0;
                            }
                            
                            else if (numFlies == 3) {
                                prio += 0.0;
                            }
    
                            else if (numFlies == 0){
                                prio += 6.0;
                            }
                        }

                    // adding the cells to the priority queue                 
                    prio_Queue.add(two_cells_away, prio);
                    
                } 
                catch(Exception e){
                    continue;
                }
            }
  
        }
    }   }

    /**
     * 
     * @param cell
     * @return returns true if there is an allegator cell nearby
     */
    private boolean check_alligator(Hexagon cell){
        
        // looping through the neighboring cells to see if there is an allegator nearby
        for(int i = 0; i < 6; i++){
            Hexagon neighbouring_cell = cell.getNeighbour(i);
        
        // try catch structure to handle any possible cells out of bounds of the pond
        try{
            if(neighbouring_cell.isAlligator()){
                return true;
                }
            
        }   
        catch(Exception e){
            continue;
        }

    }
    return false;
    }

    /**
     * creates a stack using the ArrayStack class to keep track of the cells that 
     * the frog has visited in its path from the starting cell towards the end cell
     * 
     * @return returns a string containing the cell ID's of every cell Freddy visits
     * along his path 
     */
    public String findPath(){

        // creating a stack using the array stack class
        ArrayStack<Hexagon> stack = new ArrayStack<>();
        
        // setting the start of the stack
        Hexagon start = pond.getStart();
        
        stack.push(start);
        
        // tracking the number of flies eaton
        int fliesEaton = 0;
        
        StringBuilder empString = new StringBuilder();

        while(!stack.isEmpty()){

            // getting the top of the cell from the stack 
           Hexagon curr = stack.peek(); 
           empString.append(curr.getID() + " ");

           // breaking the loop if the current cell is the end
           if (curr.isEnd()) {
                break;
           }

           // if the current cell is a food cell (flies on it) the flies are eaton and removed
           if(curr instanceof FoodHexagon){
               fliesEaton = fliesEaton + ((FoodHexagon) curr).getNumFlies();
               ((FoodHexagon)curr).removeFlies();
           }

           // finding the next best cell
           Hexagon next = findBest(curr);
           
           /*  if there is no best cell found, the stack pops, and the current cell 
           is marked out of the stack 
           */
           if(next == null){
            stack.pop();
            curr.markOutStack();
           }

           /* 
           * if there is a best cell found, the next cell is pushed in the stack and is marked
           * in the stack
            */
           else{
            stack.push(next);
            next.markInStack();
           }

        }
       
        // if the stack is empty, the string length is set to 0 and the string is set to "No solution"
        if(stack.isEmpty()){
            empString.setLength(0);
            empString.append("No solution"); 
        }
        // else the string is set to the cell path IDs and the amount of flies eaton
        else{
            empString.append("ate "+ fliesEaton + " flies");
        }
    
        return empString.toString();
    
    }
}