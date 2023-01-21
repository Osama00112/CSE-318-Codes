import java.util.Comparator;

public class CourseIdComparator implements Comparator<Course> {
    @Override
    public int compare(Course o1, Course o2) {
        return (o1.id - o2.id);
    }
}
