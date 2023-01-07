import java.util.ArrayList;
import java.util.List;

public class Solver {
    public int VAH_type;
    public Var_Heuristics VAR_H;
    public ValOrder_Heuristics VAL_H;
    public LatinBoard board;
    public LatinBoard solvedBoard;
    int nodecount;
    int BTcount;
    public Solver(int VAH_type, LatinBoard board){
        this.VAH_type = VAH_type;
        this.board = board;
        solvedBoard = null;
        nodecount = 0;
        BTcount = 0;
    }

    public void variableHeuristics(){
        if(VAH_type == 1)
            board.sortByDomainSize();
        else if(VAH_type == 2)
            board.sortByDegree();
    }


    public void backTrack(){
        variableHeuristics();
        solvedBoard = backTrackingSolve(board);
    }
    public LatinBoard backTrackingSolve(LatinBoard b){
        int affecedNodeCount = 0;
        List<SquareCell> affected;
        //System.out.println("kkkkkk");
        if(b.unAssignedCells.size() == 0)
            return b;

        SquareCell tempCell = b.unAssignedCells.get(0); //add heuristics later

        for(int dom: tempCell.domain){
            nodecount++;
            if(b.rowDomains[tempCell.x].charAt(dom - 1) == '1' && b.colDomains[tempCell.y].charAt(dom - 1) == '1'){
                tempCell.setValue(dom);
                //b.cells[tempCell.x][tempCell.y] = tempCell;
                b.cells[tempCell.x][tempCell.y].setValue(dom);
                b.rowDomains[tempCell.x] = b.changeCh(b.rowDomains[tempCell.x], dom - 1, '0' );
                b.colDomains[tempCell.y] = b.changeCh(b.colDomains[tempCell.y], dom - 1, '0' );
                b.unAssignedCells.remove(tempCell);
                affected = new ArrayList<>(b.updateCellDomains(tempCell.x, tempCell.y, tempCell.getValue()));
                affecedNodeCount = affected.size();
                //System.out.println(b);
                //temp.updateCellDomains(tempCell);
                LatinBoard returned = backTrackingSolve(b);
                if(returned != null)
                    return returned;
                else{
                    //temp = b;
                    BTcount++;
                    b.revertCellDomains(tempCell.x, tempCell.y, tempCell.getValue(), affecedNodeCount);
                    b.unAssignedCells.add(0,tempCell);
                    b.rowDomains[tempCell.x] = b.changeCh(b.rowDomains[tempCell.x], dom - 1, '1' );
                    b.colDomains[tempCell.y] = b.changeCh(b.colDomains[tempCell.y], dom - 1, '1' );
                    b.cells[tempCell.x][tempCell.y].setValue(0);
                }
            }

        }
        return null;
    }
}