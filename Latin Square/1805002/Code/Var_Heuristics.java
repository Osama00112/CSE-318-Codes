import java.util.List;
import java.util.Random;

public class Var_Heuristics {
    int type;
    int size;
    LatinBoard board;
    public Var_Heuristics(int n, int size){
        type = n;
        this.size = size;
        //this.board = board;
    }

    public int getVariable(List<SquareCell> givenSet){
//        if(board.unAssignedCells.size() == 0)
//            return -1;
        int index = 0;
        int i = 0;
        int minDomainSize = 2*size;
        double minRatio = 2*size;
        int maxDegree = 0;
        Random random = new Random();
        if(type == 1){
            //board.sortByDomainSize();
            for(SquareCell sc: givenSet){
                if(sc.domain.size() < minDomainSize){
                    minDomainSize = sc.domain.size();
                    index = i;
                }
                i++;
            }
            return index;
        }else if(type == 2){
            for(SquareCell sc: givenSet){
                if(sc.degree > maxDegree){
                    maxDegree = sc.degree;
                    minDomainSize = sc.domain.size();
                    index = i;
                }else if(sc.degree == maxDegree){
                    if(sc.domain.size() < minDomainSize){
                        minDomainSize = sc.domain.size();
                        index = i;
                    }
                }
                i++;
            }
            return index;
        }else if(type == 3 ){
            for(SquareCell sc: givenSet){
                if(sc.domain.size() < minDomainSize){
                    minDomainSize = sc.domain.size();
                    maxDegree = sc.degree;
                    index = i;
                }else if(sc.domain.size() == minDomainSize){
                    if(sc.degree > maxDegree){
                        index = i;
                        maxDegree = sc.degree;
                    }
                }
                i++;
            }
            return index;
        }else if(type == 4){
            for(SquareCell sc: givenSet){
                if (sc.degree == 0)
                    return  i;
                double ratio = sc.domain.size()/sc.degree;

                if(ratio < minRatio){
                    minRatio = ratio;
                    index = i;
                }
                i++;
            }
            return index;
        }
        else if(type == 5){
            return random.nextInt(givenSet.size());
        }
        else{
            return 0;
        }
    }

}
