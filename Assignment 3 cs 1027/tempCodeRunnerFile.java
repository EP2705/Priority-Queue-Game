private void on_lilypad(Hexagon cell, double prio, ArrayUniquePriorityQueue<Hexagon> prio_Queue){
        
        if(cell.isLilyPadCell()){
            prio = 0.0;
            
            for(int i = 0; i < 6; i++){
                try{

                    cell.markInStack();

                    Hexagon neighbouring_cell = cell.getNeighbour(i);
                
                    for(int j = 0; j < 6; j++){
                        Hexagon two_cells_away = neighbouring_cell.getNeighbour(j);
    
                        check_alligator(two_cells_away);

                        if (two_cells_away.isMarkedInStack()) {
                            continue;
                        }

                        if (two_cells_away.isMarkedOutStack()) {
                            continue;
                        }
                    
                        if(check_alligator(two_cells_away)){
                            continue;
                        }

                        if (i == j) {
                            prio = 0.5;
                        }
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
                    
                    prio_Queue.add(two_cells_away, prio);
                    }
                } 
                catch(Exception e){
                    continue;
                }
            }
        } else {
            return;
        }
    }

    private boolean check_alligator(Hexagon cell){
            
        for(int i = 0; i < 6; i++){
            Hexagon neighbouring_cell = cell.getNeighbour(i);
                
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