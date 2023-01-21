import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ConstructiveHeuristics {
    private int type;
    private List<Course> courseList;

    public ConstructiveHeuristics(List<Course> courseList, int type){
        this.type = type;
        this.courseList = courseList;
    }

    public List<Course> getMethod(){
        if(type == 1){
            LargestDegree();
            return courseList;
        }
        else return null;
    }

    public void LargestDegree(){
        //courseList.sort(new ConflictCountComparator());
        courseList.sort((o1, o2) -> {
            int result = - o1.conflictCount() + o2.conflictCount();
            if (result == 0) {
                int random = (int) Math.round(Math.random());
                if(random == 0)
                    return (- o1.conflictCount() + o2.conflictCount());
                else
                    return (+ o1.conflictCount() - o2.conflictCount());
            }
            return result;
        });
    }
    public void SaturationDegree(){

    }
    public void LargestEnrollment(){

    }
    public void RandomOrdering(){

    }



    public void setType(int type) {
        this.type = type;
    }
}
