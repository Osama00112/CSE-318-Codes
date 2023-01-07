import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class Solver {
    int algoMethod;
    public int VAH_type;
    public Var_Heuristics VAR_H;
    public ValOrder_Heuristics VAL_H;
    public LatinBoard board;
    public LatinBoard solvedBoard;
    int nodecount;
    int BTcount;
    public Solver(int algoMethod, int VAH_type, LatinBoard board){
        this.algoMethod = algoMethod;
        this.VAH_type = VAH_type;
        this.board = board;
        this.board.method = algoMethod;
        solvedBoard = null;
        nodecount = 1;
        BTcount = 0;
    }

    public void variableHeuristics(LatinBoard b){
        if(VAH_type == 1)
            b.sortByDomainSize();
        else if(VAH_type == 2)
            b.sortByDegree();
        else if(VAH_type == 3)
            b.sortByVah1TiesByVah2();
        else if(VAH_type == 4)
            b.sortByVah4();
        else if(VAH_type == 5)
            b.sortByVah5();
        else {
            System.out.println("ERROR choosing heuristics");
            exit(0);
        }
    }


    public void backTrack(){
        variableHeuristics(board);
        if(algoMethod == 1)
            solvedBoard = backTrackingSolve(board);
        else
            solvedBoard = forwareCheckingSolve(board);
    }
    public LatinBoard backTrackingSolve(LatinBoard b){
        int DomAffectedNodeCount = 0;
        //int DegAffectedNodeCount = 0;

        List<SquareCell> DomAffected;
        //List<SquareCell> DegreeAffected;
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
                DomAffected = new ArrayList<>(b.updateCellDomains(tempCell.x, tempCell.y, tempCell.getValue()));
                //DegreeAffected = new ArrayList<>(b.updateDegree(tempCell.x, tempCell.y, tempCell.getValue()));

                DomAffectedNodeCount = DomAffected.size();
                //DegAffectedNodeCount = DegreeAffected.size();
                variableHeuristics(b);
                //System.out.println(b);
                //temp.updateCellDomains(tempCell);
                LatinBoard returned = backTrackingSolve(b);
                if(returned != null)
                    return returned;
                else{
                    //temp = b;
                    BTcount++;
                    b.revertCellDomains(tempCell.getValue(), DomAffectedNodeCount);
                    //b.revertDegreeChanges(DegAffectedNodeCount);
                    b.unAssignedCells.add(tempCell);
                    variableHeuristics(b);
                    b.rowDomains[tempCell.x] = b.changeCh(b.rowDomains[tempCell.x], dom - 1, '1' );
                    b.colDomains[tempCell.y] = b.changeCh(b.colDomains[tempCell.y], dom - 1, '1' );
                    b.cells[tempCell.x][tempCell.y].setValue(0);
                }
            }

        }
        return null;
    }

    public  LatinBoard forwareCheckingSolve(LatinBoard b){
        int DomAffectedNodeCount = 0;
        //int DegAffectedNodeCount = 0;

        List<SquareCell> DomAffected;
        //List<SquareCell> DegreeAffected;
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
                DomAffected = (b.updateCellDomains(tempCell.x, tempCell.y, tempCell.getValue()));
                //DegreeAffected = new ArrayList<>(b.updateDegree(tempCell.x, tempCell.y, tempCell.getValue()));
                LatinBoard returned;
                variableHeuristics(b);
                if(DomAffected == null){
                    DomAffectedNodeCount = 0;
                    returned = null;
                }else{
                    DomAffectedNodeCount = DomAffected.size();
                    returned = forwareCheckingSolve(b);
                }
                if(returned != null)
                    return returned;
                else{
                    //temp = b;
                    BTcount++;
                    b.revertCellDomains(tempCell.getValue(), DomAffectedNodeCount);
                    //b.revertDegreeChanges(DegAffectedNodeCount);
                    b.unAssignedCells.add(tempCell);
                    variableHeuristics(b);
                    b.rowDomains[tempCell.x] = b.changeCh(b.rowDomains[tempCell.x], dom - 1, '1' );
                    b.colDomains[tempCell.y] = b.changeCh(b.colDomains[tempCell.y], dom - 1, '1' );
                    b.cells[tempCell.x][tempCell.y].setValue(0);
                }
            }

        }
        return null;
    }
}