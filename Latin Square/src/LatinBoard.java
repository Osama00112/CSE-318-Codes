import java.util.*;

public class LatinBoard {
    public int size;
    public SquareCell[][] cells;
    public String[] rowDomains;
    public String[] colDomains;
    public List<SquareCell> unAssignedCells;

    public LatinBoard(int size){
        this.size = size;
        cells = new SquareCell[size][size];
        rowDomains = new String[size];
        colDomains = new String[size];
        unAssignedCells = new ArrayList<>();
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                cells[i][j] = new SquareCell(0,i,j);
            }
        }

        for(int i=0; i<size; i++){
            rowDomains[i] = "";
            colDomains[i] = "";
            for(int j=0; j<size; j++){
                rowDomains[i] += "1";
                colDomains[i] += "1";
            }
        }
    }
    public String changeCh(String st, int index, char ch){
        StringBuilder sb = new StringBuilder(st);
        sb.setCharAt(index,ch);
        return sb.toString();
    }

    public void updateDomains(){ // Also populates unAssigned list
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if(cells[i][j].getValue() != 0){
                    int index = cells[i][j].getValue()-1;
                    //tempDomain.add(cells[i][j].getValue());
                    rowDomains[i] = changeCh(rowDomains[i], index,  '0');
                    colDomains[j] = changeCh(colDomains[j], index, '0' );
                }else{
                    unAssignedCells.add(cells[i][j]);
                }
            }

//            for(int j=0; j<size; j++){
//                if(cells[i][j].getValue() == 0){
//                    cells[i][j].domain.removeAll(tempDomain);
//                    unAssignedCells.add(cells[i][j]);
//                }
//            }
        }
        updateCellDomains();
    }

    public void updateCellDomains(){
        for(SquareCell sc: unAssignedCells){
            List<Integer> tempDomain = new ArrayList<>();
            //System.out.println("pos "+ sc.x + " " +sc.y +" row " + rowDomains[sc.x] + " col " + colDomains[sc.y]);
            for(int i=0; i<size; i++){
                if(! (rowDomains[sc.x].charAt(i) == colDomains[sc.y].charAt(i) && rowDomains[sc.x].charAt(i) == '1')){
                    tempDomain.add(i+1); // value is 1 more than index
                }
            }
            //System.out.println("temp size " + tempDomain.size());
            sc.domain.removeAll(tempDomain);
        }
    }

    public void updateCellDomains(SquareCell s){
        List<Integer> temp = new ArrayList<>();
        temp.add(s.x);
        for(SquareCell sc: unAssignedCells){
            if(sc.x == s.x || sc.y == s.y){
                sc.domain.removeAll(temp);
            }

        }
    }
    public void printDomains(){
        System.out.println("Row domains:");
        for(int i=0; i<size; i++)
            System.out.println(rowDomains[i]);

        System.out.println("Column domains:");
        for(int i=0; i<size; i++)
            System.out.println(colDomains[i]);

    }
    public void printDomainSizes(){
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                System.out.print(cells[i][j].domain.size() + " ");
            }
            System.out.print("\n");
        }
    }
    public void sortByDomainSize(){
        unAssignedCells.sort(Comparator.comparingInt(c -> c.domain.size()));
    }
    public void sortByVah1TiesByVah2(){
        calculateDegree();
        unAssignedCells.sort((o1, o2) -> {
            int result = o1.domain.size() - o2.domain.size();
            if (result == 0) {
                result = o2.degree - o1.degree;         //reverse order, for maximum forward degree
            }
            return result;
        });
    }
    public void calculateDegree(){
        for(SquareCell cell: unAssignedCells){
            int count = 0;
            for(int i=0; i<size; i++){
                if(cell.x == i) continue;
                if(cells[i][cell.y].getValue() == 0)
                    count++;
            }
            for(int i=0; i<size; i++){
                if(cell.y == i) continue;
                if(cells[cell.x][i].getValue() == 0)
                    count++;
            }
            cell.degree = count;
        }
    }
    public void sortByDegree(){
        calculateDegree();
        unAssignedCells.sort(Comparator.comparingInt(c -> c.degree));
    }
    @Override
    public String toString(){
        StringBuilder temp = new StringBuilder();
        for (int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if(cells[i][j].getValue() > 9){
                    temp.append(cells[i][j].getValue()).append("  ");
                }else
                    temp.append(cells[i][j].getValue()).append("   ");
            }
            temp.append("\n");
        }

        return temp.toString();
    }
}
