import java.util.HashSet;
import java.util.Set;

public class Course implements Cloneable{
    public int id;
    public int timeSlot;
    public int studentsEnrolled;
    public Set<Integer> conflictingCourses;

    public Course(int id, int studentsEnrolled){
        this.id = id;
        this.timeSlot = -1;
        this.studentsEnrolled = studentsEnrolled;
        this.conflictingCourses = new HashSet<>();
    }
    public Course(Course copy){
        this.id = copy.id;
        this.timeSlot = copy.timeSlot;
        this.studentsEnrolled = copy.studentsEnrolled;
        this.conflictingCourses = copy.conflictingCourses;
    }

    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }

    public void addConflict(int n){
        conflictingCourses.add(n);
    }
    public int conflictCount(){
        return conflictingCourses.size();
    }

}
