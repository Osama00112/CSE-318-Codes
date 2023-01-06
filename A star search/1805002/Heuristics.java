import java.util.Set;

public class Heuristics {
    public Board finalBoard;
    private Board current;
    public int size;
    public int type;

    public Heuristics(Board b, int size, int type){
        this.size = size;
        finalBoard = b;
    }

    public int getCost(){
        if(type == 0)
            return hammingDist();
        else
            return manhattanDist();
    }

    public int hammingDist(){
        int count = 0;
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if (finalBoard.values[i][j] != current.values[i][j])
                    count ++;
            }
        }
        return count;
    }

    public int manhattanDist(){
        int count  = 0;
        Set<Integer> keys = finalBoard.respectivePos.keySet();
        for (Integer key: keys){
            if (key == -1) continue;
            int xDiff = Math.abs(finalBoard.respectivePos.get(key).x - current.respectivePos.get(key).x);
            int yDiff = Math.abs(finalBoard.respectivePos.get(key).y - current.respectivePos.get(key).y);
            count += (xDiff + yDiff);
        }
        return count;
    }


    public Board getCurrent() {
        return current;
    }

    public void setCurrent(Board current) {
        this.current = current;
    }
}
