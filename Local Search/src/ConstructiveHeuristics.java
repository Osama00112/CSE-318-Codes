import java.util.*;

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
        }else if(type == 2){
            SaturationDegree();
        }else if(type == 3){
            LargestEnrollment();
        }else if(type == 4){
            RandomOrdering();
        }
        return courseList;
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
        courseList.sort((o1, o2) -> {
            int result = - o1.totalNeighbourColors + o2.totalNeighbourColors;
            if (result == 0) {
                int random = (int) Math.round(Math.random());
                if(random == 0)
                    return (- o1.totalNeighbourColors + o2.totalNeighbourColors);
                else
                    return (+ o1.totalNeighbourColors - o2.totalNeighbourColors);
            }
            return result;
        });

    }
    public void LargestEnrollment(){
        courseList.sort((o1, o2) -> {
            int result = - o1.studentsEnrolled + o2.studentsEnrolled;
            if (result == 0) {
                int random = (int) Math.round(Math.random());
                if(random == 0)
                    return (- o1.studentsEnrolled + o2.studentsEnrolled);
                else
                    return (+ o1.studentsEnrolled - o2.studentsEnrolled);
            }
            return result;
        });

    }

    int getIndex(){
        int maxTotal = 0;
        int index = -1;
        for(int i=0; i<courseList.size(); i++){
            Course selected = courseList.get(i);
            if(selected.timeSlot == -1){
                if(selected.neighbourColors.size() >= maxTotal){
                    maxTotal= selected.neighbourColors.size();
                    index = i;
                }
            }
        }

        return index;
    }

    public void RandomOrdering(){
        Collections.shuffle(courseList);
    }



    public void setType(int type) {
        this.type = type;
    }
}
