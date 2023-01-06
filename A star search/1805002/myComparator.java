import java.util.Comparator;

public class myComparator implements Comparator<Node> {
    @Override
    public int compare(Node x, Node y){
        if (x.cost_f == y.cost_f){
            return Integer.compare(x.cost_h, y.cost_h);
        }
        else if(x.cost_f < y.cost_f) return -1;
        else return 1;
    }
}
