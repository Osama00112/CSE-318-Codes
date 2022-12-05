import java.util.*;

public class BFS_Util {
    public Board initial;
    public Board target;
    int size;
    public int nodeExpanded;
    public int nodeExplored;
    public PriorityQueue<Node> pq;
    public HashSet<Board> visited;
    //public Hashtable<Board, Integer> openList;
    //public Hashtable<Board, Integer> closedList;
    public Heuristics heuristics;
    final int maxSize = 100000;
    List<Coord> moveList = new ArrayList<>();
    final Coord moveRight = new Coord(1, 0);
    final Coord moveleft = new Coord(-1, 0);
    final Coord moveUp = new Coord(0, -1);
    final Coord moveDown = new Coord(0, 1);


    public BFS_Util(Board target, Board initial, int heuristicType){
        this.initial = initial;
        this.target = target;
        size = target.size;
        //openList = new Hashtable<>(100000);
        //closedList = new Hashtable<>(100000);
        pq = new PriorityQueue<>(maxSize, new myComparator());
        heuristics = new Heuristics(target, target.size, heuristicType);
        visited = new HashSet<>();
        moveList.add(moveRight);
        moveList.add(moveleft);
        moveList.add(moveUp);
        moveList.add(moveDown);
    }

    public void clearAll(){
        pq.clear();
        //openList.clear();
        //closedList.clear();
        visited.clear();
    }

    public Node bfs(){
        if(!initial.isSolvable()){
            System.out.println("Not Solvable");
            return null;
        }
        heuristics.setCurrent(initial);
        int initial_cost_h = heuristics.getCost();
        Node initialNode = new Node(initial, 0, initial_cost_h);
        initialNode.parent = null;
        clearAll();
        pq.add(initialNode);
        nodeExpanded = 0;
        nodeExplored = 0;

        while(!pq.isEmpty()){
            nodeExpanded ++;
            // if it exceeds 100000 print
//            if(nodeExpanded > 10){
//                System.out.println("overflow expanded " + nodeExpanded);
//                break;
//            }

            Node current = pq.remove();
            Coord currentBlankPos = current.b.getBlankPos();
            //System.out.println("current is : \n" + current.b);
            int x0 = currentBlankPos.x;
            int y0 = currentBlankPos.y;
            int x1, y1;
            for(Coord coord: moveList){
                x1 = coord.x + x0;
                y1 = coord.y + y0;

                // boundary check
                if(x1 < 0 || y1 < 0 || x1 >= size || y1 >= size)
                    continue;


                Board child = current.b.boardAfterOneMove(x0, y0, x1, y1);
                //System.out.println("child is : \n" + child);
                if (visited.contains(child)) continue;
                heuristics.setCurrent(child);
                int child_cost_h = heuristics.getCost();
                Node childNode = new Node(child, current.cost_g+1, child_cost_h);
                childNode.parent = current;
                pq.add(childNode);
                nodeExplored ++;

                if(child_cost_h == 0){
                    System.out.println("Node Expanded: " + nodeExpanded);
                    System.out.println("Node Explored: " + nodeExplored);
                    return childNode;
                }
            }
            visited.add(current.b);
        }
        System.out.println("Solution Not Found");
        return null;
    }

    public void printNodeSteps(Node leaf){
        Stack<Node> nodeList = new Stack<>();
        Node temp = leaf;
        while(temp != null){
            nodeList.push(temp);
            temp = temp.parent;
        }

        while (!nodeList.isEmpty()){
            Node n = nodeList.pop();
            System.out.println(n.b);
            System.out.println("|");
            System.out.println("|");
            System.out.println("V");
        }
    }

}
