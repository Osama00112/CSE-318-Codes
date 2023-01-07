import java.util.*;

public class LatinBoard {
    public int size;
    public SquareCell[][] cells;
    public String[] rowDomains;
    public String[] colDomains;
    public List<SquareCell> unAssignedCells;
    public List<SquareCell> DomainAffectedCells;
    public List<SquareCell> DegreeAffectedCells;

    public LatinBoard(int size){
        this.size = size;
        cells = new SquareCell[size][size];
        rowDomains = new String[size];
        colDomains = new String[size];
        unAssignedCells = new ArrayList<>();
        DomainAffectedCells = new ArrayList<>();
        DegreeAffectedCells = new ArrayList<>();
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                cells[i][j] = new SquareCell(0,i,j,size);
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
        //updateDomains();
        //calculateDegree();
    }
    public LatinBoard(LatinBoard b){
        this.size = b.size;
        cells = new SquareCell[size][size];
        rowDomains = new String[size];
        colDomains = new String[size];
        unAssignedCells = new ArrayList<>(b.unAssignedCells);
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                cells[i][j] = new SquareCell(b.cells[i][j].getValue(),i,j,size);
                cells[i][j].domain = new ArrayList<>(b.cells[i][j].domain);
                cells[i][j].degree = b.cells[i][j].degree;
            }
        }

        for(int i=0; i<size; i++){
            rowDomains[i] = b.rowDomains[i];
            colDomains[i] = b.colDomains[i];
        }
        updateCellDomains();
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

    public List<SquareCell> updateCellDomains(int x, int y, int value){
        List<Integer> temp = new ArrayList<>();
        List<SquareCell> dummy = new ArrayList<>();
        temp.add(value);
        for(SquareCell sc: unAssignedCells){
            if(sc.x == x && sc.y == y)
                continue;
            if(sc.x == x || sc.y == y){
                if(sc.domain.contains(value)){
                    sc.domain.removeAll(temp);
                    dummy.add(sc);
                    sc.degree--;
                    if(sc.degree < 0)
                        System.out.println("degree cant be negative");
                }
            }
        }
        DomainAffectedCells.addAll(dummy);
        return dummy;
    }
    public void revertCellDomains(int value, int count){
        int size = DomainAffectedCells.size();
        List<SquareCell> temp = DomainAffectedCells.subList(size - count, size);
        for(SquareCell sc:unAssignedCells){
            if(temp.contains(sc)){
                sc.domain.add(value);
                sc.degree++;
            }
        }
        DomainAffectedCells.subList(size - count, size).clear();
    }
//    public List<SquareCell> updateDegree(int x, int y, int value){
//        List<SquareCell> dummy = new ArrayList<>();
//
//        for(SquareCell sc: unAssignedCells){
//            if(sc.x == x && sc.y == y)
//                continue;
//            if(sc.x == x || sc.y == y){
//                if(sc.domain.contains(value)){
//                    sc.degree--;
//                    if(sc.degree < 0)
//                        System.out.println("degree cant be negative");
//                }
//            }
//        }
//        DegreeAffectedCells.addAll(dummy);
//        return dummy;
//    }
//    public void revertDegreeChanges(int count){
//        int size = DegreeAffectedCells.size();
//        List<SquareCell> temp = DegreeAffectedCells.subList(size - count, size);
//        for(SquareCell sc:unAssignedCells){
//            if(temp.contains(sc)){
//                sc.degree++;
//            }
//        }
//        DegreeAffectedCells.subList(size - count, size).clear();
//    }

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
    public void sortByDegree(){
        //calculateDegree();
        unAssignedCells.sort(Comparator.comparingInt(c -> -c.degree));
//        for(SquareCell sc: unAssignedCells)
//            System.out.println(sc.degree);
    }
    public void sortByVah1TiesByVah2(){
        //calculateDegree();
        unAssignedCells.sort((o1, o2) -> {
            int result = o1.domain.size() - o2.domain.size();
            if (result == 0) {
                result = o2.degree - o1.degree;         //reverse order, for maximum forward degree
            }
            return result;
        });
    }
    public void sortByVah4(){
        unAssignedCells.sort(Comparator.comparingInt(c -> (c.domain.size()/c.degree)));
    }
    public void sortByVah5(){
        Collections.shuffle(unAssignedCells);
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
