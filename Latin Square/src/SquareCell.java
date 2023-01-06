import java.util.ArrayList;
import java.util.List;

public class SquareCell {
    private int value;
    public int degree;
    public int x;
    public int y;
    public List<Integer> domain;

    public SquareCell(int value, int x, int y){
        this.value = value;
        this.x = x;
        this.y = y;
        degree = 0;
        domain = new ArrayList<>();
        if(value == 0){
            for(int i=0; i<10; i++){
                domain.add(i+1);
            }
        }
    }

    public int getValue(){
        return value;
    }
    public void setValue(int value){
        this.value = value;
    }
}
