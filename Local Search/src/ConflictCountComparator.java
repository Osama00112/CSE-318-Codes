import java.util.Comparator;

public class ConflictCountComparator implements Comparator<Course> {
    @Override
    public int compare(Course o1, Course o2) {
        return -(o1.conflictCount() - o2.conflictCount());
    }
}
