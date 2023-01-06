public class Node {
    Board b;
    Node parent;
    int cost_g;
    int cost_h;
    int cost_f;

    public Node(Board b, int g, int h){
        this.b = b;
        cost_g = g;
        cost_h = h;
        cost_f = g + h;
    }
}
