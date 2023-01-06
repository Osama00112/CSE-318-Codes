import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class Board {
    public int size;
    public int[][] values;
    public List<Integer> numSerial;
    public Hashtable<Integer, Coord> respectivePos;

    public Board(int size){
        this.size = size;
        values = new int[size][size];
        numSerial = new ArrayList<>();
        respectivePos = new Hashtable<>();
    }

    public Board(int n,String str){
        size = n;
        values = new int[size][size];
        numSerial = new ArrayList<>();
        respectivePos = new Hashtable<>();
        int it = 1;
        if (str.equalsIgnoreCase("target")) {
            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){
                    if( i == size-1 && j == size-1) continue;
                    respectivePos.put(it, new Coord(i,j));
                    values[i][j] = it;
                    numSerial.add(it);
                    it++;
                }
            }
            values[size-1][size-1] = -1;
            respectivePos.put(-1, new Coord(size-1,size -1));
        }
    }

    public Board(int[][] matrix, int n){
        size = n;
        values = new int[size][size];
        numSerial = new ArrayList<>();
        respectivePos = new Hashtable<>();

        for (int i=0; i< size; i++){
            for (int j=0; j<size; j++){
                int temp = matrix[i][j];
                values[i][j] = temp;
                respectivePos.put(temp, new Coord(i, j));
                if(matrix[i][j] == -1) continue;
                numSerial.add(values[i][j]);
            }
        }
    }
    public void updateHash(){
        respectivePos.clear();
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if(values[i][j] == -1)
                    respectivePos.put(-1, new Coord(i, j));
                else
                    respectivePos.put(values[i][j], new Coord(i, j));
            }
        }
    }

    public Board boardAfterOneMove(int x0, int y0, int x1, int y1){
        Board after = new Board(3);
        after.size = size;
        int [][] matrix = new int[size][size];
        for(int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                matrix[i][j] = values[i][j];
            }
        }
        int temp = matrix[x1][y1];
        matrix[x1][y1] = matrix[x0][y0];
        matrix[x0][y0] = temp;


        //after.size = size;
        after.values = new int[size][size];
        after.numSerial = new ArrayList<>();
        after.respectivePos = new Hashtable<>();

        for (int i=0; i< size; i++){
            for (int j=0; j<size; j++){
                int tempVal = matrix[i][j];
                after.values[i][j] = tempVal;
                after.respectivePos.put(tempVal, new Coord(i, j));
                if(matrix[i][j] == -1) continue;
                after.numSerial.add(values[i][j]);
            }
        }
        //System.out.println("after is : \n" + after);
        return after;
        //return new Board(matrix, size);
    }


    // Taking input from console
    public void setInitial(){
        Scanner sc = new Scanner(System.in);
        int total = size*size - 1;
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                String input = sc.next();
                if(input.equalsIgnoreCase("*")){
                    values[i][j] = -1;
                    respectivePos.put(-1, new Coord(i,j));
                }

                else{
                    int intFound = Integer.parseInt(input);
                    values[i][j] = intFound;
                    numSerial.add(intFound);
                    respectivePos.put(intFound, new Coord(i,j));
                }
            }
        }
    }

    // checking if board solvable
    public boolean isSolvable(){
        int inv_count = 0;
        int total = size*size - 1;
        for (int i=0; i<total; i++){
            if(i == total - 1) break;
            for(int j=i+1; j<total; j++){
                if(numSerial.get(i) > numSerial.get(j))
                    inv_count ++;
            }
        }

        int blank_position = 0;
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if (values[i][j] == -1){
                    blank_position = i;
                }
            }
        }
        //System.out.println(inv_count);
        //return (inv_count % 2) == 0;
        // if grid size n is odd
        if (size % 2 == 1){
            return (inv_count % 2 == 0);
        }else{
            if(blank_position % 2 == 0 && inv_count % 2 != 0)
                return true;
            else if(blank_position % 2 != 0 && inv_count % 2 == 0)
                return true;
            else
                return false;
        }
    }

    // getting the position of blank spot
    public Coord getBlankPos(){
        return respectivePos.get(-1);
    }

    @Override
    public String toString(){
        StringBuilder output = new StringBuilder();
        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                if (values[i][j] == -1)
                    output.append("*");
                else{
                    output.append(Integer.toString(values[i][j]));
                }
                output.append(" ");
            }
            output.append("\n");
        }
        return output.toString();
    }
}
