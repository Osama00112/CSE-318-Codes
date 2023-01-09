import java.util.*;

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
        VAR_H = new Var_Heuristics(VAH_type, board.size);
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

    public List<Integer> valueHeuristics(LatinBoard board, SquareCell choosenCell){
        int []freq = new int[board.size];
        HashMap<String, Integer> map = new HashMap<>();
        for(int dom : choosenCell.domain){
            int count = 0;
            for(SquareCell sc: board.unAssignedCells){
                if(sc.x == choosenCell.x && sc.y == choosenCell.y){ //same cell
                    continue;
                }
                if(sc.x == choosenCell.x){
                    if(board.colDomains[sc.y].charAt(dom - 1) == '1')
                        count ++;
                }
                if(sc.y == choosenCell.y){
                    if(board.rowDomains[sc.x].charAt(dom - 1) == '1')
                        count++;
                }
            }
            map.put(Integer.toString(dom),count);
        }
        HashMap<String, Integer> map1 = sortByValue(map);
        List<String> newDomainListStrings = new ArrayList<>(map1.keySet());
        List<Integer> newDomainList = new ArrayList<>();

        for(int i=0; i<map1.size(); i++){
            newDomainList.add(Integer.parseInt(newDomainListStrings.get(i)));
        }
        return newDomainList;
    }
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }


    public void backTrack(){
        //variableHeuristics(board);
        if(algoMethod == 1)
            solvedBoard = backTrackingSolve(board);
        else
            solvedBoard = forwareCheckingSolve(board);
    }
    public LatinBoard backTrackingSolve(LatinBoard b){
        nodecount++;
        int DomAffectedNodeCount = 0;
        //int DegAffectedNodeCount = 0;

        List<SquareCell> DomAffected;
        //List<SquareCell> DegreeAffected;
        if(b.unAssignedCells.size() == 0)
            return b;

        int index = VAR_H.getVariable(b.unAssignedCells);
        SquareCell tempCell = b.unAssignedCells.get(index); //add heuristics later

        tempCell.domain = valueHeuristics(b, tempCell);
        for(int dom: tempCell.domain){
            //nodecount++;
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
                LatinBoard returned = backTrackingSolve(b);
                if(returned != null)
                    return returned;
                else{
                    //BTcount++;
                    b.revertCellDomains(tempCell.getValue(), DomAffectedNodeCount);
                    //b.revertDegreeChanges(DegAffectedNodeCount);
                    b.unAssignedCells.add(tempCell);
                    //variableHeuristics(b);
                    b.rowDomains[tempCell.x] = b.changeCh(b.rowDomains[tempCell.x], dom - 1, '1' );
                    b.colDomains[tempCell.y] = b.changeCh(b.colDomains[tempCell.y], dom - 1, '1' );
                    b.cells[tempCell.x][tempCell.y].setValue(0);
                }
            }
            //BTcount++;
        }
        BTcount++;
        return null;
    }

    public  LatinBoard forwareCheckingSolve(LatinBoard b){
        nodecount++;
        int DomAffectedNodeCount = 0;
        //int DegAffectedNodeCount = 0;
        List<SquareCell> DomAffected;
        //List<SquareCell> DegreeAffected;
        //System.out.println("kkkkkk");
        if(b.unAssignedCells.size() == 0)
            return b;
        int index = VAR_H.getVariable(b.unAssignedCells);
        SquareCell tempCell = b.unAssignedCells.get(index); //add heuristics later

        tempCell.domain = valueHeuristics(b, tempCell);

        for(int dom: tempCell.domain){
            //nodecount++;
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
                //variableHeuristics(b);
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
                    //BTcount++;
                    b.revertCellDomains(tempCell.getValue(), DomAffectedNodeCount);
                    //b.revertDegreeChanges(DegAffectedNodeCount);
                    b.unAssignedCells.add(tempCell);
                    //variableHeuristics(b);
                    b.rowDomains[tempCell.x] = b.changeCh(b.rowDomains[tempCell.x], dom - 1, '1' );
                    b.colDomains[tempCell.y] = b.changeCh(b.colDomains[tempCell.y], dom - 1, '1' );
                    b.cells[tempCell.x][tempCell.y].setValue(0);
                }
            }

            //BTcount++;
        }
        BTcount++;
        return null;
    }
}