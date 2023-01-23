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
    public void solveSat(){
        // initially a course is selected
        Course inital = courseList.get(0);
        inital.timeSlot = 0; //timeslot needs to be set initially
        originalCourseList.get(inital.id - 1).timeSlot = 0;
        allTimeSlots.add(0);
        updateDSaturValues(inital);

        int courseCount = courseList.size();
        Arrays.fill(available, true);

        // iterate over all remaining courses
        while(true){
            //courseList = CH.getMethod();
            int index = CH.getIndex();
            if(index == -1)
                break;
            Course selected = courseList.get(index);
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
                    break;
                }
            }
            if(slot == courseCount)
                slot --;
            selected.timeSlot = slot;
            originalCourseList.get(selected.id - 1).timeSlot = slot;
            allTimeSlots.add(slot);
            updateDSaturValues(selected);
            //revert all
            Arrays.fill(available, true);
        }
    }

    public void TotalTimeSlots(){
        System.out.println("Total Time Slots: " + allTimeSlots.size());
    }

    public double calculateAvgPenalty(){
        PenaltyCalculator pen = new PenaltyCalculator(studentList, penaltyType);
        return pen.getAvgPenalty();
    }

    public void updateDSaturValues(Course c){
        for(Integer i: c.conflictingCourses){
            Course child = courseList.get(i);
            if(child.timeSlot == -1) {
                child.addColor(c.timeSlot);
                child.totalNeighbourColors++;
            }
        }

    }

}
