import java.util.List;

public class Var_Heuristics {
    int type;
    LatinBoard board;
    public Var_Heuristics(int n, LatinBoard board){
        type = n;
        this.board = board;
    }

    public int getVariable(){
        if(board.unAssignedCells.size() == 0)
            return -1;
        if(type == 1){
            board.sortByDomainSize();
            return 0;
        }else if(type == 2){
            return 0;
        }else if(type == 3 ){

            return 0;
        }
        else{
            return -1;
        }
    }

}
