import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.*;

public class PerturbativeHeuristics {
    public List<Course> courseList;
    public List<Course> tempList;
    public List<Student> studentList;
    public double originalAvg;
    public double newAvg;
    public int penType;
    public int []visited;
    public PerturbativeHeuristics(List<Course> courseList, List<Student> studentList, int penType, double originalAvg){
        this.tempList = new ArrayList<>(courseList.size());
        for(Course c: courseList){
            Course d = new Course(c);
            tempList.add(d);
        }
        tempList.sort(new CourseIdComparator());
        this.visited = new int[courseList.size()];
        Arrays.fill(this.visited, 0);
        this.penType = penType;
        this.originalAvg = originalAvg;
        this.courseList = courseList;
        this.studentList = studentList;
        this.newAvg = 0;
    }
    public void KempeChain(){
        int nodeCount = 0;
        int tolalCourse = courseList.size();
//        PenaltyCalculator calculator = new PenaltyCalculator(studentList, tempList, penType);
//        double avg = calculator.getAvgPenalty();
//        System.out.println("original avg " + avg);
        for(int i=0; i<1000; i++ ){
            nodeCount++;
            // random node
            Random random = new Random();
            Course current;
            int randomNumber;
            while(true){
                randomNumber = random.nextInt(courseList.size());
                current = tempList.get(randomNumber);
                if(current.conflictCount() != 0)
                    break;
            }
            int parentTimeSlot = current.timeSlot;
            //visited[randomNumber] = 1;

            // random child
            List<Integer> tempIntList = new ArrayList<>(current.conflictingCourses);
            randomNumber = random.nextInt(current.conflictCount());
            int childID = tempIntList.get(randomNumber);
            Course child = tempList.get(childID);
            int RandomChildTimeSlot = child.timeSlot;
            //visited[childID] = 1;
            //System.out.println("current " + current.timeSlot + " child " +  child.timeSlot/* + " 1st id " + tempList.get(0).id*/);

            // initial swap
//            int temp = current.timeSlot;
//            current.timeSlot = child.timeSlot;
//            child.timeSlot = temp;

            dfs(current,RandomChildTimeSlot);



            for(int j=0; j<tempList.size(); j++){
                if(visited[j] == 2){
                    if(tempList.get(j).timeSlot == parentTimeSlot) {
                        //System.out.println("subgraph node parents " + j);
                        tempList.get(j).timeSlot = RandomChildTimeSlot;
                    }
                    else {
                        //System.out.println("subgraph node childs" + j);
                        tempList.get(j).timeSlot = parentTimeSlot;
                    }
                }
            }


            // calculating penalty
            PenaltyCalculator calculator = new PenaltyCalculator(studentList, tempList, penType);
            double avg = calculator.getAvgUpdatedPenalty();
            //System.out.println("intermediate penalty found " + avg);
            if(avg < originalAvg){
                newAvg = avg;
                courseList = new ArrayList<>(tempList);
            }else{
//                tempList.clear();
//                tempList = new ArrayList<>(courseList.size());
//                for(Course c1: courseList){
//                    Course c2 = new Course(c1);
//                    tempList.add(c2);
//                }
                //System.out.println("temp " + tempList.size() + " og " + courseList.size());
                for(int j=0; j<tempList.size(); j++){
                    if(visited[j] == 2){
                        if(tempList.get(j).timeSlot == parentTimeSlot) {
                            //System.out.println("subgraph node parents " + j);
                            tempList.get(j).timeSlot = RandomChildTimeSlot;
                        }
                        else {
                            //System.out.println("subgraph node childs" + j);
                            tempList.get(j).timeSlot = parentTimeSlot;
                        }
                    }
                }
            }
            Arrays.fill(visited, 0);
//            if(nodeCount == tolalCourse)
//                break;
        }

    }
    public void dfs(Course c, int alternatingColor){
        visited[c.id - 1] = 1;
        int parentColor = c.timeSlot;
        for(Integer i: c.conflictingCourses){
            //visited[i] = 1;
            Course child = tempList.get(i);
            int childColor = child.timeSlot;
            if(childColor == alternatingColor && visited[i] == 0){
                //child.timeSlot = alternatingColor;
                //visited[i] = true;
                dfs(child, parentColor);
            }
        }
//        for(int i=0; i<c.conflictCount(); i++){
//            List<Integer> temp = new ArrayList<>(c.conflictingCourses);
//            int childColor = tempList.get(temp.get(i)).timeSlot;
//            if(childColor == parentColor){
//                tempList.get(i).timeSlot = alternatingColor;
//                dfs(tempList.get(i), parentColor);
//            }
//        }
//        System.out.println("skipped");
//        System.out.println(tempList.size());
        visited[c.id - 1] = 2;
        return;
    }

}
