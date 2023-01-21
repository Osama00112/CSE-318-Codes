import java.util.*;

public class Solver {
    public int type;
    public List<Course> courseList;
    public List<Student> studentList;
    public List<Course> originalCourseList;
    public ConstructiveHeuristics CH;
    public int timeSlot;
    public boolean []available;
    public int []allocatedTimeSlot;
    public Set<Integer> allTimeSlots;
    public int penaltyType;
    public double avgPenalty;

    public Solver(List<Course> courseList, List<Student> studentList, int type, int penaltyType) throws CloneNotSupportedException {
        this.type = type;
        this.courseList = new ArrayList<>();
        this.timeSlot = -1;
        this.available = new boolean[courseList.size()];
        this.allocatedTimeSlot = new int[courseList.size()];
        this.allTimeSlots = new HashSet<>();
        this.penaltyType = penaltyType;
        // Initialize all vertices as unassigned
        Arrays.fill(this.allocatedTimeSlot, -1);
//        for(Course c:courseList){
//            Course d = (Course)c.clone();
//            this.courseList.add(d);
//        }
        this.courseList = courseList;
        this.studentList = studentList;
        this.originalCourseList = new ArrayList<>(courseList);
        this.CH = new ConstructiveHeuristics(this.courseList, this.type);
    }


    public void solve(){
        courseList = CH.getMethod();
        // initially a course is selected
        courseList.get(0).timeSlot = 0; //timeslot needs to be set initially
        allTimeSlots.add(0);

        int courseCount = courseList.size();
        Arrays.fill(available, true);

        // iterate over all remaining courses
        for(int i=1; i<courseCount; i++){
            Course selected = courseList.get(i);

            // check its neighbours/conflicts
            for(Integer j: selected.conflictingCourses){
                Course c = originalCourseList.get(j);
                if(c.timeSlot != -1){
                    available[c.timeSlot] = false;
                }
            }

            // get the first available slot
            int slot;
            for (slot = 0; slot < courseCount; slot++){
                if (available[slot]) {
                    //System.out.println("pawa gese " + slot);
                    break;
                }
            }

            if(slot == courseCount)
                slot --;
            selected.timeSlot = slot;
            allTimeSlots.add(slot);

            //revert all
            Arrays.fill(available, true);
        }
    }

    public void TotalTimeSlots(){
        System.out.println(allTimeSlots.size());
    }

    public double calculateAvgPenalty(){
        PenaltyCalculator pen = new PenaltyCalculator(studentList, penaltyType);
        return pen.getAvgPenalty();
    }

}
