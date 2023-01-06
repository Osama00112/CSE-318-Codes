public class Solver {
    public int VAH_type;
    public Var_Heuristics VAR_H;
    public ValOrder_Heuristics VAL_H;
    LatinBoard board;
    public Solver(int VAH_type, LatinBoard board){
        this.VAH_type = VAH_type;
        this.board = board;
    }
    public LatinBoard backTrackingSolve(){
        LatinBoard temp = board;


        return temp;
    }
}
