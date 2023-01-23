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
        this.newAvg = originalAvg;
    }
    public void KempeChain(){
        int nodeCount = 0;
        int totalCourse = courseList.size();
        for(int i=0; i<1000; i++ ){
            nodeCount++;
            // random node
            Random random = new Random();
            Course current;
            int randomNumber;
            while(true){
                randomNumber = random.nextInt(totalCourse);
                current = tempList.get(randomNumber);
                if(current.conflictCount() != 0)
                    break;
            }
            int parentTimeSlot = current.timeSlot;

            // random child
            List<Integer> tempIntList = new ArrayList<>(current.conflictingCourses);
            randomNumber = random.nextInt(current.conflictCount());
            int childID = tempIntList.get(randomNumber);
            Course child = tempList.get(childID);
            int RandomChildTimeSlot = child.timeSlot;

            dfs(current,RandomChildTimeSlot);

            for(int j=0; j<totalCourse; j++){
                if(visited[j] == 2){
                    if(tempList.get(j).timeSlot == parentTimeSlot) {
                        tempList.get(j).timeSlot = RandomChildTimeSlot;
                    }
                    else {
                        tempList.get(j).timeSlot = parentTimeSlot;
                    }
                }
            }

            // calculating penalty
            PenaltyCalculator calculator = new PenaltyCalculator(studentList, tempList, penType);
            double avg = calculator.getAvgUpdatedPenalty();
            if(avg < newAvg){
                newAvg = avg;
                courseList = new ArrayList<>(tempList);
            }else{
                for(int j=0; j<tempList.size(); j++){
                    if(visited[j] == 2){
                        if(tempList.get(j).timeSlot == parentTimeSlot) {
                            tempList.get(j).timeSlot = RandomChildTimeSlot;
                        }
                        else {
                            tempList.get(j).timeSlot = parentTimeSlot;
                        }
                    }
                }
            }
            Arrays.fill(visited, 0);
        }

    }
    public void dfs(Course c, int alternatingColor){
        visited[c.id - 1] = 1;
        int parentColor = c.timeSlot;
        for(Integer i: c.conflictingCourses){
            Course child = tempList.get(i);
            int childColor = child.timeSlot;
            if(childColor == alternatingColor && visited[i] == 0){
                dfs(child, parentColor);
            }
        }
        visited[c.id - 1] = 2;
    }

    public void PairSwap(){
        int totalCourse = courseList.size();
        Random random = new Random();

        for(int i=0; i<1000; i++){
            // 2 random nodes
            int randomNumber = random.nextInt(totalCourse);
            Course c1 = tempList.get(randomNumber);
            int timeSlot1 = c1.timeSlot;

            randomNumber = random.nextInt(totalCourse);
            Course c2 = tempList.get(randomNumber);
            int timeSlot2 = c2.timeSlot;

            if(timeSlot1 == timeSlot2){
                continue;
            }

            int flag = 0;
            for(Integer j: c1.conflictingCourses){
                Course child = tempList.get(j);
                int childColor = child.timeSlot;
                if (childColor == timeSlot2) {
                    flag = 1;
                    break;
                }
            }

            if(flag == 1) continue;

            for(Integer j: c2.conflictingCourses){
                Course child = tempList.get(j);
                int childColor = child.timeSlot;
                if (childColor == timeSlot1) {
                    flag = 1;
                    break;
                }
            }

            if(flag == 1) continue;

            // color swap
            c1.timeSlot = timeSlot2;
            c2.timeSlot = timeSlot1;
            // calculating penalty
            PenaltyCalculator calculator = new PenaltyCalculator(studentList, tempList, penType);
            double avg = calculator.getAvgUpdatedPenalty();
            if(avg < newAvg){
                newAvg = avg;
                courseList = new ArrayList<>(tempList);
                //System.out.println("imporved penalty found :" + newAvg);
            }else{
                c1.timeSlot = timeSlot1;
                c2.timeSlot = timeSlot2;
            }


        }



    }

}
