import java.util.ArrayList;
import java.util.List;

public class Student {
    public int id;
    public List<Course> courseList;

    public Student(int id){
        this.id = id;
        courseList = new ArrayList<>();
    }

    public void addCourse(int id){
        courseList.add(new Course(id, 0));
    }
    public void addCourse(Course c){
        courseList.add(c);
    }
}
