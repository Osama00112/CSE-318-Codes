import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int size = sc.nextInt();
        Board b = new Board(size);
        b.setInitial();
        //System.out.println(b);
        //b.isSolvable();
        Board target = new Board(size, "target");

        BFS_Util util = new BFS_Util(target, b,0);
        Node found = util.bfs();
        if(found != null){
            System.out.println("Heuristic : Hamming Distance\nCost = " + found.cost_f);
            System.out.println("Steps: ");
            util.printNodeSteps(found);
        }

        util = new BFS_Util(target, b, 1);
        found = util.bfs();
        if(found != null){
            System.out.println("Heuristic : Manhattan Distance\nCost = " + found.cost_f);
            System.out.println("Steps: ");
            util.printNodeSteps(found);
        }
    }
}
